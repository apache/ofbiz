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

    // use asmSelect
    jQuery("#${multipleSelect}").asmSelect({
      addItemTarget: 'top',
      sortable: ${sortable},
      removeLabel: '${uiLabelMap.CommonRemove}'
    });
    
    // track possible relatedField changes
    if (jQuery('#${multipleSelectForm}')) {
      // on initial focus or if the field value changes, select related multi values. 
      // FIXME : not sure why focus does not work here, must be added as event/action in the multipleSelectForm.relatedField
      jQuery("#${relatedField}").bind('change focus', function() {
        typeValue = jQuery('#${typeField}').val();
        selectMultipleRelatedValues('${requestName}', '${paramKey}', '${relatedField}', '${multipleSelect}', '${type}', typeValue, '${responseName}');
      });
    } 
  }); 
</script>

<style type="text/css">
#${multipleSelectForm} {
    width: ${formSize}px; 
    position: relative;
  }

.asmListItem {
  width: ${asmListItemPercentOfForm}%; 
}
</style>