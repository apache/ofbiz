/*******************************************************************************
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
 *******************************************************************************/
package org.ofbiz.product.category;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilURL;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * SeoConfigUtil - SEO Configuration file utility.
 * 
 */
public class SeoConfigUtil {

    private static final String module = SeoConfigUtil.class.getName();
    private static Perl5Compiler perlCompiler = new Perl5Compiler();
    private static boolean isInitialed = false;
    private static boolean debug = false;
    private static boolean categoryUrlEnabled = true;
    private static boolean categoryNameEnabled = false;
    private static String categoryUrlSuffix = null;
    public static final String DEFAULT_REGEXP = "^.*/.*$";
    private static Pattern regexpIfMatch = null;
    private static boolean useUrlRegexp = false;
    private static boolean jSessionIdAnonEnabled = false;
    private static boolean jSessionIdUserEnabled = false;
    private static Map<String, String> seoReplacements = null;
    private static Map<String, Pattern> seoPatterns = null;
    private static Map<String, String> forwardReplacements = null;
    private static Map<String, Pattern> forwardPatterns = null;
    private static Map<String, Integer> forwardResponseCodes = null;
    private static Map<String, String> nameFilters = null;
    private static List<Pattern> userExceptionPatterns = null;
    private static Set<String> allowedContextPaths = null;
    private static Map<String, String> specialProductIds = null;
    public static final String ELEMENT_REGEXPIFMATCH = "regexpifmatch";
    public static final String ELEMENT_DEBUG = "debug";
    public static final String ELEMENT_CONFIG = "config";
    public static final String ELEMENT_DESCRIPTION = "description";
    public static final String ELEMENT_FORWARD = "forward";
    public static final String ELEMENT_SEO = "seo";
    public static final String ELEMENT_URLPATTERN = "url-pattern";
    public static final String ELEMENT_REPLACEMENT = "replacement";
    public static final String ELEMENT_RESPONSECODE = "responsecode";
    public static final String ELEMENT_JSESSIONID = "jsessionid";
    public static final String ELEMENT_ANONYMOUS = "anonymous";
    public static final String ELEMENT_VALUE = "value";
    public static final String ELEMENT_USER = "user";
    public static final String ELEMENT_EXCEPTIONS = "exceptions";
    public static final String ELEMENT_NAME_FILTERS = "name-filters";
    public static final String ELEMENT_FILTER = "filter";
    public static final String ELEMENT_CHARACTER_PATTERN = "character-pattern";
    public static final String SEO_CONFIG_FILENAME = "SeoConfig.xml";
    public static final int DEFAULT_RESPONSECODE = HttpServletResponse.SC_MOVED_PERMANENTLY;
    public static final String DEFAULT_ANONYMOUS_VALUE = "disable";
    public static final String DEFAULT_USER_VALUE = "disable";
    public static final String DEFAULT_CATEGORY_URL_VALUE = "enable";
    public static final String DEFAULT_CATEGORY_NAME_VALUE = "disable";
    public static final String ALLOWED_CONTEXT_PATHS_SEPERATOR = ":";
    /**
     * Initialize url regular express configuration.
     * 
     * @return result to indicate the status of initialization.
     */
    public static void init() {
        FileInputStream configFileIS = null;
        String result = "success";
        seoPatterns = new HashMap<String, Pattern>();
        seoReplacements = new HashMap<String, String>();
        forwardReplacements = new HashMap<String, String>();
        forwardPatterns = new HashMap<String, Pattern>();
        forwardResponseCodes = new HashMap<String, Integer>();
        userExceptionPatterns = FastList.newInstance();
        specialProductIds = FastMap.newInstance();
        nameFilters = FastMap.newInstance();
        try {
            Document configDoc = UtilXml.readXmlDocument(UtilURL.fromResource(SEO_CONFIG_FILENAME), false);
            Element rootElement = configDoc.getDocumentElement();

            String regexIfMatch = UtilXml.childElementValue(rootElement,
                    ELEMENT_REGEXPIFMATCH, DEFAULT_REGEXP);
            try {
                regexpIfMatch = perlCompiler.compile(regexIfMatch,
                        Perl5Compiler.DEFAULT_MASK);
            } catch (MalformedPatternException e1) {
                Debug.logWarning(e1, module);
            }
            debug = Boolean.parseBoolean(UtilXml.childElementValue(
                    rootElement, ELEMENT_DEBUG, "false"));

            // parse jsessionid element
            try {
                Element jSessionId = UtilXml.firstChildElement(rootElement,
                        ELEMENT_JSESSIONID);
                if (jSessionId != null) {
                    Element anonymous = UtilXml.firstChildElement(jSessionId, ELEMENT_ANONYMOUS);
                    if (anonymous != null) {
                        String anonymousValue = UtilXml.childElementValue(anonymous, ELEMENT_VALUE, DEFAULT_ANONYMOUS_VALUE);
                        if (DEFAULT_ANONYMOUS_VALUE.equalsIgnoreCase(anonymousValue)) {
                            jSessionIdAnonEnabled = false;
                        } else {
                            jSessionIdAnonEnabled = true;
                        }
                    }
                    
                    Element user = UtilXml.firstChildElement(jSessionId, ELEMENT_USER);
                    if (user != null) {
                        String userValue = UtilXml.childElementValue(user, ELEMENT_VALUE, DEFAULT_USER_VALUE);
                        if (DEFAULT_USER_VALUE.equalsIgnoreCase(userValue)) {
                            jSessionIdUserEnabled = false;
                        } else {
                            jSessionIdUserEnabled = true;
                        }
                        Element exceptions = UtilXml.firstChildElement(user, ELEMENT_EXCEPTIONS);
                        if (exceptions != null) {
                            List<? extends Element> exceptionUrlPatterns = UtilXml.childElementList(exceptions, ELEMENT_URLPATTERN);
                            for (int i = 0; i < exceptionUrlPatterns.size(); i++) {
                                Element element = (Element) exceptionUrlPatterns.get(i);
                                String urlpattern = element.getTextContent();
                                if (UtilValidate.isNotEmpty(urlpattern)) {
                                    try {
                                        Pattern pattern = perlCompiler.compile(
                                                urlpattern, Perl5Compiler.DEFAULT_MASK);
                                        userExceptionPatterns.add(pattern);
                                    } catch (MalformedPatternException e) {
                                        Debug.logWarning(e, "skip this url replacement if any error happened", module);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                Debug.logWarning(e, "no \"jsessionid\" element", module);
            }
            
            // parse name-filters elements
            try {
                NodeList nameFilterNodes = rootElement
                        .getElementsByTagName(ELEMENT_FILTER);
                for (int i = 0; i < nameFilterNodes.getLength(); i++) {
                    Element element = (Element) nameFilterNodes.item(i);
                    String charaterPattern = UtilXml.childElementValue(element,
                            ELEMENT_CHARACTER_PATTERN, null);
                    String replacement = UtilXml.childElementValue(element,
                            ELEMENT_REPLACEMENT, null);
                    if (UtilValidate.isNotEmpty(charaterPattern)
                            && UtilValidate.isNotEmpty(replacement)) {
                        try {
                            perlCompiler.compile(
                                    charaterPattern, Perl5Compiler.DEFAULT_MASK);
                            nameFilters.put(charaterPattern,
                                    replacement);
                        } catch (MalformedPatternException e) {
                            Debug.logWarning(e, "skip this filter (character-pattern replacement) if any error happened", module);
                        }
                    }
                }
            } catch (NullPointerException e) {
                Debug.logWarning(e, "no \"name-filters\" element", module);
            }

            // parse config elements
            try {
                // construct seo patterns
                NodeList seos = rootElement
                        .getElementsByTagName(ELEMENT_SEO);
                for (int i = 0; i < seos.getLength(); i++) {
                    Element element = (Element) seos.item(i);
                    String urlpattern = UtilXml.childElementValue(element,
                            ELEMENT_URLPATTERN, null);
                    String replacement = UtilXml.childElementValue(element,
                            ELEMENT_REPLACEMENT, null);
                    if (UtilValidate.isNotEmpty(urlpattern)
                            && UtilValidate.isNotEmpty(replacement)) {
                        try {
                            Pattern pattern = perlCompiler.compile(
                                    urlpattern, Perl5Compiler.DEFAULT_MASK);
                            seoReplacements.put(urlpattern,
                                    replacement);
                            seoPatterns.put(urlpattern, pattern);
                        } catch (MalformedPatternException e) {
                            Debug.logWarning(e, "skip this url replacement if any error happened", module);
                        }
                    }
                }

                // construct forward patterns
                NodeList forwards = rootElement
                        .getElementsByTagName(ELEMENT_FORWARD);
                for (int i = 0; i < forwards.getLength(); i++) {
                    Element element = (Element) forwards.item(i);
                    String urlpattern = UtilXml.childElementValue(element,
                            ELEMENT_URLPATTERN, null);
                    String replacement = UtilXml.childElementValue(element,
                            ELEMENT_REPLACEMENT, null);
                    String responseCode = UtilXml.childElementValue(element,
                            ELEMENT_RESPONSECODE, String.valueOf(DEFAULT_RESPONSECODE));
                    if (UtilValidate.isNotEmpty(urlpattern)
                            && UtilValidate.isNotEmpty(replacement)) {
                        try {
                            Pattern pattern = perlCompiler.compile(
                                    urlpattern, Perl5Compiler.DEFAULT_MASK);
                            forwardReplacements.put(urlpattern,
                                    replacement);
                            forwardPatterns.put(urlpattern, pattern);
                            if (UtilValidate.isNotEmpty(responseCode)) {
                                Integer responseCodeInt = DEFAULT_RESPONSECODE;
                                try {
                                    responseCodeInt = Integer.valueOf(responseCode);
                                } catch (NumberFormatException nfe) {
                                    Debug.logWarning(nfe, module);
                                }
                                forwardResponseCodes.put(urlpattern, responseCodeInt);
                            }
                        } catch (MalformedPatternException e) {
                            Debug.logWarning(e, "skip this url replacement if any error happened", module);
                        }
                    }
                }

            } catch (NullPointerException e) {
                Debug.logWarning(e, "no \"config\" element", module);
            }
        } catch (SAXException e) {
            result = "error";
            Debug.logError(e, module);
        } catch (ParserConfigurationException e) {
            result = "error";
            Debug.logError(e, module);
        } catch (IOException e) {
            result = "error";
            Debug.logError(e, module);
        } finally {
            if (configFileIS != null) {
                try {
                    configFileIS.close();
                } catch (IOException e) {
                    result = "error";
                    Debug.logError(e, module);
                }
            }
        }
        if (seoReplacements.keySet().isEmpty()) {
            useUrlRegexp = false;
        } else {
            useUrlRegexp = true;
        }
        if (result.equals("success")) {
            isInitialed = true;
        }
    }
    
    /**
     * Check whether the configuration file has been read.
     * 
     * @return a boolean value to indicate whether the configuration file has been read.
     */
    public static boolean isInitialed() {
        return isInitialed;
    }

    /**
     * Check whether debug is enabled.
     * 
     * @return a boolean value to indicate whether debug is enabled.
     */
    public static boolean isDebugEnabled() {
        return debug;
    }

    /**
     * Check whether url regexp should be used.
     * 
     * @return a boolean value to indicate whether url regexp should be used.
     */
    public static boolean checkUseUrlRegexp() {
        return useUrlRegexp;
    }

    /**
     * Get the general regexp pattern.
     * 
     * @return the general regexp pattern.
     */
    public static Pattern getGeneralRegexpPattern() {
        return regexpIfMatch;
    }
    
    /**
     * Check whether category url is enabled.
     * 
     * @return a boolean value to indicate whether category url is enabled.
     */
    public static boolean checkCategoryUrl() {
        return categoryUrlEnabled;
    }

    /**
     * Check whether the context path is enabled.
     * 
     * @return a boolean value to indicate whether the context path is enabled.
     */
    public static boolean isCategoryUrlEnabled(String contextPath) {
        if (contextPath == null) {
            return false;
        }
        if (UtilValidate.isEmpty(contextPath)) {
            contextPath = "/";
        }
        if (categoryUrlEnabled) {
            if (allowedContextPaths.contains(contextPath.trim())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Check whether category name is enabled.
     * 
     * @return a boolean value to indicate whether category name is enabled.
     */
    public static boolean isCategoryNameEnabled() {
        return categoryNameEnabled;
    }

    /**
     * Get category url suffix.
     * 
     * @return String category url suffix.
     */
    public static String getCategoryUrlSuffix() {
        return categoryUrlSuffix;
    }

    /**
     * Check whether jsessionid is enabled for anonymous.
     * 
     * @return a boolean value to indicate whether jsessionid is enabled for anonymous.
     */
    public static boolean isJSessionIdAnonEnabled() {
        return jSessionIdAnonEnabled;
    }

    /**
     * Check whether jsessionid is enabled for user.
     * 
     * @return a boolean value to indicate whether jsessionid is enabled for user.
     */
    public static boolean isJSessionIdUserEnabled() {
        return jSessionIdUserEnabled;
    }

    /**
     * Get user exception url pattern configures.
     * 
     * @return user exception url pattern configures (java.util.List<Pattern>)
     */
    public static List<Pattern> getUserExceptionPatterns() {
        return userExceptionPatterns;
    }

    /**
     * Get name filters.
     * 
     * @return name filters (java.util.Map<String, String>)
     */
    public static Map<String, String> getNameFilters() {
        return nameFilters;
    }

    /**
     * Get seo url pattern configures.
     * 
     * @return seo url pattern configures (java.util.Map<String, Pattern>)
     */
    public static Map<String, Pattern> getSeoPatterns() {
        return seoPatterns;
    }

    /**
     * Get seo replacement configures.
     * 
     * @return seo replacement configures (java.util.Map<String, String>)
     */
    public static Map<String, String> getSeoReplacements() {
        return seoReplacements;
    }

    /**
     * Get forward url pattern configures.
     * 
     * @return forward url pattern configures (java.util.Map<String, Pattern>)
     */
    public static Map<String, Pattern> getForwardPatterns() {
        return forwardPatterns;
    }

    /**
     * Get forward replacement configures.
     * 
     * @return forward replacement configures (java.util.Map<String, String>)
     */
    public static Map<String, String> getForwardReplacements() {
        return forwardReplacements;
    }

    /**
     * Get forward response codes.
     * 
     * @return forward response code configures (java.util.Map<String, Integer>)
     */
    public static Map<String, Integer> getForwardResponseCodes() {
        return forwardResponseCodes;
    }

    /**
     * Check whether a product id is in the special list. If we cannot get a product from a lower cased 
     * or upper cased product id, then it's special.
     * 
     * @return boolean to indicate whether the product id is special.
     */
    public static boolean isSpecialProductId(String productId) {
        return specialProductIds.containsKey(productId);
    }

    /**
     * Add a special product id to the special list.
     * 
     * @param productId a product id get from database.
     * @return true to indicate it has been added to special product id; false to indicate it's not special.
     * @throws Exception to indicate there's already same lower cased product id in the list but value is a different product id.
     */
    public static boolean addSpecialProductId(String productId) throws Exception {
        if (productId.toLowerCase().equals(productId) || productId.toUpperCase().equals(productId)) {
            return false;
        }
        if (isSpecialProductId(productId.toLowerCase())) {
            if (specialProductIds.containsValue(productId)) {
                return true;
            } else {
                throw new Exception("This product Id cannot be lower cased for SEO URL purpose: " + productId);
            }
        }
        specialProductIds.put(productId.toLowerCase(), productId);
        return true;
    }
    
    /**
     * Get a product id is in the special list.
     * 
     * @return String of the original product id
     */
    public static String getSpecialProductId(String productId) {
        return specialProductIds.get(productId);
    }

}
