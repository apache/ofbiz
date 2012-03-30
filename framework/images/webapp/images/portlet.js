/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
function toggleParentScreenlet(id){
    var element = jQuery("#"+id);
    element.parents("div.screenlet").filter(':first').children("div").filter(':first')
            .children("ul").filter(':first').children("li.expanded").filter(':first').children('a')
            .filter(':first').click();
}

/*Begin addon modification : event-messages*/
/** Submit form, update multiple areas (HTML container elements).
 * @param form The form element
 * @param areaCsvString The area CSV string. The CSV string is a flat array in the
 * form of: areaId, target, target parameters, boolean addParam [, areaId, target, target parameters...].
 * @param returnParamMap The parameters to return. It is a map in the  form of:
 *  returnName1: parameterName1 , returnName2: parameterName2,... and will be added to the update area parameters with boolean addParam
 */

function ajaxSubmitFormUpdateAreasWithReturn(form, areaCsvString, returnParamMap) {
  submitFormDisableSubmits($(form));
  waitSpinnerShow();
  updateFunction = function(data) {
      hideErrorContainer = function() {
          jQuery('#content-messages').remove();
      }
      if ((data._ERROR_MESSAGE_LIST_ != undefined || data._ERROR_MESSAGE_ != undefined) 
           && (data.responseMessage == undefined || data.responseMessage != "fail") ) {
          showMessages('errorMessage',data._ERROR_MESSAGE_,data._ERROR_MESSAGE_LIST_);
          waitSpinnerHide();
      }else {
          //update areas
          var areaArray = areaCsvString.split(",");
          var numAreas = parseInt(areaArray.length / 4);
          for (var i = 0; i < numAreas * 4; i = i + 4) {
              //add return param to last update area request if necessary
              var addParams = areaArray[i + 3];
              params = areaArray[i + 2];
              //alert('addParams='+addParams+' params='+params+' areaArray[i]='+areaArray[i]+' areaArray[i+1]='+areaArray[i+1]);
              if (addParams && addParams != "false") {
                  for (var key in returnParamMap) {
                        if(data[returnParamMap[key]] != undefined){
                            params = params + "&" + key + "=" + data[returnParamMap[key]];
                       }
                  }
              }
              ajaxUpdateArea(areaArray[i], areaArray[i + 1], params);
          }
          // now show message if needed
          if (data.responseMessage != undefined && data.responseMessage == "fail") {
              showMessages('failMessage',data._ERROR_MESSAGE_,data._ERROR_MESSAGE_LIST_);
          }
          else if (data._EVENT_MESSAGE_LIST_ != undefined || data._EVENT_MESSAGE_ != undefined){
              showMessages('eventMessage',data._EVENT_MESSAGE_,data._EVENT_MESSAGE_LIST_);
          }
          else {
              if(jQuery('#content-messages').text()) {
                jQuery('#content-messages').remove();
              }
          }
          waitSpinnerHide();
      }
  }
  jQuery.ajax({
      type: "POST",
      url: jQuery("#" + form).attr("action"),
      data: jQuery("#" + form).serialize(),
      success: function(data) {
              updateFunction(data);
      }
  });
}

/**
 * Add message div after content-main-section, and Print message to user with class=classMessage
 * @param classMessage
 * @param message
 * @param messageList
 * @return
 */
function showMessages(classMessage, message, messageList){
    if(!jQuery('#content-messages').text()) {
        //add this div just after app-navigation
         if(jQuery('#content-main-section')){
             jQuery('#content-main-section').before('<div id="content-messages" onclick="hideErrorContainer()"></div>');
         }
    }
    jQuery('#content-messages').removeClass();
    jQuery('#content-messages').addClass(classMessage);
    if (message != undefined && messageList != undefined) {
        jQuery('#content-messages' ).html(message + " " + messageList);
    } else if (messageList != undefined) {
        jQuery('#content-messages' ).html("" + messageList);
    } else {
        jQuery('#content-messages' ).html("" + message);
    }
    jQuery('#content-messages').fadeIn('fast');
}

function clickLink(linkDivId) {
    var div = document.getElementById(linkDivId); 
    if(!div ) return; 
    var aNodeList = div.getElementsByTagName("A");
    link = aNodeList.item(0);
    target =link.href;
    if(target.match(new RegExp("javascript:.*"))){
        eval(target);
    } else {
        window.location.href = target;
    }
}
function refrshPortlet(areaTargets, areaIds, areaParams, formName, areaForms, collapse, toggle, markSelected){
    waitSpinnerShow();
    var arealist = areaIds.split(";");
    var targetlist = areaTargets.split(";");
    var paramsList = areaParams.split(";");
    var formList = areaForms.split(";");
    var areasCsv = "";
    for( var index =0; index<arealist.length; index= index+1){
        areasCsv = areasCsv + arealist[index] + ",";
        areasCsv = areasCsv + targetlist[index] + ",";
        appendFormParams = formList[index];
        var targetParams = paramsList[index];
        if (targetParams== undefined) {
            targetParams = "";
        }
        if (appendFormParams) {
            var forms = appendFormParams.split(",")
            for (var i=0; i<forms.length; i++) {
                var toSerializeName = forms[i];
                var formToSerialize =jQuery("#"+toSerializeName); 
                if(formToSerialize.length == 1) {
                    var formFields = formToSerialize.serialize().split("&");
                    for (var j=0; j < formFields.length; j++) {
                        var formField = formFields[j].split("=");
                        var name = formField[0];
                        if(formField.length > 1 &&(( targetParams.indexOf(name + "=") == -1) || 
                                ( targetParams.indexOf(name + "=") > 0 && targetParams.indexOf("&" + name + "=") > 0))) 
                        {
                            targetParams = targetParams  + "&" + name + "=" + formField[1];
                        }
                    }
                }
            }
            //targetParams = targetParams  + "&" +$("#"+appendFormParams).serialize();
        }
        areasCsv = areasCsv + targetParams + ",";
    }
    areasCsv = areasCsv.substring(0,areasCsv.length-1)
    ajaxUpdateAreas(areasCsv);
    if ("true" == collapse) {
        toggleParentScreenlet(toggle);
    }
    if ("true" == markSelected) {
        markRowAsSelected(toggle);
    }
}
function markRowAsSelected(id){
    var element = jQuery("#"+id);
    // to ensure that all precedent selection are removed.
    // a row is marked as selected by changing the css class of its tr.
    // the tr is the first ancestor of the element defined by id.
    var parentTR = element.parents("tr");
    element.parents("tbody").children("tr").removeClass("selected");
    parentTR.addClass('selected');

}
function evalScripts(element) {
var scripts = element.find("script");
    for (i=0; i < scripts.length; i++) {
        // if src, eval it, otherwise eval the body
        if (scripts[i].hasAttribute("src")) {
            var src = scripts[i].getAttribute("src");
            var script = document.createElement('script');
            script.setAttribute("src", src);
            document.getElementsByTagName('body')[0].appendChild(script);
        } else {
            eval(scripts[i].innerHTML);
        }
    }
}
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
function toggleParentScreenlet(id){
    var element = jQuery("#"+id);
    element.parents("div.screenlet").filter(':first').children("div").filter(':first')
            .children("ul").filter(':first').children("li.expanded").filter(':first').children('a')
            .filter(':first').click();
}

/*Begin addon modification : event-messages*/
/** Submit form, update multiple areas (HTML container elements).
 * @param form The form element
 * @param areaCsvString The area CSV string. The CSV string is a flat array in the
 * form of: areaId, target, target parameters, boolean addParam [, areaId, target, target parameters...].
 * @param returnParamMap The parameters to return. It is a map in the  form of:
 *  returnName1: parameterName1 , returnName2: parameterName2,... and will be added to the update area parameters with boolean addParam
 */

function ajaxSubmitFormUpdateAreasWithReturn(form, areaCsvString, returnParamMap) {
  submitFormDisableSubmits($(form));
  waitSpinnerShow();
  updateFunction = function(data) {
      hideErrorContainer = function() {
          jQuery('#content-messages').remove();
      }
      if ((data._ERROR_MESSAGE_LIST_ != undefined || data._ERROR_MESSAGE_ != undefined) 
           && (data.responseMessage == undefined || data.responseMessage != "fail") ) {
          showMessages('errorMessage',data._ERROR_MESSAGE_,data._ERROR_MESSAGE_LIST_);
          waitSpinnerHide();
      }else {
          //update areas
          var areaArray = areaCsvString.split(",");
          var numAreas = parseInt(areaArray.length / 4);
          for (var i = 0; i < numAreas * 4; i = i + 4) {
              //add return param to last update area request if necessary
              var addParams = areaArray[i + 3];
              params = areaArray[i + 2];
              //alert('addParams='+addParams+' params='+params+' areaArray[i]='+areaArray[i]+' areaArray[i+1]='+areaArray[i+1]);
              if (addParams && addParams != "false") {
                  for (var key in returnParamMap) {
                        if(data[returnParamMap[key]] != undefined){
                            params = params + "&" + key + "=" + data[returnParamMap[key]];
                       }
                  }
              }
              ajaxUpdateArea(areaArray[i], areaArray[i + 1], params);
          }
          // now show message if needed
          if (data.responseMessage != undefined && data.responseMessage == "fail") {
              showMessages('failMessage',data._ERROR_MESSAGE_,data._ERROR_MESSAGE_LIST_);
          }
          else if (data._EVENT_MESSAGE_LIST_ != undefined || data._EVENT_MESSAGE_ != undefined){
              showMessages('eventMessage',data._EVENT_MESSAGE_,data._EVENT_MESSAGE_LIST_);
          }
          else {
              if(jQuery('#content-messages').text()) {
                jQuery('#content-messages').remove();
              }
          }
          waitSpinnerHide();
      }
  }
  jQuery.ajax({
      type: "POST",
      url: jQuery("#" + form).attr("action"),
      data: jQuery("#" + form).serialize(),
      success: function(data) {
              updateFunction(data);
      }
  });
}

/**
 * Add message div after content-main-section, and Print message to user with class=classMessage
 * @param classMessage
 * @param message
 * @param messageList
 * @return
 */
function showMessages(classMessage, message, messageList){
    if(!jQuery('#content-messages').text()) {
        //add this div just after app-navigation
         if(jQuery('#content-main-section')){
             jQuery('#content-main-section').before('<div id="content-messages" onclick="hideErrorContainer()"></div>');
         }
    }
    jQuery('#content-messages').removeClass();
    jQuery('#content-messages').addClass(classMessage);
    if (message != undefined && messageList != undefined) {
        jQuery('#content-messages' ).html(message + " " + messageList);
    } else if (messageList != undefined) {
        jQuery('#content-messages' ).html("" + messageList);
    } else {
        jQuery('#content-messages' ).html("" + message);
    }
    jQuery('#content-messages').fadeIn('fast');
}

function clickLink(linkDivId) {
    var div = document.getElementById(linkDivId); 
    if(!div ) return; 
    var aNodeList = div.getElementsByTagName("A");
    link = aNodeList.item(0);
    target =link.href;
    if(target.match(new RegExp("javascript:.*"))){
        eval(target);
    } else {
        window.location.href = target;
    }
}
function refrshPortlet(areaTargets, areaIds, areaParams, formName, areaForms, collapse, toggle, markSelected){
    waitSpinnerShow();
    var arealist = areaIds.split(";");
    var targetlist = areaTargets.split(";");
    var paramsList = areaParams.split(";");
    var formList = areaForms.split(";");
    var areasCsv = "";
    for( var index =0; index<arealist.length; index= index+1){
        areasCsv = areasCsv + arealist[index] + ",";
        areasCsv = areasCsv + targetlist[index] + ",";
        appendFormParams = formList[index];
        var targetParams = paramsList[index];
        if (targetParams== undefined) {
            targetParams = "";
        }
        if (appendFormParams) {
            var forms = appendFormParams.split(",")
            for (var i=0; i<forms.length; i++) {
                var toSerializeName = forms[i];
                var formToSerialize =jQuery("#"+toSerializeName); 
                if(formToSerialize.length == 1) {
                    var formFields = formToSerialize.serialize().split("&");
                    for (var j=0; j < formFields.length; j++) {
                        var formField = formFields[j].split("=");
                        var name = formField[0];
                        if(formField.length > 1 &&(( targetParams.indexOf(name + "=") == -1) || 
                                ( targetParams.indexOf(name + "=") > 0 && targetParams.indexOf("&" + name + "=") > 0))) 
                        {
                            targetParams = targetParams  + "&" + name + "=" + formField[1];
                        }
                    }
                }
            }
            //targetParams = targetParams  + "&" +$("#"+appendFormParams).serialize();
        }
        areasCsv = areasCsv + targetParams + ",";
    }
    areasCsv = areasCsv.substring(0,areasCsv.length-1)
    ajaxUpdateAreas(areasCsv);
    if ("true" == collapse) {
        toggleParentScreenlet(toggle);
    }
    if ("true" == markSelected) {
        markRowAsSelected(toggle);
    }
}
function markRowAsSelected(id){
    var element = jQuery("#"+id);
    // to ensure that all precedent selection are removed.
    // a row is marked as selected by changing the css class of its tr.
    // the tr is the first ancestor of the element defined by id.
    var parentTR = element.parents("tr");
    element.parents("tbody").children("tr").removeClass("selected");
    parentTR.addClass('selected');

}
function evalScripts(element) {
var scripts = element.find("script");
    for (i=0; i < scripts.length; i++) {
        // if src, eval it, otherwise eval the body
        if (scripts[i].hasAttribute("src")) {
            var src = scripts[i].getAttribute("src");
            var script = document.createElement('script');
            script.setAttribute("src", src);
            document.getElementsByTagName('body')[0].appendChild(script);
        } else {
            eval(scripts[i].innerHTML);
        }
    }
}