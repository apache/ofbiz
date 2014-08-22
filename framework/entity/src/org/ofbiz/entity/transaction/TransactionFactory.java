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
package org.ofbiz.entity.transaction;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.GenericEntityConfException;
import org.ofbiz.entity.config.EntityConfigUtil;

/**
 * TransactionFactory - central source for JTA objects
 */
public class TransactionFactory {

    public static final String module = TransactionFactory.class.getName();
    private static final TransactionFactoryInterface txFactory = createTransactionFactoryInterface();

    private static TransactionFactoryInterface createTransactionFactoryInterface() {
        TransactionFactoryInterface instance = null;
        try {
            String className = EntityConfigUtil.getTxFactoryClass();
            if (className == null) {
                throw new IllegalStateException("Could not find transaction factory class name definition");
            }
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> tfClass = loader.loadClass(className);
            instance = (TransactionFactoryInterface) tfClass.newInstance();
        } catch (GenericEntityConfException gece) {
            Debug.logError(gece, "Could not find transaction factory class name definition", module);
        } catch (ClassNotFoundException cnfe) {
            Debug.logError(cnfe, "Could not find transaction factory class", module);
        } catch (Exception e) {
            Debug.logError(e, "Unable to instantiate the transaction factory", module);
        }
        return instance;
    }

    public static TransactionFactoryInterface getInstance() {
        if (txFactory == null) {
            throw new IllegalStateException("The Transaction Factory is not initialized.");
        }
        return txFactory;
    }
}
