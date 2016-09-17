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
<div class="col-lg-9 col-md-9">
  <div class="panel panel-default">
    <div class="panel-heading">${uiLabelMap.CommonContactUs}</div>
    <script type="text/javascript" language="JavaScript">
    <!--
        function reloadCaptcha(fieldName) {
            var captchaUri = "<@ofbizUrl>captcha.jpg?captchaCodeId=" + fieldName + "&amp;unique=_PLACEHOLDER_</@ofbizUrl>";
            var unique = Date.now();
            captchaUri = captchaUri.replace("_PLACEHOLDER_", unique);
            document.getElementById(fieldName).src = captchaUri;
        }
    //-->
    </script>
    <div class="panel-body">
      <form id="contactForm" method="post" action="<@ofbizUrl>submitAnonContact</@ofbizUrl>">
        <input type="hidden" name="partyIdFrom" value="${(userLogin.partyId)!}" />
        <input type="hidden" name="partyIdTo" value="${productStore.payToPartyId!}"/>
        <input type="hidden" name="contactMechTypeId" value="WEB_ADDRESS" />
        <input type="hidden" name="communicationEventTypeId" value="WEB_SITE_COMMUNICATI" />
        <input type="hidden" name="productStoreId" value="${productStore.productStoreId}" />
        <input type="hidden" name="emailType" value="CONT_NOTI_EMAIL" />
        <div class="form-group">
          <label>${uiLabelMap.EcommerceSubject} *</label>
          <input type="text" name="subject" id="subject" class="form-control required" value="${requestParameters.subject!}"/>
        </div>
        <div class="form-group">
          <label>${uiLabelMap.CommonMessage}</label>
          <textarea name="content" id="message" class="form-control required" cols="50" rows="5">${requestParameters.content!}</textarea>
        </div>
        <div class="form-group">
          <label>${uiLabelMap.FormFieldTitle_emailAddress} *</label>
          <input type="text" name="emailAddress" id="emailAddress" class="form-control required" value="${requestParameters.emailAddress!}"/>
        </div>
        <div class="form-group">
          <label>${uiLabelMap.PartyFirstName}</label>
          <input type="text" name="firstName" id="firstName" class="form-control" value="${requestParameters.firstName!}"/>
        </div>
        <div class="form-group">
          <label>${uiLabelMap.PartyLastName}</label>
          <input type="text" name="lastName" id="lastName" class="form-control" value="${requestParameters.lastName!}"/>
        </div>
        <div class="form-group">
          <label>${uiLabelMap.CommonVerifyCaptchaCode}</label><br/>
          <img id="captchaImage" src="<@ofbizUrl>captcha.jpg?captchaCodeId=captchaImage&amp;unique=${nowTimestamp.getTime()}</@ofbizUrl>" alt="" />
          <a href="javascript:reloadCaptcha('captchaImage');"><i class="fa fa-refresh" aria-hidden="true"></i></a>
        </div>
        <div class="form-group">
          <label>${uiLabelMap.CommonCaptchaCode} *</label>
          <input type="text" autocomplete="off" name="captcha" class="form-control required"/>
        </div>
        <button type="submit" class="btn btn-default">${uiLabelMap.CommonSubmit}</button>
      </form>
    </div>
  </div>
</div>