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

// ================= FIELD LOOKUP METHODS ============================
var NS4 = (navigator.appName.indexOf("Netscape") >= 0 && ! document.getElementById)? true: false;
var IE4 = (document.all && ! document.getElementById)? true: false;
var IE5 = (document.getElementById && document.all)? true: false;
var NS6 = (document.getElementById && navigator.appName.indexOf("Netscape") >= 0)? true: false;
var mx, my;
var ACTIVATED_LOOKUP = null;
var LOOKUP_DIV = null;
INITIALLY_COLLAPSED = null;

function moveobj(evt) {
    if (NS4 || NS6) {
        mx = evt.screenX;
        my = evt.screenY;
    } else if (IE5 || IE4) {
        mx = event.screenX;
        my = event.screenY;
    }
}

var target = null;
var target2 = null;
var targetW = null;
var lookups =[];

function call_fieldlookup(target, viewName, formName, viewWidth, viewheight) {
    var fieldLookup = new fieldLookup1(target);
    if (! viewWidth) viewWidth = 350;
    if (! viewheight) viewheight = 200;
    fieldLookup.popup(viewName, formName, viewWidth, viewheight);
}
function call_fieldlookupLayer(target, viewName, lookupWidth, lookupHeight, lookupPosition, fadeBackground, initiallyCollapsed) {
    if (isEmpty(target) || isEmpty(viewName)) {
        return lookup_error("Lookup can't be created, one of these variables is missing: target=" + target + " viewName=" + viewName);
    }

    var fieldLookupPopup = new FieldLookupPopup(target, viewName, lookupWidth, lookupHeight, lookupPosition, fadeBackground, initiallyCollapsed, arguments);
    fieldLookupPopup.showLookup();
    this.target = target;
}

function call_fieldlookupLayer3(target, target2, viewName, lookupWidth, lookupHeight, lookupPosition, fadeBackground, initiallyCollapsed) {
    if (isEmpty(target) || isEmpty(target2) || isEmpty(viewName)) {
        return lookup_error("Lookup can't be created, one of these variables is missing: target=" + target + " target2=" + target2 + " viewName=" + viewName);
    }

    var fieldLookupPopup = new FieldLookupPopup(target, viewName, lookupWidth, lookupHeight, lookupPosition, fadeBackground, initiallyCollapsed, arguments);
    fieldLookupPopup.showLookup();
    this.target = target;
    this.target2 = target2;
}

function call_fieldlookup2(target, viewName) {
    var fieldLookup = new fieldLookup1(target, arguments);
    fieldLookup.popup2(viewName);
}

function call_fieldlookup3(target, target2, viewName) {
    var fieldLookup = new fieldLookup2(target, target2, arguments);
    fieldLookup.popup2(viewName);
}

function fieldLookup1(obj_target, args) {
    this.args = args;
    // passing methods
    this.popup = lookup_popup1;
    this.popup2 = lookup_popup2;
    
    // validate input parameters
    if (! obj_target) return lookup_error("Error calling the field lookup: no target control specified");
    if (obj_target.value == null) return lookup_error("Error calling the field lookup: parameter specified is not valid target control");
    //this.target = obj_target;
    targetW = obj_target;
    
    // register in global collections
    //this.id = lookups.length;
    //lookups[this.id] = this;
}
function fieldLookup2(obj_target, obj_target2, args) {
    this.args = args;
    // passing methods
    this.popup = lookup_popup1;
    this.popup2 = lookup_popup2;
    // validate input parameters
    if (! obj_target) return lookup_error("Error calling the field lookup: no target control specified");
    if (obj_target.value == null) return lookup_error("Error calling the field lookup: parameter specified is not valid target control");
    targetW = obj_target;
    // validate input parameters
    if (! obj_target2) return lookup_error("Error calling the field lookup: no target2 control specified");
    if (obj_target2.value == null) return lookup_error("Error calling the field lookup: parameter specified is not valid target2 control");
    target2 = obj_target2;
    
    
    // register in global collections
    //this.id = lookups.length;
    //lookups[this.id] = this;
}

function lookup_popup1(view_name, form_name, viewWidth, viewheight) {
    var obj_lookupwindow = window.open(view_name + '?formName=' + form_name + '&id=' + this.id, '_blank', 'width=' + viewWidth + ',height=' + viewheight + ',scrollbars=yes,status=no,resizable=yes,top=' + my + ',left=' + mx + ',dependent=yes,alwaysRaised=yes');
    obj_lookupwindow.opener = window;
    obj_lookupwindow.focus();
}
function lookup_popup2(view_name) {
    var argString = "";
    if (this.args != null) {
        if (this.args.length > 2) {
            for (var i = 2; i < this.args.length; i++) {
                argString += "&parm" + (i - 2) + "=" + this.args[i];
            }
        }
    }
    var sep = "?";
    if (view_name.indexOf("?") >= 0) {
        sep = "&";
    }
    var obj_lookupwindow = window.open(view_name + sep + 'id=' + this.id + argString, '_blank', 'width=700,height=550,scrollbars=yes,status=no,resizable=yes,top=' + my + ',left=' + mx + ',dependent=yes,alwaysRaised=yes');
    obj_lookupwindow.opener = window;
    obj_lookupwindow.focus();
}
function lookup_error(str_message) {
    alert(str_message);
    return null;
}

function initiallyCollapse() {
    if ((!LOOKUP_DIV) || (INITIALLY_COLLAPSED != "true")) return;
    var slTitleBars = LOOKUP_DIV.getElementsByClassName('screenlet-title-bar');
    for (i in slTitleBars) {
        var slTitleBar = slTitleBars[i];
        var ul = slTitleBar.firstChild;
        if ((typeof ul) != 'object') continue;

        var childElements = ul.childNodes;
        for (j in childElements) {
            if (childElements[j].className == 'expanded' || childElements[j].className == 'collapsed') {
                break;
            }
        }        
        var childEle = childElements[j].firstChild;
        CollapsePanel(childEle, 'lec' + COLLAPSE);
        break;
    }
}

function CollapsePanel(link, areaId){
    var container = $(areaId);
    var liElement = $(link).up('li');
    liElement.removeClassName('expanded');
    liElement.addClassName('collapsed');
    Effect.toggle(container, 'appear');
}

function initiallyCollapseDelayed() {
    setTimeout("initiallyCollapse()", 400);
}


/*************************************
* Fieldlookup Class & Methods
*************************************/

function FieldLookupCounter() {
	this.refArr = {};
    
    this.setReference = function (key, ref) {
        //if key doesn't exist in the array and
        for (itm in this.refArr) {
            if (itm == key) {
                prefix = key.substring(0, key.indexOf("_"));
                key = prefix + "_" + key; 
                this.refArr[""+ key + ""] = ref;
                return this.refArr[key];
            }
        }
        this.refArr[""+ key + ""] = ref;
        return this.refArr[key];
    };
    
    this.getReference = function (key) {
        // when key does not exist return null?
        return this.refArr[key] != null ? this.refArr[key] : null;
    };
    
    this.createNextKey = function () {
        return this.countFields() + "_lookupId";
    };
    
    this.countFields = function () {
        var count = 0;
        jQuery.each(this.refArr, function (itm) {count++;});

        return count;
    };
    
    this.removeReference = function (key) {
        // deletes the Array entry (doesn't effect the array length)
        delete this.refArr[key];
        
        // if all lookups closed, kill the referenze
        if (this.countFields() == 0) {
            ACTIVATED_LOOKUP = null;
        }
    };
    
};
var GLOBAL_LOOKUP_REF = new FieldLookupCounter;


/**
* returns true if a String is empty
* @param value - String value
* @return
*/
function isEmpty(value) {
    if (value == null || value == "") {
        return true;
    }
    return false;
}

function identifyLookup (newAl) {
    if (ACTIVATED_LOOKUP != newAl) { 
        ACTIVATED_LOOKUP = newAl;
    }
}

function hideLookup() {
        obj = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP);
        obj.closeLookup();
    }

//global expand/col button var
var COLLAPSE = 1999;
function getNextCollapseSeq() {
    COLLAPSE++;
    return COLLAPSE;
}

//modify expande/collapse button
function modifyCollapseable(lookupDiv){
    if (!lookupDiv) {
        return;
    }
    var slTitleBars = jQuery("#" + lookupDiv + " .screenlet-title-bar");
    //jQuery("#" + lookupDiv + " li.expanded");
    
    jQuery.each(slTitleBars, function(i) {
    	var slTitleBar = slTitleBars[i];
    	var ul = slTitleBar.firstChild;
        if ((typeof ul) != 'object') {
            return true;
        }
        var childElements = ul.childNodes;

        for (j in childElements) {
            if (childElements[j].className == 'expanded' || childElements[j].className == 'collapsed') {
                break;
            }
        }
        
        getNextCollapseSeq();
        var childEle = childElements[j].firstChild;
        
        childEle.setAttribute('onclick', "javascript:toggleScreenlet(this, 'lec" + COLLAPSE +"', 'true', 'Expand', 'Collapse');");
        childEle.href = "javascript:void(0);"
        jQuery(slTitleBar).next('div').attr('id', 'lec' + COLLAPSE);
    
    });
}

function modifySubmitButton (lookupDiv) {
	/* changes form/submit behavior for Lookup Layer */
    if (lookupDiv) {
        modifyCollapseable(lookupDiv);
        
        //find the lookup form and input button
    	var lookupForm = jQuery("#" + lookupDiv + " form:first");
    	
    	//set new form name and id
        oldFormName = lookupForm.attr("name");
        lookupForm.attr("name", "form_" + lookupDiv);
        lookupForm.attr("id", "form_" + lookupDiv);
        lookupForm = jQuery("#form_" + lookupDiv);
        //set new links for lookups
        var newLookups = jQuery("#" + lookupDiv + " .field-lookup");
        
        /* TODO Problem bei Rekursiven Kalender Aufrufen, da ID des Input feldes immer die gleiche.
         * evtl alle input felder mit DatePicker löschen und neu anlegen?!
        jQuery.each(newLookups, function(newLookup){
        	
        	var link = jQuery(newLookups[newLookup]).find("a:first");
        	alert(link.attr("href"));
            var replaced = new  RegExp('document.' + oldFormName, 'g');
            newLookup.getElementsByTagName('a')[0].href = link.replace(replaced, 'document.'+'form_' + GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).lookupId);
        });
        
        //set new calendar links
        var newLookups = jQuery("#" + lookupDiv + " .view-calendar");
        jQuery.each(newLookups, function(newLookup){
            link = $A(newLookup.getElementsByTagName('a'));
            link.each(function(cal){
                cal.href = cal.href.replace('document.' + oldFormName, 'document.'+'form_' + GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).lookupId);
            });
        });*/
    	
    	var formAction = lookupForm.attr("action");
    	// remove the form action
    	lookupForm.attr("action", "");
    	var input = jQuery("#" + lookupDiv + " input[type=submit]").css({display: "block"});
        
    	// remove the original input button and replace with a new one
        
    	var txt = input.attr("value");
        (input.parent()).append(jQuery("<button/>", {
        	id: "lookupSubmitButton",
        	href: "javascript:void(0);",
        	click: function () {
	            lookupFormAjaxRequest(formAction, lookupForm.attr("id"));
	            return false;
	        },
	        text: txt
        }));
        
        input.remove();
        
        jQuery(document).bind("keypress", function (event) {
            if (event.which == 13) {
            	lookupFormAjaxRequest(formAction, lookupForm.attr("id"));
            	return false;
            }
        });
        
        //modify nav-pager
        var navPagers = jQuery("#" + lookupDiv + " .nav-pager a");
        jQuery.each(navPagers, function(navPager) {
        	jQuery(navPagers[navPager]).attr("href", "javascript:lookupPaginationAjaxRequest('" + jQuery(navPagers[navPager]).attr("href") + "', '" + lookupForm.id + "', 'link')");
        });
        
        var navPagersSelect = jQuery("#" + lookupDiv + " .nav-pager select");
        jQuery.each(navPagersSelect, function(navPager) {
        	// that's quiet wierd maybe someone have a better idea ... that's where the magic happens
        	try {
                  var oc = jQuery(navPagersSelect[navPager]).attr("onchange");
                  if((typeof oc) == "function"){ // IE6/7 Fix
                    oc = oc.toString();
                    var ocSub = oc.substring((oc.indexOf('=') + 2),(oc.length - 4));
                    var searchPattern = /'\+this.value\+'/g;
                    var searchPattern2 = /'\+this.valu/g;

                    if (searchPattern.test(ocSub)) {
                        var viewSize = navPagersSelect[navPager].value;
                        var spl = ocSub.split(searchPattern);
                        navPagersSelect[navPager].onchange = function () {
                            lookupPaginationAjaxRequest(spl[0] + this.value + spl[1], lookupForm.id, 'select');
                        };
                    } else if (searchPattern2.test(ocSub)) {
                        ocSub = ocSub.replace(searchPattern2, "");
                        if (searchPattern.test(ocSub)) {
                            ocSub.replace(searchPattern, viewSize);
                        }
                        navPagersSelect[navPager].onchange = function () {
                            lookupPaginationAjaxRequest(ocSub + this.value, lookupForm.id, 'select');
                        };
                    }
                } else {
                    var ocSub = oc.substring((oc.indexOf('=') + 1),(oc.length - 1));
                    navPagersSelect[navPager].setAttribute("onchange", "lookupPaginationAjaxRequest(" + ocSub + ", '" + lookupForm.id +"')");
                }

                if (resultTable == null) {
                    return;
                }
                resultTable = resultTable.childElements()[0];
                var resultElements = resultTable.childElements();
                for (i in resultElements) {
                    var childElements = resultElements[i].childElements();
                    if (childElements.size() == 1) {
                        continue;
                    }
                    
                    for (k = 1; k < childElements.size(); k++) {
                        var cell = childElements[k];
                        var cellChild = null;
                        cellChild = cell.childElements();
                        if (cellChild.size() > 0) {
                            
                            for (l in cellChild) {
                                var cellElement = cellChild[l];
                                if (cellElement.tagName == 'A') {
                                    var link = cellElement.href;
                                    var liSub = link.substring(link.lastIndexOf('/')+1,(link.length));
                                    if (liSub.contains("javascript:set_")) {
                                        cellElement.href = liSub;
                                    } else {
                                        cellElement.href = "javascript:lookupAjaxRequest('" + liSub + "')";
                                    }
                                }
                            }
                        }
                    }
                }                
            }
            catch (ex) {
            }
        
        
        });
        // modify links in result table ...
        var resultTable= jQuery("#search-results table:first tbody");
        var tableChildren = resultTable.children();
        jQuery.each(tableChildren, function(tableChild){
        	var childElements = jQuery(tableChildren[tableChild]);
        	var tableRow = childElements.children();
        	jQuery.each(tableRow, function(cell){
        		//to skip the first Entry of the row, because it's the normal id link
        		if (cell == 0) return true;
        		
        		var cellChild = null;
                cellChild = jQuery(tableRow[cell]).children();
                jQuery.each(cellChild, function (child) {
                	if (cellChild[child].tagName == "A"){
                		var link = cellChild[child].href;
                        var liSub = link.substring(link.lastIndexOf('/')+1,(link.length));
                        cellChild[child].href = "javascript:lookupAjaxRequest('" + liSub + "')";
                	}
                });
                
        	});
        	
        });
    }
}
/**
 * Create an ajax Request
 */
function lookupAjaxRequest(request) {
    // get request arguments
    var arg = request.substring(request.indexOf('?')+1,(request.length));    

    lookupId = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).lookupId;
	$("#" + lookupId).load(request, arg);
	window.setTimeout("modifySubmitButton('" + lookupId +"')", 800);
}

/**
* Create an ajax request to get the search results
* @param formAction - action target
* @param form - formId
* @return
*/
function lookupFormAjaxRequest(formAction, form) {
	lookupId = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).lookupId;
	$("#" + lookupId).load(formAction, $("#" + form).serialize());
	window.setTimeout("modifySubmitButton('" + lookupId +"')", 800);
}

function lookupPaginationAjaxRequest(navAction, form, type) {
    lookupDiv = (GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).divRef);
    lookupContent = (GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).contentRef);

    if (type == 'link') {
        navAction = navAction.substring(0, navAction.length - 1);
    }
    navAction = navAction + "&presentation=layer";
    
    lookupId = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).lookupId;
	$("#" + lookupId).load(navAction);
	window.setTimeout("modifySubmitButton('" + lookupId +"')", 800);
	
}

/*******************************************************************************************************
* This code inserts the value lookedup by a popup window back into the associated form element
*******************************************************************************************************/
var re_id = new RegExp('id=(\\d+)');
var num_id = (re_id.exec(String(window.location))? new Number(RegExp.$1): 0);
var obj_caller = (window.opener? window.opener.lookups[num_id]: null);
if (obj_caller == null && window.opener != null) {
    obj_caller = window.opener;
} else if (obj_caller == null && window.opener == null) {
    obj_caller = parent;
}

var bkColor = "yellow";
function setSourceColor(src) {
    if (src != null) {
    	src.css({"background-color": bkColor});
    }
}
// function passing selected value to calling window
function set_value (value) {
    if(GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP)){
        obj_caller.target = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).target;
    }
    else{
        obj_caller.target = obj_caller.targetW;
    }    
    var target = obj_caller.target;
    
    write_value(value, target);
    
    closeLookup();
}
// function passing selected value to calling window
function set_values (value, value2) {
    if(GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP)){
        obj_caller.target = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).target;
        obj_caller.target2 = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).target2;
    }
    else{
        obj_caller.target = obj_caller.targetW;        
    }
    var target = obj_caller.target;
    var target2 = obj_caller.target2;
    write_value(value, target);
    write_value(value2, target2)
    
    closeLookup();
}

function write_value (value, target) {
    if (! target) return;
    if (target == null) return;
    
    setSourceColor(target);
    target.val(value);
    //target.fire("lookup:changed");
    //if (target.onchange != null) {     
    //    target.onchange();                    
    //}
}
function set_multivalues(value) {
    obj_caller.target.value = value;
    obj_caller.target.fire("lookup:changed");
    var thisForm = obj_caller.target.form;
    var evalString = "";
    
    if (arguments.length > 2) {
        for (var i = 1; i < arguments.length; i = i + 2) {
            evalString = "setSourceColor(thisForm." + arguments[i] + ")";
            eval(evalString);
            evalString = "thisForm." + arguments[i] + ".value='" + arguments[i + 1] + "'";
            eval(evalString);
        }
    }
    closeLookup();
}
//close the window after passing the value
function closeLookup() {
    if (window.opener != null && GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP) == null) {
        window.close();
    } else {
        obj = GLOBAL_LOOKUP_REF.getReference(ACTIVATED_LOOKUP).lookupId;
        jQuery("#" + obj).dialog("close");
    }
}

//load description for lookup fields 
function lookupDescriptionLoaded(fieldId, url, params) {

    this.fieldId = fieldId;
    this.url = url;
    this.params = params;
    this.updateLookup();
    $(fieldId).observe('change', this.updateLookup.bind(this));
    $(fieldId).observe('lookup:changed', this.updateLookup.bind(this));


    this.updateLookup = function() {
        var tooltipElement = $(this.fieldId + '_lookupDescription');
        if (tooltipElement) {//first remove current description
            tooltipElement.remove();
        }
        if (!$F(this.fieldId)) {
            return;
        }
        //actual server call
        var allParams = this.params + '&' + $(this.fieldId).serialize() + '&' + 'searchType=EQUALS'
        new Ajax.Request(this.url,{parameters: allParams, onSuccess: this.updateFunction.bind(this)});
    }, 
    
    this.updateFunction = function(transport) {
        var wrapperElement = new Element('div').insert(transport.responseText);
        if('UL'!= wrapperElement.firstDescendant().tagName || (wrapperElement.firstDescendant().childElements().length != 1)) {    
            //alert(transport.responseText); response is error or more than one entries are found
            return;
        }
        Element.cleanWhitespace(wrapperElement);
        Element.cleanWhitespace(wrapperElement.down());
        setLookDescription(this.fieldId, wrapperElement.firstDescendant().firstDescendant().innerHTML);
    }            
};