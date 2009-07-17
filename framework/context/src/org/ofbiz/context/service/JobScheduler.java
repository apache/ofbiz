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
package org.ofbiz.context.service;

import java.util.Map;

/**
 * Job Scheduler interface
 */
public interface JobScheduler {

    /**
     * Schedule a job to start at a specific time with specific recurrence info
     *@param serviceName The name of the service to invoke
     *@param context The context for the service
     *@param startTime The time in milliseconds the service should run
     *@param frequency The frequency of the recurrence (HOURLY,DAILY,MONTHLY,etc)
     *@param interval The interval of the frequency recurrence
     *@param count The number of times to repeat
     */
    public void schedule(String serviceName, Map<String, ? extends Object> context, long startTime, int frequency, int interval, int count) throws JobManagerException;

    /**
     * Schedule a job to start at a specific time with specific recurrence info
     *@param serviceName The name of the service to invoke
     *@param context The context for the service
     *@param startTime The time in milliseconds the service should run
     *@param frequency The frequency of the recurrence (HOURLY,DAILY,MONTHLY,etc)
     *@param interval The interval of the frequency recurrence
     *@param endTime The time in milliseconds the service should expire
     */
    public void schedule(String serviceName, Map<String, ? extends Object> context, long startTime, int frequency, int interval, long endTime) throws JobManagerException;

    /**
     * Schedule a job to start at a specific time with specific recurrence info
     *@param serviceName The name of the service to invoke
     *@param context The context for the service
     *@param startTime The time in milliseconds the service should run
     *@param frequency The frequency of the recurrence (HOURLY,DAILY,MONTHLY,etc)
     *@param interval The interval of the frequency recurrence
     *@param count The number of times to repeat
     *@param endTime The time in milliseconds the service should expire
     */
    public void schedule(String serviceName, Map<String, ? extends Object> context, long startTime, int frequency, int interval, int count, long endTime) throws JobManagerException;

    /**
     * Schedule a job to start at a specific time with specific recurrence info
     *@param poolName The name of the pool to run the service from
     *@param serviceName The name of the service to invoke
     *@param context The context for the service
     *@param startTime The time in milliseconds the service should run
     *@param frequency The frequency of the recurrence (HOURLY,DAILY,MONTHLY,etc)
     *@param interval The interval of the frequency recurrence
     *@param count The number of times to repeat
     *@param endTime The time in milliseconds the service should expire
     */
    public void schedule(String poolName, String serviceName, Map<String, ? extends Object> context, long startTime, int frequency, int interval, int count, long endTime) throws JobManagerException;

    /**
     * Schedule a job to start at a specific time with specific recurrence info
     *@param jobName The name of the job
     *@param poolName The name of the pool to run the service from
     *@param serviceName The name of the service to invoke
     *@param context The context for the service
     *@param startTime The time in milliseconds the service should run
     *@param frequency The frequency of the recurrence (HOURLY,DAILY,MONTHLY,etc)
     *@param interval The interval of the frequency recurrence
     *@param count The number of times to repeat
     *@param endTime The time in milliseconds the service should expire
     *@param maxRetry The max number of retries on failure (-1 for no max)
     */
    public void schedule(String jobName, String poolName, String serviceName, Map<String, ? extends Object> context, long startTime, int frequency, int interval, int count, long endTime, int maxRetry) throws JobManagerException;

    /**
     * Schedule a job to start at a specific time with specific recurrence info
     *@param poolName The name of the pool to run the service from
     *@param serviceName The name of the service to invoke
     *@param dataId The persisted context (RuntimeData.runtimeDataId)
     *@param startTime The time in milliseconds the service should run
     */
    public void schedule(String poolName, String serviceName, String dataId, long startTime) throws JobManagerException;

    /**
     * Schedule a job to start at a specific time with specific recurrence info
     *@param jobName The name of the job
     *@param poolName The name of the pool to run the service from
     *@param serviceName The name of the service to invoke
     *@param dataId The persisted context (RuntimeData.runtimeDataId)
     *@param startTime The time in milliseconds the service should run
     *@param frequency The frequency of the recurrence (HOURLY,DAILY,MONTHLY,etc)
     *@param interval The interval of the frequency recurrence
     *@param count The number of times to repeat
     *@param endTime The time in milliseconds the service should expire
     *@param maxRetry The max number of retries on failure (-1 for no max)
     */
    public void schedule(String jobName, String poolName, String serviceName, String dataId, long startTime, int frequency, int interval, int count, long endTime, int maxRetry) throws JobManagerException;
}
