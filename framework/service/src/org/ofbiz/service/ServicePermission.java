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

import java.security.Permission;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;

/**
 * Service permission class. Invokes a service permission that
 * returns hasPermission true or false.
 */
@SuppressWarnings("serial")
public class ServicePermission extends Permission {

	protected final static String module = ServicePermission.class.getName();
	protected final String serviceName;
	protected final ExecutionContext executionContext;

	public ServicePermission(String serviceName, ExecutionContext executionContext) {
		super("service=" + serviceName);
		this.serviceName = serviceName;
		this.executionContext = executionContext;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		try {
			ServicePermission that = (ServicePermission) obj;
			return this.serviceName.equals(that.serviceName);
		} catch (Exception e) {}
		return false;
	}

	@Override
	public String getActions() {
		return null;
	}

	@Override
	public int hashCode() {
		return this.serviceName.hashCode();
	}

	/** Invokes the permission service and returns the result. The
	 * <code>permission</code> parameter is not used - it has no meaning.
	 * 
	 */
	@Override
	public boolean implies(Permission permission) {
		if (this.executionContext.getUserLogin() == null) {
			// This is here for development purposes
			return true;
		}
		try {
			LocalDispatcher dispatcher = this.executionContext.getDispatcher();
			DispatchContext ctx = dispatcher.getDispatchContext();
			ModelService modelService;
			modelService = ctx.getModelService(this.serviceName);
			Map<String, Object> context = FastMap.newInstance();
			Map<String, ? extends Object> params = this.executionContext.getParameters();
			if (params != null) {
				context.putAll(params);
			}
			if (!context.containsKey("userLogin")) {
				context.put("userLogin", this.executionContext.getUserLogin());
			}
			if (!context.containsKey("locale")) {
				context.put("locale", this.executionContext.getLocale());
			}
			if (!context.containsKey("timeZone")) {
				context.put("timeZone", this.executionContext.getTimeZone());
			}
			context = modelService.makeValid(context, ModelService.IN_PARAM);
			Map<String, Object> result = dispatcher.runSync(this.serviceName, context);
			Boolean hasPermission = (Boolean) result.get("hasPermission");
			return hasPermission != null && hasPermission.booleanValue();
		} catch (Exception e) {
			Debug.logError(e, module);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
