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

<div class="col-lg-12 col-md-12">
<#if party??>
<#-- Main Heading >
<table width="100%" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td>
      <h2>${uiLabelMap.PartyTheProfileOf}
        <#if person??>
          ${person.personalTitle!}
          ${person.firstName!}
          ${person.middleName!}
          ${person.lastName!}
          ${person.suffix!}
        <#else>
          "${uiLabelMap.PartyNewUser}"
        </#if>
      </h2>
    </td>
    <td align="right">
      <#if showOld>
        <a href="<@ofbizUrl>viewprofile</@ofbizUrl>" class="button">${uiLabelMap.PartyHideOld}</a>
      <#else>
        <a href="<@ofbizUrl>viewprofile?SHOW_OLD=true</@ofbizUrl>" class="button">${uiLabelMap.PartyShowOld}</a>
      </#if>
      <#if (productStore.enableDigProdUpload)! == "Y">
        <a href="<@ofbizUrl>digitalproductlist</@ofbizUrl>" class="button">${uiLabelMap.EcommerceDigitalProductUpload}</a>
      </#if>
    </td>
  </tr>
</table>
-->
<div class="panel panel-default">
  <div class="panel-heading">
    <span class="pull-right">
      <a href="<@ofbizUrl>editperson</@ofbizUrl>" class="submenutextright">
      <#if person??>${uiLabelMap.CommonUpdate}<#else>${uiLabelMap.CommonCreate}</#if></a>
    </span>
    ${uiLabelMap.PartyPersonalInformation}
  </div>
  <div class="panel-body">
    <#if person??>
    <div>
      <table class="table">
        <tr>
          <td>${uiLabelMap.PartyName}</td>
          <td>
              ${person.personalTitle!}
              ${person.firstName!}
              ${person.middleName!}
              ${person.lastName!}
              ${person.suffix!}
          </td>
        </tr>
      <#if person.nickname?has_content><tr><td align="right">${uiLabelMap.PartyNickName}</td><td>${person.nickname}</td></tr></#if>
      <#if person.gender?has_content><tr><td align="right">${uiLabelMap.PartyGender}</td><td>${person.gender}</td></tr></#if>
      <#if person.birthDate??><tr><td align="right">${uiLabelMap.PartyBirthDate}</td><td>${person.birthDate.toString()}</td></tr></#if>
      <#if person.height??><tr><td align="right">${uiLabelMap.PartyHeight}</td><td>${person.height}</td></tr></#if>
      <#if person.weight??><tr><td align="right">${uiLabelMap.PartyWeight}</td><td>${person.weight}</td></tr></#if>
      <#if person.mothersMaidenName?has_content><tr><td align="right">${uiLabelMap.PartyMaidenName}</td><td>${person.mothersMaidenName}</td></tr></#if>
      <#if person.maritalStatus?has_content><tr><td align="right">${uiLabelMap.PartyMaritalStatus}</td><td>${person.maritalStatus}</td></tr></#if>
      <#if person.socialSecurityNumber?has_content><tr><td align="right">${uiLabelMap.PartySocialSecurityNumber}</td><td>${person.socialSecurityNumber}</td></tr></#if>
      <#if person.passportNumber?has_content><tr><td align="right">${uiLabelMap.PartyPassportNumber}</td><td>${person.passportNumber}</td></tr></#if>
      <#if person.passportExpireDate??><tr><td align="right">${uiLabelMap.PartyPassportExpireDate}</td><td>${person.passportExpireDate.toString()}</td></tr></#if>
      <#if person.totalYearsWorkExperience??><tr><td align="right">${uiLabelMap.PartyYearsWork}</td><td>${person.totalYearsWorkExperience}</td></tr></#if>
      <#if person.comments?has_content><tr><td align="right">${uiLabelMap.CommonComments}</td><td>${person.comments}</td></tr></#if>
      </table>
    </div>
    <#else>
      <label>${uiLabelMap.PartyPersonalInformationNotFound}</label>
    </#if>
  </div>
</div>

<#-- ============================================================= -->
<#if monthsToInclude?? && totalSubRemainingAmount?? && totalOrders??>
<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.EcommerceLoyaltyPoints}</div>
  <div class="panel-body">
    <label>${uiLabelMap.EcommerceYouHave} ${totalSubRemainingAmount} ${uiLabelMap.EcommercePointsFrom} ${totalOrders} ${uiLabelMap.EcommerceOrderInLast} ${monthsToInclude} ${uiLabelMap.EcommerceMonths}</label>
  </div>
</div>
</#if>

<#-- ============================================================= -->
<div class="panel panel-default">
  <div class="panel-heading">
    <a class="pull-right" href="<@ofbizUrl>editcontactmech</@ofbizUrl>" class="submenutextright">${uiLabelMap.CommonCreateNew}</a>
    ${uiLabelMap.PartyContactInformation}
  </div>
  <div class="panel-body">
  <#if partyContactMechValueMaps?has_content>
    <table class="table">
      <tr>
        <th>${uiLabelMap.PartyContactType}</th>
        <th></th>
        <th>${uiLabelMap.CommonInformation}</th>
        <th colspan="2">${uiLabelMap.PartySolicitingOk}?</th>
        <th></th>
        <th></th>
      </tr>
      <#list partyContactMechValueMaps as partyContactMechValueMap>
        <#assign contactMech = partyContactMechValueMap.contactMech! />
        <#assign contactMechType = partyContactMechValueMap.contactMechType! />
        <#assign partyContactMech = partyContactMechValueMap.partyContactMech! />
          <tr>
            <td>
              ${contactMechType.get("description",locale)}
            </td>
            <td>&nbsp;</td>
            <td valign="top">
              <#list partyContactMechValueMap.partyContactMechPurposes! as partyContactMechPurpose>
                <#assign contactMechPurposeType = partyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true) />
                <div>
                  <#if contactMechPurposeType??>
                    ${contactMechPurposeType.get("description",locale)}
                    <#if contactMechPurposeType.contactMechPurposeTypeId == "SHIPPING_LOCATION" && (profiledefs.defaultShipAddr)?default("") == contactMech.contactMechId>
                      <span class="buttontextdisabled">${uiLabelMap.EcommerceIsDefault}</span>
                    <#elseif contactMechPurposeType.contactMechPurposeTypeId == "SHIPPING_LOCATION">
                      <form name="defaultShippingAddressForm" method="post" action="<@ofbizUrl>setprofiledefault/viewprofile</@ofbizUrl>">
                        <input type="hidden" name="productStoreId" value="${productStoreId}" />
                        <input type="hidden" name="defaultShipAddr" value="${contactMech.contactMechId}" />
                        <input type="hidden" name="partyId" value="${party.partyId}" />
                        <input type="submit" value="${uiLabelMap.EcommerceSetDefault}" class="button" />
                      </form>
                    </#if>
                  <#else>
                    ${uiLabelMap.PartyPurposeTypeNotFound}: "${partyContactMechPurpose.contactMechPurposeTypeId}"
                  </#if>
                  <#if partyContactMechPurpose.thruDate??>(${uiLabelMap.CommonExpire}:${partyContactMechPurpose.thruDate.toString()})</#if>
                </div>
              </#list>
              <#if contactMech.contactMechTypeId! = "POSTAL_ADDRESS">
                <#assign postalAddress = partyContactMechValueMap.postalAddress! />
                <div>
                  <#if postalAddress??>
                    <#if postalAddress.toName?has_content>${uiLabelMap.CommonTo}: ${postalAddress.toName}<br /></#if>
                    <#if postalAddress.attnName?has_content>${uiLabelMap.PartyAddrAttnName}: ${postalAddress.attnName}<br /></#if>
                    ${postalAddress.address1}<br />
                    <#if postalAddress.address2?has_content>${postalAddress.address2}<br /></#if>
                    ${postalAddress.city}<#if postalAddress.stateProvinceGeoId?has_content>,&nbsp;${postalAddress.stateProvinceGeoId}</#if>&nbsp;${postalAddress.postalCode!}
                    <#if postalAddress.countryGeoId?has_content><br />${postalAddress.countryGeoId}</#if>
                    <#if (!postalAddress.countryGeoId?has_content || postalAddress.countryGeoId! = "USA")>
                      <#assign addr1 = postalAddress.address1! />
                      <#if (addr1.indexOf(" ") > 0)>
                        <#assign addressNum = addr1.substring(0, addr1.indexOf(" ")) />
                        <#assign addressOther = addr1.substring(addr1.indexOf(" ")+1) />
                        <a target="_blank" href="${uiLabelMap.CommonLookupWhitepagesAddressLink}" class="linktext">(${uiLabelMap.CommonLookupWhitepages})</a>
                      </#if>
                    </#if>
                  <#else>
                    ${uiLabelMap.PartyPostalInformationNotFound}.
                  </#if>
                  </div>
              <#elseif contactMech.contactMechTypeId! = "TELECOM_NUMBER">
                <#assign telecomNumber = partyContactMechValueMap.telecomNumber!>
                <div>
                <#if telecomNumber??>
                  ${telecomNumber.countryCode!}
                  <#if telecomNumber.areaCode?has_content>${telecomNumber.areaCode}-</#if>${telecomNumber.contactNumber!}
                  <#if partyContactMech.extension?has_content>ext&nbsp;${partyContactMech.extension}</#if>
                  <#if (!telecomNumber.countryCode?has_content || telecomNumber.countryCode = "011")>
                    <a target="_blank" href="${uiLabelMap.CommonLookupAnywhoLink}" class="linktext">${uiLabelMap.CommonLookupAnywho}</a>
                    <a target="_blank" href="${uiLabelMap.CommonLookupWhitepagesTelNumberLink}" class="linktext">${uiLabelMap.CommonLookupWhitepages}</a>
                  </#if>
                <#else>
                  ${uiLabelMap.PartyPhoneNumberInfoNotFound}.
                </#if>
                </div>
              <#elseif contactMech.contactMechTypeId! = "EMAIL_ADDRESS">
                  ${contactMech.infoString}
                  <a href="mailto:${contactMech.infoString}" class="linktext">(${uiLabelMap.PartySendEmail})</a>
              <#elseif contactMech.contactMechTypeId! = "WEB_ADDRESS">
                <div>
                  ${contactMech.infoString}
                  <#assign openAddress = contactMech.infoString! />
                  <#if !openAddress.startsWith("http") && !openAddress.startsWith("HTTP")><#assign openAddress = "http://" + openAddress /></#if>
                  <a target="_blank" href="${openAddress}" class="linktext">(${uiLabelMap.CommonOpenNewWindow})</a>
                </div>
              <#else>
                ${contactMech.infoString!}
              </#if>
              <div>(${uiLabelMap.CommonUpdated}:&nbsp;${partyContactMech.fromDate.toString()})</div>
              <#if partyContactMech.thruDate??><div>${uiLabelMap.CommonDelete}:&nbsp;${partyContactMech.thruDate.toString()}</div></#if>
            </td>
            <td align="center" valign="top"><div>(${partyContactMech.allowSolicitation!})</div></td>
            <td>&nbsp;</td>
            <td align="right" valign="top">
              <a href="<@ofbizUrl>editcontactmech?contactMechId=${contactMech.contactMechId}</@ofbizUrl>" class="button">${uiLabelMap.CommonUpdate}</a>
            </td>
            <td align="right" valign="top">
              <form name= "deleteContactMech_${contactMech.contactMechId}" method= "post" action= "<@ofbizUrl>deleteContactMech</@ofbizUrl>">
                <div>
                <input type= "hidden" name= "contactMechId" value= "${contactMech.contactMechId}"/>
                <a href='javascript:document.deleteContactMech_${contactMech.contactMechId}.submit()' class='button'>${uiLabelMap.CommonExpire}</a>
              </div>
              </form>
            </td>
          </tr>
      </#list>
    </table>
  <#else>
    <label>${uiLabelMap.PartyNoContactInformation}.</label><br />
  </#if>
  </div>
</div>

<#-- ============================================================= -->

<div class="panel panel-default">
  <div class="panel-heading">
    <div class="list-unstyled pull-right">
      <a href="<@ofbizUrl>editcreditcard</@ofbizUrl>" class="submenutext">${uiLabelMap.PartyCreateNewCreditCard}</a>&nbsp;&nbsp;
      <a href="<@ofbizUrl>editgiftcard</@ofbizUrl>" class="submenutext">${uiLabelMap.PartyCreateNewGiftCard}</a>&nbsp;&nbsp;
      <a href="<@ofbizUrl>editeftaccount</@ofbizUrl>" class="submenutextright">${uiLabelMap.PartyCreateNewEftAccount}</a>
    </div>
    ${uiLabelMap.AccountingPaymentMethodInformation}
  </div>
  <div class="panel-body">
    <table class="table">
      <tr>
        <td>
          <#if paymentMethodValueMaps?has_content>
          <table width="100%" cellpadding="2" cellspacing="0" border="0">
            <#list paymentMethodValueMaps as paymentMethodValueMap>
              <#assign paymentMethod = paymentMethodValueMap.paymentMethod! />
              <#assign creditCard = paymentMethodValueMap.creditCard! />
              <#assign giftCard = paymentMethodValueMap.giftCard! />
              <#assign eftAccount = paymentMethodValueMap.eftAccount! />
              <tr>
                <#if paymentMethod.paymentMethodTypeId! == "CREDIT_CARD">
                <td valign="top">
                  <div>
                    ${uiLabelMap.AccountingCreditCard}:
                    <#if creditCard.companyNameOnCard?has_content>${creditCard.companyNameOnCard}&nbsp;</#if>
                    <#if creditCard.titleOnCard?has_content>${creditCard.titleOnCard}&nbsp;</#if>
                    ${creditCard.firstNameOnCard}&nbsp;
                    <#if creditCard.middleNameOnCard?has_content>${creditCard.middleNameOnCard}&nbsp;</#if>
                    ${creditCard.lastNameOnCard}
                    <#if creditCard.suffixOnCard?has_content>&nbsp;${creditCard.suffixOnCard}</#if>
                    &nbsp;${Static["org.apache.ofbiz.party.contact.ContactHelper"].formatCreditCard(creditCard)}
                    <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                    <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate.toString()})</#if>
                    <#if paymentMethod.thruDate??>(${uiLabelMap.CommonDelete}:&nbsp;${paymentMethod.thruDate.toString()})</#if>
                  </div>
                </td>
                <td>&nbsp;</td>
                <td align="right" valign="top">
                  <a href="<@ofbizUrl>editcreditcard?paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>" class="button">
                            ${uiLabelMap.CommonUpdate}</a>
                </td>
                <#elseif paymentMethod.paymentMethodTypeId! == "GIFT_CARD">
                  <#if giftCard?has_content && giftCard.cardNumber?has_content>
                    <#assign giftCardNumber = "" />
                    <#assign pcardNumber = giftCard.cardNumber />
                    <#if pcardNumber?has_content>
                      <#assign psize = pcardNumber?length - 4 />
                      <#if (0 < psize)>
                        <#list 0 .. psize-1 as foo>
                          <#assign giftCardNumber = giftCardNumber + "*" />
                        </#list>
                         <#assign giftCardNumber = giftCardNumber + pcardNumber[psize .. psize + 3] />
                      <#else>
                         <#assign giftCardNumber = pcardNumber />
                      </#if>
                    </#if>
                  </#if>

                  <td valign="top">
                    <div>
                      ${uiLabelMap.AccountingGiftCard}: ${giftCardNumber}
                      <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                      <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate.toString()})</#if>
                      <#if paymentMethod.thruDate??>(${uiLabelMap.CommonDelete}:&nbsp;${paymentMethod.thruDate.toString()})</#if>
                    </div>
                  </td>
                  <td >&nbsp;</td>
                  <td align="right" valign="top">
                    <a href="<@ofbizUrl>editgiftcard?paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>" class="button">
                            ${uiLabelMap.CommonUpdate}</a>
                  </td>
                  <#elseif paymentMethod.paymentMethodTypeId! == "EFT_ACCOUNT">
                  <td valign="top">
                    <div>
                      ${uiLabelMap.AccountingEFTAccount}: ${eftAccount.nameOnAccount!} - <#if eftAccount.bankName?has_content>${uiLabelMap.AccountingBank}: ${eftAccount.bankName}</#if> <#if eftAccount.accountNumber?has_content>${uiLabelMap.AccountingAccount} #: ${eftAccount.accountNumber}</#if>
                      <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                      <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate.toString()})</#if>
                      <#if paymentMethod.thruDate??>(${uiLabelMap.CommonDelete}:&nbsp;${paymentMethod.thruDate.toString()})</#if>
                    </div>
                  </td>
                  <td>&nbsp;</td>
                  <td align="right" valign="top">
                    <a href="<@ofbizUrl>editeftaccount?paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>" class="button">
                            ${uiLabelMap.CommonUpdate}</a>
                  </td>
                </#if>
                <td align="right" valign="top">
                 <a href="<@ofbizUrl>deletePaymentMethod/viewprofile?paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>" class="button">
                        ${uiLabelMap.CommonExpire}</a>
                </td>
                <td align="right" valign="top">
                  <#if (profiledefs.defaultPayMeth)?default("") == paymentMethod.paymentMethodId>
                    <span class="buttontextdisabled">${uiLabelMap.EcommerceIsDefault}</span>
                  <#else>
                    <form name="defaultPaymentMethodForm" method="post" action="<@ofbizUrl>setprofiledefault/viewprofile</@ofbizUrl>">
                      <input type="hidden" name="productStoreId" value="${productStoreId}" />
                      <input type="hidden" name="defaultPayMeth" value="${paymentMethod.paymentMethodId}" />
                      <input type="hidden" name="partyId" value="${party.partyId}" />
                      <button type="submit" class="btn btn-default">${uiLabelMap.EcommerceSetDefault}</button>
                    </form>
                  </#if>
                </td>
              </tr>
            </#list>
          </table>
          <#else>
            ${uiLabelMap.AccountingNoPaymentMethodInformation}.
          </#if>
        </td>
      </tr>
    </table>
  </div>
</div>

<#-- ============================================================= -->
<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.PartyTaxIdentification}</div>
  <div class="panel-body">
    <form method="post" action="<@ofbizUrl>createCustomerTaxAuthInfo</@ofbizUrl>" name="createCustTaxAuthInfoForm">
      <div class="form-group">
      <input type="hidden" name="partyId" value="${party.partyId}"/>
      ${screens.render("component://order/widget/ordermgr/OrderEntryOrderScreens.xml#customertaxinfo")}
      <button type="submit" class="btn btn-default">${uiLabelMap.CommonAdd}</button>
      </div>
    </form>
  </div>
</div>

<#-- ============================================================= -->
<div class="panel panel-default">
  <div class="panel-heading">
    <a class="pull-right" href="<@ofbizUrl>changepassword</@ofbizUrl>" class="submenutextright">${uiLabelMap.PartyChangePassword}</a>
    ${uiLabelMap.CommonUsername} &amp; ${uiLabelMap.CommonPassword}
  </div>
  <div class="panel-body">
    <dl class="dl-horizontal">
      <dt>${uiLabelMap.CommonUsername}</dt>
      <dd>${userLogin.userLoginId}</dd>
    </dl>
  </div>
</div>

<#-- ============================================================= -->
<div class="panel panel-default">
  <div class="panel-heading">
    <#if profiledefs?has_content && profiledefs.defaultShipAddr?has_content && carrierShipMethods?has_content><a href="javascript:document.setdefaultshipmeth.submit();" class="submenutextright">${uiLabelMap.EcommerceSetDefault}</a></#if>
    ${uiLabelMap.EcommerceDefaultShipmentMethod}
  </div>
  <div class="panel-body">
    <form name="setdefaultshipmeth" action="<@ofbizUrl>setprofiledefault/viewprofile</@ofbizUrl>" method="post">
      <input type="hidden" name="productStoreId" value="${productStoreId}" />
      <table class="table">
        <#if profiledefs?has_content && profiledefs.defaultShipAddr?has_content && carrierShipMethods?has_content>
          <#list carrierShipMethods as shipMeth>
            <#assign shippingMethod = shipMeth.shipmentMethodTypeId + "@" + shipMeth.partyId />
            <tr>
              <td>&nbsp;</td>
              <td>
                <div><span style="white-space:;"><#if shipMeth.partyId != "_NA_">${shipMeth.partyId!}&nbsp;</#if>${shipMeth.get("description",locale)!}</span></div>
              </td>
              <td><input type="radio" name="defaultShipMeth" value="${shippingMethod}" <#if profiledefs.defaultShipMeth?default("") == shippingMethod>checked="checked"</#if> /></td>
            </tr>
          </#list>
        <#else>
        <tr><td>${uiLabelMap.EcommerceDefaultShipmentMethodMsg}</td></tr>
        </#if>
      </table>
    </form>
  </div>
</div>

<#-- ============================================================= -->
<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.EcommerceFileManager}</div>
  <div class="panel-body">
    <table class="table">
      <#if partyContent?has_content>
        <#list partyContent as contentRole>
        <#assign content = contentRole.getRelatedOne("Content", false) />
        <#assign contentType = content.getRelatedOne("ContentType", true) />
        <#assign mimeType = content.getRelatedOne("MimeType", true)! />
        <#assign status = content.getRelatedOne("StatusItem", true) />
          <tr>
            <td><a href="<@ofbizUrl>img?imgId=${content.dataResourceId!}</@ofbizUrl>" class="button">${content.contentId}</a></td>
            <td>${content.contentName!}</td>
            <td>${(contentType.get("description",locale))!}</td>
            <td>${(mimeType.description)!}</td>
            <td>${(status.get("description",locale))!}</td>
            <td>${contentRole.fromDate!}</td>
            <td align="right">
              <form name="removeContent_${contentRole.contentId}" method="post" action="removePartyAsset">
                <input name="partyId" type="hidden" value="${userLogin.partyId}"/>
                <input name="contentId" type="hidden" value="${contentRole.contentId}"/>
                <input name="roleTypeId" type="hidden" value="${contentRole.roleTypeId}"/>
              </form>
              <a href="<@ofbizUrl>img?imgId=${content.dataResourceId!}</@ofbizUrl>" class="button">${uiLabelMap.CommonView}</a>
              <a href="javascript:document.removeContent_${contentRole.contentId}.submit();" class="button">${uiLabelMap.CommonRemove}</a>
            </td>
          </tr>
        </#list>
      <#else>
         <tr><td>${uiLabelMap.EcommerceNoFiles}</td></tr>
      </#if>
    </table>
    <div>&nbsp;</div>
    <div>
      <form method="post" enctype="multipart/form-data" action="<@ofbizUrl>uploadPartyContent</@ofbizUrl>">
      <div>
        <input type="hidden" name="partyId" value="${party.partyId}"/>
        <input type="hidden" name="dataCategoryId" value="PERSONAL"/>
        <input type="hidden" name="contentTypeId" value="DOCUMENT"/>
        <input type="hidden" name="statusId" value="CTNT_PUBLISHED"/>
        <input type="hidden" name="roleTypeId" value="OWNER"/>
        <div class="row">
          <div class="form-group col-lg-4 col-md-4">
            <label>${uiLabelMap.EcommerceUploadNewFile}</label>
            <input type="file" name="uploadedFile" size="50"/>
          </div>
        </div>
        <div class="row">
          <div class="form-group col-lg-4 col-md-4">
            <select name="partyContentTypeId" class="form-control">
              <option value="">${uiLabelMap.PartySelectPurpose}</option>
              <#list partyContentTypes as partyContentType>
                <option value="${partyContentType.partyContentTypeId}">${partyContentType.get("description", locale)?default(partyContentType.partyContentTypeId)}</option>
              </#list>
            </select>
          </div>
        </div>
        <div class="row">
          <div class="form-group col-lg-4 col-md-4">
            <select name="mimeTypeId" class="form-control">
              <option value="">${uiLabelMap.PartySelectMimeType}</option>
              <#list mimeTypes as mimeType>
                <option value="${mimeType.mimeTypeId}">${mimeType.get("description", locale)?default(mimeType.mimeTypeId)}</option>
              </#list>
            </select>
          </div>
        </div>
        <button type="submit" class="btn btn-default">${uiLabelMap.CommonUpload}</button>
        </div>
      </form>
    </div>
  </div>
</div>

<#-- ============================================================= -->
<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.PartyContactLists}</div>
  <div class="panel-body">
    <table class="table table-condensed">
      <thead>
      <tr>
        <th>${uiLabelMap.EcommerceListName}</th>
        <#-- <th >${uiLabelMap.OrderListType}</th> -->
        <th>${uiLabelMap.CommonFromDate}</th>
        <th>${uiLabelMap.CommonThruDate}</th>
        <th>${uiLabelMap.CommonStatus}</th>
        <th>${uiLabelMap.CommonEmail}</th>
        <th>&nbsp;</th>
        <th>&nbsp;</th>
      </tr>
      </thead>
      <tbody>
      <#list contactListPartyList as contactListParty>
      <#assign contactList = contactListParty.getRelatedOne("ContactList", false)! />
      <#assign statusItem = contactListParty.getRelatedOne("StatusItem", true)! />
      <#assign emailAddress = contactListParty.getRelatedOne("PreferredContactMech", true)! />
      <#-- <#assign contactListType = contactList.getRelatedOne("ContactListType", true)/> -->
      <tr>
        <td>${contactList.contactListName!}<#if contactList.description?has_content>&nbsp;-&nbsp;${contactList.description}</#if></td>
        <#-- <td><div>${contactListType.get("description",locale)!}</div></td> -->
        <td>${contactListParty.fromDate!}</td>
        <td>${contactListParty.thruDate!}</td>
        <td>${(statusItem.get("description",locale))!}</td>
        <td>${emailAddress.infoString!}</td>
        <td>&nbsp;</td>
        <td>
          <#if (contactListParty.statusId! == "CLPT_ACCEPTED")>            
            <form method="post" action="<@ofbizUrl>updateContactListParty</@ofbizUrl>" name="clistRejectForm${contactListParty_index}">
            <div>
              <#assign productStoreId = Static["org.apache.ofbiz.product.store.ProductStoreWorker"].getProductStoreId(request) />
              <input type="hidden" name="productStoreId" value="${productStoreId!}" />
              <input type="hidden" name="partyId" value="${party.partyId}"/>
              <input type="hidden" name="contactListId" value="${contactListParty.contactListId}"/>
              <input type="hidden" name="preferredContactMechId" value="${contactListParty.preferredContactMechId}"/>
              <input type="hidden" name="fromDate" value="${contactListParty.fromDate}"/>
              <input type="hidden" name="statusId" value="CLPT_REJECTED"/>
              <button type="submit" class="btn btn-default">${uiLabelMap.EcommerceUnsubscribe}</button>
              </div>
            </form>
          <#elseif (contactListParty.statusId! == "CLPT_PENDING")>
            <form method="post" action="<@ofbizUrl>updateContactListParty</@ofbizUrl>" name="clistAcceptForm${contactListParty_index}">
            <div>
              <input type="hidden" name="partyId" value="${party.partyId}"/>
              <input type="hidden" name="contactListId" value="${contactListParty.contactListId}"/>
              <input type="hidden" name="preferredContactMechId" value="${contactListParty.preferredContactMechId}"/>
              <input type="hidden" name="fromDate" value="${contactListParty.fromDate}"/>
              <input type="hidden" name="statusId" value="CLPT_ACCEPTED"/>
              <input type="text" size="10" name="optInVerifyCode" value="" class="form-control"/>
              <button type="submit" class="btn btn-default">${uiLabelMap.EcommerceVerifySubscription}</button>
              </div>
            </form>
          <#elseif (contactListParty.statusId! == "CLPT_REJECTED")>
            <form method="post" action="<@ofbizUrl>updateContactListParty</@ofbizUrl>" name="clistPendForm${contactListParty_index}">
            <div>
              <input type="hidden" name="partyId" value="${party.partyId}"/>
              <input type="hidden" name="contactListId" value="${contactListParty.contactListId}"/>
              <input type="hidden" name="preferredContactMechId" value="${contactListParty.preferredContactMechId}"/>
              <input type="hidden" name="fromDate" value="${contactListParty.fromDate}"/>
              <input type="hidden" name="statusId" value="CLPT_PENDING"/>
              <button type="submit" class="btn btn-default">${uiLabelMap.EcommerceSubscribe}</button>
              </div>
            </form>
          </#if>
        </td>
      </tr>
      </#list>
      </tbody>
    </table>
    <div>
      <form method="post" action="<@ofbizUrl>createContactListParty</@ofbizUrl>" name="clistPendingForm">
        <div>
        <input type="hidden" name="partyId" value="${party.partyId}"/>
        <input type="hidden" name="statusId" value="CLPT_PENDING"/>
        <span class="tableheadtext">${uiLabelMap.EcommerceNewListSubscription}: </span>
        <div class="form-group">
        <select name="contactListId" class="form-control">
          <#list publicContactLists as publicContactList>
            <#-- <#assign publicContactListType = publicContactList.getRelatedOne("ContactListType", true)> -->
            <#assign publicContactMechType = publicContactList.getRelatedOne("ContactMechType", true)! />
            <option value="${publicContactList.contactListId}">${publicContactList.contactListName!} <#-- ${publicContactListType.get("description",locale)} --> <#if publicContactMechType?has_content>[${publicContactMechType.get("description",locale)}]</#if></option>
          </#list>
        </select>
        </div>
        <div class="form-group">
        <select name="preferredContactMechId" class="form-control">
        <#-- <option></option> -->
          <#list partyAndContactMechList as partyAndContactMech>
            <option value="${partyAndContactMech.contactMechId}"><#if partyAndContactMech.infoString?has_content>${partyAndContactMech.infoString}<#elseif partyAndContactMech.tnContactNumber?has_content>${partyAndContactMech.tnCountryCode!}-${partyAndContactMech.tnAreaCode!}-${partyAndContactMech.tnContactNumber}<#elseif partyAndContactMech.paAddress1?has_content>${partyAndContactMech.paAddress1}, ${partyAndContactMech.paAddress2!}, ${partyAndContactMech.paCity!}, ${partyAndContactMech.paStateProvinceGeoId!}, ${partyAndContactMech.paPostalCode!}, ${partyAndContactMech.paPostalCodeExt!} ${partyAndContactMech.paCountryGeoId!}</#if></option>
          </#list>
        </select>
        </div>
        <button type="submit" class="btn btn-default">${uiLabelMap.EcommerceSubscribe}</button>
        </div>
      </form>
    </div>
    <label>${uiLabelMap.EcommerceListNote}</label>
  </div>
</div>

<#-- ============================================================= -->
<#if surveys?has_content>
<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.EcommerceSurveys}</div>
  <div class="panel-body">
    <table class="table">
      <#list surveys as surveyAppl>
        <#assign survey = surveyAppl.getRelatedOne("Survey", false) />
        <tr>
          <td>&nbsp;</td>
          <td valign="top"><div>${survey.surveyName!}&nbsp;-&nbsp;${survey.description!}</div></td>
          <td>&nbsp;</td>
          <td valign="top">
            <#assign responses = Static["org.apache.ofbiz.product.store.ProductStoreWorker"].checkSurveyResponse(request, survey.surveyId)?default(0)>
            <#if (responses < 1)>${uiLabelMap.EcommerceNotCompleted}<#else>${uiLabelMap.EcommerceCompleted}</#if>
          </td>
          <#if (responses == 0 || survey.allowMultiple?default("N") == "Y")>
            <#assign surveyLabel = uiLabelMap.EcommerceTakeSurvey />
            <#if (responses > 0 && survey.allowUpdate?default("N") == "Y")>
              <#assign surveyLabel = uiLabelMap.EcommerceUpdateSurvey />
            </#if>
            <td align="right"><a href="<@ofbizUrl>takesurvey?productStoreSurveyId=${surveyAppl.productStoreSurveyId}</@ofbizUrl>" class="button">${surveyLabel}</a></td>
          <#else>
          &nbsp;
          </#if>
        </tr>
      </#list>
    </table>
  </div>
</div>
</#if>

<#-- ============================================================= -->
<#-- only 5 messages will show; edit the ViewProfile.groovy to change this number -->
${screens.render("component://ecommerce/widget/CustomerScreens.xml#messagelist-include")}

${screens.render("component://ecommerce/widget/CustomerScreens.xml#FinAccountList-include")}

<#-- Serialized Inventory Summary -->
${screens.render('component://ecommerce/widget/CustomerScreens.xml#SerializedInventorySummary')}

<#-- Subscription Summary -->
${screens.render('component://ecommerce/widget/CustomerScreens.xml#SubscriptionSummary')}

<#-- Reviews -->
${screens.render('component://ecommerce/widget/CustomerScreens.xml#showProductReviews')}

<#else>
    <h3>${uiLabelMap.PartyNoPartyForCurrentUserName}: ${userLogin.userLoginId}</h3>
</#if>
</div>