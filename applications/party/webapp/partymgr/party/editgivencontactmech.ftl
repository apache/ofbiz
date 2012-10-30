<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->
<#if !mechMap.contactMech?exists>
  <#-- When creating a new contact mech, first select the type, then actually create -->
  <#if !preContactMechTypeId?has_content>
    <h1>${uiLabelMap.PartyCreateNewContact}</h1>
    <form method="post" action="<@ofbizUrl>editcontactmech</@ofbizUrl>" name="createcontactmechform">
      <input type="hidden" name="primaryId" value="${parameters.primaryId}" />
      <input type="hidden" name="entity" value="${parameters.entity}" />
      <input type="hidden" name="portalPageId" value="${parameters.portalPageId}" />
      <input type="hidden" name="portalPortletId" value="${parameters.portalPortletId}" />
      <input type="hidden" name="portletSeqId" value="${parameters.portletSeqId}" />
      <table class="basic-table" cellspacing="0">
        <tr>
          <td class="label">${uiLabelMap.PartySelectContactType}</td>
          <td>
            <select name="preContactMechTypeId">
              <#list mechMap.contactMechTypes as contactMechType>
                <option value="${contactMechType.contactMechTypeId}">${contactMechType.get("description",locale)}</option>
              </#list>
            </select>
            <a href="javascript:document.createcontactmechform.submit()" class="smallSubmit">${uiLabelMap.CommonCreate}</a>
          </td>
        </tr>
      </table>
    </form>
    </#if>
<#elseif parameters.entity == "Party" && mechMap.partyContactMech?exists>
    <#assign entityContactMech = mechMap.partyContactMech>
<#elseif parameters.entity == "Facility" && mechMap.facilityContactMech?exists>
    <#assign entityContactMech = mechMap.facilityContactMech>
<#elseif parameters.entity == "WorkEffort" && mechMap.workEffortContactMech?exists>
    <#assign entityContactMech = mechMap.workEffortContactMech>
</#if>
<#if mechMap.contactMechTypeId?has_content>
  <#if !mechMap.contactMech?has_content>
    <h1>${uiLabelMap.PartyCreateNewContact}</h1>
    <div id="mech-purpose-types">
    <#if contactMechPurposeType?exists>
      <p>(${uiLabelMap.PartyMsgContactHavePurpose} <b>"${contactMechPurposeType.get("description",locale)?if_exists}"</b>)</p>
    </#if>
    <form method="post" action="<@ofbizUrl>${mechMap.requestName}Entity</@ofbizUrl>" id="editcontactmechform" onsubmit="javascript:submitFormDisableSubmits(this)" class="basic-form" name="editcontactmechform">
      <table class="basic-table" cellspacing="0">
        <input type="hidden" name="DONE_PAGE" value="${donePage?if_exists}" />
        <input type="hidden" name="contactMechTypeId" value="${mechMap.contactMechTypeId}" />
        <input type="hidden" name="primaryId" value="${parameters.primaryId}" />
        <input type="hidden" name="entity" value="${parameters.entity}" />
        <input type="hidden" name="portalPageId" value="${parameters.portalPageId}" />
        <input type="hidden" name="portalPortletId" value="${parameters.portalPortletId}" />
        <input type="hidden" name="portletSeqId" value="${parameters.portletSeqId}" />
        <#if cmNewPurposeTypeId?has_content><input type="hidden" name="contactMechPurposeTypeId" value="${cmNewPurposeTypeId}" /></#if>
        <#if preContactMechTypeId?exists><input type="hidden" name="preContactMechTypeId" value="${preContactMechTypeId}" /></#if>
        <#if contactMechPurposeTypeId?exists><input type="hidden" name="contactMechPurposeTypeId" value="${contactMechPurposeTypeId?if_exists}" /></#if>
        <#if paymentMethodId?has_content><input type='hidden' name='paymentMethodId' value='${paymentMethodId}' /></#if>
  <#else>
    <h1>${uiLabelMap.PartyEditContactInformation}</h1>
    <div id="mech-purpose-types">
      <table class="basic-table" cellspacing="0">
      <#if mechMap.purposeTypes?has_content>
        <tr>
          <td class="label">${uiLabelMap.PartyContactPurposes}</td>
          <td>
            <table class="basic-table" cellspacing="0">
              <#if parameters.entity == "Party" && mechMap.partyContactMechPurposes?has_content>
                  <#assign entityContactMech = mechMap.partyContactMech>
                  <#assign entityContactMechPurposes = mechMap.partyContactMechPurposes> 
              <#elseif parameters.entity == "Facility" && mechMap.facilityContactMechPurposes?has_content>
                  <#assign entityContactMech = mechMap.facilityContactMech>
                  <#assign entityContactMechPurposes = mechMap.facilityContactMechPurposes>
              </#if>
              <#if entityContactMechPurposes?has_content>
                <#list entityContactMechPurposes as entityContactMechPurpose>
                  <#assign contactMechPurposeType = entityContactMechPurpose.getRelatedOneCache("ContactMechPurposeType")>
                  <tr>
                    <td>
                      <#if contactMechPurposeType?has_content>
                        ${contactMechPurposeType.get("description",locale)}
                      <#else>
                        ${uiLabelMap.PartyPurposeTypeNotFound}: "${partyContactMechPurpose.contactMechPurposeTypeId}"
                      </#if>
                    </td>
                    <td>
                     <#assign FromDate = Static["org.ofbiz.base.util.UtilDateTime"].toDateString(entityContactMechPurpose.fromDate, "dd/MM/yyyy")/>  
                     ${FromDate}
                     <#if entityContactMechPurpose.thruDate?has_content>(${uiLabelMap.CommonExpire}: ${entityContactMechPurpose.thruDate.toString()}</#if>
                    </td>
                    <td>
                    <#if entityContactMechPurpose.thruDate?has_content>
                     <#-- Reactivate contactMechPurpose option -->
                     <a href="javascript:ajaxSubmitFormUpdateAreas('reactivateContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}', '${parameters.editAreaDivId},editContactMechMgmt,editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;contactMechId=${contactMechId}&amp;idDescription=${parameters.idDescription?if_exists}&amp;SHOW_OLD=${parameters.SHOW_OLD?if_exists}&amp;areaId=${parameters.areaId?if_exists}');"><img title="${uiLabelMap.CommonReactivate}" alt="" src="${iconsLocation}/clock_add.png"></a>
                     <form name="reactivateContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}" id="reactivateContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}"
                       method="post" action="<@ofbizUrl>updateGivenContactMechPurpose</@ofbizUrl>" >
                         <input type="hidden" name="thruDate" value="" />
                    <#else>
                     <#-- Deactivate contactMechPurpose option -->
                     <a href="javascript:ajaxSubmitFormUpdateAreas('deactivateContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}', '${parameters.editAreaDivId},editContactMechMgmt,editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;contactMechId=${contactMechId}&amp;idDescription=${parameters.idDescription?if_exists}&amp;SHOW_OLD=${parameters.SHOW_OLD?if_exists}&amp;areaId=${parameters.areaId?if_exists}');"><img title="${uiLabelMap.CommonDeactivate}" alt="" src="${iconsLocation}/clock_delete.png"></a>
                     <form name="deactivateContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}" id="deactivateContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}"
                       method="post" action="<@ofbizUrl>deleteGivenContactMechPurpose</@ofbizUrl>" >
                    </#if>
                         <input type="hidden" name="primaryId" value="${parameters.primaryId}" />
                         <input type="hidden" name="entity" value="${parameters.entity}" />
                         <input type="hidden" name="contactMechId" value="${contactMechId}" />
                         <input type="hidden" name="contactMechPurposeTypeId" value="${entityContactMechPurpose.contactMechPurposeTypeId}" />
                         <input type="hidden" name="fromDate" value="${entityContactMechPurpose.fromDate.toString()}" />
                         <input type="hidden" name="useValues" value="true"/>
                      </form>
                    </td>
                    <#--
                    <td>
                     <#-- Remove contactMechPurpose option -->
                     <#--
                     <a href="javascript:ajaxSubmitFormUpdateAreas('removeContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}', '${parameters.editAreaDivId},editContactMechMgmt,editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;contactMechId=${contactMechId}&amp;idDescription=${parameters.idDescription?if_exists}&amp;SHOW_OLD=${parameters.SHOW_OLD?if_exists}&amp;areaId=${parameters.areaId?if_exists}');"><img title="${uiLabelMap.CommonDelete}" alt="" src="${iconsLocation}/bin.png"></a>
                     <form name="removeContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}" id="removeContactMechPurpose_${entityContactMechPurpose.contactMechPurposeTypeId}"
                       method="post" action="<@ofbizUrl>removeGivenContactMechPurpose</@ofbizUrl>" >
                         <input type="hidden" name="primaryId" value="${parameters.primaryId}" />
                         <input type="hidden" name="entity" value="${parameters.entity}" />
                         <input type="hidden" name="contactMechId" value="${contactMechId}" />
                         <input type="hidden" name="contactMechPurposeTypeId" value="${entityContactMechPurpose.contactMechPurposeTypeId}" />
                         <input type="hidden" name="fromDate" value="${entityContactMechPurpose.fromDate.toString()}" />
                         <input type="hidden" name="useValues" value="true"/>
                      </form>
                    </td>
                    -->
                  </tr>
                </#list>
              </#if>
              <tr>
                </table>
                <table>
                  <td>
                    <a href="javascript:ajaxSubmitFormUpdateAreas('newpurposeform', '${parameters.editAreaDivId},editContactMechMgmt,editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;contactMechId=${contactMechId}&amp;idDescription=${parameters.idDescription?if_exists}&amp;SHOW_OLD=${parameters.SHOW_OLD?if_exists}&amp;areaId=${parameters.areaId?if_exists}');"><img title="${uiLabelMap.CommonAdd}" alt="" src="${iconsLocation}/add.png"></a>
                  </td>
                  <td>
                  <form method="post" action="<@ofbizUrl>createGivenContactMechPurpose</@ofbizUrl>" name="newpurposeform" id="newpurposeform">
                    <input type="hidden" name="primaryId" value="${parameters.primaryId}" />
                    <input type="hidden" name="entity" value="${parameters.entity}" />
                    <input type="hidden" name="DONE_PAGE" value="${donePage?if_exists}" />
                    <input type="hidden" name="useValues" value="true" />
                    <input type="hidden" name="contactMechId" value="${contactMechId?if_exists}" />
                      <select name="contactMechPurposeTypeId">
                        <option></option>
                        <#list mechMap.purposeTypes as contactMechPurposeType>
                          <option value="${contactMechPurposeType.contactMechPurposeTypeId}">${contactMechPurposeType.get("description",locale)}</option>
                        </#list>
                      </select>
                  </form>
                  </td>
              <hr>
            </table>
          </tr>
      </#if>
      </table>
      <hr>
      <form method="post" action="<@ofbizUrl>${mechMap.requestName}Entity</@ofbizUrl>" id="editcontactmechform" name="editcontactmechform">
      <table class="basic-table" cellspacing="0">
        <input type="hidden" name="contactMechId" value="${contactMechId}" />
        <input type="hidden" name="contactMechTypeId" value="${mechMap.contactMechTypeId}" />
        <input type="hidden" name="primaryId" value="${parameters.primaryId}" />
        <input type="hidden" name="entity" value="${parameters.entity}" />
        <input type="hidden" name="DONE_PAGE" value="${donePage?if_exists}" />
  </#if>
  <#if "POSTAL_ADDRESS" = mechMap.contactMechTypeId?if_exists>
    <tr>
      <td class="label">${uiLabelMap.PartyAttentionName}</td>
      <td>
        <input type="text" size="50" maxlength="100" name="attnName" value="${(mechMap.postalAddress.attnName)?default(request.getParameter('attnName')?if_exists)}" />
      </td>
    </tr>
    <tr>
      <td class="label">${uiLabelMap.PartyAddressLine1} *</td>
      <td>
        <input type="text" size="100" maxlength="255" name="address1" value="${(mechMap.postalAddress.address1)?default(request.getParameter('address1')?if_exists)}" />
      </td>
    </tr>
    <tr>
      <td class="label">${uiLabelMap.PartyAddressLine2}</td>
      <td>
        <input type="text" size="100" maxlength="255" name="address2" value="${(mechMap.postalAddress.address2)?default(request.getParameter('address2')?if_exists)}" />
      </td>
    </tr>
    <tr>
        <td class="label">
            <span id="postalCode_title">${uiLabelMap.PartyZipCode} *</span>
        </td>
        <td id="null" colspan="4">
            <span class="field-lookup">
                <input type="text" name="postalCode" size="5" id="postalCode" value="${(mechMap.postalAddress.postalCode)?default(request.getParameter('postalCode')?if_exists)}" autocomplete="off">
            </span>
        </td>
    </tr>
    <tr>
        <td class="label">
            <span id="EditCompany_city_title">${uiLabelMap.PartyCity} *</span>
        </td>
        <td id="cityArea" colspan="4">
            <input type="text" name="city" size="30" maxlength="60" value="${(mechMap.postalAddress.city)?default(request.getParameter('city')?if_exists)}">
        </td>
    </tr>
    <tr>
      <td class="label">${uiLabelMap.PartyState}</td>
      <td>
        <select name="stateProvinceGeoId">
          <option>${(mechMap.postalAddress.stateProvinceGeoId)?if_exists}</option>
          <option></option>
          ${screens.render("component://common/widget/CommonScreens.xml#states")}
        </select>
      </td>
    </tr>
    <tr>
      <td class="label">${uiLabelMap.CommonCountry}</td>
      <td>
        <select name="countryGeoId">
          <#if (mechMap.postalAddress?exists) && (mechMap.postalAddress.countryGeoId?exists)>
            <#assign defaultCountryGeoId = (mechMap.postalAddress.countryGeoId)>
            <option selected="selected" value="${defaultCountryGeoId}">
          <#else>
           <#assign defaultCountryGeoId = Static["org.ofbiz.base.util.UtilProperties"].getPropertyValue("general.properties", "country.geo.id.default")>
           <option selected="selected" value="${defaultCountryGeoId}">
          </#if>
          <#assign countryGeo = delegator.findByPrimaryKey("Geo",Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId",defaultCountryGeoId))>
          ${countryGeo.get("geoName",locale)}
          </option>
          <option></option>
          ${screens.render("component://common/widget/CommonScreens.xml#countries")}
        </select>
      </td>
    </tr>
    <#assign isUsps = Static["org.ofbiz.party.contact.ContactMechWorker"].isUspsAddress(mechMap.postalAddress)>
    <tr>
      <td class="label">${uiLabelMap.PartyIsUsps}</td>
      <td><#if isUsps>${uiLabelMap.CommonY}<#else>${uiLabelMap.CommonN}</#if>
      </td>
    </tr>

  <#elseif "TELECOM_NUMBER" = mechMap.contactMechTypeId?if_exists>
    <tr>
      <td class="label">${uiLabelMap.PartyPhoneNumber}</td>
      <td>
        <input type="hidden" name="fromDate" value="${(entityContactMech.fromDate)?default(request.getParameter('fromDate')?if_exists)}" />
      <#if parameters.useCountryCode?has_content && parameters.useCountryCode = 'Y'>
        <input type="text" size="4" maxlength="10" name="countryCode" value="${(mechMap.telecomNumber.countryCode)?default(request.getParameter('countryCode')?if_exists)}" />
        -&nbsp;
      </#if>
      <#if parameters.useAreaCode?has_content && parameters.useAreaCode = 'Y'>
        <input type="text" size="4" maxlength="10" name="areaCode" value="${(mechMap.telecomNumber.areaCode)?default(request.getParameter('areaCode')?if_exists)}" />
        -&nbsp;
      </#if>
        <input type="text" size="15" maxlength="15" name="contactNumber" value="${(mechMap.telecomNumber.contactNumber)?default(request.getParameter('contactNumber')?if_exists)}" />
      <#if parameters.useExtCode?has_content && parameters.useExtCode = 'Y'>
        &nbsp;${uiLabelMap.PartyContactExt}&nbsp;<input type="text" size="6" maxlength="10" name="extension" value="${(entityContactMech.extension)?default(request.getParameter('extension')?if_exists)}" />
      </#if>
      </td>
    </tr>
    <tr>
      <td class="label"></td>
      <td><#if parameters.useCountryCode?has_content && parameters.useCountryCode = 'Y'>[${uiLabelMap.PartyCountryCode}] </#if><#if parameters.useAreaCode?has_content && parameters.useAreaCode = 'Y'>[${uiLabelMap.PartyAreaCode}] </#if>[${uiLabelMap.PartyContactNumber}]<#if parameters.useExtCode?has_content && parameters.useExtCode = 'Y'> [${uiLabelMap.PartyContactExt}]</#if></td>
    </tr>
  <#elseif "EMAIL_ADDRESS" = mechMap.contactMechTypeId?if_exists>
    <tr>
      <td class="label">${mechMap.contactMechType.get("description",locale)}</td>
      <td>
        <input type="text" size="60" maxlength="255" name="emailAddress" value="${(mechMap.contactMech.infoString)?default(request.getParameter('emailAddress')?if_exists)}" />
      </td>
    </tr>
  <#else>
    <tr>
      <td class="label">${mechMap.contactMechType.get("description",locale)}</td>
      <td>
        <input type="hidden" name="fromDate" value="${(entityContactMech.fromDate)?default(request.getParameter('fromDate')?if_exists)}" />
        <input type="text" size="60" maxlength="255" name="infoString" value="${(mechMap.contactMech.infoString)?if_exists}" />
      </td>
    </tr>
  </#if>
  </table>
  <input type="button" onclick="ajaxSubmitFormUpdateAreas('editcontactmechform', '${parameters.areaId},showPortlet,portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;areaId=${parameters.areaId}&amp;idDescription=${parameters.idDescription?if_exists}&amp;SHOW_OLD=${parameters.SHOW_OLD?if_exists}')" value="${uiLabelMap.CommonSave}" name="submitButton" class="smallSubmit"/>
  <input type="button" onclick="ajaxUpdateArea('${parameters.areaId}', 'showPortlet', 'portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;areaId=${parameters.areaId}&amp;idDescription=${parameters.idDescription?if_exists}&amp;SHOW_OLD=${parameters.SHOW_OLD?if_exists}')" value="${uiLabelMap.CommonReturn}" name="submitButton" class="smallSubmit"/>
  </form>
  </div>
  
</#if>
