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

<#assign productPrice = productPriceList[0]!/>

<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.PageTitleDigitalProductEdit}</div>
  <div class="panel-body">
<#if !supplierProduct?has_content && parameters.productId?has_content>
  <div><h3>${uiLabelMap.EcommerceMessage1} [${parameters.productId}] ${uiLabelMap.EcommerceMessage2}</h3></div>
<#else>

    <#if !supplierProduct??>
      <h1>${uiLabelMap.EcommerceAddNewDigitalProduct}</h1>
      <form method="post" action="<@ofbizUrl>createCustomerDigitalDownloadProduct</@ofbizUrl>" name="editdigitaluploadform" style="margin: 0;">
        <input type="hidden" name="productStoreId" value="${productStore.productStoreId}" />
    <#else>
      <h1>${uiLabelMap.EcommerceUpdateDigitalProduct}</h1>
      <form method="post" action="<@ofbizUrl>updateCustomerDigitalDownloadProduct</@ofbizUrl>" name="editdigitaluploadform" style="margin: 0;">
        <input type="hidden" name="productId" value="${parameters.productId}" />
        <input type="hidden" name="currencyUomId" value="${parameters.currencyUomId}" />
        <input type="hidden" name="minimumOrderQuantity" value="${parameters.minimumOrderQuantity}" />
        <input type="hidden" name="availableFromDate" value="${parameters.availableFromDate}" />
    </#if>
    &nbsp;<a href="<@ofbizUrl>digitalproductlist</@ofbizUrl>" class="buttontext">${uiLabelMap.CommonBackToList}</a>

    <div class="form-group">
      <label>${uiLabelMap.ProductProductName}</label>
      <input type="text" class="inputBox form-control" name="productName" value="${(product.productName)!}"/>
    </div>
    <div class="form-group">
      <label>${uiLabelMap.ProductProductDescription}</label>
      <input type="text" class="inputBox form-control" name="description" value="${(product.description)!}"/>
    </div>
    <div class="form-group">
      <label>${uiLabelMap.ProductPrice}</label>
      <input type="text" class="inputBox form-control" name="price" value="${(productPrice.price)!}"/>
    </div>
    <a href="javascript:document.editdigitaluploadform.submit()" class="btn btn-default">${uiLabelMap.CommonSave}</a>
  </form>
</#if>
    </div>
</div>

<#if supplierProduct?has_content>
<div class="panel panel-default">
  <div class="panel-heading">${uiLabelMap.OrderDigitalProductFiles}</div>
  <div class="panel-body">
        <#list productContentAndInfoList as productContentAndInfo>
            <div>
              ${productContentAndInfo.contentName} (${uiLabelMap.CommonSince}: ${productContentAndInfo.fromDate})
              <a href="<@ofbizUrl>removeCustomerDigitalDownloadProductFile?contentId=${productContentAndInfo.contentId}&amp;productContentTypeId=${productContentAndInfo.productContentTypeId}&amp;fromDate=${productContentAndInfo.fromDate}&amp;productId=${parameters.productId}&amp;currencyUomId=${parameters.currencyUomId}&amp;minimumOrderQuantity=${parameters.minimumOrderQuantity}&amp;availableFromDate=${parameters.availableFromDate}</@ofbizUrl>" class="buttontext">${uiLabelMap.CommonRemove}</a>
            </div>
        </#list>

        <div><hr /></div>
        <div class="tableheadtext">${uiLabelMap.EcommerceDigitalAddFromMyFiles}</div>
        <div>
        <form method="post" action="<@ofbizUrl>addCustomerDigitalDownloadProductFile</@ofbizUrl>" name="adddigitaluploadfile" style="margin: 0;">
          <input type="hidden" name="productId" value="${parameters.productId}" />
          <input type="hidden" name="currencyUomId" value="${parameters.currencyUomId}" />
          <input type="hidden" name="minimumOrderQuantity" value="${parameters.minimumOrderQuantity}" />
          <input type="hidden" name="availableFromDate" value="${parameters.availableFromDate}" />
          <div class="form-group">
          <select name="contentId" class="selectBox form-control">
            <#list ownerContentAndRoleList as ownerContentAndRole>
              <option value="${ownerContentAndRole.contentId}">${ownerContentAndRole.contentName}</option>
            </#list>
          </select>
          </div>
          <a href="javascript:document.adddigitaluploadfile.submit()" class="buttontext">${uiLabelMap.CommonAdd}</a>
        </form>
        </div>
    &nbsp;<a href="<@ofbizUrl>digitalproductlist</@ofbizUrl>" class="buttontext">${uiLabelMap.CommonBackToList}</a>
    </div>
</div>
</#if>
