/***********************************************
APACHE OPEN FOR BUSINESS
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
***********************************************/

//jQuery.noConflict();
jQuery(document).ready(function() {
	//Button bar 1. convert buttontext to btn btn-primary
	jQuery('div.button-bar a.buttontext').each(function(){
		var text = jQuery(this).html();
		var icon = "";
		var isCreate = jQuery(this).hasClass("create");//Check for create class
		if(isCreate == true){
			var icon = '<span class="glyphicon glyphicon-plus"></span> '
		}
		var isDelete = jQuery(this).hasClass("delete");//Check for delete class
		if(isDelete == true){
			var icon = '<span class="glyphicon glyphicon-remove"></span> '
		}
		var isRefresh = jQuery(this).hasClass("refresh");//Check for refresh class
		if(isRefresh == true){
			var icon = '<span class="glyphicon glyphicon-refresh"></span> '
		}
		var isSearch = jQuery(this).hasClass("search");//Check for search class
		if(isSearch == true){
			var icon = '<span class="glyphicon glyphicon-search"></span> '
		}
		jQuery(this).html(icon+text);
		jQuery('div.button-bar a.buttontext').removeClass('buttontext').addClass('btn btn-primary btn-sm');
	});
	//Screenlet transforms
	jQuery('div.screenlet').removeClass( "screenlet" ).addClass( "panel panel-default" );
	jQuery('div.screenlet-title-bar').removeClass( "screenlet-title-bar" ).addClass( "panel-heading" );
	//Replace panel-heading ul with panel-title for screenlets defined in ftls
	jQuery('div.panel-heading').each(function(){
		var hasUl = jQuery(this).has("ul").length;
		if(!hasUl == 0){
			//var title = jQuery( "div.panel-heading ul li.h3" ).text();
			var title = jQuery(this).find("li.h3").text();
			jQuery(this).find("li.h3").replaceWith(
					'<div class="pull-left"><h3 class="panel-title">'+ title + '</h3></div>'
					);
			jQuery(this).find('div.pull-left').unwrap();
			jQuery(this).find('li').wrapAll('<ul class="pull-right"></ul>');
			//jQuery(console.log(title));
		}
	});
	
	//Table lists transforms for tables defined in xml forms
	jQuery('tr.alternate-row').removeClass('alternate-row');
	jQuery('table.basic-table.hover-bar').removeClass().addClass("table").addClass("table-hover").addClass("table-striped").addClass("table-condensed");
	jQuery('table tr td a.buttontext').removeClass("buttontext").addClass("btn btn-link btn-block").css("text-align","left");
	
	//Ftl forms transforms
	jQuery('div.screenlet-body').removeClass("screenlet-body").addClass("panel-body");
	jQuery('form').each(function(){
		var hasRole = jQuery(this).is("[role]");
		if(hasRole == false){
			jQuery(this).attr("role","form");
		}
		var hasTable = jQuery(this).has("table").length;
		if(!hasTable == 0){
			var colClass = "";
			var lastColClass = "";
			var numCols = jQuery('table tr:first',this).children('td').size();
			jQuery(console.log('Cols: '+numCols));
			var remainder = 12 % numCols;
			if(numCols == 2){
				var labelColClass = 'col-sm-2';
				var fieldColClass = 'col-sm-10';
			}else{
				if(remainder == 0){
					var colSize = 12/numCols;
					var lastColSize = colSize;
				}else{
					var colSize = (12/numCols)- remainder;
					var lastColSize = colSize + remainder;
				}
			}
			colClass = 'col-sm-'+ colSize;
			lastColClass = 'col-sm-'+ lastColSize;
			//Add the form-horizontal class
			jQuery(this).removeClass("basic-form").addClass("form-horizontal");
			//Determine the number of cells per row
			//Transform table rows
			jQuery('table tr', this).each(function(){
				var fields = [];
				for ( var i = 0; i < numCols; i++ ) {
					if(i == 0){
						var hasContent = jQuery(this).children("td:first").html().length;
						if(!hasContent == 0){
							var contents = jQuery(this).children("td:first").html();
						}else{
							var contents = "&nbsp;";
						}
						if(numCols == 2){
							colClass = labelColClass;
						}
						if(jQuery(this).hasClass("header-row-2")){
							var field = '<div class="'+ colClass +' header-row-2"><label class="header-row-2">'+contents+'</label></div>';
						}else{
							var field = '<div class="'+ colClass +'"><label class="control-label">'+contents+'</label></div>';
						}
					}else{
						var j = i+1;
						var contents = jQuery(this).children("td:nth-child("+j+")").html();
						if(numCols == 2){
							colClass = fieldColClass;
						}
						if(i != numCols){
							if(jQuery(this).hasClass("header-row-2")){
								var field = '<div class="'+ colClass +' header-row-2">'+contents+'</div>';
							}else{
								var field = '<div class="'+ colClass +'">'+contents+'</div>';
							}
						}else{
							if(jQuery(this).hasClass("header-row-2")){
								var field = '<div class="'+ lastColClass +' header-row-2">'+contents+'</div>';
							}else{
								var field = '<div class="'+ lastColClass +'">'+contents+'</div>';
							}
						}
						 
					}
					fields.push(field);
				}
				jQuery(this).replaceWith('<div class="form-group">'+fields.join('')+'</div>');
			})
			//Get all the form data
			var formdata = jQuery(this).children('table').children('tbody').html();
			jQuery(this).children('table').replaceWith(formdata);
		}
	});
	//Transform input types
	jQuery('form input').each(function(){
		var text = jQuery(this).attr("value");
		var icon = "";
		var type = jQuery(this).attr("type");
		var name = jQuery(this).attr("name");
		if(type == "submit"){
			var data = jQuery(this);
			jQuery(this).removeClass("smallSubmit").addClass("btn btn-primary btn-sm");
			/*if(name == "searchButton"){
				//var icon = '<button class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search"></span>'
				jQuery(this).insertBefore('<div class="button-group"><button class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search"></span></button>');
				//jQuery(this).after('</div>');
			}*/
		}
	});
	//Misc transforms
	jQuery('div.topcontainerhidden').removeClass('topcontainerhidden').addClass('panel panel-default');
	jQuery('td.label').removeClass('label').addClass('table-label');
	jQuery('table.basic-table').removeClass('basic-table').addClass('table table-condensed table-striped');
	//jQuery('button.ui-datepicker-trigger').html('<span class="glyphicon glyphicon-calendar"></span>').addClass('btn btn-primary btn-sm');
});

