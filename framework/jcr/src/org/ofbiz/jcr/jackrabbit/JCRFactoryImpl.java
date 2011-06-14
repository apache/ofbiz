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
package org.ofbiz.jcr.jackrabbit;

import java.io.File;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.jcr.JCRFactory;
import org.w3c.dom.Element;

public class JCRFactoryImpl implements JCRFactory {

    public static final String module = JCRFactoryImpl.class.getName();

    private static String homeDir = null;
    private static String jackrabbitConfigFile = null;
    private static String CREDENTIALS_USERNAME = null;
    private static char[] CREDENTIALS_PASSWORD = null;

    protected static Repository repository = null;
    protected Session session = null;

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.JCRFactory#initialize(org.w3c.dom.Element)
     */
    @Override
    public void initialize(Element configRootElement) throws RepositoryException {
        Element childElement = UtilXml.firstChildElement(configRootElement, "jcr-credentials");
        CREDENTIALS_USERNAME = UtilXml.elementAttribute(childElement, "username", null);
        CREDENTIALS_PASSWORD = UtilXml.elementAttribute(childElement, "password", null).toCharArray();

        jackrabbitConfigFile = UtilXml.childElementAttribute(configRootElement, "config-file-path", "path", "framework/jcr/config/jackrabbit.xml");
        homeDir = UtilXml.childElementAttribute(configRootElement, "home-dir", "path", "runtime/data/jcr/");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.JCRFactory#start()
     */
    @Override
    public void start() throws RepositoryException {
        // Transient repositories closes automatically when the last session is closed
        repository = new TransientRepository(jackrabbitConfigFile, homeDir);
        createSession();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.JCRFactory#stop(boolean)
     */
    @Override
    public void stop(boolean removeRepositoryOnShutdown) throws RepositoryException {
        if (session != null && session.isLive()) {
            session.logout();
        }

        if (removeRepositoryOnShutdown) {
            if (UtilValidate.isNotEmpty(homeDir)) {
                File homeDirFile = new File(homeDir);
                homeDirFile.deleteOnExit();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.JCRFactory#createSession()
     */
    @Override
    public Session createSession() throws RepositoryException {
        if (session == null || !session.isLive()) {
            Credentials credentials = new SimpleCredentials(CREDENTIALS_USERNAME, CREDENTIALS_PASSWORD);
            try {
                session = repository.login(credentials);
            } catch (RepositoryException e) {
                Debug.logError(e, "Could not login to the workspace");
                throw e;
            }

        }
        return session;
    }
}
