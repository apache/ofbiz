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

<#if getUsername>
<script type="text/javascript">
  //<![CDATA[
     lastFocusedName = null;
     function setLastFocused(formElement) {
         lastFocusedName = formElement.name;
         document.write.lastFocusedName;
     }
     function clickUsername() {
         if (document.getElementById('UNUSEEMAIL').checked) {
             if (lastFocusedName == "UNUSEEMAIL") {
                 jQuery('#PASSWORD').focus();
             } else if (lastFocusedName == "PASSWORD") {
                 jQuery('#UNUSEEMAIL').focus();
             } else {
                 jQuery('#PASSWORD').focus();
             }
         }
     }
     function changeEmail() {
         if (document.getElementById('UNUSEEMAIL').checked) {
             document.getElementById('USERNAME').value = jQuery('#CUSTOMER_EMAIL').val();
         }
     }
     function setEmailUsername() {
         if (document.getElementById('UNUSEEMAIL').checked) {
             document.getElementById('USERNAME').value = jQuery('#CUSTOMER_EMAIL').val();
             // don't disable, make the browser not submit the field: document.getElementById('USERNAME').disabled=true;
         } else {
             document.getElementById('USERNAME').value='';
             // document.getElementById('USERNAME').disabled=false;
         }
     }
     function hideShowUsaStates() {
         var customerStateElement = document.getElementById('newuserform_stateProvinceGeoId');
          var customerCountryElement = document.getElementById('newuserform_countryGeoId');
         if (customerCountryElement.value == "USA" || customerCountryElement.value == "UMI") {
             customerStateElement.style.display = "block";
         } else {
             customerStateElement.style.display = "none";
         }
     }
   //]]>
</script>
</#if>

<#------------------------------------------------------------------------------
NOTE: all page headings should start with an h2 tag, not an H1 tag, as 
there should generally always only be one h1 tag on the page and that 
will generally always be reserved for the logo at the top of the page.
------------------------------------------------------------------------------->

<#macro fieldErrors fieldName>
  <#if errorMessageList?has_content>
    <#assign fieldMessages = Static["org.apache.ofbiz.base.util.MessageString"].getMessagesForField(fieldName, true, errorMessageList)>
    <ul>
      <#list fieldMessages as errorMsg>
        <li class="errorMessage">${errorMsg}</li>
      </#list>
    </ul>
  </#if>
</#macro>
<#macro fieldErrorsMulti fieldName1 fieldName2 fieldName3 fieldName4>
  <#if errorMessageList?has_content>
    <#assign fieldMessages = Static["org.apache.ofbiz.base.util.MessageString"].getMessagesForField(fieldName1, fieldName2, fieldName3, fieldName4, true, errorMessageList)>
    <ul>
      <#list fieldMessages as errorMsg>
        <li class="errorMessage">${errorMsg}</li>
      </#list>
    </ul>
  </#if>
</#macro>
<div class="container-fluid">
<div class="panel panel-default">
  <div class="panel-heading">Register Customer</div>
  <div class="panel-body">
<form method="post" action="<@ofbizUrl>createcustomer${previousParams}</@ofbizUrl>" id="newuserform" name="newuserform">
  
  <#----------------------------------------------------------------------
  If you need to include a brief explanation of the form, or certain 
  elements in the form (such as explaining asterisks denote REQUIRED),
  then you should use a <p></p> tag with a class name of "desc"
  ----------------------------------------------------------------------->

  <#----------------------------------------------------------------------
  There are two types of fieldsets, regular (full width) fielsets, and
  column (half width) fieldsets. If you want to group two sets of inputs
  side by side in two columns, give each fieldset a class name of "col"
  ----------------------------------------------------------------------->
<fieldset class="col">
  <legend>${uiLabelMap.PartyFullName}</legend>
  <div class="row">
    <input type="hidden" name="emailProductStoreId" value="${productStoreId}"/>
    <#----------------------------------------------------------------------
    Each input row should be enclosed in a <div></div>. 
    This will ensure than each input field clears the one
    above it. Alternately, if you want several inputs to float next to
    each other, you can enclose them in a table as illustrated below for
    the phone numbers, or you can enclose each label/input pair in a span

    Example:
    <div>
      <span>
        <input type="text" name="expMonth" value=""/>
        <label for="expMonth">Exp. Month</label>
      </span>
      <span>
        <input type="text" name="expYear" value=""/>
        <label for="expYear">Exp. Year</label>
      </span>
    </div>
    ----------------------------------------------------------------------->
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="USER_TITLE">${uiLabelMap.CommonTitle}</label>
        <@fieldErrors fieldName="USER_TITLE"/>
        <select name="USER_TITLE" id="USER_TITLE"  class="form-control">
          <#if requestParameters.USER_TITLE?has_content >
            <option>${requestParameters.USER_TITLE}</option>
            <option value="${requestParameters.USER_TITLE}"> -- </option>
          <#else>
            <option value="">${uiLabelMap.CommonSelectOne}</option>
          </#if>
          <option>${uiLabelMap.CommonTitleMr}</option>
          <option>${uiLabelMap.CommonTitleMrs}</option>
          <option>${uiLabelMap.CommonTitleMs}</option>
          <option>${uiLabelMap.CommonTitleDr}</option>
        </select>
      </div>
    </div>
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="USER_FIRST_NAME">${uiLabelMap.PartyFirstName}*</label>
        <@fieldErrors fieldName="USER_FIRST_NAME"/>
        <input type="text" class="form-control" name="USER_FIRST_NAME" id="USER_FIRST_NAME" value="${requestParameters.USER_FIRST_NAME!}" />
      </div>
    </div>
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="USER_MIDDLE_NAME">${uiLabelMap.PartyMiddleInitial}</label>
        <@fieldErrors fieldName="USER_MIDDLE_NAME"/>
        <input type="text" class="form-control" name="USER_MIDDLE_NAME" id="USER_MIDDLE_NAME" value="${requestParameters.USER_MIDDLE_NAME!}" />
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="USER_LAST_NAME">${uiLabelMap.PartyLastName}*</label>
        <@fieldErrors fieldName="USER_LAST_NAME"/>
        <input type="text" class="form-control" name="USER_LAST_NAME" id="USER_LAST_NAME" value="${requestParameters.USER_LAST_NAME!}" />
      </div>
    </div>
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="USER_SUFFIX">${uiLabelMap.PartySuffix}</label>
        <@fieldErrors fieldName="USER_SUFFIX"/>
        <input type="text" class="form-control" class='inputBox' name="USER_SUFFIX" id="USER_SUFFIX" value="${requestParameters.USER_SUFFIX!}" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset class="col">
  <legend>${uiLabelMap.PartyShippingAddress}</legend>
  <div class="row">
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="CUSTOMER_ADDRESS1">${uiLabelMap.PartyAddressLine1}*</label>
        <@fieldErrors fieldName="CUSTOMER_ADDRESS1"/>
        <input type="text" class="form-control" name="CUSTOMER_ADDRESS1" id="CUSTOMER_ADDRESS1" value="${requestParameters.CUSTOMER_ADDRESS1!}" />
      </div>
    </div>
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="CUSTOMER_ADDRESS2">${uiLabelMap.PartyAddressLine2}</label>
        <@fieldErrors fieldName="CUSTOMER_ADDRESS2"/>
        <input type="text" class="form-control" name="CUSTOMER_ADDRESS2" id="CUSTOMER_ADDRESS2" value="${requestParameters.CUSTOMER_ADDRESS2!}" />
      </div>
    </div>
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="CUSTOMER_CITY">${uiLabelMap.PartyCity}*</label>
        <@fieldErrors fieldName="CUSTOMER_CITY"/>
        <input type="text" class="form-control" name="CUSTOMER_CITY" id="CUSTOMER_CITY" value="${requestParameters.CUSTOMER_CITY!}" />
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="CUSTOMER_POSTAL_CODE">${uiLabelMap.PartyZipCode}*</label>
        <@fieldErrors fieldName="CUSTOMER_POSTAL_CODE"/>
        <input type="text" class="form-control" name="CUSTOMER_POSTAL_CODE" id="CUSTOMER_POSTAL_CODE" value="${requestParameters.CUSTOMER_POSTAL_CODE!}" />
      </div>
    </div>
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="customerCountry">${uiLabelMap.CommonCountry}*</label>
        <@fieldErrors fieldName="CUSTOMER_COUNTRY"/>
        <select name="CUSTOMER_COUNTRY" class="form-control" id="newuserform_countryGeoId">
          ${screens.render("component://common/widget/CommonScreens.xml#countries")}
          <#assign defaultCountryGeoId = Static["org.apache.ofbiz.entity.util.EntityUtilProperties"].getPropertyValue("general", "country.geo.id.default", delegator)>
          <option selected="selected" value="${defaultCountryGeoId}">
              <#assign countryGeo = delegator.findOne("Geo",Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("geoId",defaultCountryGeoId), false)>
              ${countryGeo.get("geoName",locale)}
          </option>
        </select>
      </div>
    </div>
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="customerState">${uiLabelMap.PartyState}*</label>
        <@fieldErrors fieldName="CUSTOMER_STATE"/>
        <select name="CUSTOMER_STATE" class="form-control" id="newuserform_stateProvinceGeoId"></select>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for="CUSTOMER_ADDRESS_ALLOW_SOL">${uiLabelMap.PartyAllowAddressSolicitation}</label>
        <@fieldErrors fieldName="CUSTOMER_ADDRESS_ALLOW_SOL"/>
        <select name="CUSTOMER_ADDRESS_ALLOW_SOL" class="form-control" id="CUSTOMER_ADDRESS_ALLOW_SOL">
          <#if (((requestParameters.CUSTOMER_ADDRESS_ALLOW_SOL)!"") == "Y")><option value="Y">${uiLabelMap.CommonY}</option></#if>
          <#if (((requestParameters.CUSTOMER_ADDRESS_ALLOW_SOL)!"") == "N")><option value="N">${uiLabelMap.CommonN}</option></#if>
          <option></option>
          <option value="Y">${uiLabelMap.CommonY}</option>
          <option value="N">${uiLabelMap.CommonN}</option>
        </select>
      </div>
    </div>
  </div>
</fieldset>
<fieldset class="col">
  <legend>${uiLabelMap.PartyPhoneNumbers}</legend>
  <div class="form-group">
    <label for="customerState">${uiLabelMap.PartyHomePhone}</label>
    <div class="row">
      <div class="col-lg-1 col-md-1">
        <input type="text" name="CUSTOMER_HOME_COUNTRY" class="form-control" value="${requestParameters.CUSTOMER_HOME_COUNTRY!}" placeholder="${uiLabelMap.CommonCountry}" />
      </div>
      <div class="col-lg-2 col-md-2">
        <input type="text" name="CUSTOMER_HOME_AREA" class="form-control" value="${requestParameters.CUSTOMER_HOME_AREA!}" placeholder="${uiLabelMap.PartyAreaCode}"/>
      </div>
      <div class="col-lg-6 col-md-6">
        <input type="text" name="CUSTOMER_HOME_CONTACT" class="form-control" value="${requestParameters.CUSTOMER_HOME_CONTACT!}" placeholder="${uiLabelMap.PartyContactNumber}"/>
      </div>
      <div class="col-lg-3 col-md-3">
        <input type="text" name="CUSTOMER_HOME_EXT" class="form-control" value="${requestParameters.CUSTOMER_HOME_EXT!}" placeholder="${uiLabelMap.PartyExtension}"/>
      </div>
    </div>
  </div>
  <div class="form-group">
    <label for="customerState">${uiLabelMap.PartyBusinessPhone}</label>
    <div class="row">
      <div class="col-lg-1 col-md-1">
        <input type="text" name="CUSTOMER_WORK_COUNTRY" value="${requestParameters.CUSTOMER_WORK_COUNTRY!}" class="form-control" placeholder="${uiLabelMap.CommonCountry}" />
      </div>
      <div class="col-lg-2 col-md-2">
        <input type="text" name="CUSTOMER_WORK_AREA" class="form-control" value="${requestParameters.CUSTOMER_WORK_AREA!}" placeholder="${uiLabelMap.PartyAreaCode}"/>
      </div>
      <div class="col-lg-6 col-md-6">
        <input type="text" name="CUSTOMER_WORK_CONTACT" class="form-control" value="${requestParameters.CUSTOMER_WORK_CONTACT!}" placeholder="${uiLabelMap.PartyContactNumber}"/>
      </div>
      <div class="col-lg-3 col-md-3">
        <input type="text" name="CUSTOMER_WORK_EXT" class="form-control" value="${requestParameters.CUSTOMER_WORK_EXT!}" placeholder="${uiLabelMap.PartyExtension}"/>
      </div>
    </div>
  </div>
  <div class="form-group">
    <label for="customerState">${uiLabelMap.PartyFaxNumber}</label>
    <div class="row">
      <div class="col-lg-1 col-md-1">
        <input type="text" name="CUSTOMER_FAX_COUNTRY" class="form-control" value="${requestParameters.CUSTOMER_FAX_COUNTRY!}" placeholder="${uiLabelMap.CommonCountry}" />
      </div>
      <div class="col-lg-2 col-md-2">
        <input type="text" name="CUSTOMER_FAX_AREA" class="form-control" value="${requestParameters.CUSTOMER_FAX_AREA!}" placeholder="${uiLabelMap.PartyAreaCode}"/>
      </div>
      <div class="col-lg-6 col-md-6">
        <input type="text" name="CUSTOMER_FAX_CONTACT" class="form-control" value="${requestParameters.CUSTOMER_FAX_CONTACT!}" placeholder="${uiLabelMap.PartyContactNumber}"/>
      </div>
    </div>
  </div>
  <div class="form-group">
    <label for="customerState">${uiLabelMap.PartyMobilePhone}</label>
    <div class="row">
      <div class="col-lg-1 col-md-1">
        <input type="text" name="CUSTOMER_MOBILE_COUNTRY" class="form-control" value="${requestParameters.CUSTOMER_MOBILE_COUNTRY!}" placeholder="${uiLabelMap.CommonCountry}" />
      </div>
      <div class="col-lg-2 col-md-2">
        <input type="text" name="CUSTOMER_MOBILE_AREA" class="form-control" value="${requestParameters.CUSTOMER_MOBILE_AREA!}" placeholder="${uiLabelMap.PartyAreaCode}"/>
      </div>
      <div class="col-lg-6 col-md-6">
        <input type="text" name="CUSTOMER_MOBILE_CONTACT" class="form-control" value="${requestParameters.CUSTOMER_MOBILE_CONTACT!}" placeholder="${uiLabelMap.PartyContactNumber}"/>
      </div>
    </div>
  </div>
</fieldset>
<fieldset class="col">
  <legend>${uiLabelMap.PartyEmailAddress}</legend>
  <div class="row">
    <div class="col-lg-4 col-md-4">
      <div class="form-group">
        <label for= "CUSTOMER_EMAIL">${uiLabelMap.PartyEmailAddress}*</label>
        <@fieldErrors fieldName="CUSTOMER_EMAIL"/>
        <input type="text" class="form-control" name="CUSTOMER_EMAIL" id="CUSTOMER_EMAIL" value="${requestParameters.CUSTOMER_EMAIL!}" onchange="changeEmail()" onkeyup="changeEmail()" />
      </div>
    </div>
    <div class="col-lg-2 col-md-2">
      <div class="form-group">
        <label for="CUSTOMER_EMAIL_ALLOW_SOL">${uiLabelMap.PartyAllowSolicitation}</label>
        <select name="CUSTOMER_EMAIL_ALLOW_SOL" class="form-control" id="CUSTOMER_EMAIL_ALLOW_SOL">
          <#if (((requestParameters.CUSTOMER_EMAIL_ALLOW_SOL)!"") == "Y")><option value="Y">${uiLabelMap.CommonY}</option></#if>
          <#if (((requestParameters.CUSTOMER_EMAIL_ALLOW_SOL)!"") == "N")><option value="N">${uiLabelMap.CommonN}</option></#if>
          <option></option>
          <option value="Y">${uiLabelMap.CommonY}</option>
          <option value="N">${uiLabelMap.CommonN}</option>
        </select>
      </div>
    </div>
  </div>
</fieldset>
  <fieldset class="col">
    <legend><#if getUsername>${uiLabelMap.CommonUsername}</#if></legend>
    <#if getUsername>
    <div class="row">
      <@fieldErrors fieldName="USERNAME"/>
      <#if !requestParameters.preferredUsername?has_content>
        <div class="col-lg-4 col-md-4">
          <div class="checkbox">
            <label>
              <input type="checkbox" class="checkbox" name="UNUSEEMAIL" id="UNUSEEMAIL" value="on" onclick="setEmailUsername();" onfocus="setLastFocused(this);"/> ${uiLabelMap.EcommerceUseEmailAddress}
            </label>
          </div>
        </div>
      </#if>
      </div>
      <div class="row">
      <div class="col-lg-4 col-md-4">
        <div class="form-group">
          <label for="USERNAME">${uiLabelMap.CommonUsername}*</label>
          <#if requestParameters.preferredUsername?has_content>
              <input type="text" name="showUserName" id="showUserName" value="${requestParameters.USERNAME!}" disabled="disabled"/>
              <input type="hidden" class="form-control" name="USERNAME" id="USERNAME" value="${requestParameters.USERNAME!}"/>
          <#else>
              <input type="text" class="form-control" name="USERNAME" id="USERNAME" value="${requestParameters.USERNAME!}" onfocus="clickUsername();" onchange="changeEmail();"/>
          </#if>
        </div>
      </div>
    </div>
    </#if>
  </fieldset>

  <fieldset class="col">
    <legend>${uiLabelMap.CommonPassword}</legend>
    <div class="row">
      <#if createAllowPassword>
        <div class="col-lg-4 col-md-4">
          <div class="form-group">
            <label for="PASSWORD">${uiLabelMap.CommonPassword}*</label>
            <@fieldErrors fieldName="PASSWORD"/>
            <input type="password" class="form-control" name="PASSWORD" id="PASSWORD" onfocus="setLastFocused(this);"/>
          </div>
        </div>
        <div class="col-lg-4 col-md-4">
          <div class="form-group">
            <label for="CONFIRM_PASSWORD">${uiLabelMap.PartyRepeatPassword}*</label>
            <@fieldErrors fieldName="CONFIRM_PASSWORD"/>
            <input type="password" class="form-control inputBox" name="CONFIRM_PASSWORD" id="CONFIRM_PASSWORD" value="" maxlength="50"/>
          </div>
        </div>
        <div class="col-lg-4 col-md-4">
          <div class="form-group">
            <label for="PASSWORD_HINT">${uiLabelMap.PartyPasswordHint}</label>
            <@fieldErrors fieldName="PASSWORD_HINT"/>
            <input type="text" class="inputBox form-control" name="PASSWORD_HINT" id="PASSWORD_HINT" value="${requestParameters.PASSWORD_HINT!}" maxlength="100"/>
          </div>
        </div>
      <#else>
        <div class="form-group">
          <label>${uiLabelMap.PartyReceivePasswordByEmail}.</div>
        </div>
      </#if>
    </div>
  </fieldset>
</form>

<#------------------------------------------------------------------------------
To create a consistent look and feel for all buttons, input[type=submit], 
and a tags acting as submit buttons, all button actions should have a 
class name of "button". No other class names should be used to style 
button actions.
------------------------------------------------------------------------------->
<div class="buttons">  
  &nbsp;<a href="javascript:document.getElementById('newuserform').submit()" class="btn btn-primary">${uiLabelMap.CommonSave}</a>   
  &nbsp;<a href="<@ofbizUrl>${donePage}</@ofbizUrl>" class="btn btn-default">${uiLabelMap.CommonCancel}</a>
</div>
  </div>
</div>
</div>
<script type="text/javascript">
  //<![CDATA[
      hideShowUsaStates();
  //]]>
</script>