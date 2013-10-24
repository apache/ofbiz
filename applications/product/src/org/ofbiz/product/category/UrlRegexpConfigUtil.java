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
import javolution.util.FastSet;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilURL;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * UrlRegexpConfigUtil - Configuration file utility.
 * 
 */
public class UrlRegexpConfigUtil {

    private static final String module = UrlRegexpConfigUtil.class.getName();
    private static Perl5Compiler m_perlCompiler = new Perl5Compiler();
    private static boolean m_isInitialed = false;
    private static boolean m_debug = false;
    private static boolean m_categoryUrlEnabled = true;
    private static boolean m_categoryNameEnabled = false;
    private static String m_categoryUrlSuffix = null;
    private static Pattern m_regexpIfMatch = null;
    private static boolean m_useUrlRegexp = false;
    private static boolean m_jSessionIdAnonEnabled = false;
    private static boolean m_jSessionIdUserEnabled = false;
    private static Map<String, String> m_seoReplacements = null;
    private static Map<String, Pattern> m_seoPatterns = null;
    private static Map<String, String> m_forwardReplacements = null;
    private static Map<String, Pattern> m_forwardPatterns = null;
    private static Map<String, Integer> m_forwardResponseCodes = null;
    private static Map<String, String> m_nameFilters = null;
    private static List<Pattern> m_userExceptionPatterns = null;
    private static Set<String> m_allowedContextPaths = null;
    private static Map<String, String> m_specialProductIds = null;
    public static final String DEFAULT_REGEXP = "^.*/.*$";
    public static final String ELEMENT_REGEXPIFMATCH = "regexpifmatch";
    public static final String ELEMENT_DEBUG = "debug";
    public static final String ELEMENT_CATEGORY_URL = "category-url";
    public static final String ELEMENT_ALLOWED_CONTEXT_PATHS = "allowed-context-paths";
    public static final String ELEMENT_CATEGORY_NAME = "category-name";
    public static final String ELEMENT_CATEGORY_URL_SUFFIX = "category-url-suffix";
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
    public static final String URL_REGEXP_CONFIG_FILENAME = "SeoConfig.xml";
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
        m_seoPatterns = new HashMap<String, Pattern>();
        m_seoReplacements = new HashMap<String, String>();
        m_forwardReplacements = new HashMap<String, String>();
        m_forwardPatterns = new HashMap<String, Pattern>();
        m_forwardResponseCodes = new HashMap<String, Integer>();
        m_userExceptionPatterns = FastList.newInstance();
        m_specialProductIds = FastMap.newInstance();
        m_nameFilters = FastMap.newInstance();
        try {
            Document configDoc = UtilXml.readXmlDocument(UtilURL.fromResource(URL_REGEXP_CONFIG_FILENAME), false);
            Element rootElement = configDoc.getDocumentElement();

            String regexIfMatch = UtilXml.childElementValue(rootElement, ELEMENT_REGEXPIFMATCH, DEFAULT_REGEXP);
            try {
                m_regexpIfMatch = m_perlCompiler.compile(regexIfMatch, Perl5Compiler.DEFAULT_MASK);
            } catch (MalformedPatternException e1) {
                // do nothing
            }
            m_debug = Boolean.parseBoolean(UtilXml.childElementValue(rootElement, ELEMENT_DEBUG, "false"));

            // parse category-url element
            try {
                Element categoryUrlElement = UtilXml.firstChildElement(rootElement, ELEMENT_CATEGORY_URL);
                if (categoryUrlElement != null) {
                    String enableCategoryUrlValue = UtilXml.childElementValue(categoryUrlElement, ELEMENT_VALUE, DEFAULT_CATEGORY_URL_VALUE);
                    if (DEFAULT_CATEGORY_URL_VALUE.equalsIgnoreCase(enableCategoryUrlValue)) {
                        m_categoryUrlEnabled = true;
                    } else {
                        m_categoryUrlEnabled = false;
                    }

                    if (m_categoryUrlEnabled) {
                        String allowedContextValue = UtilXml.childElementValue(categoryUrlElement, ELEMENT_ALLOWED_CONTEXT_PATHS, null);
                        m_allowedContextPaths = FastSet.newInstance();
                        if (UtilValidate.isNotEmpty(allowedContextValue)) {
                            List<String> allowedContextPaths = StringUtil.split(allowedContextValue, ALLOWED_CONTEXT_PATHS_SEPERATOR);
                            for (String path : allowedContextPaths) {
                                if (UtilValidate.isNotEmpty(path)) {
                                    path = path.trim();
                                    if (!m_allowedContextPaths.contains(path)) {
                                        m_allowedContextPaths.add(path);
                                    }
                                }
                            }
                        }

                        String categoryNameValue = UtilXml.childElementValue(categoryUrlElement, ELEMENT_CATEGORY_NAME, DEFAULT_CATEGORY_NAME_VALUE);
                        if (DEFAULT_CATEGORY_NAME_VALUE.equalsIgnoreCase(categoryNameValue)) {
                            m_categoryNameEnabled = false;
                        } else {
                            m_categoryNameEnabled = true;
                        }

                        m_categoryUrlSuffix = UtilXml.childElementValue(categoryUrlElement, ELEMENT_CATEGORY_URL_SUFFIX, null);
                        if (UtilValidate.isNotEmpty(m_categoryUrlSuffix)) {
                            m_categoryUrlSuffix = m_categoryUrlSuffix.trim();
                            if (m_categoryUrlSuffix.contains("/")) {
                                m_categoryUrlSuffix = null;
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                // no "category-url" element
            }

            // parse jsessionid element
            try {
                Element jSessionId = UtilXml.firstChildElement(rootElement, ELEMENT_JSESSIONID);
                if (jSessionId != null) {
                    Element anonymous = UtilXml.firstChildElement(jSessionId, ELEMENT_ANONYMOUS);
                    if (anonymous != null) {
                        String anonymousValue = UtilXml.childElementValue(anonymous, ELEMENT_VALUE, DEFAULT_ANONYMOUS_VALUE);
                        if (DEFAULT_ANONYMOUS_VALUE.equalsIgnoreCase(anonymousValue)) {
                            m_jSessionIdAnonEnabled = false;
                        } else {
                            m_jSessionIdAnonEnabled = true;
                        }
                    }

                    Element user = UtilXml.firstChildElement(jSessionId, ELEMENT_USER);
                    if (user != null) {
                        String userValue = UtilXml.childElementValue(user, ELEMENT_VALUE, DEFAULT_USER_VALUE);
                        if (DEFAULT_USER_VALUE.equalsIgnoreCase(userValue)) {
                            m_jSessionIdUserEnabled = false;
                        } else {
                            m_jSessionIdUserEnabled = true;
                        }
                        Element exceptions = UtilXml.firstChildElement(user, ELEMENT_EXCEPTIONS);
                        if (exceptions != null) {
                            List<? extends Element> exceptionUrlPatterns = UtilXml.childElementList(exceptions, ELEMENT_URLPATTERN);
                            for (int i = 0; i < exceptionUrlPatterns.size(); i++) {
                                Element element = (Element) exceptionUrlPatterns.get(i);
                                String urlpattern = element.getTextContent();
                                if (UtilValidate.isNotEmpty(urlpattern)) {
                                    try {
                                        Pattern pattern = m_perlCompiler.compile(urlpattern, Perl5Compiler.DEFAULT_MASK);
                                        m_userExceptionPatterns.add(pattern);
                                    } catch (MalformedPatternException e) {
                                        // skip this url replacement if any error happened
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                // no "jsessionid" element
            }

            // parse name-filters elements
            try {
                NodeList nameFilters = rootElement.getElementsByTagName(ELEMENT_FILTER);
                for (int i = 0; i < nameFilters.getLength(); i++) {
                    Element element = (Element) nameFilters.item(i);
                    String charaterPattern = UtilXml.childElementValue(element, ELEMENT_CHARACTER_PATTERN, null);
                    String replacement = UtilXml.childElementValue(element, ELEMENT_REPLACEMENT, null);
                    if (UtilValidate.isNotEmpty(charaterPattern) && UtilValidate.isNotEmpty(replacement)) {
                        try {
                            m_perlCompiler.compile(charaterPattern, Perl5Compiler.DEFAULT_MASK);
                            m_nameFilters.put(charaterPattern, replacement);
                        } catch (MalformedPatternException e) {
                            // skip this filter (character-pattern replacement) if any error happened
                        }
                    }
                }
            } catch (NullPointerException e) {
                // no "name-filters" element
            }

            // parse config elements
            try {
                // construct seo patterns
                NodeList seos = rootElement.getElementsByTagName(ELEMENT_SEO);
                for (int i = 0; i < seos.getLength(); i++) {
                    Element element = (Element) seos.item(i);
                    String urlpattern = UtilXml.childElementValue(element, ELEMENT_URLPATTERN, null);
                    String replacement = UtilXml.childElementValue(element, ELEMENT_REPLACEMENT, null);
                    if (UtilValidate.isNotEmpty(urlpattern) && UtilValidate.isNotEmpty(replacement)) {
                        try {
                            Pattern pattern = m_perlCompiler.compile(urlpattern, Perl5Compiler.DEFAULT_MASK);
                            m_seoReplacements.put(urlpattern, replacement);
                            m_seoPatterns.put(urlpattern, pattern);
                        } catch (MalformedPatternException e) {
                            // skip this url replacement if any error happened
                        }
                    }
                }

                // construct forward patterns
                NodeList forwards = rootElement.getElementsByTagName(ELEMENT_FORWARD);
                for (int i = 0; i < forwards.getLength(); i++) {
                    Element element = (Element) forwards.item(i);
                    String urlpattern = UtilXml.childElementValue(element, ELEMENT_URLPATTERN, null);
                    String replacement = UtilXml.childElementValue(element, ELEMENT_REPLACEMENT, null);
                    String responseCode = UtilXml.childElementValue(element, ELEMENT_RESPONSECODE, String.valueOf(DEFAULT_RESPONSECODE));
                    if (UtilValidate.isNotEmpty(urlpattern) && UtilValidate.isNotEmpty(replacement)) {
                        try {
                            Pattern pattern = m_perlCompiler.compile(urlpattern, Perl5Compiler.DEFAULT_MASK);
                            m_forwardReplacements.put(urlpattern, replacement);
                            m_forwardPatterns.put(urlpattern, pattern);
                            if (UtilValidate.isNotEmpty(responseCode)) {
                                Integer responseCodeInt = DEFAULT_RESPONSECODE;
                                try {
                                    responseCodeInt = Integer.valueOf(responseCode);
                                } catch (NumberFormatException nfe) {
                                    // do nothing
                                }
                                m_forwardResponseCodes.put(urlpattern, responseCodeInt);
                            }
                        } catch (MalformedPatternException e) {
                            // skip this url replacement if any error happened
                        }
                    }
                }

            } catch (NullPointerException e) {
                // no "config" element
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
        if (m_seoReplacements.keySet().isEmpty()) {
            m_useUrlRegexp = false;
        } else {
            m_useUrlRegexp = true;
        }
        if (result.equals("success")) {
            m_isInitialed = true;
        }
    }

    /**
     * Check whether the configuration file has been read.
     * 
     * @return a boolean value to indicate whether the configuration file has been read.
     */
    public static boolean isInitialed() {
        return m_isInitialed;
    }

    /**
     * Check whether debug is enabled.
     * 
     * @return a boolean value to indicate whether debug is enabled.
     */
    public static boolean isDebugEnabled() {
        return m_debug;
    }

    /**
     * Check whether url regexp should be used.
     * 
     * @return a boolean value to indicate whether url regexp should be used.
     */
    public static boolean checkUseUrlRegexp() {
        return m_useUrlRegexp;
    }

    /**
     * Get the general regexp pattern.
     * 
     * @return the general regexp pattern.
     */
    public static Pattern getGeneralRegexpPattern() {
        return m_regexpIfMatch;
    }

    /**
     * Check whether category url is enabled.
     * 
     * @return a boolean value to indicate whether category url is enabled.
     */
    public static boolean checkCategoryUrl() {
        return m_categoryUrlEnabled;
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
        if (m_categoryUrlEnabled) {
            if (m_allowedContextPaths.contains(contextPath.trim())) {
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
        return m_categoryNameEnabled;
    }

    /**
     * Get category url suffix.
     * 
     * @return String category url suffix.
     */
    public static String getCategoryUrlSuffix() {
        return m_categoryUrlSuffix;
    }

    /**
     * Check whether jsessionid is enabled for anonymous.
     * 
     * @return a boolean value to indicate whether jsessionid is enabled for anonymous.
     */
    public static boolean isJSessionIdAnonEnabled() {
        return m_jSessionIdAnonEnabled;
    }

    /**
     * Check whether jsessionid is enabled for user.
     * 
     * @return a boolean value to indicate whether jsessionid is enabled for user.
     */
    public static boolean isJSessionIdUserEnabled() {
        return m_jSessionIdUserEnabled;
    }

    /**
     * Get user exception url pattern configures.
     * 
     * @return user exception url pattern configures (java.util.List<Pattern>)
     */
    public static List<Pattern> getUserExceptionPatterns() {
        return m_userExceptionPatterns;
    }

    /**
     * Get name filters.
     * 
     * @return name filters (java.util.Map<String, String>)
     */
    public static Map<String, String> getNameFilters() {
        return m_nameFilters;
    }

    /**
     * Get seo url pattern configures.
     * 
     * @return seo url pattern configures (java.util.Map<String, Pattern>)
     */
    public static Map<String, Pattern> getSeoPatterns() {
        return m_seoPatterns;
    }

    /**
     * Get seo replacement configures.
     * 
     * @return seo replacement configures (java.util.Map<String, String>)
     */
    public static Map<String, String> getSeoReplacements() {
        return m_seoReplacements;
    }

    /**
     * Get forward url pattern configures.
     * 
     * @return forward url pattern configures (java.util.Map<String, Pattern>)
     */
    public static Map<String, Pattern> getForwardPatterns() {
        return m_forwardPatterns;
    }

    /**
     * Get forward replacement configures.
     * 
     * @return forward replacement configures (java.util.Map<String, String>)
     */
    public static Map<String, String> getForwardReplacements() {
        return m_forwardReplacements;
    }

    /**
     * Get forward response codes.
     * 
     * @return forward response code configures (java.util.Map<String, Integer>)
     */
    public static Map<String, Integer> getForwardResponseCodes() {
        return m_forwardResponseCodes;
    }

    /**
     * Check whether a product id is in the special list. If we cannot get a product from a lower cased or upper cased product id, then it's special.
     * 
     * @return boolean to indicate whether the product id is special.
     */
    public static boolean isSpecialProductId(String productId) {
        return m_specialProductIds.containsKey(productId);
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
            if (m_specialProductIds.containsValue(productId)) {
                return true;
            } else {
                throw new Exception("This product Id cannot be lower cased for SEO URL purpose: " + productId);
            }
        }
        m_specialProductIds.put(productId.toLowerCase(), productId);
        return true;
    }

    /**
     * Get a product id is in the special list.
     * 
     * @return String of the original product id
     */
    public static String getSpecialProductId(String productId) {
        return m_specialProductIds.get(productId);
    }

}
