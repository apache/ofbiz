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
<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.PartyChangePassword}</div>
  <div class="panel-body">
  &nbsp;<a id="CommonGoBack1" href="<@ofbizUrl>${donePage}</@ofbizUrl>" class="button">${uiLabelMap.CommonGoBack}</a>
  &nbsp;<a id="CommonSave1" href="javascript:document.getElementById('changepasswordform').submit()" class="button">${uiLabelMap.CommonSave}</a>
  <p/>   
  <div class="screenlet-body">
    <form id="changepasswordform" method="post" action="<@ofbizUrl>updatePassword/${donePage}</@ofbizUrl>">
      <div class="form-group">
          <label for="currentPassword">${uiLabelMap.PartyOldPassword}</label>
          <input type="password" class='inputBox form-control' name="currentPassword" id="currentPassword" maxlength="20" />*
        </div>
        <div class="form-group">
          <label for="newPassword">${uiLabelMap.PartyNewPassword}</label>
          <input type="password" class='inputBox form-control' name="newPassword" id="newPassword" maxlength="20" />*
        </div>
        <div class="form-group">
          <label for="newPasswordVerify">${uiLabelMap.PartyNewPasswordVerify}</label>
          <input type="password" class='inputBox form-control' name="newPasswordVerify" id="newPasswordVerify" maxlength="20" />*
        </div>
        <div class="form-group">
          <label for="passwordHint">${uiLabelMap.PartyPasswordHint}</label>
          <input type="text" class='inputBox form-control' maxlength="100" name="passwordHint" id="passwordHint" value="${userLoginData.passwordHint!}" />
        </div>
        <label>${uiLabelMap.CommonFieldsMarkedAreRequired}</label>
    </form>
    <a href="<@ofbizUrl>${donePage}</@ofbizUrl>" class="button">[${uiLabelMap.CommonGoBack}]</a>
    <a href="javascript:document.getElementById('changepasswordform').submit()" class="btn btn-default">[${uiLabelMap.CommonSave}]</a>
  </div>
</div>
