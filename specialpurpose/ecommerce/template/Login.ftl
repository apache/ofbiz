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
<#assign janrainEnabled = Static["org.apache.ofbiz.entity.util.EntityUtilProperties"].getPropertyValue("ecommerce", "janrain.enabled", delegator)>
<#assign appName = Static["org.apache.ofbiz.entity.util.EntityUtilProperties"].getPropertyValue("ecommerce", "janrain.appName", delegator)>
<div class="container-fluid">
  <div class="row">
  
    <#if janrainEnabled == "Y">
      <script type="text/javascript">
(function() {
    if (typeof window.janrain !== 'object') window.janrain = {};
    window.janrain.settings = {};
    
    janrain.settings.tokenUrl = '<@ofbizUrl fullPath="true" secure="true">janrainCheckLogin</@ofbizUrl>';

    function isReady() { janrain.ready = true; };
    if (document.addEventListener) {
      document.addEventListener("DOMContentLoaded", isReady, false);
    } else {
      window.attachEvent('onload', isReady);
    }

    var e = document.createElement('script');
    e.type = 'text/javascript';
    e.id = 'janrainAuthWidget';

    if (document.location.protocol === 'https:') {
      e.src = 'https://rpxnow.com/js/lib/${appName}/engage.js';
    } else {
      e.src = 'http://widget-cdn.rpxnow.com/js/lib/${appName}/engage.js';
    }

    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(e, s);
})();
</script>

      <div class="col-lg-6 col-md-6">
        <div class="panel panel-default">
          <div class="panel-heading">${uiLabelMap.CommonLogin}</div>
          <div class="panel-body">
            <form method="post" action="<@ofbizUrl>login</@ofbizUrl>" name="loginform">
              <div class="form-group">
                <label for="exampleInputEmail1">Email address</label>
                <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Email">
              </div>
              <div class="form-group">
                <label for="userName">${uiLabelMap.CommonUsername}</label>
                <input type="text" id="userName" name="USERNAME" class="form-control" value="<#if requestParameters.USERNAME?has_content>${requestParameters.USERNAME}<#elseif autoUserLogin?has_content>${autoUserLogin.userLoginId}</#if>"/>
              </div>
              <#if autoUserLogin?has_content>
                <p>(${uiLabelMap.CommonNot} ${autoUserLogin.userLoginId}? <a href="<@ofbizUrl>${autoLogoutUrl}</@ofbizUrl>">${uiLabelMap.CommonClickHere}</a>)</p>
              </#if>
              <div class="form-group">
                <label for="password">${uiLabelMap.CommonPassword}:</label>
                <input type="password" class="form-control" id="password" name="PASSWORD" value=""/>
              </div>
              <button type="submit" class="btn btn-primary">${uiLabelMap.CommonLogin}</button>
              <div>
                <label for="newcustomer_submit">${uiLabelMap.CommonMayCreateNewAccountHere}:</label>
                <a href="<@ofbizUrl>newcustomer</@ofbizUrl>">${uiLabelMap.CommonMayCreate}</a>
              </div>
            </form>
          </div>
        </div>
      </div>

      <h1>${uiLabelMap.CommonLogin}</h1>
      <div class="panel panel-default">
        <div class="panle panel-heading">${uiLabelMap.CommonRegistered}</div>
        <div class="panel-body">
          <table width="100%" class="Signlogin">
            <tr>
                <td>
                <form method="post" action="<@ofbizUrl>login</@ofbizUrl>" name="loginform" class="horizontal">
                  <fieldset>
                    <div>
                      <label for="userName">${uiLabelMap.CommonUsername}</label>
                      <input type="text" id="userName" name="USERNAME" value="<#if requestParameters.USERNAME?has_content>${requestParameters.USERNAME}<#elseif autoUserLogin?has_content>${autoUserLogin.userLoginId}</#if>"/>
                    </div>
                    <#if autoUserLogin?has_content>
                      <p>(${uiLabelMap.CommonNot} ${autoUserLogin.userLoginId}? <a href="<@ofbizUrl>${autoLogoutUrl}</@ofbizUrl>">${uiLabelMap.CommonClickHere}</a>)</p>
                    </#if>
                    <div>
                      <label for="password">${uiLabelMap.CommonPassword}:</label>
                      <input type="password" id="password" name="PASSWORD" value=""/>
                    </div>
                    <div>
                      <input type="submit" class="button" value="${uiLabelMap.CommonLogin}"/>
                    </div>
                    <div>
                      <label for="newcustomer_submit">${uiLabelMap.CommonMayCreateNewAccountHere}:</label>
                      <a href="<@ofbizUrl>newcustomer</@ofbizUrl>">${uiLabelMap.CommonMayCreate}</a>
                    </div>
                  </fieldset>
                </form>
                </td>
                <td><div id="janrainEngageEmbed"></div></td>
            </tr>
        </table>
        </div>
      </div>
    <#else>

      <div class="col-lg-6 col-md-6">
        <div class="panel panel-default">
          <div class="panel-heading">${uiLabelMap.CommonLogin}</div>
          <div class="panel-body">
            <form method="post" action="<@ofbizUrl>login</@ofbizUrl>" name="loginform">
              <div class="row">
                <div class="col-lg-12 col-md-12">
                  <div class="form-group">
                    <label for="userName">${uiLabelMap.CommonUsername}</label>
                    <input type="text" class="form-control col-sm-2 control-label" id="userName" name="USERNAME" value="<#if requestParameters.USERNAME?has_content>${requestParameters.USERNAME}<#elseif autoUserLogin?has_content>${autoUserLogin.userLoginId}</#if>"/>
                  </div>
                </div>
              </div>
              <#if autoUserLogin?has_content>
                <p>(${uiLabelMap.CommonNot} ${autoUserLogin.userLoginId}? <a href="<@ofbizUrl>${autoLogoutUrl}</@ofbizUrl>">${uiLabelMap.CommonClickHere}</a>)</p>
              </#if>
              <div class="row">
                <div class="col-lg-12 col-md-12">
                  <div class="form-group">
                    <label for="password">${uiLabelMap.CommonPassword}:</label>
                    <input type="password" class="form-control" id="password" name="PASSWORD" value=""/>
                  </div>
                </div>
              </div>
              <button type="submit" class="btn btn-primary">${uiLabelMap.CommonLogin}</button>
              <div>
                <label for="newcustomer_submit">${uiLabelMap.CommonMayCreateNewAccountHere}:</label>
                <a href="<@ofbizUrl>newcustomer</@ofbizUrl>">${uiLabelMap.CommonMayCreate}</a>
              </div>
            </form>
          </div>
        </div>
      </div>
    </#if>

    <div class="col-lg-6 col-md-6">
      <div class="panel panel-default">
        <div class="panel-heading">${uiLabelMap.CommonForgotYourPassword}</div>
        <div class="panel-body">
          <form method="post" action="<@ofbizUrl>forgotpassword</@ofbizUrl>" class="horizontal">
            <div class="row">
              <div class="col-lg-12 col-md-12">
                <div class="form-group">
                  <label class="control-label" for="forgotpassword_userName">${uiLabelMap.CommonUsername}</label>
                  <input type="text" class="form-control" id="forgotpassword_userName" name="USERNAME" value="<#if requestParameters.USERNAME?has_content>${requestParameters.USERNAME}<#elseif autoUserLogin?has_content>${autoUserLogin.userLoginId}</#if>"/>
                </div>
              </div>
            </div>
            <input type="submit" class="btn btn-primary" name="GET_PASSWORD_HINT" value="${uiLabelMap.CommonGetPasswordHint}"/>
            <input type="submit" class="btn btn-default" name="EMAIL_PASSWORD" value="${uiLabelMap.CommonEmailPassword}"/>
          </form>
          </div>
        </div>
      </div>

<#--    
<div class="screenlet">
  <h3>${uiLabelMap.CommonNewUser}</h3>
  <form method="post" action="<@ofbizUrl>newcustomer</@ofbizUrl>">
    <div>
      <label for="newcustomer_submit">${uiLabelMap.CommonMayCreateNewAccountHere}:</p>
      <input type="submit" class="button" id="newcustomer_submit" value="${uiLabelMap.CommonMayCreate}"/>
    <div>
  </form>
</div>
-->
  </div>
</div>

<script language="JavaScript" type="text/javascript">
  <#if autoUserLogin?has_content>document.loginform.PASSWORD.focus();</#if>
  <#if !autoUserLogin?has_content>document.loginform.USERNAME.focus();</#if>
</script>