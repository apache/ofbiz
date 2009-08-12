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

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.security.Security;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ExecutionContext;

/** ExecutionContext implementation. */
public class ExecutionContextImpl extends org.ofbiz.api.context.ExecutionContextImpl implements ExecutionContext {

    protected GenericDelegator delegator = null;
    protected LocalDispatcher dispatcher = null;
    protected Security security = null;
    protected GenericValue userLogin = null;

	public GenericDelegator getDelegator() {
		return this.delegator;
	}

	public LocalDispatcher getDispatcher() {
		return this.dispatcher;
	}

	public Security getSecurity() {
		return this.security;
	}

	public GenericValue getUserLogin() {
		return this.userLogin;
	}

	public void initializeContext(Map<String, ? extends Object> params) {
		this.setLocale((Locale) params.get("locale")); 
		this.setTimeZone((TimeZone) params.get("timeZone"));
		this.setUserLogin((GenericValue) params.get("userLogin"));
	}

	public void setDelegator(GenericDelegator delegator) {
		if (delegator != null) {
			delegator.setExecutionContext(this);
			this.delegator = delegator;
		}
	}

	public void setDispatcher(LocalDispatcher dispatcher) {
		if (dispatcher != null) {
			this.dispatcher = dispatcher;
		}
	}

	public void setSecurity(Security security) {
		if (security != null) {
			this.security = security;
		}
	}

	public void setUserLogin(GenericValue userLogin) {
		if (userLogin != null) {
			this.userLogin = userLogin;
		}
	}
}
