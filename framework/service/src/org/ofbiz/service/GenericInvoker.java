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
package org.ofbiz.service;

import java.util.Map;

import org.ofbiz.service.engine.GenericEngine;

import org.webslinger.invoker.Invoker;

/**
 * Generic Invoker Interface
 */
public interface GenericInvoker extends Invoker {

    /**
     * Run the service synchronously and return the result.
     *
     * @param localName Name of the LocalDispatcher.
     * @param engine GenericEngine object.
     * @param context Map of name, value pairs composing the context.
     * @return Map of name, value pairs composing the result.
     * @throws GenericServiceException
     */
    public Map<String, Object> runSync(String localName, GenericEngine engine, Map<String, Object> context) throws GenericServiceException;

    /**
     * Run the service synchronously and IGNORE the result.
     *
     * @param localName Name of the LocalDispatcher.
     * @param engine GenericEngine object.
     * @param context Map of name, value pairs composing the context.
     * @throws GenericServiceException
     */
    public void runSyncIgnore(String localName, GenericEngine engine, Map<String, Object> context) throws GenericServiceException;

    /**
     * Run the service asynchronously, passing an instance of GenericRequester that will receive the result.
     *
     * @param localName Name of the LocalDispatcher.
     * @param engine GenericEngine object.
     * @param context Map of name, value pairs composing the context.
     * @param requester Object implementing GenericRequester interface which will receive the result.
     * @param persist True for store/run; False for run.
     * @throws GenericServiceException
     */
    public void runAsync(String localName, GenericEngine engine, Map<String, Object> context, GenericRequester requester, boolean persist)
        throws GenericServiceException;

    /**
     * Send the service callbacks
     * @param engine GenericEngine object
     * @param context Map of name, value pairs composing the context
     * @param result Object to return to callback (Throwable or Map)
     * @param mode Service mode (sync or async)
     * @throws GenericServiceException
     */
    public void sendCallbacks(GenericEngine engine, Map<String, Object> context, Map<String, Object> result, Throwable t, int mode) throws GenericServiceException;
    public GenericInvoker copy(ModelService modelService);
}

