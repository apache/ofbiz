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
package org.ofbiz.context;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.service.ThreadContext;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ModelService;

/**
 * SecurityAwareIterator class. This class decorates an <code>
 * Iterator</code> instance and filters a list of
 * <code>Object</code>s based on a set of permission services.
 * <p>The permission service must implement <code>permissionInterface</code>
 * and accept an optional <code>candidateObject</code> parameter (parameter
 * type is <code>java.lang.Object</code>). The service should
 * return <code>hasPermission = true</code> if the user is granted access
 * to the <code>candidateObject</code>.</p>
 */
public class SecurityAwareIterator<E> implements Iterator<E> {

    public static final String module = SecurityAwareIterator.class.getName();
    protected final Iterator<E> iterator;
    protected final Set<String> serviceNameList;
    protected E nextValue = null;

    public SecurityAwareIterator(Iterator<E> iterator, Set<String> serviceNameList) {
        this.iterator = iterator;
        this.serviceNameList = serviceNameList;
        getNext();
    }

    protected void getNext() {
        // Unusual loop for EntityListIterator compatibility
        E value = null;
        try {
            value = this.iterator.next();
        } catch (Exception e) {}
        while (value != null) {
            if (this.hasPermission(value)) {
                this.nextValue = value;
                return;
            }
            value = null;
            try {
                value = this.iterator.next();
            } catch (Exception e) {}
        }
    }

    public boolean hasNext() {
        return this.nextValue != null;
    }

    public E next() {
        E value = this.nextValue;
        this.nextValue = null;
        this.getNext();
        return value;
    }

    public void remove() {
        this.iterator.remove();
    }

    protected boolean hasPermission(E value) {
        if (ThreadContext.getUserLogin() == null) {
            // This is here for development purposes
            return true;
        }
        try {
            LocalDispatcher dispatcher = ThreadContext.getDispatcher();
            DispatchContext ctx = dispatcher.getDispatchContext();
            Map<String, ? extends Object> params = ThreadContext.getParameters();
            for (String serviceName : this.serviceNameList) {
                ModelService modelService = ctx.getModelService(serviceName);
                Map<String, Object> context = FastMap.newInstance();
                if (params != null) {
                    context.putAll(params);
                }
                if (!context.containsKey("userLogin")) {
                    context.put("userLogin", ThreadContext.getUserLogin());
                }
                if (!context.containsKey("locale")) {
                    context.put("locale", ThreadContext.getLocale());
                }
                if (!context.containsKey("timeZone")) {
                    context.put("timeZone", ThreadContext.getTimeZone());
                }
                context.put("candidateObject", value);
                context = modelService.makeValid(context, ModelService.IN_PARAM);
                Map<String, Object> result = dispatcher.runSync(serviceName, context);
                Boolean hasPermission = (Boolean) result.get("hasPermission");
                if (hasPermission != null && !hasPermission.booleanValue()) {
                    return false;
                }
            }
        } catch (Exception e) {
            Debug.logError(e, module);
        }
        return true;
    }
}
