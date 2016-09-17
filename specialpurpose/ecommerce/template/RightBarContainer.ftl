<#if rightbarScreenName?has_content>
  <div class="col-lg-3 col-md-3 pull-right">
    ${screens.render("component://ecommerce/widget/CommonScreens.xml#language")}
    ${screens.render("component://ecommerce/widget/CartScreens.xml#minicart")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#compareproductslist")}
    ${screens.render("component://ecommerce/widget/CartScreens.xml#minipromotext")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#miniassocprods")}
    ${screens.render("component://ecommerce/widget/ContentScreens.xml#factoids")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#minilastviewedcategories")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#minilastproductsearches")}
    ${screens.render("component://ecommerce/widget/ContentScreens.xml#recentlyviewed")}
    ${screens.render("component://ecommerce/widget/ContentScreens.xml#recentlyviewed")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#populartags")}
  </div>
</#if>