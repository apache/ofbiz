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
package org.ofbiz.entity;

/**
 * ExecutionContext Interface. This interface extends the ExecutionContext
 * interface defined in the <code>base</code> component.
 */
public interface ExecutionContext extends org.ofbiz.api.context.ExecutionContext {

	/** Returns the current <code>GenericDelegator</code> instance.
	 * 
	 * @return The current <code>GenericDelegator</code> instance
	 */
	public GenericDelegator getDelegator();

	/** Returns the current userLogin <code>GenericValue</code>.
	 * 
	 * @return The current userLogin <code>GenericValue</code>
	 */
	public GenericValue getUserLogin();

	/** Sets the current <code>Delegator</code> instance.
	 * 
	 * @param delegator The new <code>Delegator</code> instance
	 */
	public void setDelegator(GenericDelegator delegator);

    /** Sets the current userLogin <code>GenericValue</code>.
     * 
     * @param userLogin The new userLogin <code>GenericValue</code>.
     */
	public void setUserLogin(GenericValue userLogin);
}
