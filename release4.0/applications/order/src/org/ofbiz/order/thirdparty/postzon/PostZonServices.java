package org.ofbiz.order.thirdparty.postzon;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilURL;
import org.ofbiz.datafile.DataFile;
import org.ofbiz.datafile.DataFileException;
import org.ofbiz.datafile.Record;
import org.ofbiz.datafile.RecordIterator;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.security.Security;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

/**
 * PostZon and other PAF related tables import services
 */
public class PostZonServices {

    public static final String module = PostZonServices.class.getName();
    public static final String postZonDataFile = "org/ofbiz/order/thirdparty/PostZon/PostZonFile.xml";
    public static final String postZonData = "PostZonData";
    public static final String resource_error = "OrderErrorUiLabels";
    
    // import PostZonLookup table service    
    public static Map importPostZonData(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        Security security = dctx.getSecurity();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String postZonFileLocation = (String) context.get("postZonFileLocation");
        Locale locale = (Locale) context.get("locale");

        // do security check
        if (!security.hasPermission("SERVICE_INVOKE_ANY", userLogin)) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderYouDoNotHavePermissionToLoadPostZonTables",locale));
        }

        // load the data file
        DataFile tdf = null;
        try {
            tdf = DataFile.makeDataFile(UtilURL.fromResource(postZonDataFile), postZonData);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToReadPostZonDataFile",locale));
        }

        // locate the file to be imported
        URL tUrl = UtilURL.fromResource(postZonFileLocation);
        if (tUrl == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToLocatePostZonAtLocation", UtilMisc.toMap("postZonFileLocation",postZonFileLocation), locale));
        }

        RecordIterator tri = null;
        try {
            tri = tdf.makeRecordIterator(tUrl);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderProblemGettingTheRecordIterator",locale));
        }
        if (tri != null) {
            Record entry = null;
            postCodesUsed();
            while (tri.hasNext()) {
                entry = null;
                try {
                    entry = tri.next();
                } catch (DataFileException e) {
                    Debug.logError(e, module);
                }
                //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                if ("data".equals(entry.getRecordName())) {                                
                    Iterator pci = postCodesUsedList.iterator();
                    String postCodeUsed = null;
                    String postCodeToCompare = entry.getStringAndEmpty("postCode");
                    if (null == postCodeToCompare) { // skip header and trailer
                        continue;
                    }
                    //Debug.log("postCodeToCompare = " + postCodeToCompare);
                    boolean used = false;
                    while (pci.hasNext()) {
                        postCodeUsed = (String) pci.next();
                        int postCodeUsedLength = postCodeUsed.length();
                        //Debug.log("postCodeUsed " + postCodeUsed);
                        if (postCodeUsed.equals(postCodeToCompare.substring(0, postCodeUsedLength))) {
                            used = true;
                            break;
                        }
                    }
                    //if (used && null != entry.get("gridRefEast") && null != entry.get("gridRefNorth")) {
                    if (used) {
                        GenericValue newValue = delegator.makeValue("PostZonLookup", null);
                        // PK fields
                        newValue.set("postCodeId", entry.getStringAndEmpty("postCode").trim().replace(' ', '-'));
                                
                        // non-PK fields                    
                        newValue.set("outCode", entry.getStringAndEmpty("postCode").substring(0,4).trim());
                        newValue.set("inCode", entry.getStringAndEmpty("postCode").substring(4).trim());
                        newValue.set("inCodeSector",  Long.valueOf(entry.getStringAndEmpty("postCode").substring(4,5)));
                        newValue.set("postCodeName", newValue.getString("outCode") + " "  + newValue.getString("inCode"));
                        if (null != entry.get("gridRefEast")) {
                            newValue.set("gridRefEast", new Long(Long.valueOf(entry.getStringAndEmpty("gridRefEast")).longValue() * 10));
                            newValue.set("gridRefNorth", new Long(Long.valueOf(entry.getStringAndEmpty("gridRefNorth")).longValue() * 10));
                        }
                        else {
                            newValue.set("gridRefEast", new Long(0));
                            newValue.set("gridRefNorth", new Long(0));                            
                        }
                            
                        try {
                            delegator.createOrStore(newValue);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                        }
        
                        // console log
                        //Debug.log(newValue.get("postCodeId") + " / " + newValue.get("gridRefEast").toString() + " / " + newValue.get("gridRefNorth").toString());
                    }
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }
    
    public static final String addressDataFile = "org/ofbiz/order/thirdparty/PostZon/AddressFile.xml";
    public static final String addressData = "AddressData";
    
    // import PAF addresses table service    
    public static Map importAddressData(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        Security security = dctx.getSecurity();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String addressFileLocation = (String) context.get("addressFileLocation");
        Locale locale = (Locale) context.get("locale");

        // do security check
        if (!security.hasPermission("SERVICE_INVOKE_ANY", userLogin)) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderYouDoNotHavePermissionToLoadPostZonTables",locale));
        }

        // load the data file
        DataFile tdf = null;
        try {
            tdf = DataFile.makeDataFile(UtilURL.fromResource(addressDataFile), addressData);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToReadAddressDataFile",locale));
        }

        // locate the file to be imported
        URL tUrl = UtilURL.fromResource(addressFileLocation);
        if (tUrl == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToLocateAddressAtLocation", UtilMisc.toMap("addressFileLocation",addressFileLocation), locale));
        }
        //int i = 0;
        RecordIterator tri = null;
        try {
            tri = tdf.makeRecordIterator(tUrl);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderProblemGettingTheRecordIterator",locale));
        }
        if (tri != null) {
            Record entry = null;
            postCodesUsed();
            while (tri.hasNext()) {
                entry = null;
                try {
                    entry = tri.next();
                } catch (DataFileException e) {
                    Debug.logError(e, module);
                }
                //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                //Debug.log("i = " + new Integer (i++).toString());
                if ("localityDetails".equals(entry.getRecordName())
                        || "thoroughfareDetails".equals(entry.getRecordName())
                        || "deliveryPointDetails".equals(entry.getRecordName())) {                                
                    Iterator pci = postCodesUsedList.iterator();
                    String postCodeUsed = null;
                    String postCodeToCompare = entry.getStringAndEmpty("postCode");
                    if (null == postCodeToCompare) { // skip header and trailer
                        continue;
                    }
                    //Debug.log("postCodeToCompare = " + postCodeToCompare);
                    boolean used = false;
                    while (pci.hasNext()) {
                        postCodeUsed = (String) pci.next();
                        int postCodeUsedLength = postCodeUsed.length();
                        //Debug.log("postCodeUsed " + postCodeUsed);
                        if (postCodeUsed.equals(postCodeToCompare.substring(0, postCodeUsedLength))) {
                            used = true;
                            break;
                        }
                    }
                    if (used && "localityDetails".equals(entry.getRecordName())) {
                        //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                        GenericValue newValue = delegator.makeValue("LocalityDetails", null);
                        // PK field
                        newValue.set("postCodeId", entry.getStringAndEmpty("postCode").trim().replace(' ', '-'));
                                                        
                        // non-PK fields                    
                        newValue.set("postCodeType", entry.getStringAndEmpty("postCodeType"));
                        newValue.set("postTown", entry.getStringAndEmpty("postTown").trim());
                        newValue.set("dependentLocality", entry.getStringAndEmpty("dependentLocality").trim());
                        newValue.set("doubleDependentLocality", entry.getStringAndEmpty("doubleDependentLocality").trim());
                            
                        try {
                            delegator.createOrStore(newValue);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                        }
        
                        // console log
                        //Debug.log(newValue.get("postCodeId") + " postTown : " + newValue.get("postTown"));
                    }
                    else if (used && "thoroughfareDetails".equals(entry.getRecordName())) {
                        //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                        GenericValue newValue = delegator.makeValue("ThoroughfareDetails", null);
                        // PK field
                        newValue.set("postCodeId", entry.getStringAndEmpty("postCode").trim().replace(' ', '-'));
                        newValue.set("thoroughfareSeqNb", entry.getStringAndEmpty("thoroughfareSeqNb"));                       
                                                        
                        // non-PK fields                    
                        newValue.set("postCodeType", entry.getStringAndEmpty("postCodeType"));
                        newValue.set("thoroughfareName", entry.getStringAndEmpty("thoroughfareName").trim());
                        newValue.set("thoroughfareDescriptor", entry.getStringAndEmpty("thoroughfareDescriptor").trim());
                        newValue.set("dependentThoroughfareName", entry.getStringAndEmpty("dependentThoroughfareName").trim());
                        newValue.set("dependentThoroughfareDesc", entry.getStringAndEmpty("dependentThoroughfareDesc").trim());
                            
                        try {
                            delegator.createOrStore(newValue);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                        }
        
                        // console log
                        //Debug.log(newValue.get("postCodeId") + " thoroughfareName : " + newValue.get("thoroughfareName"));
                    }
                    else if (used && "deliveryPointDetails".equals(entry.getRecordName())) {
                        //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                        GenericValue newValue = delegator.makeValue("DeliveryPointDetails", null);
                        // PK fields
                        newValue.set("postCodeId", entry.getStringAndEmpty("postCode").trim().replace(' ', '-'));
                        newValue.set("addressKeyId", entry.getLong("addressKey"));
                        newValue.set("organisationKeyId", entry.getLong("organisationKey"));                                               
                                                                                                        
                        // non-PK fields                    
                        newValue.set("postCodeType", entry.getStringAndEmpty("postCodeType"));
                        newValue.set("thoroughfareSeqNb", entry.getStringAndEmpty("thoroughfareSeqNb"));                       
                        newValue.set("delivPointSufx", entry.getStringAndEmpty("DeliveryPointSuffix"));    
                        newValue.set("buildingNumber", entry.getLong("buildingNumber"));
                        newValue.set("buildingName", entry.getStringAndEmpty("buildingName").trim());
                        newValue.set("subBuildingName", entry.getStringAndEmpty("subBuildingName").trim());
                        newValue.set("organisationName", entry.getStringAndEmpty("organisationName").trim());
                        newValue.set("departementName", entry.getStringAndEmpty("departementName").trim());
                        newValue.set("poBoxNb", entry.getStringAndEmpty("poBoxNb").trim());
                        newValue.set("nbOfHouseHolds", entry.getLong("nbOfHouseHolds"));
                        newValue.set("smallUserOrgIndic", "Y" == entry.getStringAndEmpty("smallUserOrgIndic") ? "Y" : "N" );
                        
                        try {
                            delegator.createOrStore(newValue);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                        }
        
                        // console log                        
                        //Debug.log(newValue.get("postCodeId") + " buildingName : " + newValue.get("buildingName"));
                    }
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }
    
    public static final String azMapDataFile = "org/ofbiz/order/thirdparty/PostZon/AzMapFile.xml";
    public static final String azMapData = "AzMapData";
    
    //  import AZMapLookup table service
    public static Map importAzMapData(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        Security security = dctx.getSecurity();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String azMapFileLocation = (String) context.get("azMapFileLocation");
        Locale locale = (Locale) context.get("locale");

        // do security check
        if (!security.hasPermission("SERVICE_INVOKE_ANY", userLogin)) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderYouDoNotHavePermissionToLoadPostZonTables",locale));
        }

        // load the data file
        DataFile tdf = null;
        try {
            tdf = DataFile.makeDataFile(UtilURL.fromResource(azMapDataFile), azMapData);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToReadAzMapDataFile",locale));
        }

        // locate the file to be imported
        URL tUrl = UtilURL.fromResource(azMapFileLocation);
        if (tUrl == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToLocateAzMapFileAtLocation", UtilMisc.toMap("azMapFileLocation",azMapFileLocation), locale));
        }

        RecordIterator tri = null;
        try {
            tri = tdf.makeRecordIterator(tUrl);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderProblemGettingTheRecordIterator",locale));
        }
        if (tri != null) {
            Record entry = null;
            postCodesUsed();
            while (tri.hasNext()) {
                entry = null;
                try {
                    entry = tri.next();
                } catch (DataFileException e) {
                    Debug.logError(e, module);
                }
                //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                if ("data".equals(entry.getRecordName())) {                    
                    GenericValue newValue = delegator.makeValue("AzMapLookup", null);
                    // PK fields
                    //Debug.log("entry.getString('square').trim() = " + entry.getStringAndEmpty("square").trim().replace(' ', '-'));                    
                    newValue.set("squareId", entry.getStringAndEmpty("square").trim().replace(' ', '-'));
    
                    // non-PK fields                        
                    newValue.set("squareName", entry.getStringAndEmpty("square").trim());
                    newValue.set("gridRefLeft", entry.getLong("gridRefLeft"));
                    newValue.set("gridRefRight", entry.getLong("gridRefRight"));
                    newValue.set("gridRefBottom", entry.getLong("gridRefBottom"));
                    newValue.set("gridRefTop", entry.getLong("gridRefTop"));
                        
                    try {
                        //Debug.log("newValue = " + newValue.toString());
                        delegator.createOrStore(newValue);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                    }
    
                    // console log
                    //Debug.log(newValue.get("square") + " / " + newValue.get("gridRefLeft")  + " / " + newValue.get("gridRefRight")  + " / " + newValue.get("gridRefBottom")  + " / " + newValue.get("gridRefTop")); 
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }
    
    public static List postCodesUsedList = null;
    /** Returns a List of used post codes */
    public static List postCodesUsed() {
        if (postCodesUsedList == null) {
            synchronized(UtilMisc.class) {
                if (postCodesUsedList == null) {
                    postCodesUsedList = new LinkedList();
                    String postCodesString = UtilProperties.getPropertyValue("postzon", "postcodes.used");
                    if (postCodesString != null && postCodesString.length() > 0) { // check if post codes need to be limited according postzon.properties file
                        int end = -1;
                        int start = 0;
                        for (int i=0; start < postCodesString.length(); i++) {
                            end = postCodesString.indexOf(",", start);
                            if (end == -1) {
                                end = postCodesString.length();
                            }
                            postCodesUsedList.add(postCodesString.substring(start, end));
                            start = end + 1;
                        }
                    }
                }
            }
        }
        return postCodesUsedList;
    }
    
    public static Map testFindAzMapSquareFromPostCode(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        LocalDispatcher dispatcher = dctx.getDispatcher();
        
        List postZonLookupList = null;
        try {
            postZonLookupList = delegator.findAll("PostZonLookup");
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        }
        Iterator i = postZonLookupList.iterator();
        Map result = null;
        while (i.hasNext()) {
            GenericValue postZonLookup = (GenericValue) i.next();
            try {
                result = dispatcher.runSync("findAzMapSquareFromPostCode", UtilMisc.toMap("postCodeId", postZonLookup.get("postCodeId"),
                        "userLogin", userLogin));
                Long counter = Long.valueOf(result.get("counter").toString());
                long counterValue = counter.longValue(); 
                if ( counterValue > 1 ||counterValue ==  0) {
                    Debug.log("counter = " + result.get("counter"));
                    return ServiceUtil.returnFailure();
                }
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            }              
        }
        return ServiceUtil.returnSuccess();
    }
    
    public static final String postCodeSectorSiteAllocationDataFile = "org/ofbiz/order/thirdparty/PostZon/PostCodeSectorSiteAllocation.xml";
    public static final String postCodeSectorSiteAllocationData = "PostCodeSectorSiteAllocation";    
    
    //  import PostCodeSectorSiteAllocation table service
    public static Map importPostCodeSectorSiteAllocationData(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        Security security = dctx.getSecurity();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String postCodeSectorSiteAllocationFileLocation = (String) context.get("postCodeSectorSiteAllocationFileLocation");
        Locale locale = (Locale) context.get("locale");

        // do security check
        if (!security.hasPermission("SERVICE_INVOKE_ANY", userLogin)) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderYouDoNotHavePermissionToLoadPostZonTables",locale));
        }

        // load the data file
        DataFile tdf = null;
        try {
            tdf = DataFile.makeDataFile(UtilURL.fromResource(postCodeSectorSiteAllocationDataFile), postCodeSectorSiteAllocationData);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToReadPostCodeSectorSiteAllocationDataFile",locale));
        }

        // locate the file to be imported
        URL tUrl = UtilURL.fromResource(postCodeSectorSiteAllocationFileLocation);
        if (tUrl == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToLocatePostCodeSectorSiteAllocationFileAtLocation", UtilMisc.toMap("postCodeSectorSiteAllocationFileLocation",postCodeSectorSiteAllocationFileLocation), locale));
        }

        RecordIterator tri = null;
        try {
            tri = tdf.makeRecordIterator(tUrl);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderProblemGettingTheRecordIterator",locale));
        }
        if (tri != null) {
            Record entry = null;
            postCodesUsed();
            while (tri.hasNext()) {
                entry = null;
                try {
                    entry = tri.next();
                } catch (DataFileException e) {
                    Debug.logError(e, module);
                }
                //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                if ("data".equals(entry.getRecordName()) && ! "NULL".equals(entry.getStringAndEmpty("siteId").trim().toUpperCase())) {
                    GenericValue newValue = delegator.makeValue("PostCodeSectorSiteAlloc", null);
                    // PK fields                   
                    newValue.set("outCodeId", entry.getStringAndEmpty("outCode").trim()); 
                    newValue.set("inCodeSectorId",  entry.getLong("inCodeSector")); 
                    newValue.set("siteId", Long.valueOf(entry.getStringAndEmpty("siteId").trim()));
    
                    // non-PK fields                        
                    newValue.set("exclusiv", null == entry.getStringAndEmpty("exclusive") ? "Y" : "N" );
                        
                    try {
                        //Debug.log("newValue = " + newValue.toString());
                        delegator.createOrStore(newValue);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                    }
    
                    // console log
                    //Debug.log(newValue.get("outCodeSectorId") + ""); 
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }

    public static final String postCodeSectorSiteExceptionDataFile = "org/ofbiz/order/thirdparty/PostZon/PostCodeSectorSiteException.xml";
    public static final String postCodeSectorSiteExceptionData = "PostCodeSectorSiteException";    
    
    //  import PostCodeSectorSiteException table service
    public static Map importPostCodeSectorSiteExceptionData(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        Security security = dctx.getSecurity();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String postCodeSectorSiteExceptionFileLocation = (String) context.get("postCodeSectorSiteExceptionFileLocation");
        Locale locale = (Locale) context.get("locale");

        // do security check
        if (!security.hasPermission("SERVICE_INVOKE_ANY", userLogin)) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderYouDoNotHavePermissionToLoadPostZonTables",locale));
        }

        // load the data file
        DataFile tdf = null;
        try {
            tdf = DataFile.makeDataFile(UtilURL.fromResource(postCodeSectorSiteExceptionDataFile), postCodeSectorSiteExceptionData);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToReadPostCodeSectorSiteExceptionDataFile",locale));
        }

        // locate the file to be imported
        URL tUrl = UtilURL.fromResource(postCodeSectorSiteExceptionFileLocation);
        if (tUrl == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToLocatePostCodeSectorSiteExceptionFileAtLocation", UtilMisc.toMap("postCodeSectorSiteExceptionFileLocation",postCodeSectorSiteExceptionFileLocation), locale));
        }

        RecordIterator tri = null;
        try {
            tri = tdf.makeRecordIterator(tUrl);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderProblemGettingTheRecordIterator",locale));
        }
        if (tri != null) {
            Record entry = null;
            postCodesUsed();
            while (tri.hasNext()) {
                entry = null;
                try {
                    entry = tri.next();
                } catch (DataFileException e) {
                    Debug.logError(e, module);
                }
                //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                if ("data".equals(entry.getRecordName()) && ! "NULL".equals(entry.getStringAndEmpty("siteId").trim().toUpperCase())) {                    
                    GenericValue newValue = delegator.makeValue("PostCodeSectorSiteExcept", null);
                    // PK fields                    
                    newValue.set("outCodeId", entry.getStringAndEmpty("outCode").trim()); 
                    newValue.set("inCodeSectorId",  Long.valueOf(entry.getStringAndEmpty("inCode").trim().substring(0,1)));  
                    newValue.set("inCodeId",  entry.getStringAndEmpty("inCode").trim()); 
                    newValue.set("siteId", Long.valueOf(entry.getStringAndEmpty("siteId").trim()));
    
                    // non-PK fields                        
                    //newValue.set("exclusiv", null == entry.getStringAndEmpty("exclusive") ? "Y" : "N" );
                        
                    try {
                        //Debug.log("newValue = " + newValue.toString());
                        delegator.createOrStore(newValue);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                    }
    
                    // console log
                    //Debug.log(newValue.get("outCodeId") + ""); 
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }
    
    public static final String postCodeSectorSiteDescriptionDataFile = "org/ofbiz/order/thirdparty/PostZon/PostCodeSectorSiteDescription.xml";
    public static final String postCodeSectorSiteDescriptionData = "PostCodeSectorSiteDescription";    
    
    //  import PostCodeSectorSiteDescription table service
    public static Map importPostCodeSectorSiteDescriptionData(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        Security security = dctx.getSecurity();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String postCodeSectorSiteDescriptionFileLocation = (String) context.get("postCodeSectorSiteDescriptionFileLocation");
        Locale locale = (Locale) context.get("locale");

        // do security check
        if (!security.hasPermission("SERVICE_INVOKE_ANY", userLogin)) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderYouDoNotHavePermissionToLoadPostZonTables",locale));
        }

        // load the data file
        DataFile tdf = null;
        try {
            tdf = DataFile.makeDataFile(UtilURL.fromResource(postCodeSectorSiteDescriptionDataFile), postCodeSectorSiteDescriptionData);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToReadPostCodeSectorSiteDescriptionDataFile",locale));
        }

        // locate the file to be imported
        URL tUrl = UtilURL.fromResource(postCodeSectorSiteDescriptionFileLocation);
        if (tUrl == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderUnableToLocatePostCodeSectorSiteDescriptionFileAtLocation", UtilMisc.toMap("postCodeSectorSiteDescriptionFileLocation",postCodeSectorSiteDescriptionFileLocation), locale));
        }

        RecordIterator tri = null;
        try {
            tri = tdf.makeRecordIterator(tUrl);
        } catch (DataFileException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderProblemGettingTheRecordIterator",locale));
        }
        if (tri != null) {
            Record entry = null;
            postCodesUsed();
            while (tri.hasNext()) {
                entry = null;
                try {
                    entry = tri.next();
                } catch (DataFileException e) {
                    Debug.logError(e, module);
                }
                //Debug.log("entry.getRecordName() = " + entry.getRecordName());
                if ("data".equals(entry.getRecordName()) && ! "NULL".equals(entry.getStringAndEmpty("siteId").trim().toUpperCase())) {
                    GenericValue newValue = delegator.makeValue("SiteDescription", null);
                    // PK field                    
                    newValue.set("siteId", Long.valueOf(entry.getStringAndEmpty("siteId").trim()));
    
                    // non-PK field                        
                    newValue.set("siteDescription", entry.getStringAndEmpty("siteDescription").trim());
                        
                    try {
                        //Debug.log("newValue = " + newValue.toString());
                        delegator.createOrStore(newValue);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,"OrderErrorWritingRecordsToTheDatabase",locale));
                    }

                    
                    // console log
                    //Debug.log(newValue.get("outCodeId") + ""); 
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }
}