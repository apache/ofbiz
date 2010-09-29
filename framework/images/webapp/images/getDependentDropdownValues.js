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
// inputField   = optional name of an input field to use instead of a dropdown (this will be extended later to use an of autocompleted dropdown, instead of dropdown or a lookup, when there are too much values to populate)   
// hide         = optional argument, if true the dependend dropdown field (targetField) will be hidden when no options are available else only disabled. False by default.
function getDependentDropdownValues(request, paramKey, paramField, targetField, responseName, keyName, descName, selected, callback, hide) {
	data = [ { name: paramKey, value: jQuery('#' + paramField).val()} ];  // get requested value from parent dropdown field 
	// Call jQuery.post with a json formatted result (see end of code)
    jQuery.post(request, data, function(result) { 
        target = '#' + targetField;
        optionList = '';
        list = result[responseName];
        // this is to handle a specific case where an input field is needed, uses inputField for the field name
        if (!list) {
			jQuery(target).hide();
			jQuery(target).after("<input type='text' name=arguments[9] id=targetField + '_input' size=3>"); 
        	return;
        } else { 
        	if (jQuery(target + '_input')) { 
        		jQuery(target + '_input').remove();            		
				jQuery(target).show();
        	}
        }
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
        jQuery(target).html(optionList).click().change(); // .change() needed when using also asmselect on same field, .click() specifically for IE8
        // Hide/show the dependent dropdown if hide=true else simply disable/enable
        if ((list.size() < 1) || ((list.size() == 1) && list[0].indexOf("_NA_") >=0)) {
        	jQuery(target).attr('disabled', 'disabled');
        	if (hide) {
	            if (jQuery(target).is(':visible')) {
	                jQuery(target).fadeOut();
	            }
        	}
        } else {
        	jQuery(target).removeAttr('disabled');
        	if (hide) {
        		if (!jQuery(target).is(':visible')) {
        			jQuery(target).fadeIn();
        		}
            }
        }
        if (callback != null) eval(callback);
    }, 'json');
}
  
//*** calls any service already mounted as an event
function getServiceResult(request, params) {
    data = [];
    jQuery.each(params, function (key, value) { data.add({ name: key, value: value}) });
    return jQuery.getJSON(request, data, function(result) { return result } )
}

//*** checkUomConversion returns true if an UomConversion exists 
function checkUomConversion(request, params) {
    data = getServiceResult(request, params);    
    return data['exist']; 
}

