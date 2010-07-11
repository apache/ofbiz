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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.jcr.Repository;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.jackrabbit.core.TransientRepository;
import org.ofbiz.base.location.FlexibleLocation;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;

public class RepositoryFactory {

    public static final String module = RepositoryFactory.class.getName();
    private static final Repository repository = createRepoInstance();

    private static Repository createEmbedded() throws MalformedURLException, URISyntaxException {
        String homeDirURL = UtilProperties.getPropertyValue("repository.properties", "jackrabbit.repHomeDir");
        String configFilePath = UtilProperties.getPropertyValue("repository.properties", "jackrabbit.configFilePath");
        File homeDir = new File(homeDirURL);
        URL configUrl = FlexibleLocation.resolveLocation(configFilePath);
        return new TransientRepository(new File(configUrl.toURI()), homeDir);
    }

    private static Repository createRepoInstance() {
        Repository result = null;
        try {
            result = createUsingJndi();
        } catch (Exception e) {
            Debug.logError(e, module);
        }
        if (result == null) {
            try {
                result = createEmbedded();
            } catch (Exception e) {
                Debug.logError(e, module);
            }
        }
        return result;
    }
    
    private static Repository createUsingJndi() throws NamingException {
        String repoUrl = UtilProperties.getPropertyValue("repository.properties", "jndi.repository.url");
        if (UtilValidate.isEmpty(repoUrl)) {
            return null;
        }
        return (Repository) new InitialContext().lookup(repoUrl);
    }

    public static Repository getRepository() {
        return repository;
    }
}
