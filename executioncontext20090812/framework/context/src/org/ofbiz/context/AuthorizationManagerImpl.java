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

import static org.ofbiz.api.authorization.BasicPermissions.Admin;

import java.security.AccessControlException;
import java.security.Permission;
import java.util.List;
import java.util.ListIterator;

import org.ofbiz.entity.AccessController;
import org.ofbiz.api.authorization.PermissionsIntersection;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.security.AuthorizationManager;
import org.ofbiz.security.OFBizSecurity;
import org.ofbiz.service.ExecutionContext;
import org.ofbiz.service.ServicePermission;

/**
 * An implementation of the AuthorizationManager interface that uses the OFBiz database
 * for authorization data storage.
 */
public class AuthorizationManagerImpl<E> extends OFBizSecurity implements AuthorizationManager {

    // Right now this class is being used as a test jig for the various classes
    // it will be working with. The actual implementation will occur once the
    // entities are defined and in place.

    public static final String module = AuthorizationManagerImpl.class.getName();
    
    protected Permission testPermission = null;
    protected Permission getTestPermission(ExecutionContext executionContext) {
    	if (this.testPermission == null) {
    		// Build test permissions
    		this.testPermission = new PermissionsIntersection("TestPermissions",
    				UtilMisc.toList(new ServicePermission("securityRedesignTest", executionContext),
    						Admin));
    	}
		return this.testPermission;
    }

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

	public AccessController<E> getAccessController(org.ofbiz.api.context.ExecutionContext executionContext) {
		return new AccessControllerImpl<E>((ExecutionContext) executionContext, this.getTestPermission((ExecutionContext) executionContext));
	}

	protected static class AccessControllerImpl<E> implements AccessController<E> {

		protected final ExecutionContext executionContext;
        protected final String executionPath;
		protected final Permission permission;
		// Temporary - will be removed later
		protected boolean verbose = false;
		protected List<String> serviceNameList = UtilMisc.toList("securityRedesignTest");

		protected AccessControllerImpl(ExecutionContext executionContext, Permission permission) {
			this.executionContext = executionContext;
			this.executionPath = executionContext.getExecutionPath();
			this.permission = permission;
		    this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.verbose"));
		}

		public void checkPermission(Permission permission) throws AccessControlException {
			if (this.verbose) {
                Debug.logInfo("Checking permission: " + this.executionPath + "[" + permission + "]", module);
			}
			if (!this.permission.implies(permission)) {
				throw new AccessControlException(this.executionPath);
			}
		}

		public List<E> applyFilters(List<E> list) {
		    String upperPath = this.executionPath.toUpperCase();
		    if (upperPath.startsWith("OFBIZ/EXAMPLE")) {
	            if (this.verbose) {
	                Debug.logInfo("Applying List filter \"securityRedesignTest\" for path " + this.executionPath, module);
	            }
		        return new SecurityAwareList<E>(list, this.serviceNameList, this.executionContext);
		    }
			return list;
		}

		public ListIterator<E> applyFilters(ListIterator<E> listIterator) {
            String upperPath = this.executionPath.toUpperCase();
            if (upperPath.startsWith("OFBIZ/EXAMPLE")) {
                if (this.verbose) {
                    Debug.logInfo("Applying ListIterator filter \"securityRedesignTest\" for path " + this.executionPath, module);
                }
                return new SecurityAwareListIterator<E>(listIterator, this.serviceNameList, this.executionContext);
            }
			return listIterator;
		}

        public EntityListIterator applyFilters(EntityListIterator listIterator) {
            String upperPath = this.executionPath.toUpperCase();
            if (upperPath.startsWith("OFBIZ/EXAMPLE")) {
                if (this.verbose) {
                    Debug.logInfo("Applying EntityListIterator filter \"securityRedesignTest\" for path " + this.executionPath, module);
                }
                // Commented out for now - causes problems with list pagination in UI
//                return new SecurityAwareEli(listIterator, this.serviceNameList, this.executionContext);
            }
            return listIterator;
        }
	}

}
