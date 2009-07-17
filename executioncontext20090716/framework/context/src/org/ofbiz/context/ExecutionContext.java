/*
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
 */
package org.ofbiz.context;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.ofbiz.context.entity.GenericDelegator;
import org.ofbiz.context.service.JobScheduler;
import org.ofbiz.context.service.LocalDispatcher;

/**
 * Interface definition for object used throughout the OFBiz Framework to manage contextual execution information and tool access interfaces.
 */

public interface ExecutionContext {
    String getContextId();
    
    GenericDelegator getDelegator();
    GenericDelegator getDelegatorByName(String delegatorName);
    LocalDispatcher getDispatcher();
    JobScheduler getJobScheduler();
    
    Locale getLocale();
    TimeZone getTimeZone();
    /** @return String The ISO currency code */
    String getCurrencyUom();
    
    String getCurrentUserIdentifier();
    String getInitialUserIdentifier();
    void pushUserIdentifier(String userIdentifier);
    String popUserIdentifier();

    String getCurrentSessionIdentifier();
    void pushSessionIdentifier(String sessionIdentifier);
    String popSessionIdentifier();
    
    ExecutionArtifactInfo getCurrentExecutionArtifactInfo();
    List<ExecutionArtifactInfo> getExecutionArtifactInfoStack();
    void pushExecutionArtifactInfo(ExecutionArtifactInfo executionArtifactInfo);
    ExecutionArtifactInfo popExecutionArtifactInfo();
}
