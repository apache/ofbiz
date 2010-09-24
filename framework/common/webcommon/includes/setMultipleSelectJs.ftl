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
<script type="text/javascript">
jQuery(document).ready(function() {
<#if asm_title?exists> <#-- set the dropdown "title" if exists -->
  jQuery("#${asm_multipleSelect}").attr('title', '${asm_title}');
</#if>
  // use asmSelect
  jQuery("#${asm_multipleSelect}").asmSelect({
    addItemTarget: 'top',
    sortable: ${asm_sortable},
    removeLabel: '${uiLabelMap.CommonRemove}'
  });
    
<#if asm_relatedField?exists> <#-- can be used without related field -->
  // track possible relatedField changes
  if (jQuery('#${asm_multipleSelectForm}')) {
    // on initial focus or if the field value changes, select related multi values. 
    // FIXME : not sure why focus does not work here, must be added as event/action in the multipleSelectForm.relatedField
    jQuery("#${asm_relatedField}").bind('change focus', function() {
      typeValue = jQuery('#${asm_typeField}').val();
      selectMultipleRelatedValues('${asm_requestName}', '${asm_paramKey}', '${asm_relatedField}', '${asm_multipleSelect}', '${asm_type}', typeValue, '${asm_responseName}');
    });
  }    
</#if>
});
</script>

<style type="text/css">
#${asm_multipleSelectForm} {
    width: ${asm_formSize}px; 
    position: relative;
}

.asmListItem {
  width: ${asm_asmListItemPercentOfForm}%; 
}
</style>