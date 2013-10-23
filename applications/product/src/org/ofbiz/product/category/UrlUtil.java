package org.ofbiz.product.category;

import org.ofbiz.base.util.UtilValidate;

public class UrlUtil {
    public static String replaceSpecialCharsUrl(String url) {
        if (UtilValidate.isEmpty(url)) {
            url = "";
        }
        for (String characterPattern : UrlRegexpConfigUtil.getNameFilters().keySet()) {
            url = url.replaceAll(characterPattern, UrlRegexpConfigUtil.getNameFilters().get(characterPattern));
        }
        return url;
    }
}
