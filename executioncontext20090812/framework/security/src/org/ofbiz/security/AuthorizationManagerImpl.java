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
package org.ofbiz.security;

import static org.ofbiz.api.authorization.BasicPermissions.Admin;

import java.security.AccessControlException;
import java.security.Permission;

import org.ofbiz.api.authorization.AccessController;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;

/**
 * An implementation of the AuthorizationManager interface that uses the OFBiz database
 * for authorization data storage.
 */
public class AuthorizationManagerImpl extends OFBizSecurity implements AuthorizationManager {

    public static final String module = AuthorizationManagerImpl.class.getName();

    public AuthorizationManagerImpl() {
    }

	public void assignGroupPermission(String userGroupId, String artifactId,
			Permission permission) {
		// TODO Auto-generated method stub
		
	}

	public void assignGroupToGroup(String childGroupId, String parentGroupId) {
		// TODO Auto-generated method stub
		
	}

	public void assignUserPermission(String userLoginId, String artifactId,
			Permission permission) {
		// TODO Auto-generated method stub
		
	}

	public void assignUserToGroup(String userLoginId, String userGroupId) {
		// TODO Auto-generated method stub
		
	}

	public void createUser(String userLoginId, String password) {
		// TODO Auto-generated method stub
		
	}

	public String createUserGroup(String description) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteGroupFromGroup(String childGroupId, String parentGroupId) {
		// TODO Auto-generated method stub
		
	}

	public void deleteGroupPermission(String userGroupId, String artifactId,
			Permission permission) {
		// TODO Auto-generated method stub
		
	}

	public void deleteUser(String userLoginId) {
		// TODO Auto-generated method stub
		
	}

	public void deleteUserFromGroup(String userLoginId, String userGroupId) {
		// TODO Auto-generated method stub
		
	}

	public void deleteUserGroup(String userGroupId) {
		// TODO Auto-generated method stub
		
	}

	public void deleteUserPermission(String userLoginId, String artifactId,
			Permission permission) {
		// TODO Auto-generated method stub
		
	}

	public void updateUser(String userLoginId, String password) {
		// TODO Auto-generated method stub
		
	}

	public void updateUserGroup(String userGroupId, String description) {
		// TODO Auto-generated method stub
		
	}

	public AccessController getAccessController(org.ofbiz.api.context.ExecutionContext executionContext) {
		return new AccessControllerImpl(executionContext.getExecutionPath(), Admin);
	}

	protected static class AccessControllerImpl implements AccessController {

		protected final String executionPath;
		protected final Permission permission;
		// Temporary - will be removed later
		protected boolean verbose = false;

		protected AccessControllerImpl(String executionPath, Permission permission) {
			this.executionPath = executionPath;
			this.permission = permission;
		    this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.verbose"));
		}

		public void checkPermission(Permission permission) throws AccessControlException {
			if (this.verbose) {
                Debug.logInfo("Checking permission " + permission + " for path " + this.executionPath, module);
			}
			if (!this.permission.implies(permission)) {
				throw new AccessControlException(this.executionPath);
			}
		}
		
	}

}
