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

// *** getDependentDropdownValues allows to dynamically populate a dependent dropdown on change on its parent dropdown, doesn't require any fixed naming convention 
// request      = request calling the service which retrieve the info from the DB, ex: getAssociatedStateList
// paramKey     = parameter value used in the called service 
// paramField   = parent dropdown field Id (mainId)
// targetField  = dependend dropdown field Id (dependentId)
// responseName = result returned by the service (using a standard json response, ie chaining json request)
// keyName      = keyName of the dependent dropdown  
// descName     = name of the dependent dropdown description
// selected     = optional name of a selected option
// callback     = optional javascript function called at end
function getDependentDropdownValues(request, paramKey, paramField, targetField, responseName, keyName, descName, selected, callback) {
	data = [ { name: paramKey, value: jQuery('#' + paramField).val()} ];  // get requested value from parent dropdown field 
    jQuery.post(request, data, function(result) { 
        optionList = '';
        list = result[responseName];
        // Create and show dependent select options
        jQuery.each(list, function (key, value) {
            if (typeof value == 'string') {
                values = value.split(': ');
                if (values[1].indexOf(selected) >=0) {
                    optionList += "<option selected='selected' value = " + values[1] + " >" + values[0] + "</option>";
                } else {
                    optionList +=  "<option value = " + values[1] + " >" + values[0] + "</option>";
                }
            } else {
                if (value[keyName] == selected) {
                    optionList += "<option selected='selected' value = " + value[keyName] + " >" + value[descName] + "</option>";
                } else {
                    optionList += "<option value = " + value[keyName] + " >" + value[descName] + "</option>";
                }
            }
        });
        target = '#' + targetField;
        jQuery(target).html(optionList);
        // Hide/show the dependent dropdown
        if ((list.size() < 1) || ((list.size() == 1) && list[0].indexOf("_NA_") >=0)) {                
            if (jQuery(target).is(':visible')) {
                jQuery(target).fadeOut();
            }
        } else {
            if (!jQuery(target).is(':visible')) {
                jQuery(target).fadeIn();
            }
        }
        if (callback != null) eval(callback);
    }, 'json');
}
  
// calls any service already mounted as an event
function getServiceResult(request, params) {
    var data;
    new Ajax.Request(request, {
        asynchronous: false,
        parameters: params,
        onSuccess: function(transport) {
            data = transport.responseText.evalJSON(true);           
        }
    });
    return data;
}