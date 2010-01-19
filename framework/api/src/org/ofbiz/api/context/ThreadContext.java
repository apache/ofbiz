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
package org.ofbiz.api.context;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.ofbiz.api.authorization.AccessController;
import org.ofbiz.api.authorization.AuthorizationManager;

/** A convenience class for accessing the current thread's <code>ExecutionContext</code>.
 * @see {@link org.ofbiz.service.ExecutionContext}
 */
public class ThreadContext {

    public static final String module = ThreadContext.class.getName();

    protected static final ThreadLocal<ExecutionContext> executionContext = new ThreadLocal<ExecutionContext>() {
        protected synchronized ExecutionContext initialValue() {
            return ExecutionContextFactory.getInstance();
        }
    };

    public static <E> List<E> applyFilters(List<E> list) {
        return executionContext.get().getAccessController().applyFilters(list);
    }

    public static void endRunUnprotected() {
        executionContext.get().endRunUnprotected();
    }

    public static AccessController getAccessController() {
        return executionContext.get().getAccessController();
    }

    public static String getCurrencyUom() {
        return executionContext.get().getCurrencyUom();
    }

    public static ArtifactPath getExecutionPath() {
        return executionContext.get().getExecutionPath();
    }

    public static String[] getExecutionPathAsArray() {
        return executionContext.get().getExecutionPathAsArray();
    }

    public static String getExecutionPathAsString() {
        return executionContext.get().getExecutionPathAsString();
    }

    public static Locale getLocale() {
        return executionContext.get().getLocale();
    }

    public static Map<String, ? extends Object> getParameters() {
        return executionContext.get().getParameters();
    }

    public static Object getProperty(String key) {
        return executionContext.get().getProperty(key);
    }

    public static AuthorizationManager getSecurity() {
        return executionContext.get().getSecurity();
    }

    public static TimeZone getTimeZone() {
        return executionContext.get().getTimeZone();
    }

    public static void initializeContext(Map<String, ? extends Object> params) {
        executionContext.get().initializeContext(params);
    }

    public static void popExecutionArtifact() {
        executionContext.get().popExecutionArtifact();
    }

    public static void pushExecutionArtifact(ExecutionArtifact artifact) {
        executionContext.get().pushExecutionArtifact(artifact);
    }

    public static void pushExecutionArtifact(ExecutionArtifact artifact, Map<String, ? extends Object> parameters) {
        pushExecutionArtifact(new GenericParametersArtifact(artifact, parameters));
    }

    public static void pushExecutionArtifact(String location, String name) {
        pushExecutionArtifact(new GenericExecutionArtifact(location, name));
    }

    public static void pushExecutionArtifact(String location, String name, Map<String, ? extends Object> parameters) {
        pushExecutionArtifact(new GenericParametersArtifact(location, name, parameters));
    }

    public static void reset() {
        executionContext.get().reset();
    }

    public static void runUnprotected() {
        executionContext.get().runUnprotected();
    }

    public static void setCurrencyUom(String currencyUom) {
        executionContext.get().setCurrencyUom(currencyUom);
    }

    public static void setLocale(Locale locale) {
        executionContext.get().setLocale(locale);
    }

    public static Object setProperty(String key, Object value) {
        return executionContext.get().setProperty(key, value);
    }

    public static void setSecurity(AuthorizationManager security) {
        executionContext.get().setSecurity(security);
    }

    public static void setTimeZone(TimeZone timeZone) {
        executionContext.get().setTimeZone(timeZone);
    }

}
