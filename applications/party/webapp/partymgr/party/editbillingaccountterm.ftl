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
<#assign termTypes = delegator.findByAnd("TermType",{'parentTypeId':'FINANCIAL_TERM'})>
<#assign uomIds = delegator.findByAnd("Uom",{'uomTypeId':'CURRENCY_MEASURE'})>
<div>
  <table class="basic-table" cellspacing="0">
    <tr>
      <td class="label">${uiLabelMap.PartyTerms}</td>
      <td>
        <table class="basic-table" cellspacing="0">
          <#assign billingAccountTerms = delegator.findByAnd("BillingAccountTerm",{'billingAccountId',parameters.billingAccountId})>
          <#if billingAccountTerms?has_content>
            <#list billingAccountTerms as billingAccountTerm>
              <#assign termType = billingAccountTerm.getRelatedOneCache("TermType")>
              <tr>
                <#if parameters.billingAccountTermId?has_content && parameters.billingAccountTermId = billingAccountTerm.billingAccountTermId>
                <td>
                    <#-- Edit existing term type -->
                    <form name="update_${billingAccountTerm.billingAccountTermId}" id="update_${billingAccountTerm.billingAccountTermId}"
                          method="post" action="<@ofbizUrl>updateBillingAccountTermPortlet</@ofbizUrl>" >
                         <input type="hidden" name="billingAccountId" value="${billingAccountTerm.billingAccountId}" />
                         <input type="hidden" name="billingAccountTermId" value="${billingAccountTerm.billingAccountTermId}" />
                         <#if termTypes?has_content>
                         ${uiLabelMap.PartyTermType}
                         <select name="termTypeId" class="required">
                             <option value="">${uiLabelMap.CommonSelectOne}</option>
                             <#list termTypes as type>
                                 <option value="${type.termTypeId}" <#if type.termTypeId == billingAccountTerm.termTypeId>selected="selected"</#if>>${type.get("description", locale)?default(type.termTypeId)}</option>
                             </#list>
                         </select>
                         </#if>
                         ${uiLabelMap.PartyTermValue}
                         <input type="text" name="termValue" value="${billingAccountTerm.termValue}" size="10" class="required"/>
                         <#if uomIds?has_content>
                         ${uiLabelMap.CommonUom}
                         <select name="uomId">
                             <option value="">${uiLabelMap.CommonSelectOne}</option>
                             <#list uomIds as uom>
                                 <option value="${uom.uomId}" <#if uom.uomId == billingAccountTerm.uomId?if_exists>selected="selected"</#if>>${uom.get("description", locale)?default(uom.uomId)}</option>
                             </#list>
                         </select>
                         </#if>
                         <a class="buttontext" href="javascript:ajaxSubmitFormUpdateAreas('update_${billingAccountTerm.billingAccountTermId}', '${parameters.editAreaDivId},editPartyBillingAccount,partyId=${parameters.partyId}&amp;billingAccountId=${parameters.billingAccountId}&amp;editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;areaId=${parameters.areaId}&amp;idDescription=${parameters.idDescription?if_exists}');">${uiLabelMap.CommonUpdate}</a>
                         <a class="buttontext" href="javascript:refrshPortlet('editPartyBillingAccount', '${parameters.editAreaDivId}','partyId=${parameters.partyId}&amp;billingAccountId=${parameters.billingAccountId}&amp;editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;areaId=${parameters.areaId}&amp;idDescription=${parameters.idDescription?if_exists}','ListPartyPayment','','false','','false');">${uiLabelMap.CommonCancel}</a>
                    </form>
                </td>
                <#else>
                <td>
                    <#-- Display existing term type -->
                    <#if termType?has_content>${termType.get("description",locale)}<#else>${billingAccountTerm.termTypeId}</#if>
                    (${uiLabelMap.PartyTermValue}:${billingAccountTerm.termValue?if_exists})
                    <#if billingAccountTerm.uomId?has_content>
                        <#assign uom = billingAccountTerm.getRelatedOneCache("Uom")>
                        (${uiLabelMap.CommonUom}:${uom.description?if_exists})
                    </#if>
                </td>
                <td>
                    <a href="javascript:refrshPortlet('editPartyBillingAccount', '${parameters.editAreaDivId}','partyId=${parameters.partyId}&amp;billingAccountId=${parameters.billingAccountId}&amp;billingAccountTermId=${billingAccountTerm.billingAccountTermId}&amp;editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;areaId=${parameters.areaId}&amp;idDescription=${parameters.idDescription?if_exists}','ListPartyPayment','','false','','false');"><img title="${uiLabelMap.IconsTooltips_Edit}" alt="" src="${iconsPurpose.Edit}"></a>
                </td>
                </#if>
                <td>
                    <form name="remove_${billingAccountTerm.billingAccountTermId}" id="remove_${billingAccountTerm.billingAccountTermId}"
                          method="post" action="<@ofbizUrl>removeBillingAccountTermPortlet</@ofbizUrl>" >
                         <input type="hidden" name="billingAccountTermId" value="${billingAccountTerm.billingAccountTermId}" />
                         <a href="javascript:ajaxSubmitFormUpdateAreas('remove_${billingAccountTerm.billingAccountTermId}', '${parameters.editAreaDivId},editPartyBillingAccount,partyId=${parameters.partyId}&amp;billingAccountId=${parameters.billingAccountId}&amp;editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;areaId=${parameters.areaId}&amp;idDescription=${parameters.idDescription?if_exists}');" onclick="return confirm('${uiLabelMap.CommonConfirmDelete}')"><img title="${uiLabelMap.IconsTooltips_Delete}" alt="" src="${iconsPurpose.Delete}"></a>
                    </form>
                </td>
              </tr>
            </#list>
          </#if>
          <tr/>
          <tr>
            </table>
            <form method="post" action="<@ofbizUrl>createBillingAccountTermPortlet</@ofbizUrl>" name="newtermform" id="newtermform">
              <table class="basic-table" cellspacing="0">  
              <input type="hidden" name="billingAccountId" value="${parameters.billingAccountId}" />
              <td>
                  <a href="javascript:ajaxSubmitFormUpdateAreas('newtermform', '${parameters.editAreaDivId},editPartyBillingAccount,partyId=${parameters.partyId}&amp;billingAccountId=${parameters.billingAccountId}&amp;editAreaDivId=${parameters.editAreaDivId}&amp;portalPageId=${parameters.portalPageId}&amp;portalPortletId=${parameters.portalPortletId}&amp;portletSeqId=${parameters.portletSeqId}&amp;areaId=${parameters.areaId}&amp;idDescription=${parameters.idDescription?if_exists}');"><img title="${uiLabelMap.IconsTooltips_Add}" alt="" src="/images/icons/famfamfam/add.png"></a>
                  <#if termTypes?has_content>
                  ${uiLabelMap.PartyTermType}
                  <select name="termTypeId" class="required">
                      <option value="">${uiLabelMap.CommonSelectOne}</option>
                      <#list termTypes as type>
                          <option value="${type.termTypeId}">${type.get("description", locale)?default(type.termTypeId)}</option>
                       </#list>
                  </select>
                  </#if>
                  ${uiLabelMap.PartyTermValue}
                  <input type="text" name="termValue" value="" size="10" class="required"/>
                  <#if uomIds?has_content>
                  ${uiLabelMap.CommonUom}
                  <select name="uomId">
                      <option value="">${uiLabelMap.CommonSelectOne}</option>
                      <#list uomIds as uom>
                          <option value="${uom.uomId}">${uom.get("description", locale)?default(uom.uomId)}</option>
                      </#list>
                  </select>
                  </#if>
              </td>
            </form>
          </tr>
        </table><hr>
      </tr>
  </table>
</div>
