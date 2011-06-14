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

import javax.jcr.RepositoryException;

import org.ofbiz.base.config.GenericConfigException;
import org.ofbiz.base.config.ResourceLoader;
import org.ofbiz.base.container.Container;
import org.ofbiz.base.container.ContainerConfig;
import org.ofbiz.base.container.ContainerException;
import org.ofbiz.base.util.UtilXml;
import org.w3c.dom.Element;

/**
 * A container for a local JCR-compliant content repository. The default
 * implementation uses Apache Jackrabbit.
 */
public class JCRContainer implements Container {

    public static final String module = JCRContainer.class.getName();

    public static final String DEFAULT_JCR_CONFIG_PATH = "framework/jcr/config/jcr-config.xml";

    private static String jndiName = null;
    private static String factoryClassName = null;
    private static String jcrContextName = null;

    private String configFilePath = null;
    private boolean removeRepositoryOnShutdown = false;

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.base.container.Container#init(java.lang.String[],
     * java.lang.String)
     */
    @Override
    public void init(String[] args, String configFile) throws ContainerException {
        // get the container config
        ContainerConfig.Container cc = ContainerConfig.getContainer("jcr-container", configFile);
        if (cc == null) {
            throw new ContainerException("No jcr-container configuration found in container config!");
        }

        // embedded properties
        jndiName = ContainerConfig.getPropertyValue(cc, "jndiName", "jcr/local");
        removeRepositoryOnShutdown = ContainerConfig.getPropertyValue(cc, "removeRepositoryOnShutdown", false);
        configFilePath = ContainerConfig.getPropertyValue(cc, "configFilePath", DEFAULT_JCR_CONFIG_PATH);

        Element configRootElement = null;
        try {
            configRootElement = ResourceLoader.getXmlRootElement(configFilePath);
        } catch (GenericConfigException e) {
            throw new ContainerException("Could not load the jcr configuration in file " + configFilePath, e);
        }

        if (configRootElement == null) {
            throw new ContainerException("No jcr configuration found in file " + configFilePath);
        }

        Element childElement = UtilXml.firstChildElement(configRootElement, "jcr-context");
        jcrContextName = UtilXml.elementAttribute(childElement, "name", "default");

        // find the default JCR implementation
        for (Element curElement : UtilXml.childElementList(configRootElement, "jcr")) {
            if (jcrContextName.equals(curElement.getAttribute("name"))) {
                factoryClassName = curElement.getAttribute("class");
                break;
            }
        }

        // get the default JCR factory
        JCRFactory jcrFactory = JCRFactoryUtil.getJCRFactory();

        if (jcrFactory == null) {
            throw new ContainerException("Cannot load JCRFactory implementation class");
        }

        try {
            jcrFactory.initialize(configRootElement);
        } catch (RepositoryException e) {
            throw new ContainerException("Cannot initialize JCRFactory context", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.base.container.Container#start()
     */
    @Override
    public boolean start() throws ContainerException {
        JCRFactory jcrFactory = JCRFactoryUtil.getJCRFactory();
        if (jcrFactory == null) {
            throw new ContainerException("Cannot load JCRFactory implementation class");
        }

        try {
            jcrFactory.start();
        } catch (RepositoryException e) {
            throw new ContainerException("Cannot start JCRFactory context", e);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.base.container.Container#stop()
     */
    @Override
    public void stop() throws ContainerException {
        JCRFactory jcrFactory = JCRFactoryUtil.getJCRFactory();
        if (jcrFactory == null) {
            throw new ContainerException("Cannot load JCRFactory implementation class");
        }

        try {
            jcrFactory.stop(removeRepositoryOnShutdown);
        } catch (RepositoryException e) {
            throw new ContainerException("Cannot stop JCRFactory context", e);
        }
    }

    /**
     * returns the class name of the JCRFactory implementation
     *
     * @return
     */
    public static String getFactoryClassName() {
        return factoryClassName;
    }
}
