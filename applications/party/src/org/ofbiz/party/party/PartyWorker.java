/*
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
 */

package org.ofbiz.party.party;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilFormatOut;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityFunction;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.util.EntityUtil;
//#Bam# Portlet-Party
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.entity.condition.EntityDateFilterCondition;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.model.ModelKeyMap;
//#Eam# Portlet-Party

/**
 * Worker methods for Party Information
 */
public class PartyWorker {

    public static String module = PartyWorker.class.getName();

    private PartyWorker() {}

    public static Map<String, GenericValue> getPartyOtherValues(ServletRequest request, String partyId, String partyAttr, String personAttr, String partyGroupAttr) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, GenericValue> result = FastMap.newInstance();
        try {
            GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);

            if (party != null)
                result.put(partyAttr, party);
        } catch (GenericEntityException e) {
            Debug.logWarning(e, "Problems getting Party entity", module);
        }

        try {
            GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

            if (person != null)
                result.put(personAttr, person);
        } catch (GenericEntityException e) {
            Debug.logWarning(e, "Problems getting Person entity", module);
        }

        try {
            GenericValue partyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", partyId), false);

            if (partyGroup != null)
                result.put(partyGroupAttr, partyGroup);
        } catch (GenericEntityException e) {
            Debug.logWarning(e, "Problems getting PartyGroup entity", module);
        }
        return result;
    }

    /**
     * Generate a sequenced club id using the prefix passed and a sequence value + check digit
     * @param delegator used to obtain a sequenced value
     * @param prefix prefix inserted at the beginning of the ID
     * @param length total length of the ID including prefix and check digit
     * @return Sequenced Club ID string with a length as defined starting with the prefix defined
     */
    public static String createClubId(Delegator delegator, String prefix, int length) {
        final String clubSeqName = "PartyClubSeq";
        String clubId = prefix != null ? prefix : "";

        // generate the sequenced number and pad
        Long seq = delegator.getNextSeqIdLong(clubSeqName);
        clubId = clubId + UtilFormatOut.formatPaddedNumber(seq.longValue(), (length - clubId.length() - 1));

        // get the check digit
        int check = UtilValidate.getLuhnCheckDigit(clubId);
        clubId = clubId + Integer.toString(check);

        return clubId;
    }

    // #Bam# Portlet-Party
    public static Map<String, ? extends Object> preparePartyDynamicViewAndCondition(Delegator delegator, Map<String, ? extends Object> context, GenericValue userLogin)
    throws GenericEntityException {

        List<String> orderBy = FastList.newInstance();
        List<String> fieldsToSelect = FastList.newInstance();
        DynamicViewEntity dynamicView = new DynamicViewEntity();

        // default view settings
        dynamicView.addMemberEntity("PT", "Party");
        dynamicView.addAlias("PT", "partyId");
        dynamicView.addAlias("PT", "statusId");
        dynamicView.addAlias("PT", "partyTypeId");
        dynamicView.addRelation("one-nofk", "", "PartyType", ModelKeyMap.makeKeyMapList("partyTypeId"));
        dynamicView.addRelation("many", "", "UserLogin", ModelKeyMap.makeKeyMapList("partyId"));

        // define the main condition & expression list
        List<EntityCondition> andExprs = FastList.newInstance();

        // fields we need to select; will be used to set distinct
        fieldsToSelect.add("partyId");
        fieldsToSelect.add("statusId");
        fieldsToSelect.add("partyTypeId");

        // filter on parties that have relationship with logged in user
        String partyRelationshipTypeId = (String) context.get("partyRelationshipTypeId");
        if (UtilValidate.isNotEmpty(partyRelationshipTypeId)) {
            // add relation to view
            dynamicView.addMemberEntity("PRSHP", "PartyRelationship");
            dynamicView.addAlias("PRSHP", "partyIdTo");
            dynamicView.addAlias("PRSHP", "partyRelationshipTypeId");
            dynamicView.addViewLink("PT", "PRSHP", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId", "partyIdTo"));
            List<String> ownerPartyIds = UtilGenerics.cast(context.get("ownerPartyIds"));
            EntityCondition relationshipCond = null;
            if (UtilValidate.isEmpty(ownerPartyIds)) {
                String partyIdFrom = userLogin.getString("partyId");
                relationshipCond = EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("partyIdFrom"), EntityOperator.EQUALS, EntityFunction.UPPER(partyIdFrom));
            } else {
                relationshipCond = EntityCondition.makeCondition("partyIdFrom", EntityOperator.IN, ownerPartyIds);
            }
            dynamicView.addAlias("PRSHP", "partyIdFrom");
            // add the expr
            andExprs.add(EntityCondition.makeCondition(
                    relationshipCond, EntityOperator.AND,
                    EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("partyRelationshipTypeId"), EntityOperator.EQUALS, EntityFunction.UPPER(partyRelationshipTypeId))));
            fieldsToSelect.add("partyIdTo");
        }

        String contactName = (String) context.get("contactName");
        if (UtilValidate.isNotEmpty(contactName)) {
            // add relation to view
            dynamicView.addMemberEntity("PRSHPC", "PartyRelationship");
            dynamicView.addAlias("PRSHPC", "contactPartyId","partyIdTo","contactPartyId",Boolean.FALSE,Boolean.FALSE,null);
            dynamicView.addAlias("PRSHPC", "contactRelationshipTypeId","partyRelationshipTypeId","contactRelationshipTypeId",Boolean.FALSE,Boolean.FALSE,null);
            dynamicView.addAlias("PRSHPC", "contactThruDate","thruDate","contactThruDate",Boolean.FALSE,Boolean.FALSE,null);
            dynamicView.addViewLink("PT", "PRSHPC", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId", "partyIdFrom"));
            
            dynamicView.addMemberEntity("PRSHPPTY", "Party");
            dynamicView.addViewLink("PRSHPC", "PRSHPPTY", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyIdTo", "partyId"));

            dynamicView.addMemberEntity("PRSHPPSN", "Person");
            dynamicView.addAlias("PRSHPPSN", "contactName","lastName","contactName",Boolean.FALSE,Boolean.FALSE,null);
            dynamicView.addViewLink("PRSHPPTY", "PRSHPPSN", Boolean.TRUE, ModelKeyMap.makeKeyMapList("partyId"));

            dynamicView.addMemberEntity("PRSHPPTG", "PartyGroup");
            dynamicView.addAlias("PRSHPPTG", "contactGroupName","groupName","contactGroupName",Boolean.FALSE,Boolean.FALSE,null);
            dynamicView.addViewLink("PRSHPPTY", "PRSHPPTG", Boolean.TRUE, ModelKeyMap.makeKeyMapList("partyId"));
            
            //add condition
            EntityCondition contactNameCondition = EntityCondition.makeCondition(
                    EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("contactName"), EntityOperator.LIKE, EntityFunction.UPPER("%"+contactName+"%")), EntityOperator.OR,
                    EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("contactGroupName"), EntityOperator.LIKE, EntityFunction.UPPER("%"+contactName+"%")));
            fieldsToSelect.add("contactName");
            fieldsToSelect.add("contactGroupName");

            // add the expr
            andExprs.add(EntityCondition.makeCondition(
                    EntityCondition.makeCondition(
                            EntityCondition.makeCondition("contactThruDate", EntityOperator.EQUALS, null), EntityOperator.OR,
                            EntityCondition.makeCondition("contactThruDate", EntityOperator.GREATER_THAN_EQUAL_TO, UtilDateTime.nowTimestamp())),
                            EntityOperator.AND,
                            contactNameCondition));
        }

        // get the params
        String partyId = (String) context.get("partyId");
        String statusId = (String) context.get("statusId");
        String userLoginId = (String) context.get("userLoginId");
        String firstName = (String) context.get("firstName");
        String lastName = (String) context.get("lastName");
        String groupName = (String) context.get("groupName");
        String partyTypeId = (String) context.get("partyTypeId");
        String roleTypeId = (String) context.get("roleTypeId");
        String stateProvinceGeoId = (String) context.get("stateProvinceGeoId");

            // check for a partyId
            if (UtilValidate.isNotEmpty(partyId)) {
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("partyId"), EntityOperator.LIKE, EntityFunction.UPPER("%"+partyId+"%")));
            }

            // now the statusId - send ANY for all statuses; leave null for just enabled; or pass a specific status
            if (statusId != null) {
                if (!"ANY".equalsIgnoreCase(statusId)) {
                    andExprs.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, statusId));
                }
            } else {
                // NOTE: _must_ explicitly allow null as it is not included in a not equal in many databases... odd but true
                andExprs.add(EntityCondition.makeCondition(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, null), EntityOperator.OR, EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PARTY_DISABLED")));
            }
            // check for partyTypeId
            if (partyTypeId != null && !"ANY".equals(partyTypeId)) {
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("partyTypeId"), EntityOperator.LIKE, EntityFunction.UPPER("%"+partyTypeId+"%")));
            }

            // ----
            // UserLogin Fields
            // ----

            // filter on user login
            if (UtilValidate.isNotEmpty(userLoginId)) {

                // modify the dynamic view
                dynamicView.addMemberEntity("UL", "UserLogin");
                dynamicView.addAlias("UL", "userLoginId");
                dynamicView.addViewLink("PT", "UL", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));

                // add the expr
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("userLoginId"), EntityOperator.LIKE, EntityFunction.UPPER("%"+userLoginId+"%")));

                fieldsToSelect.add("userLoginId");

            }

            // ----
            // PartyGroup Fields
            // ----

            // filter on groupName
            if (UtilValidate.isNotEmpty(groupName)) {

                // modify the dynamic view
                dynamicView.addMemberEntity("PG", "PartyGroup");
                dynamicView.addAlias("PG", "groupName");
                dynamicView.addViewLink("PT", "PG", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));

                // add the expr
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("groupName"), EntityOperator.LIKE, EntityFunction.UPPER("%"+groupName+"%")));

                fieldsToSelect.add("groupName");
            }

            // ----
            // Person Fields
            // ----

            // modify the dynamic view
            if (UtilValidate.isNotEmpty(firstName) || UtilValidate.isNotEmpty(lastName)) {
                dynamicView.addMemberEntity("PE", "Person");
                dynamicView.addAlias("PE", "firstName");
                dynamicView.addAlias("PE", "lastName");
                dynamicView.addViewLink("PT", "PE", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));

                fieldsToSelect.add("firstName");
                fieldsToSelect.add("lastName");
                orderBy.add("lastName");
                orderBy.add("firstName");
            }

            // filter on firstName
            if (UtilValidate.isNotEmpty(firstName)) {
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("firstName"), EntityOperator.LIKE, EntityFunction.UPPER("%"+firstName+"%")));
            }

            // filter on lastName
            if (UtilValidate.isNotEmpty(lastName)) {
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("lastName"), EntityOperator.LIKE, EntityFunction.UPPER("%"+lastName+"%")));
            }

            // ----
            // RoleType Fields
            // ----

            // filter on role member
            if (roleTypeId != null && !"ANY".equals(roleTypeId)) {

                // add role to view
                dynamicView.addMemberEntity("PR", "PartyRole");
                dynamicView.addAlias("PR", "roleTypeId");
                dynamicView.addViewLink("PT", "PR", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));

                // add the expr
                andExprs.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, roleTypeId));

                fieldsToSelect.add("roleTypeId");
            }
            //Begin addon modification : portlet-party
            else if (UtilValidate.isNotEmpty(context.get("roleTypeGroupId"))) {
                String roleTypeGroupId = (String) context.get("roleTypeGroupId");

                //retrieve related roles
                List<GenericValue> roles = delegator.findByAnd("RoleType", UtilMisc.toMap("parentTypeId", roleTypeGroupId),null,false);
                if (UtilValidate.isNotEmpty(roles)) {
                    // add role to view
                    dynamicView.addMemberEntity("PR", "PartyRole");
                    dynamicView.addAlias("PR", "roleTypeId");
                    dynamicView.addViewLink("PT", "PR", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));

                    // add the expr
                    List<String> roleTypeIds = FastList.newInstance();
                    for (GenericValue role : roles) {
                        roleTypeIds.add(role.getString("roleTypeId"));
                    }
                    andExprs.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.IN, roleTypeIds));
                }
            }
            //End addon modification : portlet-party

            // ----
            // InventoryItem Fields
            // ----

            // filter on inventory item's fields
            String inventoryItemId = (String) context.get("inventoryItemId");
            String serialNumber = (String) context.get("serialNumber");
            String softIdentifier = (String) context.get("softIdentifier");
            if (UtilValidate.isNotEmpty(inventoryItemId) ||
                UtilValidate.isNotEmpty(serialNumber) ||
                UtilValidate.isNotEmpty(softIdentifier)) {

                // add role to view
                dynamicView.addMemberEntity("II", "InventoryItem");
                dynamicView.addAlias("II", "ownerPartyId");
                dynamicView.addViewLink("PT", "II", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId", "ownerPartyId"));
            }
            if (UtilValidate.isNotEmpty(inventoryItemId)) {
                dynamicView.addAlias("II", "inventoryItemId");
                // add the expr
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("inventoryItemId"), EntityOperator.LIKE, EntityFunction.UPPER("%" + inventoryItemId + "%")));
                fieldsToSelect.add("inventoryItemId");
            }
            if (UtilValidate.isNotEmpty(serialNumber)) {
                dynamicView.addAlias("II", "serialNumber");
                // add the expr
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("serialNumber"), EntityOperator.LIKE, EntityFunction.UPPER("%" + serialNumber + "%")));
                fieldsToSelect.add("serialNumber");
            }
            if (UtilValidate.isNotEmpty(softIdentifier)) {
                dynamicView.addAlias("II", "softIdentifier");
                // add the expr
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("softIdentifier"), EntityOperator.LIKE, EntityFunction.UPPER("%" + softIdentifier + "%")));
                fieldsToSelect.add("softIdentifier");
            }

            // ----
            // PostalAddress fields
            // ----
            //Begin addon modification : portlet-party
            /*
            if ("P".equals(extInfo)) {
            */
            String extInfo = (String) context.get("extInfo");
            if ("P".equals(extInfo) ||
                    UtilValidate.isNotEmpty(context.get("address1"))|| UtilValidate.isNotEmpty(context.get("address2"))||
                    UtilValidate.isNotEmpty(context.get("city"))|| UtilValidate.isNotEmpty(context.get("postalCode"))|| 
                    UtilValidate.isNotEmpty(context.get("countryGeoId"))|| UtilValidate.isNotEmpty(stateProvinceGeoId)) {
            //End addon modification : portlet-party
                // add address to dynamic view
                dynamicView.addMemberEntity("PC", "PartyContactMech");
                dynamicView.addMemberEntity("PA", "PostalAddress");
                dynamicView.addAlias("PC", "contactMechId");
                dynamicView.addAlias("PA", "address1");
                dynamicView.addAlias("PA", "address2");
                dynamicView.addAlias("PA", "city");
                dynamicView.addAlias("PA", "stateProvinceGeoId");
                dynamicView.addAlias("PA", "countryGeoId");
                dynamicView.addAlias("PA", "postalCode");
                dynamicView.addViewLink("PT", "PC", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));
                dynamicView.addViewLink("PC", "PA", Boolean.FALSE, ModelKeyMap.makeKeyMapList("contactMechId"));

                // filter on address1
                String address1 = (String) context.get("address1");
                if (UtilValidate.isNotEmpty(address1)) {
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("address1"), EntityOperator.LIKE, EntityFunction.UPPER("%" + address1 + "%")));
                }

                // filter on address2
                String address2 = (String) context.get("address2");
                if (UtilValidate.isNotEmpty(address2)) {
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("address2"), EntityOperator.LIKE, EntityFunction.UPPER("%" + address2 + "%")));
                }

                // filter on city
                String city = (String) context.get("city");
                if (UtilValidate.isNotEmpty(city)) {
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("city"), EntityOperator.LIKE, EntityFunction.UPPER("%" + city + "%")));
                }

                // filter on state geo
                if (stateProvinceGeoId != null && !"ANY".equals(stateProvinceGeoId)) {
                    andExprs.add(EntityCondition.makeCondition("stateProvinceGeoId", EntityOperator.EQUALS, stateProvinceGeoId));
                }

                // filter on country geo
                String countryGeoId = (String) context.get("countryGeoId");
                if (UtilValidate.isNotEmpty(countryGeoId)) {
                    andExprs.add(EntityCondition.makeCondition("countryGeoId", EntityOperator.EQUALS, countryGeoId));
                    fieldsToSelect.add("countryGeoId");
                }

                // filter on postal code
                String postalCode = (String) context.get("postalCode");
                if (UtilValidate.isNotEmpty(postalCode)) {
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("postalCode"), EntityOperator.LIKE, EntityFunction.UPPER("%" + postalCode + "%")));
                }

                fieldsToSelect.add("postalCode");
                fieldsToSelect.add("city");
                fieldsToSelect.add("stateProvinceGeoId");
            }

            // ----
            // Generic CM Fields
            // ----
            //Begin addon modification : portlet-party
            /*
            if ("O".equals(extInfo)) {
            */
            if ("O".equals(extInfo) || UtilValidate.isNotEmpty(context.get("infoString"))) {
            //End addon modification : portlet-party
                // add info to dynamic view
                dynamicView.addMemberEntity("PC", "PartyContactMech");
                dynamicView.addMemberEntity("CM", "ContactMech");
                dynamicView.addAlias("PC", "contactMechId");
                dynamicView.addAlias("CM", "infoString");
                dynamicView.addViewLink("PT", "PC", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));
                dynamicView.addViewLink("PC", "CM", Boolean.FALSE, ModelKeyMap.makeKeyMapList("contactMechId"));

                // filter on infoString
                String infoString = (String) context.get("infoString");
                if (UtilValidate.isNotEmpty(infoString)) {
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("infoString"), EntityOperator.LIKE, EntityFunction.UPPER("%"+infoString+"%")));
                    fieldsToSelect.add("infoString");
                }

            }

            // ----
            // TelecomNumber Fields
            // ----
            //Begin addon modification : portlet-party
            /*
            if ("T".equals(extInfo)) {
            */
            if ("T".equals(extInfo) ||
                    UtilValidate.isNotEmpty(context.get("countryCode")) 
                    || UtilValidate.isNotEmpty(context.get("areaCode")) 
                    || UtilValidate.isNotEmpty(context.get("contactNumber"))) {
                // add telecom to dynamic view
                dynamicView.addMemberEntity("PC", "PartyContactMech");
                dynamicView.addMemberEntity("TM", "TelecomNumber");
                dynamicView.addAlias("PC", "contactMechId");
                dynamicView.addAlias("TM", "countryCode");
                dynamicView.addAlias("TM", "areaCode");
                dynamicView.addAlias("TM", "contactNumber");
                dynamicView.addViewLink("PT", "PC", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));
                dynamicView.addViewLink("PC", "TM", Boolean.FALSE, ModelKeyMap.makeKeyMapList("contactMechId"));

                // filter on countryCode
                String countryCode = (String) context.get("countryCode");
                if (UtilValidate.isNotEmpty(countryCode)) {
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("countryCode"), EntityOperator.EQUALS, EntityFunction.UPPER(countryCode)));
                }

                // filter on areaCode
                String areaCode = (String) context.get("areaCode");
                if (UtilValidate.isNotEmpty(areaCode)) {
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("areaCode"), EntityOperator.EQUALS, EntityFunction.UPPER(areaCode)));
                }

                // filter on contact number
                String contactNumber = (String) context.get("contactNumber");
                //remove non alphanumeric 
                if (UtilValidate.isNotEmpty(contactNumber)) {
                    contactNumber = StringUtil.removeNonNumeric(contactNumber); 
                    andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("contactNumber"), EntityOperator.EQUALS, EntityFunction.UPPER(contactNumber)));
                }

                fieldsToSelect.add("contactNumber");
                fieldsToSelect.add("areaCode");
            }

            //Begin addon modification : portlet-party
            List<String> partyClassificationGroupId = UtilGenerics.checkList( context.get("partyClassificationGroupId") );
            if (UtilValidate.isNotEmpty(partyClassificationGroupId)) {
                List<EntityCondition> classCond = FastList.newInstance();
                int i = 0;
                for (String classificationGroupId : partyClassificationGroupId) {
                    GenericValue partyClass = null;
                    partyClass = delegator.findOne("PartyClassificationGroup", true, UtilMisc.toMap("partyClassificationGroupId", classificationGroupId));
                    if (partyClass == null) continue;

                    // modify the dynamic view
                    dynamicView.addMemberEntity("PCL" + i, "PartyClassification");
                    dynamicView.addAlias("PCL" + i, "partyClassificationGroupId" + i, "partyClassificationGroupId", "partyClassificationGroupId" + i, Boolean.FALSE, Boolean.FALSE, null);
                    dynamicView.addAlias("PCL" + i, "partyClassificationFromDate" + i, "fromDate", "partyClassificationFromDate" + i, Boolean.FALSE, Boolean.FALSE, null);
                    dynamicView.addAlias("PCL" + i, "partyClassificationThruDate" + i, "thruDate", "partyClassificationThruDate" + i, Boolean.FALSE, Boolean.FALSE, null);
                    dynamicView.addViewLink("PT", "PCL" + i, Boolean.TRUE, ModelKeyMap.makeKeyMapList("partyId"));

                    dynamicView.addMemberEntity("PCLG" + i, "PartyClassificationGroup");
                    dynamicView.addAlias("PCLG" + i, "partyClassificationTypeId" + i, "partyClassificationTypeId", "partyClassificationTypeId" + i, Boolean.FALSE, Boolean.FALSE, null);
                    dynamicView.addViewLink("PCL" + i, "PCLG" + i, Boolean.TRUE, ModelKeyMap.makeKeyMapList("partyClassificationGroupId"));

                    classCond.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("partyClassificationGroupId" + i), EntityOperator.EQUALS, classificationGroupId));
                    classCond.add(EntityDateFilterCondition.makeCondition(UtilDateTime.nowTimestamp(), "partyClassificationFromDate" + i, "partyClassificationThruDate" + i));
                    classCond.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("partyClassificationTypeId" + i), EntityOperator.EQUALS, partyClass.get("partyClassificationTypeId")));
                    i++;
                }
                // add the expr
                andExprs.add(EntityCondition.makeCondition(classCond)); 
                //fieldsToSelect.add("partyClassificationGroupId");
            }

            String comments = (String) context.get("comments");
            if (UtilValidate.isNotEmpty(comments) && UtilValidate.isNotEmpty(partyTypeId)) {
                if ("PERSON".equals(partyTypeId)) {
                    // modify the dynamic view
                    if (UtilValidate.isEmpty(firstName) && UtilValidate.isEmpty(lastName)) {
                        //Add memberEntity if necessary
                        dynamicView.addMemberEntity("PE", "Person");
                        dynamicView.addViewLink("PT", "PE", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));
                    }
                    dynamicView.addAlias("PE", "comments","comments","comments",Boolean.FALSE,Boolean.FALSE,null);

                } else if ("PARTY_GROUP".equals(partyTypeId)) {
                    // modify the dynamic view
                    if (UtilValidate.isEmpty(groupName)) {
                        //Add memberEntity if necessary
                        dynamicView.addMemberEntity("PG", "PartyGroup");
                        dynamicView.addViewLink("PT", "PG", Boolean.FALSE, ModelKeyMap.makeKeyMapList("partyId"));
                    }
                    dynamicView.addAlias("PG", "comments","comments","comments",Boolean.FALSE,Boolean.FALSE,null);
                }

                // add the expr
                andExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("comments"), EntityOperator.LIKE, EntityFunction.UPPER("%"+comments+"%")));

                fieldsToSelect.add("comments");
            }

            if (context.containsKey("roleTypeGroupId")) {
                dynamicView.setGroupBy(fieldsToSelect);
            }

            // ---- End of Dynamic View Creation

            // build the main condition
            Map<String, Object> result = FastMap.newInstance();
            if (andExprs.size() > 0) result.put("conditions", EntityCondition.makeCondition(andExprs, EntityOperator.AND));
            
            result.put("dynamicView", dynamicView);
            result.put("fieldsToSelect", fieldsToSelect);
            return result;
    }
    // #Eam# Portlet-Party

    public static GenericValue findPartyLatestContactMech(String partyId, String contactMechTypeId, Delegator delegator) {
        try {
            List<GenericValue> cmList = delegator.findByAnd("PartyAndContactMech", UtilMisc.toMap("partyId", partyId, "contactMechTypeId", contactMechTypeId), UtilMisc.toList("-fromDate"), false);
            cmList = EntityUtil.filterByDate(cmList);
            return EntityUtil.getFirst(cmList);
        } catch (GenericEntityException e) {
            Debug.logError(e, "Error while finding latest ContactMech for party with ID [" + partyId + "] TYPE [" + contactMechTypeId + "]: " + e.toString(), module);
            return null;
        }
    }

    public static GenericValue findPartyLatestPostalAddress(String partyId, Delegator delegator) {
        GenericValue pcm = findPartyLatestContactMech(partyId, "POSTAL_ADDRESS", delegator);
        if (pcm != null) {
            try {
                return pcm.getRelatedOne("PostalAddress", false);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Error while finding latest PostalAddress for party with ID [" + partyId + "]: " + e.toString(), module);
            }
        }
        return null;
    }

    public static GenericValue findPartyLatestPostalAddressGeoPoint(String partyId, Delegator delegator) {
        GenericValue latestPostalAddress = findPartyLatestPostalAddress(partyId, delegator);
        if (latestPostalAddress  != null) {
            try {
                GenericValue latestGeoPoint =  latestPostalAddress.getRelatedOne("GeoPoint", false);
                if (latestGeoPoint  != null) {
                    return latestGeoPoint;
                }
                return null;
            } catch (GenericEntityException e) {
                Debug.logError(e, "Error while finding latest GeoPoint for party with ID [" + partyId + "]: " + e.toString(), module);
            }
        }
        return null;
    }

    public static GenericValue findPartyLatestTelecomNumber(String partyId, Delegator delegator) {
        GenericValue pcm = findPartyLatestContactMech(partyId, "TELECOM_NUMBER", delegator);
        if (pcm != null) {
            try {
                return pcm.getRelatedOne("TelecomNumber", false);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Error while finding latest TelecomNumber for party with ID [" + partyId + "]: " + e.toString(), module);
            }
        }
        return null;
    }

    public static GenericValue findPartyLatestUserLogin(String partyId, Delegator delegator) {
        try {
            List<GenericValue> userLoginList = delegator.findByAnd("UserLogin", UtilMisc.toMap("partyId", partyId), UtilMisc.toList("-" + ModelEntity.STAMP_FIELD), false);
            return EntityUtil.getFirst(userLoginList);
        } catch (GenericEntityException e) {
            Debug.logError(e, "Error while finding latest UserLogin for party with ID [" + partyId + "]: " + e.toString(), module);
            return null;
        }
    }

    public static Timestamp findPartyLastLoginTime(String partyId, Delegator delegator) {
        try {
            List<GenericValue> loginHistory = delegator.findByAnd("UserLoginHistory", UtilMisc.toMap("partyId", partyId), UtilMisc.toList("-fromDate"), false);
            GenericValue v = EntityUtil.getFirst(loginHistory);
            if (v != null) {
                return v.getTimestamp("fromDate");
            } else {
                return null;
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, "Error while finding latest login time for party with ID [" + partyId + "]: " + e.toString(), module);
            return null;
        }

    }

    public static Locale findPartyLastLocale(String partyId, Delegator delegator) {
        // just get the most recent UserLogin for this party, if there is one...
        GenericValue userLogin = findPartyLatestUserLogin(partyId, delegator);
        if (userLogin == null) {
            return null;
        }
        String localeString = userLogin.getString("lastLocale");
        if (UtilValidate.isNotEmpty(localeString)) {
            return UtilMisc.parseLocale(localeString);
        } else {
            return null;
        }
    }

    public static String findFirstMatchingPartyId(Delegator delegator, String address1, String address2, String city,
            String stateProvinceGeoId, String postalCode, String postalCodeExt, String countryGeoId,
            String firstName, String middleName, String lastName) throws GeneralException {

        String[] info = findFirstMatchingPartyAndContactMechId(delegator, address1, address2, city, stateProvinceGeoId, postalCode,
                postalCodeExt, countryGeoId, firstName, middleName, lastName);
        if (info != null) {
            return info[0];
        }
        return null;
    }

    public static String[] findFirstMatchingPartyAndContactMechId(Delegator delegator, String address1, String address2, String city,
            String stateProvinceGeoId, String postalCode, String postalCodeExt, String countryGeoId,
            String firstName, String middleName, String lastName) throws GeneralException {

        List<GenericValue> matching = findMatchingPersonPostalAddresses(delegator, address1, address2, city, stateProvinceGeoId, postalCode,
            postalCodeExt, countryGeoId, firstName, middleName, lastName);
        GenericValue v = EntityUtil.getFirst(matching);
        if (v != null) {
            return new String[] { v.getString("partyId"), v.getString("contactMechId") };
        }
        return null;
    }

    /** Finds all matching PartyAndPostalAddress records based on the values provided.  Excludes party records with a statusId of PARTY_DISABLED.  Results are ordered by descending PartyContactMech.fromDate.
     * The matching process is as follows:
     * 1. Calls {@link #findMatchingPartyPostalAddress(Delegator, String, String, String, String, String, String, String, String)} to retrieve a list of address matched PartyAndPostalAddress records.  Results are limited to Parties of type PERSON.
     * 2. For each matching PartyAndPostalAddress record, the Person record for the Party is then retrieved and an upper case comparison is performed against the supplied firstName, lastName and if provided, middleName.
     * 
     * @param delegator             Delegator instance
     * @param address1              PostalAddress.address1 to match against (Required).
     * @param address2              Optional PostalAddress.address2 to match against.
     * @param city                  PostalAddress.city value to match against (Required).
     * @param stateProvinceGeoId    Optional PostalAddress.stateProvinceGeoId value to match against.  If null or "**" is passed then the value will be ignored during matching.  "NA" can be passed in place of "_NA_".
     * @param postalCode            PostalAddress.postalCode value to match against.  Cannot be null but can be skipped by passing a value starting with an "*".  If the length of the supplied string is 10 characters and the string contains a "-" then the postal code will be split at the "-" and the second half will be used as the postalCodeExt.
     * @param postalCodeExt         Optional PostalAddress.postalCodeExt value to match against.  Will be overridden if a postalCodeExt value is retrieved from postalCode as described above.
     * @param countryGeoId          Optional PostalAddress.countryGeoId value to match against.
     * @param firstName             Person.firstName to match against (Required).
     * @param middleName            Optional Person.middleName to match against.
     * @param lastName              Person.lastName to match against (Required).
     * @return List of PartyAndPostalAddress GenericValue objects that match the supplied criteria.
     * @throws GeneralException
     */
    public static List<GenericValue> findMatchingPersonPostalAddresses(Delegator delegator, String address1, String address2, String city,
            String stateProvinceGeoId, String postalCode, String postalCodeExt, String countryGeoId,
            String firstName, String middleName, String lastName) throws GeneralException {
        // return list
        List<GenericValue> returnList = FastList.newInstance();

        // address information
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException();
        }

        List<GenericValue> validFound = findMatchingPartyPostalAddress(delegator, address1, address2, city, stateProvinceGeoId, postalCode, postalCodeExt, countryGeoId, "PERSON");

        if (UtilValidate.isNotEmpty(validFound)) {
            for (GenericValue partyAndAddr: validFound) {
                String partyId = partyAndAddr.getString("partyId");
                if (UtilValidate.isNotEmpty(partyId)) {
                    GenericValue p = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);
                    if (p != null) {
                        String fName = p.getString("firstName");
                        String lName = p.getString("lastName");
                        String mName = p.getString("middleName");
                        if (lName.toUpperCase().equals(lastName.toUpperCase())) {
                            if (fName.toUpperCase().equals(firstName.toUpperCase())) {
                                if (mName != null && middleName != null) {
                                    if (mName.toUpperCase().equals(middleName.toUpperCase())) {
                                        returnList.add(partyAndAddr);
                                    }
                                } else if (middleName == null) {
                                    returnList.add(partyAndAddr);
                                }
                            }
                        }
                    }
                }
            }
        }

        return returnList;
    }

    /**
     * @deprecated Renamed to {@link #findMatchingPersonPostalAddresses(Delegator, String, String, String, String, String, String, String, String, String, String)}
     */
    @Deprecated
    public static List<GenericValue> findMatchingPartyAndPostalAddress(Delegator delegator, String address1, String address2, String city,
                            String stateProvinceGeoId, String postalCode, String postalCodeExt, String countryGeoId,
                            String firstName, String middleName, String lastName) throws GeneralException {
        return PartyWorker.findMatchingPersonPostalAddresses(delegator, address1, address2, city, stateProvinceGeoId, postalCode, postalCodeExt, countryGeoId, firstName, middleName, lastName);
    }

    /**
     * Finds all matching parties based on the values provided.  Excludes party records with a statusId of PARTY_DISABLED.  Results are ordered by descending PartyContactMech.fromDate.
     * 1. Candidate addresses are found by querying PartyAndPostalAddress using the supplied city and if provided, stateProvinceGeoId, postalCode, postalCodeExt and countryGeoId
     * 2. In-memory address line comparisons are then performed against the supplied address1 and if provided, address2.  Address lines are compared after the strings have been converted using {@link #makeMatchingString(Delegator, String)}.
     * 
     * @param delegator             Delegator instance
     * @param address1              PostalAddress.address1 to match against (Required).
     * @param address2              Optional PostalAddress.address2 to match against.
     * @param city                  PostalAddress.city value to match against (Required).
     * @param stateProvinceGeoId    Optional PostalAddress.stateProvinceGeoId value to match against.  If null or "**" is passed then the value will be ignored during matching.  "NA" can be passed in place of "_NA_".
     * @param postalCode            PostalAddress.postalCode value to match against.  Cannot be null but can be skipped by passing a value starting with an "*".  If the length of the supplied string is 10 characters and the string contains a "-" then the postal code will be split at the "-" and the second half will be used as the postalCodeExt.
     * @param postalCodeExt         Optional PostalAddress.postalCodeExt value to match against.  Will be overridden if a postalCodeExt value is retrieved from postalCode as described above.
     * @param countryGeoId          Optional PostalAddress.countryGeoId value to match against.
     * @param partyTypeId           Optional Party.partyTypeId to match against.
     * @return List of PartyAndPostalAddress GenericValue objects that match the supplied criteria.
     * @throws GenericEntityException
     */
    public static List<GenericValue> findMatchingPartyPostalAddress(Delegator delegator, String address1, String address2, String city, 
                            String stateProvinceGeoId, String postalCode, String postalCodeExt, String countryGeoId, String partyTypeId) throws GenericEntityException {

        if (address1 == null || city == null || postalCode == null) {
            throw new IllegalArgumentException();
        }

        List<EntityCondition> addrExprs = FastList.newInstance();
        if (stateProvinceGeoId != null) {
            if ("**".equals(stateProvinceGeoId)) {
                Debug.logWarning("Illegal state code passed!", module);
            } else if ("NA".equals(stateProvinceGeoId)) {
                addrExprs.add(EntityCondition.makeCondition("stateProvinceGeoId", EntityOperator.EQUALS, "_NA_"));
            } else {
                addrExprs.add(EntityCondition.makeCondition("stateProvinceGeoId", EntityOperator.EQUALS, stateProvinceGeoId.toUpperCase()));
            }
        }

        if (!postalCode.startsWith("*")) {
            if (postalCode.length() == 10 && postalCode.indexOf("-") != -1) {
                String[] zipSplit = postalCode.split("-", 2);
                postalCode = zipSplit[0];
                postalCodeExt = zipSplit[1];
            }
            addrExprs.add(EntityCondition.makeCondition("postalCode", EntityOperator.EQUALS, postalCode));
        }

        if (postalCodeExt != null) {
            addrExprs.add(EntityCondition.makeCondition("postalCodeExt", EntityOperator.EQUALS, postalCodeExt));
        }

        addrExprs.add(EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("city"), EntityOperator.EQUALS, EntityFunction.UPPER(city)));

        if (countryGeoId != null) {
            addrExprs.add(EntityCondition.makeCondition("countryGeoId", EntityOperator.EQUALS, countryGeoId.toUpperCase()));
        }

        // limit to only non-disabled status
        addrExprs.add(EntityCondition.makeCondition(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, null),
                EntityOperator.OR, EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PARTY_DISABLED")));

        if (partyTypeId != null) {
            addrExprs.add(EntityCondition.makeCondition("partyTypeId", EntityOperator.EQUALS, partyTypeId));
        }

        List<String> sort = UtilMisc.toList("-fromDate");
        EntityCondition addrCond = EntityCondition.makeCondition(addrExprs, EntityOperator.AND);
        List<GenericValue> addresses = EntityUtil.filterByDate(delegator.findList("PartyAndPostalAddress", addrCond, null, sort, null, false));
        //Debug.logInfo("Checking for matching address: " + addrCond.toString() + "[" + addresses.size() + "]", module);

        if (UtilValidate.isEmpty(addresses)) {
            // No address matches, return an empty list
            return addresses;
        }

        List<GenericValue> validFound = FastList.newInstance();
        // check the address line
        for (GenericValue address: addresses) {
            // address 1 field
            String addr1Source = PartyWorker.makeMatchingString(delegator, address1);
            String addr1Target = PartyWorker.makeMatchingString(delegator, address.getString("address1"));

            if (addr1Target != null) {
                Debug.logInfo("Comparing address1 : " + addr1Source + " / " + addr1Target, module);
                if (addr1Target.equals(addr1Source)) {

                    // address 2 field
                    if (address2 != null) {
                        String addr2Source = PartyWorker.makeMatchingString(delegator, address2);
                        String addr2Target = PartyWorker.makeMatchingString(delegator, address.getString("address2"));
                        if (addr2Target != null) {
                            Debug.logInfo("Comparing address2 : " + addr2Source + " / " + addr2Target, module);

                            if (addr2Source.equals(addr2Target)) {
                                Debug.logInfo("Matching address2; adding valid address", module);
                                validFound.add(address);
                                //validParty.put(address.getString("partyId"), address.getString("contactMechId"));
                            }
                        }
                    } else {
                        if (address.get("address2") == null) {
                            Debug.logInfo("No address2; adding valid address", module);
                            validFound.add(address);
                            //validParty.put(address.getString("partyId"), address.getString("contactMechId"));
                        }
                    }
                }
            }
        }
        return validFound;
    }

    /**
     * Converts the supplied String into a String suitable for address line matching.
     * Performs the following transformations on the supplied String:
     * - Converts to upper case
     * - Retrieves all records from the AddressMatchMap table and replaces all occurrences of addressMatchMap.mapKey with addressMatchMap.mapValue using upper case matching.
     * - Removes all non-word characters from the String i.e. everything except A-Z, 0-9 and _
     * @param delegator     A Delegator instance
     * @param address       The address String to convert
     * @return              The converted Address
     */
    public static String makeMatchingString(Delegator delegator, String address) {
        if (address == null) {
            return null;
        }

        // upper case the address
        String str = address.trim().toUpperCase();

        // replace mapped words
        List<GenericValue> addressMap = null;
        try {
            addressMap = delegator.findList("AddressMatchMap", null, null, UtilMisc.toList("sequenceNum"), null, false);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        }

        if (addressMap != null) {
            for (GenericValue v: addressMap) {
                str = str.replaceAll(v.getString("mapKey").toUpperCase(), v.getString("mapValue").toUpperCase());
            }
        }

        // remove all non-word characters
        return str.replaceAll("\\W", "");
    }

    public static List<String> getAssociatedPartyIdsByRelationshipType(Delegator delegator, String partyIdFrom, String partyRelationshipTypeId) {
        List<GenericValue> partyList = FastList.newInstance();
        List<String> partyIds = null;
        try {
            EntityConditionList<EntityExpr> baseExprs = EntityCondition.makeCondition(UtilMisc.toList(
                    EntityCondition.makeCondition("partyIdFrom", partyIdFrom),
                    EntityCondition.makeCondition("partyRelationshipTypeId", partyRelationshipTypeId)), EntityOperator.AND);
            List<GenericValue> associatedParties = delegator.findList("PartyRelationship", baseExprs, null, null, null, true);
            partyList.addAll(associatedParties);
            while (UtilValidate.isNotEmpty(associatedParties)) {
                List<GenericValue> currentAssociatedParties = FastList.newInstance();
                for (GenericValue associatedParty : associatedParties) {
                    EntityConditionList<EntityExpr> innerExprs = EntityCondition.makeCondition(UtilMisc.toList(
                            EntityCondition.makeCondition("partyIdFrom", associatedParty.get("partyIdTo")),
                            EntityCondition.makeCondition("partyRelationshipTypeId", partyRelationshipTypeId)), EntityOperator.AND);
                    List<GenericValue> associatedPartiesChilds = delegator.findList("PartyRelationship", innerExprs, null, null, null, true);
                    if (UtilValidate.isNotEmpty(associatedPartiesChilds)) {
                        currentAssociatedParties.addAll(associatedPartiesChilds);
                    }
                    partyList.add(associatedParty);
                }
                associatedParties  = currentAssociatedParties;
            }
            partyIds = EntityUtil.getFieldListFromEntityList(partyList, "partyIdTo", true);
        } catch (GenericEntityException e) {
            Debug.logWarning(e, module);
        }
        return partyIds;
    }

    /**
     * Generic service to find party by id.
     * By default return the party find by partyId
     * but you can pass searchPartyFirst at false if you want search in partyIdentification before
     * or pass searchAllId at true to find apartyuct with this id (party.partyId and partyIdentification.idValue)
     * @param delegator the delegator
     * @param idToFind the party id to find
     * @param partyIdentificationTypeId the party identification type id to use
     * @param searchPartyFirst search first with party id
     * @param searchAllId search all the party ids
     * @return returns the parties founds
     * @throws GenericEntityException
     */
    public static List<GenericValue> findPartiesById(Delegator delegator,
            String idToFind, String partyIdentificationTypeId,
            boolean searchPartyFirst, boolean searchAllId) throws GenericEntityException {

        if (Debug.verboseOn()) Debug.logVerbose("Analyze partyIdentification: entered id = " + idToFind + ", partyIdentificationTypeId = " + partyIdentificationTypeId, module);

        GenericValue party = null;
        List<GenericValue> partiesFound = null;

        // 1) look if the idToFind given is a real partyId
        if (searchPartyFirst) {
            party = delegator.findOne("Party", UtilMisc.toMap("partyId", idToFind), true);
        }

        if (searchAllId || (searchPartyFirst && UtilValidate.isEmpty(party))) {
            // 2) Retrieve party in PartyIdentification
            Map<String, String> conditions = UtilMisc.toMap("idValue", idToFind);
            if (UtilValidate.isNotEmpty(partyIdentificationTypeId)) {
                conditions.put("partyIdentificationTypeId", partyIdentificationTypeId);
            }
            partiesFound = delegator.findByAnd("PartyIdentificationAndParty", conditions, UtilMisc.toList("partyId"), true);
        }

        if (! searchPartyFirst) {
            party = delegator.findOne("Party", UtilMisc.toMap("partyId", idToFind), true);
        }

        if (UtilValidate.isNotEmpty(party)) {
            if (UtilValidate.isNotEmpty(partiesFound)) partiesFound.add(party);
            else partiesFound = UtilMisc.toList(party);
        }
        if (Debug.verboseOn()) Debug.logVerbose("Analyze partyIdentification: found party.partyId = " + party + ", and list : " + partiesFound, module);
        return partiesFound;
    }

    public static List<GenericValue> findPartiesById(Delegator delegator, String idToFind, String partyIdentificationTypeId)
    throws GenericEntityException {
        return findPartiesById(delegator, idToFind, partyIdentificationTypeId, true, false);
    }

    public static String findPartyId(Delegator delegator, String idToFind, String partyIdentificationTypeId) throws GenericEntityException {
        GenericValue party = findParty(delegator, idToFind, partyIdentificationTypeId);
        if (UtilValidate.isNotEmpty(party)) {
            return party.getString("partyId");
        } else {
            return null;
        }
    }

    public static String findPartyId(Delegator delegator, String idToFind) throws GenericEntityException {
        return findPartyId(delegator, idToFind, null);
    }

    public static GenericValue findParty(Delegator delegator, String idToFind, String partyIdentificationTypeId) throws GenericEntityException {
        List<GenericValue> parties = findPartiesById(delegator, idToFind, partyIdentificationTypeId);
        GenericValue party = EntityUtil.getFirst(parties);
        return party;
    }

    public static List<GenericValue> findParties(Delegator delegator, String idToFind, String partyIdentificationTypeId) throws GenericEntityException {
        List<GenericValue> partiesByIds = findPartiesById(delegator, idToFind, partyIdentificationTypeId);
        List<GenericValue> parties = null;
        if (UtilValidate.isNotEmpty(partiesByIds)) {
            for (GenericValue party : partiesByIds) {
                GenericValue partyToAdd = party;
                //retreive party GV if the actual genericValue came from viewEntity
                if (! "Party".equals(party.getEntityName())) {
                    partyToAdd = delegator.findOne("Party", UtilMisc.toMap("partyId", party.get("partyId")), true);
                }

                if (UtilValidate.isEmpty(parties)) {
                    parties = UtilMisc.toList(partyToAdd);
                }
                else {
                    parties.add(partyToAdd);
                }
            }
        }
        return parties;
    }

    public static List<GenericValue> findParties(Delegator delegator, String idToFind) throws GenericEntityException {
        return findParties(delegator, idToFind, null);
    }

    public static GenericValue findParty(Delegator delegator, String idToFind) throws GenericEntityException {
        return findParty(delegator, idToFind, null);
    }

}
