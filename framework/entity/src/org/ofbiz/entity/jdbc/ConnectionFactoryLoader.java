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
package org.ofbiz.entity.jdbc;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.config.model.EntityConfig;
import org.ofbiz.entity.connection.ConnectionFactoryInterface;

/**
 * ConnectionFactoryLoader - utility class that loads the connection manager and provides to client code a reference to it (ConnectionFactoryInterface)
 *
 */
public class ConnectionFactoryLoader {
    // Debug module name
    public static final String module = ConnectionFactoryLoader.class.getName();
    private static final ConnectionFactoryInterface connFactory = createConnectionFactoryInterface();

    private static ConnectionFactoryInterface createConnectionFactoryInterface() {
        ConnectionFactoryInterface instance = null;
        try {
            String className = EntityConfig.getInstance().getConnectionFactory().getClassName();
            if (className == null) {
                throw new IllegalStateException("Could not find connection factory class name definition");
            }
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> tfClass = loader.loadClass(className);
            instance = (ConnectionFactoryInterface) tfClass.newInstance();
        } catch (ClassNotFoundException cnfe) {
            Debug.logError(cnfe, "Could not find connection factory class", module);
        } catch (Exception e) {
            Debug.logError(e, "Unable to instantiate the connection factory", module);
        }
        return instance;
    }

    public static ConnectionFactoryInterface getInstance() {
        if (connFactory == null) {
            throw new IllegalStateException("The Connection Factory is not initialized.");
        }
        return connFactory;
    }
}
