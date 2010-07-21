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
package org.ofbiz.jackrabbit;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Repository;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RepositoryFactory {

    public static final String module = RepositoryFactory.class.getName();
    private static final Map<String, Repository> repositoryMap = createRepositoryMap();

    private static Repository createFromFactory(String repositoryName, ClassLoader loader, String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        JcrRepositoryFactory factory = (JcrRepositoryFactory) loader.loadClass(className).newInstance();
        return factory.getInstance(repositoryName);
    }
    
    private static Map<String, Repository> createRepositoryMap() {
        Map<String, Repository> result = new HashMap<String, Repository>();
        loadRepositories(result);
        Debug.logInfo("Repositories loaded: " + result.size(), module);
        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns the default repository.
     * 
     * @return
     */
    public static Repository getRepository() {
        return repositoryMap.get("default");
    }

    /**
     * Returns the specified repository, or <code>null</code> if the
     * specified repository doesn't exist.
     * 
     * @param name
     * @return
     */
    public static Repository getRepository(String name) {
        return repositoryMap.get(name);
    }

    private static void loadRepositories(Map<String, Repository> map) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("jcr-repositories.xml");
        } catch (IOException e) {
            Debug.logError(e, "Could not load list of jcr-repositories.xml", module);
            return;
        }
        while (resources.hasMoreElements()) {
            URL repositoriesURL = resources.nextElement();
            Debug.logInfo("Loading repositories from: " + repositoriesURL, module);
            Document doc = null;
            try {
                doc = UtilXml.readXmlDocument(repositoriesURL, false);
            } catch (Exception e) {
                Debug.logError(e, module);
                continue;
            }
            Element resourceElement = doc.getDocumentElement();
            List<? extends Element> repositoryList = UtilXml.childElementList(resourceElement, "repository");
            for (Element element : repositoryList) {
                String name = element.getAttribute("name");
                if (UtilValidate.isEmpty(name)) {
                    continue;
                }
                String jndiName = element.getAttribute("jndi-name");
                if (UtilValidate.isNotEmpty(jndiName)) {
                    try {
                        map.put(name, (Repository) new InitialContext().lookup(jndiName));
                    } catch (NamingException e) {
                        Debug.logError(e, module);
                    }
                    continue;
                }
                String className = element.getAttribute("class-name");
                if (UtilValidate.isNotEmpty(className)) {
                    try {
                        map.put(name, createFromFactory(name, loader, className));
                    } catch (Exception e) {
                        Debug.logError(e, module);
                    }
                }
            }
        }
        
    }

    private RepositoryFactory() {}
}
