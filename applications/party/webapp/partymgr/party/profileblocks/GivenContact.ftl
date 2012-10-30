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


  <#if contactMeches?has_content>
    <table class="basic-table" cellspacing="0">
      <tr>
        <th text-align="CENTER">&nbsp;</th>
        <#if enableGeolocation?has_content && enableGeolocation == 'Y'>
        <th text-align="CENTER">&nbsp;</th>
        </#if>
        <th>${uiLabelMap.PartyContactType}</th>
        <th>${uiLabelMap.PartyContactInformation}</th>
        <th>${uiLabelMap.CommonUpdated}</th>
        <th>&nbsp;</th>
        <th>&nbsp;</th>
      </tr>
      <#assign contactMechNb = 0>
      <#list contactMeches as contactMechMap>
        <#assign contactMech = contactMechMap.contactMech>
        <#if entity == "Party">
            <#assign entityContactMech = contactMechMap.partyContactMech>
            <#assign entityContactMechPurposes = contactMechMap.partyContactMechPurposes> 
        <#elseif entity == "Facility">
            <#assign entityContactMech = contactMechMap.facilityContactMech>
            <#assign entityContactMechPurposes = contactMechMap.facilityContactMechPurposes>
        <#elseif entity == "WorkEffort">
            <#assign entityContactMech = contactMechMap.workEffortContactMech>
        </#if>
        <tr><td colspan="7"><hr /></td></tr>
        <tr>
          <td>
            <#if security.hasEntityPermission("PARTYMGR", "_UPDATE", session)>
              <a href="javascript:ajaxUpdateArea('${editAreaDivId}', 'editContactMechMgmt', 'portalPortletId=${portalPortletId}&amp;portalPageId=${portalPageId}&amp;portletSeqId=${portletSeqId}&amp;editAreaDivId=${editAreaDivId}&amp;primaryId=${parameters.primaryId}&amp;contactMechId=${contactMech.contactMechId}&amp;entity=${entity}&amp;areaId=${areaId}&amp;idDescription=${parameters.idDescription?if_exists}&amp;SHOW_OLD=${parameters.SHOW_OLD?if_exists}&amp;useAreaCode=${useAreaCode?if_exists}&amp;useCountryCode=${useCountryCode?if_exists}&amp;useExtCode=${useExtCode?if_exists}');"><img title="${uiLabelMap.CommonUpdate}" alt="" src="${iconsLocation}/page_white_edit.png"></a>
            </#if>
          </td>
            <#if enableGeolocation?has_content && enableGeolocation == 'Y'>
          <td>
            <#if contactMech.contactMechTypeId == 'POSTAL_ADDRESS'>
              <a class="label" href="showPortalPage?portalPageId=PostalAddrGeoLoc&idDescription=[${primaryId?if_exists}]&partyId=${primaryId?if_exists}&contactMechId=${contactMech.contactMechId?if_exists}" target="_blank"><img title="${uiLabelMap.IconsTooltips_GeoLoc?if_exists}" alt="${uiLabelMap.IconsTooltips_GeoLoc?if_exists}" src="${iconsPurpose.GeoLoc?if_exists}"></a>
            </#if>
          </td>
            </#if>
          <td class="label align-top">
            <#assign cmt = contactMechMap.contactMechType>
            <#if cmt?has_content>${cmt.get("description",locale)}</#if>
          </td>
          <td>
            <#if entityContactMechPurposes?has_content>
            <#list entityContactMechPurposes as ContactMechPurpose>
              <#assign contactMechPurposeType = ContactMechPurpose.getRelatedOneCache("ContactMechPurposeType")>
              <div>
                <#if contactMechPurposeType?has_content>
                  <b>${contactMechPurposeType.get("description",locale)}</b>
                <#else>
                  <b>${uiLabelMap.PartyMechPurposeTypeNotFound}: "${ContactMechPurpose.contactMechPurposeTypeId}"</b>
                </#if>
                <#if ContactMechPurpose.thruDate?has_content>
                  (${uiLabelMap.CommonExpire}: ${ContactMechPurpose.thruDate})
                </#if>
              </div>
            </#list>
            </#if>
            <#if "POSTAL_ADDRESS" = contactMech.contactMechTypeId>
              <#assign postalAddress = contactMechMap.postalAddress>
              <#if postalAddress?has_content>
              <div>
                <#if postalAddress.toName?has_content><b>${uiLabelMap.PartyAddrToName}:</b> ${postalAddress.toName}<br /></#if>
                <#if postalAddress.attnName?has_content><b>${uiLabelMap.PartyAddrAttnName}:</b> ${postalAddress.attnName}<br /></#if>
                  ${postalAddress.address1?if_exists}<br />
                <#if postalAddress.address2?has_content>${postalAddress.address2}<br /></#if>
                <#if postalAddress.postalCodeGeoId?has_content>
                  <#assign postalCode = postalAddress.getRelatedOneCache("PostalCodeGeo")>
                  ${postalCode.geoName?default(postalCode.geoName)}
                  <#else>
                  ${postalAddress.postalCode?if_exists}
                </#if>
                ${postalAddress.city?if_exists},
                <#if postalAddress.stateProvinceGeoId?has_content>
                  <#assign stateProvince = postalAddress.getRelatedOneCache("StateProvinceGeo")>
                  ${stateProvince.abbreviation?default(stateProvince.geoName)}
                </#if>
                <#if postalAddress.countryGeoId?has_content><br />
                  <#assign country = postalAddress.getRelatedOneCache("CountryGeo")>
                  ${country.geoName?default(country.geoName)}
                </#if>
              </div>
              </#if>
            <#elseif "TELECOM_NUMBER" = contactMech.contactMechTypeId>
              <#assign telecomNumber = contactMechMap.telecomNumber>
              <div>
                ${telecomNumber.countryCode?if_exists}
                <#if telecomNumber.areaCode?has_content>${telecomNumber.areaCode?default("000")}-</#if>${telecomNumber.contactNumber?default("000-0000")}
                <#if entityContactMech.extension?has_content>${uiLabelMap.PartyContactExt}&nbsp;${entityContactMech.extension}</#if>
              </div>
            <#elseif "EMAIL_ADDRESS" = contactMech.contactMechTypeId>
              <div>
                ${contactMech.infoString?if_exists}
                <form method="post" action="<@ofbizUrl>NewDraftCommunicationEvent</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)" name="createEmail${contactMech.infoString?replace("&#64;","")?replace(".","")}">
                  <#if userLogin.partyId?has_content>
                  <input name="partyIdFrom" value="${userLogin.partyId}" type="hidden"/>
                  </#if>
                  <input name="contactMechIdTo" value="${contactMech.contactMechId}" type="hidden"/>
                  <input name="my" value="My" type="hidden"/>
                  <input name="statusId" value="COM_PENDING" type="hidden"/>
                  <input name="communicationEventTypeId" value="EMAIL_COMMUNICATION" type="hidden"/>
                </form>
              </div>
            <#else>
              <div>${contactMech.infoString?if_exists}</div>
            </#if>
            <#-- create cust request -->
            <#if custRequestTypes?exists>
              <form name="createCustRequestForm" action="<@ofbizUrl>createCustRequest</@ofbizUrl>" method="post" onsubmit="javascript:submitFormDisableSubmits(this)">
                <input type="hidden" name="partyId" value="${partyId}"/>
                <input type="hidden" name="fromPartyId" value="${partyId}"/>
                <input type="hidden" name="fulfillContactMechId" value="${contactMech.contactMechId}"/>
                <select name="custRequestTypeId">
                  <#list custRequestTypes as type>
                    <option value="${type.custRequestTypeId}">${type.get("description", locale)}</option>
                  </#list>
                </select>
                <input type="submit" class="smallSubmit" value="${uiLabelMap.PartyCreateNewCustRequest}"/>
              </form>
            </#if>
          </td>
          <td>
             <#assign FromDate = Static["org.ofbiz.base.util.UtilDateTime"].toDateString(entityContactMech.fromDate, "dd/MM/yyyy")/>
            <div>${FromDate}</div>
            <#if entityContactMech.thruDate?has_content>
            <#assign ToDate = Static["org.ofbiz.base.util.UtilDateTime"].toDateString(entityContactMech.thruDate, "dd/MM/yyyy")/>
            <div><b>${uiLabelMap.PartyContactEffectiveThru} :&nbsp;${ToDate}</b></div></#if>
          </td>
          <td>
            <#if security.hasEntityPermission("PARTYMGR", "_DELETE", session)>
              <#if entityContactMech.thruDate?has_content>
              <#-- Reactivate contactMechPurpose option -->
                  <a href="javascript:ajaxSubmitFormUpdateAreas('partyReactivateContact_${contactMechNb}', '${areaId},showPortlet,portalPortletId=${portalPortletId}&amp;portalPageId=${portalPageId}&amp;portletSeqId=${portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;areaId=${areaId}&amp;idDescription=${parameters.idDescription?if_exists}');"><img title="${uiLabelMap.CommonReactivate}" alt="" src="${iconsLocation}/clock_add.png"></a>
                  <form name="partyReactivateContact" id="partyReactivateContact_${contactMechNb}" method="post" action="<@ofbizUrl>updateGivenContactMech</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)">
                    <input name="thruDate" value="" type="hidden" />
              <#else>
                  <a href="javascript:ajaxSubmitFormUpdateAreas('partyDeleteContact_${contactMechNb}', '${areaId},showPortlet,portalPortletId=${portalPortletId}&amp;portalPageId=${portalPageId}&amp;portletSeqId=${portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;areaId=${areaId}&amp;idDescription=${parameters.idDescription?if_exists}');"><img title="${uiLabelMap.CommonDeactivate}" alt="" src="${iconsLocation}/clock_delete.png"></a>
                  <form name="partyDeleteContact" id="partyDeleteContact_${contactMechNb}" method="post" action="<@ofbizUrl>deleteGivenContactMech</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)">
              </#if>
                <input name="primaryId" value="${parameters.primaryId}" type="hidden"/>
                <input name="entity" value="${entity}" type="hidden"/>
                <input name="contactMechId" value="${contactMech.contactMechId}" type="hidden"/>
                <input name="fromDate" value="${entityContactMech.fromDate?if_exists}" type="hidden"/>
              </form>
          </td>
          <td>
            <form name="partyRemoveContact" id="partyRemoveContact_${contactMechNb}" method="post" action="<@ofbizUrl>removeGivenContactMech</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)">
                <input name="primaryId" value="${parameters.primaryId}" type="hidden"/>
                <input name="entity" value="${entity}" type="hidden"/>
                <input name="contactMechId" value="${contactMech.contactMechId}" type="hidden"/>
            </form>
               <a onclick="return confirm('${uiLabelMap.CommonConfirm}')" href="javascript:ajaxSubmitFormUpdateAreas('partyRemoveContact_${contactMechNb}', '${areaId},showPortlet,portalPortletId=${portalPortletId}&amp;portalPageId=${portalPageId}&amp;portletSeqId=${portletSeqId}&amp;primaryId=${parameters.primaryId}&amp;entity=${parameters.entity}&amp;areaId=${areaId}&amp;idDescription=${parameters.idDescription?if_exists}');"><img title="${uiLabelMap.CommonDelete}" alt="" src="${iconsLocation}/bin.png"></a>
            </#if>
          </td>
        </tr>
      <#assign contactMechNb = contactMechNb + 1>
      </#list>
    </table>
  <#else>
    ${uiLabelMap.PartyNoContactInformation}
  </#if>

