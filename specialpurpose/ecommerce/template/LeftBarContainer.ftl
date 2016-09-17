<#if leftbarScreenName?has_content>
  <div class="col-lg-2 col-md-2">
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#choosecatalog")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#keywordsearchbox")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#productCategories")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#LayeredNavBar")}
    ${screens.render("component://ecommerce/widget/CatalogScreens.xml#minireorderprods")}
    ${screens.render("component://ecommerce/widget/EmailContactListScreens.xml#signupforcontactlist")}
    ${screens.render("component://ecommerce/widget/ContentScreens.xml#minipoll")}
    ${screens.render("component://ecommerce/widget/ForumScreens.xml#forums")}
    ${screens.render("component://ecommerce/widget/ContentScreens.xml#content_categories")}
    ${screens.render("component://ecommerce/widget/blog/BlogScreens.xml#blogs")}
  </div>
</#if>