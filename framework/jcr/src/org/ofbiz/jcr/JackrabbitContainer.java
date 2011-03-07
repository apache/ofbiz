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
package org.ofbiz.jcr;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;

import org.apache.jackrabbit.core.TransientRepository;
import org.ofbiz.base.container.Container;
import org.ofbiz.base.container.ContainerConfig;
import org.ofbiz.base.container.ContainerException;
import org.ofbiz.base.location.FlexibleLocation;
import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

/**
 * A container for a local JCR-compliant content repository. The current
 * implementation uses Apache Jackrabbit.
 */
public class JackrabbitContainer implements Container {

    public static final String module = JackrabbitContainer.class.getName();

    private static File homeDir = null;
    private static File jackrabbitConfigFile = null;
    private static String jndiName;
    protected static Repository repository;
    private static Session session;

    @Override
    public void init(String[] args, String configFile) throws ContainerException {
        ContainerConfig.Container cc = ContainerConfig.getContainer("jackrabbit", configFile);
        String homeDirURL;
        try {
            homeDirURL = ContainerConfig.getPropertyValue(cc, "repHomeDir", "runtime/data/jackrabbit/");
            jndiName = ContainerConfig.getPropertyValue(cc, "jndiName", "jcr/local");
            homeDir = new File(homeDirURL);
            URL jackrabbitConfigUrl = FlexibleLocation.resolveLocation(ContainerConfig.getPropertyValue(cc, "configFilePath", "framework/jackrabbit/config/jackrabbit.xml"));
            jackrabbitConfigFile = new File(jackrabbitConfigUrl.toURI());

        } catch (MalformedURLException e) {
            Debug.logError(e, module);
        } catch (URISyntaxException e) {
            Debug.logError(e, module);
        }
    }

    @Override
    public boolean start() throws ContainerException {
        repository = new TransientRepository(jackrabbitConfigFile, homeDir);
        try {
            Delegator delegator = DelegatorFactory.getDelegator("default");
            GenericValue userLogin = delegator.findOne("UserLogin", true, "userLoginId", "system");
            // Open a session to get the repo running
            session = JackrabbitContainer.getUserSession(userLogin);
        } catch (LoginException e) {
            Debug.logError(e, module);
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        }
        try {
            Reference ref = new Reference(Repository.class.getName(), LocalRepositoryFactory.class.getName(), null);
            Context context = new InitialContext();
            context.bind(jndiName, ref);
        } catch (NamingException e) {
            Debug.logError(e, module);
        }
        // Test JNDI bind
        RepositoryFactory.getRepository();
        return true;
    }

    @Override
    public void stop() throws ContainerException {
        try {
            Context context = new InitialContext();
            context.unbind(jndiName);
        } catch (NamingException e) {
            Debug.logError(e, module);
        }
        if (session != null) {
            session.logout();
        }
        if (repository != null) {
            // Not needed - Jackrabbit shuts down when the session is closed
//            repository.shutdown();
        }
    }

    public static Session getUserSession(GenericValue userLogin) throws RepositoryException {
        String currentPassword = userLogin.getString("currentPassword") == null ? "" : userLogin.getString("currentPassword");
        Credentials creds = new SimpleCredentials(userLogin.getString("userLoginId"), currentPassword.toCharArray());
        return repository.login(creds);
    }
}
