
import org.ofbiz.party.contact.*;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.util.EntityUtil;

//this script is a mix between EditContactMech.groovy from party and from facility

primaryId = parameters.primaryId;
context.primaryId = primaryId;
boolean showOld = Boolean.valueOf(parameters.SHOW_OLD);
if (UtilValidate.isEmpty(showOld)) {
    showOld = false;
}

mechMap = [:]; //Map mechMap = new HashMap();

if ( parameters.entity == "Facility") {
    facility = delegator.findOne("Facility", [facilityId : primaryId], false);
    context.facility = facility;
    ContactMechWorker.getFacilityContactMechAndRelated(request, primaryId, mechMap, showOld);
    context.mechMap = mechMap;
    
}
else if (parameters.entity == "Party") {
    ContactMechWorker.getContactMechAndRelated(request, primaryId, mechMap, showOld);
} else if (parameters.entity == "WorkEffort") {
    ContactMechWorker.getWorkEffortContactMechAndRelated(request, primaryId, mechMap, showOld);
}

context.mechMap = mechMap;

context.contactMechId = mechMap.contactMechId;
context.preContactMechTypeId = parameters.preContactMechTypeId;
context.paymentMethodId = parameters.paymentMethodId;

cmNewPurposeTypeId = parameters.contactMechPurposeTypeId;
if (cmNewPurposeTypeId) {
    contactMechPurposeType = delegator.findByPrimaryKey("ContactMechPurposeType", [contactMechPurposeTypeId : cmNewPurposeTypeId]);
    if (contactMechPurposeType) {
        context.contactMechPurposeType = contactMechPurposeType;
    } else {
        cmNewPurposeTypeId = null;
    }
    context.cmNewPurposeTypeId = cmNewPurposeTypeId;
}
//context.donePage = parameters.DONE_PAGE ?:"viewprofile?party_id=" + partyId + "&partyId=" + partyId;
