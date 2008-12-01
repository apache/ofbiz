/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.shipment.packing;

import java.util.*;
import java.math.BigDecimal;

import javolution.util.FastMap;
import javolution.util.FastList;
import javolution.util.FastSet;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilFormatOut;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.GenericDispatcher;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.product.product.ProductWorker;

public class PackingSession implements java.io.Serializable {

    public static final String module = PackingSession.class.getName();

    protected GenericValue userLogin = null;
    protected String pickerPartyId = null;
    protected String primaryOrderId = null;
    protected String primaryShipGrp = null;
    protected String dispatcherName = null;
    protected String delegatorName = null;
    protected String picklistBinId = null;
    protected String facilityId = null;
    protected String shipmentId = null;
    protected String instructions = null;
    protected String weightUomId = null;
    protected BigDecimal additionalShippingCharge = null;
    protected Map packageWeights = null;
    protected List packEvents = null;
    protected List packLines = null;
    protected List itemInfos = null;
    protected int packageSeq = -1;
    protected int status = 1;

    private transient GenericDelegator _delegator = null;
    private transient LocalDispatcher _dispatcher = null;

    public PackingSession(LocalDispatcher dispatcher, GenericValue userLogin, String facilityId, String binId, String orderId, String shipGrp) {
        this._dispatcher = dispatcher;
        this.dispatcherName = dispatcher.getName();

        this._delegator = _dispatcher.getDelegator();
        this.delegatorName = _delegator.getDelegatorName();

        this.primaryOrderId = orderId;
        this.primaryShipGrp = shipGrp;
        this.picklistBinId = binId;
        this.userLogin = userLogin;
        this.facilityId = facilityId;
        this.packLines = FastList.newInstance();
        this.packEvents = FastList.newInstance();
        this.itemInfos = FastList.newInstance();
        this.packageSeq = 1;
        this.packageWeights = FastMap.newInstance();
    }

    public PackingSession(LocalDispatcher dispatcher, GenericValue userLogin, String facilityId) {
        this(dispatcher, userLogin, facilityId, null, null, null);
    }

    public PackingSession(LocalDispatcher dispatcher, GenericValue userLogin) {
        this(dispatcher, userLogin, null, null, null, null);
    }

    public void addOrIncreaseLine(String orderId, String orderItemSeqId, String shipGroupSeqId, String productId, BigDecimal quantity, int packageSeqId, BigDecimal weight, boolean update) throws GeneralException {
        // reset the session if we just completed
        if (status == 0) {
            throw new GeneralException("Packing session has been completed; be sure to CLEAR before packing a new order! [000]");
        }

        // do nothing if we are trying to add a quantity of 0
        if (!update && quantity.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // find the actual product ID
        productId = ProductWorker.findProductId(this.getDelegator(), productId);

        // set the default null values - primary is the assumed first item
        if (orderId == null) {
            orderId = primaryOrderId;
        }
        if (shipGroupSeqId == null) {
            shipGroupSeqId = primaryShipGrp;
        }
        if (orderItemSeqId == null && productId != null) {
            orderItemSeqId = this.findOrderItemSeqId(productId, orderId, shipGroupSeqId, quantity);
        }

        // get the reservations for the item
        Map invLookup = FastMap.newInstance();
        invLookup.put("orderId", orderId);
        invLookup.put("orderItemSeqId", orderItemSeqId);
        invLookup.put("shipGroupSeqId", shipGroupSeqId);
        List reservations = this.getDelegator().findByAnd("OrderItemShipGrpInvRes", invLookup, UtilMisc.toList("quantity DESC"));

        // no reservations we cannot add this item
        if (UtilValidate.isEmpty(reservations)) {
            throw new GeneralException("No inventory reservations available; cannot pack this item! [101]");
        }

        // find the inventoryItemId to use
        if (reservations.size() == 1) {
            GenericValue res = EntityUtil.getFirst(reservations);
            int checkCode = this.checkLineForAdd(res, orderId, orderItemSeqId, shipGroupSeqId, productId, quantity, packageSeqId, update);
            this.createPackLineItem(checkCode, res, orderId, orderItemSeqId, shipGroupSeqId, productId, quantity, weight, packageSeqId);
        } else {
            // more than one reservation found
            Map toCreateMap = FastMap.newInstance();
            Iterator i = reservations.iterator();
            BigDecimal qtyRemain = quantity;

            while (i.hasNext() && qtyRemain.compareTo(BigDecimal.ZERO) > 0) {
                GenericValue res = (GenericValue) i.next();

                // Check that the inventory item product match with the current product to pack
                if (!productId.equals(res.getRelatedOne("InventoryItem").getString("productId"))) {
                    continue;
                }

                BigDecimal resQty = res.getBigDecimal("quantity");
                BigDecimal resPackedQty = this.getPackedQuantity(orderId, orderItemSeqId, shipGroupSeqId, productId, res.getString("inventoryItemId"), -1);
                if (resPackedQty.compareTo(resQty) >= 0) {
                    continue;
                } else if (!update) {
                    resQty = resQty.subtract(resPackedQty);
                }
                
                BigDecimal thisQty = resQty.compareTo(qtyRemain) > 0 ? qtyRemain : resQty;

                int thisCheck = this.checkLineForAdd(res, orderId, orderItemSeqId, shipGroupSeqId, productId, thisQty, packageSeqId, update);
                switch (thisCheck) {
                    case 2:
                        Debug.log("Packing check returned '2' - new pack line will be created!", module);
                        toCreateMap.put(res, thisQty);
                        qtyRemain = qtyRemain.subtract(thisQty);
                        break;
                    case 1:
                        Debug.log("Packing check returned '1' - existing pack line has been updated!", module);
                        qtyRemain = qtyRemain.subtract(thisQty);
                        break;
                    case 0:
                        Debug.log("Packing check returned '0' - doing nothing.", module);
                        break;
                }
            }

            if (qtyRemain.compareTo(BigDecimal.ZERO) == 0) {
                Iterator x = toCreateMap.keySet().iterator();
                while (x.hasNext()) {
                    GenericValue res = (GenericValue) x.next();
                    BigDecimal qty = (BigDecimal) toCreateMap.get(res);
                    this.createPackLineItem(2, res, orderId, orderItemSeqId, shipGroupSeqId, productId, qty, weight, packageSeqId);
                }
            } else {
                throw new GeneralException("Not enough inventory reservation available; cannot pack the item! [103]");
            }
        }

        // run the add events
        this.runEvents(PackingEvent.EVENT_CODE_ADD);
    }

    public void addOrIncreaseLine(String orderId, String orderItemSeqId, String shipGroupSeqId, BigDecimal quantity, int packageSeqId) throws GeneralException {
        this.addOrIncreaseLine(orderId, orderItemSeqId, shipGroupSeqId, null, quantity, packageSeqId, BigDecimal.ZERO, false);
    }

    public void addOrIncreaseLine(String productId, BigDecimal quantity, int packageSeqId) throws GeneralException {
        this.addOrIncreaseLine(null, null, null, productId, quantity, packageSeqId, BigDecimal.ZERO, false);
    }

    public PackingSessionLine findLine(String orderId, String orderItemSeqId, String shipGroupSeqId, String productId, String inventoryItemId, int packageSeq) {
        List lines = this.getLines();
        Iterator i = lines.iterator();
        while (i.hasNext()) {
            PackingSessionLine line = (PackingSessionLine) i.next();
            if (orderId.equals(line.getOrderId()) &&
                    orderItemSeqId.equals(line.getOrderItemSeqId()) &&
                    shipGroupSeqId.equals(line.getShipGroupSeqId()) &&
                    productId.equals(line.getProductId()) &&
                    inventoryItemId.equals(line.getInventoryItemId()) && 
                    packageSeq == line.getPackageSeq()) {
                return line;
            }
        }
        return null;
    }

    protected void createPackLineItem(int checkCode, GenericValue res, String orderId, String orderItemSeqId, String shipGroupSeqId, String productId, BigDecimal quantity, BigDecimal weight, int packageSeqId) throws GeneralException {
        // process the result; add new item if necessary
        switch(checkCode) {
            case 0:
                // not enough reserved
                throw new GeneralException("Not enough inventory reservation available; cannot pack the item! [201]");
            case 1:
                // we're all good to go; quantity already updated
                break;
            case 2:
                // need to create a new item
                String invItemId = res.getString("inventoryItemId");
                packLines.add(new PackingSessionLine(orderId, orderItemSeqId, shipGroupSeqId, productId, invItemId, quantity, weight, packageSeqId));
                break;
        }

        // Add the line weight to the package weight
        if (weight.compareTo(BigDecimal.ZERO) > 0) this.addToPackageWeight(packageSeqId, weight);
        
        // update the package sequence
        if (packageSeqId > packageSeq) {
            this.packageSeq = packageSeqId;
        }
    }

    protected String findOrderItemSeqId(String productId, String orderId, String shipGroupSeqId, BigDecimal quantity) throws GeneralException {
        Map lookupMap = FastMap.newInstance();
        lookupMap.put("orderId", orderId);
        lookupMap.put("productId", productId);
        lookupMap.put("statusId", "ITEM_APPROVED");
        lookupMap.put("shipGroupSeqId", shipGroupSeqId);

        List sort = UtilMisc.toList("-quantity");
        List orderItems = this.getDelegator().findByAnd("OrderItemAndShipGroupAssoc", lookupMap, sort);

        String orderItemSeqId = null;
        if (orderItems != null) {
            Iterator i = orderItems.iterator();
            while (i.hasNext()) {
                GenericValue item = (GenericValue) i.next();

                // get the reservations for the item
                Map invLookup = FastMap.newInstance();
                invLookup.put("orderId", orderId);
                invLookup.put("orderItemSeqId", item.getString("orderItemSeqId"));
                invLookup.put("shipGroupSeqId", shipGroupSeqId);
                List reservations = this.getDelegator().findByAnd("OrderItemShipGrpInvRes", invLookup);
                Iterator resIter = reservations.iterator();
                while (resIter.hasNext()) {
                    GenericValue res = (GenericValue) resIter.next();
                    BigDecimal qty = res.getBigDecimal("quantity");
                    if (quantity.compareTo(qty) <= 0) {
                        orderItemSeqId = item.getString("orderItemSeqId");
                        break;
                    }
                }
            }
        }

        if (orderItemSeqId != null) {
            return orderItemSeqId;
        } else {
            throw new GeneralException("No valid order item found for product [" + productId + "] with quantity: " + quantity);
        }
    }

    protected int checkLineForAdd(GenericValue res, String orderId, String orderItemSeqId, String shipGroupSeqId, String productId, BigDecimal quantity, int packageSeqId, boolean update) {
        // check to see if the reservation can hold the requested quantity amount
        String invItemId = res.getString("inventoryItemId");
        BigDecimal resQty = res.getBigDecimal("quantity");

        PackingSessionLine line = this.findLine(orderId, orderItemSeqId, shipGroupSeqId, productId, invItemId, packageSeqId);
        BigDecimal packedQty = this.getPackedQuantity(orderId, orderItemSeqId, shipGroupSeqId, productId);

        Debug.log("Packed quantity [" + packedQty + "] + [" + quantity + "]", module);

        if (line == null) {
            Debug.log("No current line found testing [" + invItemId + "] R: " + resQty + " / Q: " + quantity, module);
            if (resQty.compareTo(quantity) < 0) {
                return 0;
            } else {
                return 2;
            }
        } else {
            BigDecimal newQty = update ? quantity : (line.getQuantity().add(quantity));
            Debug.log("Existing line found testing [" + invItemId + "] R: " + resQty + " / Q: " + newQty, module);
            if (resQty.compareTo(newQty) < 0) {
                return 0;
            } else {
                line.setQuantity(newQty);
                return 1;
            }
        }
    }

    public void addItemInfo(List infos) {
        Iterator i = infos.iterator();
        while (i.hasNext()) {
            GenericValue v = (GenericValue) i.next();
            ItemDisplay newItem = new ItemDisplay(v);
            int currentIdx = itemInfos.indexOf(newItem);
            if (currentIdx != -1) {
                ItemDisplay existingItem = (ItemDisplay) itemInfos.get(currentIdx);
                existingItem.quantity = existingItem.quantity.add(newItem.quantity);
            } else {
                itemInfos.add(newItem);
            }
        }
    }

    public List getItemInfos() {
        return itemInfos;
    }

    public void clearItemInfos() {
        itemInfos.clear();
    }

    public String getShipmentId() {
        return this.shipmentId;
    }

    public List getLines() {
        return this.packLines;
    }

    public int nextPackageSeq() {
        return ++packageSeq;
    }

    public int getCurrentPackageSeq() {
        return packageSeq;
    }

    public BigDecimal getPackedQuantity(String orderId, String orderItemSeqId, String shipGroupSeqId, String productId) {
        return getPackedQuantity(orderId, orderItemSeqId, shipGroupSeqId,  productId, null, -1);
    }

    public BigDecimal getPackedQuantity(String orderId, String orderItemSeqId, String shipGroupSeqId, String productId, int packageSeq) {
        return getPackedQuantity(orderId, orderItemSeqId, shipGroupSeqId,  productId, null, packageSeq);
    }

    public BigDecimal getPackedQuantity(String orderId, String orderItemSeqId, String shipGroupSeqId, String productId, String inventoryItemId, int packageSeq) {
        BigDecimal total = BigDecimal.ZERO;
        List lines = this.getLines();
        Iterator i = lines.iterator();
        while (i.hasNext()) {
            PackingSessionLine line = (PackingSessionLine) i.next();
            if (orderId.equals(line.getOrderId()) && orderItemSeqId.equals(line.getOrderItemSeqId()) &&
                    shipGroupSeqId.equals(line.getShipGroupSeqId()) && productId.equals(line.getProductId())) {
                if (inventoryItemId == null || inventoryItemId.equals(line.getInventoryItemId())) {
                    if (packageSeq == -1 || packageSeq == line.getPackageSeq()) {
                        total = total.add(line.getQuantity());
                    }
                }
            }
        }
        return total;
    }

    public BigDecimal getPackedQuantity(String productId, int packageSeq) {
        if (productId != null) {
            try {
                productId = ProductWorker.findProductId(this.getDelegator(), productId);
            } catch (GenericEntityException e) {
                Debug.logError(e, module);
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        if (productId != null ) {
            List lines = this.getLines();
            Iterator i = lines.iterator();
            while (i.hasNext()) {
                PackingSessionLine line = (PackingSessionLine) i.next();
                if (productId.equals(line.getProductId())) {
                    if (packageSeq == -1 || packageSeq == line.getPackageSeq()) {
                        total = total.add(line.getQuantity());
                    }
                }
            }
        }
        return total;
    }

    public BigDecimal getPackedQuantity(int packageSeq) {
        BigDecimal total = BigDecimal.ZERO;
        List lines = this.getLines();
        Iterator i = lines.iterator();
        while (i.hasNext()) {
            PackingSessionLine line = (PackingSessionLine) i.next();
            if (packageSeq == -1 || packageSeq == line.getPackageSeq()) {
                total = total.add(line.getQuantity());
            }
        }
        return total;
    }

    public BigDecimal getPackedQuantity(String productId) {
        return getPackedQuantity(productId, -1);
    }

    public BigDecimal getCurrentReservedQuantity(String orderId, String orderItemSeqId, String shipGroupSeqId, String productId) {
        BigDecimal reserved = BigDecimal.ONE.negate();
        try {
            GenericValue res = EntityUtil.getFirst(this.getDelegator().findByAnd("OrderItemAndShipGrpInvResAndItemSum", UtilMisc.toMap("orderId", orderId,
                    "orderItemSeqId", orderItemSeqId, "shipGroupSeqId", shipGroupSeqId, "inventoryProductId", productId)));
            reserved = res.getBigDecimal("totQuantityAvailable");
            if (reserved == null) {
                reserved = BigDecimal.ONE.negate();
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        }
        return reserved;
    }

    public BigDecimal getCurrentShippedQuantity(String orderId, String orderItemSeqId, String shipGroupSeqId) {
        BigDecimal shipped = BigDecimal.ZERO;
        List issues = this.getItemIssuances(orderId, orderItemSeqId, shipGroupSeqId);
        if (issues != null) {
            Iterator i = issues.iterator();
            while (i.hasNext()) {
                GenericValue v = (GenericValue) i.next();
                BigDecimal qty = v.getBigDecimal("quantity");
                if (qty == null) qty = BigDecimal.ZERO;
                shipped = shipped.add(qty);
            }
        }

        return shipped;
    }

    public List getCurrentShipmentIds(String orderId, String orderItemSeqId, String shipGroupSeqId) {
        Set shipmentIds = FastSet.newInstance();
        List issues = this.getItemIssuances(orderId, orderItemSeqId, shipGroupSeqId);

        if (issues != null) {
            Iterator i = issues.iterator();
            while (i.hasNext()) {
                GenericValue v = (GenericValue) i.next();
                shipmentIds.add(v.getString("shipmentId"));
            }
        }

        List retList = FastList.newInstance();
        retList.addAll(shipmentIds);
        return retList;
    }

    public List getCurrentShipmentIds(String orderId, String shipGroupSeqId) {
        return this.getCurrentShipmentIds(orderId, null, shipGroupSeqId);
    }

    public void registerEvent(PackingEvent event) {
        this.packEvents.add(event);
        this.runEvents(PackingEvent.EVENT_CODE_EREG);
    }

    public LocalDispatcher getDispatcher() {
        if (_dispatcher == null) {
            _dispatcher = GenericDispatcher.getLocalDispatcher(dispatcherName, this.getDelegator());
        }
        return _dispatcher;
    }

    public GenericDelegator getDelegator() {
        if (_delegator == null) {
            _delegator = GenericDelegator.getGenericDelegator(delegatorName);
        }
        return _delegator;
    }

    public GenericValue getUserLogin() {
        return this.userLogin;
    }

    public int getStatus() {
        return this.status;
    }

    public String getFacilityId() {
        return this.facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getPrimaryOrderId() {
        return this.primaryOrderId;
    }

    public void setPrimaryOrderId(String orderId) {
        this.primaryOrderId = orderId;
    }

    public String getPrimaryShipGroupSeqId() {
        return this.primaryShipGrp;
    }

    public void setPrimaryShipGroupSeqId(String shipGroupSeqId) {
        this.primaryShipGrp = shipGroupSeqId;
    }

    public void setPicklistBinId(String binId) {
        this.picklistBinId = binId;
    }

    public String getPicklistBinId() {
        return this.picklistBinId;
    }

    public String getHandlingInstructions() {
        return this.instructions;
    }

    public void setHandlingInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setPickerPartyId(String partyId) {
        this.pickerPartyId = partyId;
    }

    public String getPickerPartyId() {
        return this.pickerPartyId;
    }

    public int clearLastPackage() {
        if (packageSeq == 1) {
            this.clear();
            return packageSeq;
        }
        
        List currentLines = new ArrayList(this.packLines);
        Iterator i = currentLines.iterator();
        while (i.hasNext()) {
            PackingSessionLine line = (PackingSessionLine) i.next();
            if (line.getPackageSeq() == packageSeq) {
                this.clearLine(line);
            }
        }
        return --packageSeq;
    }

    public void clearLine(PackingSessionLine line) {
        this.packLines.remove(line);
    }

    public void clearAllLines() {
        this.packLines.clear();
    }

    public void clear() {
        this.packLines.clear();
        this.instructions = null;
        this.pickerPartyId = null;
        this.picklistBinId = null;
        this.primaryOrderId = null;
        this.primaryShipGrp = null;
        this.additionalShippingCharge = null;
        if (this.packageWeights != null) this.packageWeights.clear();
        this.weightUomId = null;
        this.packageSeq = 1;
        this.status = 1;
        this.runEvents(PackingEvent.EVENT_CODE_CLEAR);
    }

    public String complete(boolean force) throws GeneralException {
        // clear out empty lines
        // this.checkEmptyLines(); // removing, this seems to be causeing issues -  mja

        // check to see if there is anything to process
        if (this.getLines().size() == 0) {
            return "EMPTY";
        }

        // check for errors
        this.checkReservations(force);
        // set the status to 0
        this.status = 0;
        // create the shipment
        this.createShipment();
        // create the packages
        this.createPackages();
        // issue the items
        this.issueItemsToShipment();
        // assign items to packages
        this.applyItemsToPackages();
        // update ShipmentRouteSegments with total weight and weightUomId
        this.updateShipmentRouteSegments();
        // set the shipment to packed
        this.setShipmentToPacked();
        // set role on picklist
        this.setPickerOnPicklist();
        // run the complete events
        this.runEvents(PackingEvent.EVENT_CODE_COMPLETE);

        return this.shipmentId;
    }

    protected void checkReservations(boolean ignore) throws GeneralException {
        List errors = FastList.newInstance();        
        Iterator i = this.getLines().iterator();
        while (i.hasNext()) {
            PackingSessionLine line = (PackingSessionLine) i.next();
            BigDecimal reservedQty =  this.getCurrentReservedQuantity(line.getOrderId(), line.getOrderItemSeqId(), line.getShipGroupSeqId(), line.getProductId());
            BigDecimal packedQty = this.getPackedQuantity(line.getOrderId(), line.getOrderItemSeqId(), line.getShipGroupSeqId(), line.getProductId());

            if (packedQty != reservedQty) {
                errors.add("Packed amount does not match reserved amount for item (" + line.getProductId() + ") [" + packedQty + " / " + reservedQty + "]");
            }
        }

        if (errors.size() > 0) {
            if (!ignore) {
                throw new GeneralException("Attempt to pack order failed.", errors);
            } else {
                Debug.logWarning("Packing warnings: " + errors, module);
            }
        }
    }

    protected void checkEmptyLines() throws GeneralException {
        List lines = FastList.newInstance();
        lines.addAll(this.getLines());
        Iterator i = lines.iterator();
        while (i.hasNext()) {
            PackingSessionLine l = (PackingSessionLine) i.next();
            if (l.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                this.packLines.remove(l);
            }
        }
    }

    protected void runEvents(int eventCode) {
        if (this.packEvents.size() > 0) {
            Iterator i = this.packEvents.iterator();
            while (i.hasNext()) {
                PackingEvent event = (PackingEvent) i.next();
                event.runEvent(this, eventCode);
            }
        }
    }

    protected List getItemIssuances(String orderId, String orderItemSeqId, String shipGroupSeqId) {
        List issues = null;
        if (orderId == null) {
            throw new IllegalArgumentException("Value for orderId is  null");
        }

        Map lookupMap = FastMap.newInstance();
        lookupMap.put("orderId", orderId);
        if (UtilValidate.isNotEmpty(orderItemSeqId)) {
            lookupMap.put("orderItemSeqId", orderItemSeqId);
        }
        if (UtilValidate.isNotEmpty(shipGroupSeqId)) {
            lookupMap.put("shipGroupSeqId", shipGroupSeqId);
        }
        try {
            issues = this.getDelegator().findByAnd("ItemIssuance",  lookupMap);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        }

        return issues;
    }

    protected void createShipment() throws GeneralException {
        // first create the shipment
        Map newShipment = FastMap.newInstance();
        newShipment.put("originFacilityId", this.facilityId);
        newShipment.put("primaryShipGroupSeqId", primaryShipGrp);
        newShipment.put("primaryOrderId", primaryOrderId);
        newShipment.put("shipmentTypeId", "OUTGOING_SHIPMENT");
        newShipment.put("statusId", "SHIPMENT_INPUT");
        newShipment.put("handlingInstructions", instructions);
        newShipment.put("picklistBinId", picklistBinId);
        newShipment.put("additionalShippingCharge", additionalShippingCharge);
        newShipment.put("userLogin", userLogin);
        Debug.log("Creating new shipment with context: " + newShipment, module);
        Map newShipResp = this.getDispatcher().runSync("createShipment", newShipment);

        if (ServiceUtil.isError(newShipResp)) {
            throw new GeneralException(ServiceUtil.getErrorMessage(newShipResp));
        }
        this.shipmentId = (String) newShipResp.get("shipmentId");
    }

    protected void issueItemsToShipment() throws GeneralException {
        List processedLines = FastList.newInstance();
        List lines = this.getLines();
        Iterator i = lines.iterator();
        while (i.hasNext()) {
            PackingSessionLine line = (PackingSessionLine) i.next();
            if (this.checkLine(processedLines, line)) {
                BigDecimal totalPacked = this.getPackedQuantity(line.getOrderId(),  line.getOrderItemSeqId(),
                        line.getShipGroupSeqId(), line.getProductId(), line.getInventoryItemId(), -1);

                line.issueItemToShipment(shipmentId, picklistBinId, userLogin, totalPacked, getDispatcher());
                processedLines.add(line);
            }
        }
    }

    protected boolean checkLine(List processedLines, PackingSessionLine line) {
        Iterator i = processedLines.iterator();
        while (i.hasNext()) {
            PackingSessionLine l = (PackingSessionLine) i.next();
            if (line.isSameItem(l)) {
                line.setShipmentItemSeqId(l.getShipmentItemSeqId());
                return false;
            }
        }

        return true;
    }
    
    protected void createPackages() throws GeneralException {
        for (int i = 0; i < packageSeq; i++) {
            String shipmentPackageSeqId = UtilFormatOut.formatPaddedNumber(i+1, 5);

            Map pkgCtx = FastMap.newInstance();
            pkgCtx.put("shipmentId", shipmentId);
            pkgCtx.put("shipmentPackageSeqId", shipmentPackageSeqId);
            //pkgCtx.put("shipmentBoxTypeId", "");
            pkgCtx.put("weight", getPackageWeight(i+1));
            pkgCtx.put("weightUomId", getWeightUomId());
            pkgCtx.put("userLogin", userLogin);
            Map newPkgResp = this.getDispatcher().runSync("createShipmentPackage", pkgCtx);

            if (ServiceUtil.isError(newPkgResp)) {
                throw new GeneralException(ServiceUtil.getErrorMessage(newPkgResp));
            }
        }
    }

    protected void applyItemsToPackages() throws GeneralException {
        List lines = this.getLines();
        Iterator i = lines.iterator();
        while (i.hasNext()) {
            PackingSessionLine line = (PackingSessionLine) i.next();
            line.applyLineToPackage(shipmentId, userLogin, getDispatcher());
        }
    }

    protected void updateShipmentRouteSegments() throws GeneralException {
    	BigDecimal shipmentWeight = getTotalWeight();
        if (shipmentWeight.compareTo(BigDecimal.ZERO) <= 0) return;
        List shipmentRouteSegments = getDelegator().findByAnd("ShipmentRouteSegment", UtilMisc.toMap("shipmentId", this.getShipmentId()));
        if (! UtilValidate.isEmpty(shipmentRouteSegments)) {
            Iterator srit = shipmentRouteSegments.iterator();
            while (srit.hasNext()) {
                GenericValue shipmentRouteSegment = (GenericValue) srit.next();
                shipmentRouteSegment.set("billingWeight", shipmentWeight);
                shipmentRouteSegment.set("billingWeightUomId", getWeightUomId());
            }
            getDelegator().storeAll(shipmentRouteSegments);
        }
    }
 
    protected void setShipmentToPacked() throws GeneralException {
        Map packedCtx = UtilMisc.toMap("shipmentId", shipmentId, "statusId", "SHIPMENT_PACKED", "userLogin", userLogin);
        Map packedResp = this.getDispatcher().runSync("updateShipment", packedCtx);
        if (packedResp != null && ServiceUtil.isError(packedResp)) {
            throw new GeneralException(ServiceUtil.getErrorMessage(packedResp));
        }
    }

    protected void setPickerOnPicklist() throws GeneralException {
        if (picklistBinId != null) {
            // first find the picklist id
            GenericValue bin = this.getDelegator().findByPrimaryKey("PicklistBin", UtilMisc.toMap("picklistBinId", picklistBinId));
            if (bin != null) {
                Map ctx = FastMap.newInstance();
                ctx.put("picklistId", bin.getString("picklistId"));
                ctx.put("partyId", pickerPartyId);
                ctx.put("roleTypeId", "PICKER");

                // check if the role already exists and is valid
                List currentRoles = this.getDelegator().findByAnd("PicklistRole", ctx);
                currentRoles = EntityUtil.filterByDate(currentRoles);

                // if not; create the role
                if (UtilValidate.isNotEmpty(currentRoles)) {
                    ctx.put("userLogin", userLogin);
                    Map addRole = this.getDispatcher().runSync("createPicklistRole", ctx);
                    if (ServiceUtil.isError(addRole)) {
                        throw new GeneralException(ServiceUtil.getErrorMessage(addRole));
                    }
                }
            }
        }
    }

    public BigDecimal getAdditionalShippingCharge() {
        return additionalShippingCharge;
    }

    public void setAdditionalShippingCharge(BigDecimal additionalShippingCharge) {
        this.additionalShippingCharge = additionalShippingCharge;
    }
    
    public BigDecimal getTotalWeight() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < packageSeq; i++) {
        	BigDecimal packageWeight = getPackageWeight(i);
            if (! UtilValidate.isEmpty(packageWeight)) {
                total = total.add(packageWeight);
            }
        }
        return total;
    }

    public BigDecimal getShipmentCostEstimate(GenericValue orderItemShipGroup, String productStoreId, List shippableItemInfo, BigDecimal shippableTotal, BigDecimal shippableWeight, BigDecimal shippableQuantity) {
        return getShipmentCostEstimate(orderItemShipGroup.getString("contactMechId"), orderItemShipGroup.getString("shipmentMethodTypeId"),
                                       orderItemShipGroup.getString("carrierPartyId"), orderItemShipGroup.getString("carrierRoleTypeId"), 
                                       productStoreId, shippableItemInfo, shippableTotal, shippableWeight, shippableQuantity);
    }
    
    public BigDecimal getShipmentCostEstimate(GenericValue orderItemShipGroup, String productStoreId) {
        return getShipmentCostEstimate(orderItemShipGroup.getString("contactMechId"), orderItemShipGroup.getString("shipmentMethodTypeId"),
                                       orderItemShipGroup.getString("carrierPartyId"), orderItemShipGroup.getString("carrierRoleTypeId"), 
                                       productStoreId, null, null, null, null);
    }
    
    public BigDecimal getShipmentCostEstimate(String shippingContactMechId, String shipmentMethodTypeId, String carrierPartyId, String carrierRoleTypeId, String productStoreId, List shippableItemInfo, BigDecimal shippableTotal, BigDecimal shippableWeight, BigDecimal shippableQuantity) {

        BigDecimal shipmentCostEstimate = null;
        Map serviceResult = null;
        try {
            Map serviceContext = FastMap.newInstance();
            serviceContext.put("shippingContactMechId", shippingContactMechId);
            serviceContext.put("shipmentMethodTypeId", shipmentMethodTypeId);
            serviceContext.put("carrierPartyId", carrierPartyId);
            serviceContext.put("carrierRoleTypeId", carrierRoleTypeId);
            serviceContext.put("productStoreId", productStoreId);
    
            if (UtilValidate.isEmpty(shippableItemInfo)) {
                shippableItemInfo = FastList.newInstance();
                Iterator lit = getLines().iterator();
                while (lit.hasNext()) {
                    PackingSessionLine line = (PackingSessionLine) lit.next();
                    List oiasgas = getDelegator().findByAnd("OrderItemAndShipGroupAssoc", UtilMisc.toMap("orderId", line.getOrderId(), "orderItemSeqId", line.getOrderItemSeqId(), "shipGroupSeqId", line.getShipGroupSeqId()));
                    shippableItemInfo.addAll(oiasgas);
                }
            }
            serviceContext.put("shippableItemInfo", shippableItemInfo);

            if (UtilValidate.isEmpty(shippableWeight)) {
                shippableWeight = getTotalWeight();
            }
            serviceContext.put("shippableWeight", shippableWeight);

            if (UtilValidate.isEmpty(shippableQuantity)) {
                shippableQuantity = getPackedQuantity(-1);
            }
            serviceContext.put("shippableQuantity", shippableQuantity);

            if (UtilValidate.isEmpty(shippableTotal)) {
                shippableTotal = BigDecimal.ZERO;
            }
            serviceContext.put("shippableTotal", shippableTotal);
    
            serviceResult = getDispatcher().runSync("calcShipmentCostEstimate", serviceContext);
        } catch( GenericEntityException e ) {
            Debug.logError(e, module);
        } catch( GenericServiceException e ) {
            Debug.logError(e, module);
        }
        
        if (! UtilValidate.isEmpty(serviceResult.get("shippingEstimateAmount"))) {
            shipmentCostEstimate = (BigDecimal) serviceResult.get("shippingEstimateAmount");
        }
        
        return shipmentCostEstimate;
        
    }
   
    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }
    
    public List getPackageSeqIds() {
        Set packageSeqIds = new TreeSet();
        if (! UtilValidate.isEmpty(this.getLines())) {
            Iterator lit = this.getLines().iterator();
            while (lit.hasNext()) {
                PackingSessionLine line = (PackingSessionLine) lit.next();
                packageSeqIds.add(new Integer(line.getPackageSeq()));
            }
        }
        return new ArrayList(packageSeqIds);
    }
    
    public void setPackageWeight(int packageSeqId, BigDecimal packageWeight) {
        if (UtilValidate.isEmpty(packageWeight)) {
            packageWeights.remove(new Integer(packageSeqId));
        } else {
            packageWeights.put(new Integer(packageSeqId), packageWeight);
        }
    }
    
    public BigDecimal getPackageWeight(int packageSeqId) {
        if (this.packageWeights == null) return null;
        BigDecimal packageWeight = null;
        Object p = packageWeights.get(new Integer(packageSeqId));
        if (p != null) {
            packageWeight = (BigDecimal) p;
        }
        return packageWeight;
    }
    
    public void addToPackageWeight(int packageSeqId, BigDecimal weight) {
        if (UtilValidate.isEmpty(weight)) return;
        BigDecimal packageWeight = getPackageWeight(packageSeqId);
        BigDecimal newPackageWeight = UtilValidate.isEmpty(packageWeight) ? weight : weight.add(packageWeight);
        setPackageWeight(packageSeqId, newPackageWeight);
    }

    class ItemDisplay extends AbstractMap {

        public GenericValue orderItem;
        public BigDecimal quantity;
        public String productId;

        public ItemDisplay(GenericValue v) {
            if ("PicklistItem".equals(v.getEntityName())) {
                quantity = v.getBigDecimal("quantity").setScale(2, BigDecimal.ROUND_HALF_UP);
                try {
                    orderItem = v.getRelatedOne("OrderItem");
                    productId = v.getRelatedOne("InventoryItem").getString("productId");
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                }
            } else {
                // this is an OrderItemAndShipGrpInvResAndItemSum
                orderItem = v;
                productId = v.getString("inventoryProductId");
                quantity = v.getBigDecimal("totQuantityReserved").setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            Debug.log("created item display object quantity: " + quantity + " (" + productId + ")", module);
        }

        public GenericValue getOrderItem() {
            return orderItem;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public Set entrySet() {
            return null;
        }

        public Object get(Object name) {
            if ("orderItem".equals(name.toString())) {
                return orderItem;
            } else if ("quantity".equals(name.toString())) {
                return quantity;
            } else if ("productId".equals(name.toString())) {
                return productId;
            }
            return null;
        }

        public boolean equals(Object o) {
            if (o instanceof ItemDisplay) {
                ItemDisplay d = (ItemDisplay) o;
                boolean sameOrderItemProduct = true;
                if (d.getOrderItem().getString("productId") != null && orderItem.getString("productId") != null) {
                    sameOrderItemProduct = d.getOrderItem().getString("productId").equals(orderItem.getString("productId"));
                } else if (d.getOrderItem().getString("productId") != null || orderItem.getString("productId") != null) {
                    sameOrderItemProduct = false;
                }
                return (d.productId.equals(productId) &&
                        d.getOrderItem().getString("orderItemSeqId").equals(orderItem.getString("orderItemSeqId")) && 
                        sameOrderItemProduct);
            } else {
                return false;
            }            
        }
    }
}
