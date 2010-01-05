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

import java.security.AccessControlException;
import java.security.Permission;
import java.util.List;

import org.ofbiz.api.authorization.AccessController;
import org.ofbiz.api.authorization.BasicPermissions;
import org.ofbiz.api.authorization.AuthorizationManager;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
//import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.cache.UtilCache;
import org.ofbiz.security.OFBizSecurity;
import org.ofbiz.service.ThreadContext;

/**
 * An implementation of the AuthorizationManager interface that uses the Entity Engine
 * for authorization data storage.
 */
public class AuthorizationManagerImpl extends OFBizSecurity implements AuthorizationManager {

    // Right now this class implements permission checking only.

    public static final String module = AuthorizationManagerImpl.class.getName();
    protected static final UtilCache<String, AccessController> userPermCache = UtilCache.createUtilCache("authorization.UserPermissions");

    public AuthorizationManagerImpl() {
    }

	public void assignGroupPermission(String userGroupId, String artifactId, Permission permission) {
		// TODO Auto-generated method stub
		
	}

	public void assignGroupToGroup(String childGroupId, String parentGroupId) {
		// TODO Auto-generated method stub
		
	}

	public void assignUserPermission(String userLoginId, String artifactId, Permission permission) {
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

	public void deleteGroupPermission(String userGroupId, String artifactId, Permission permission) {
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

	public void deleteUserPermission(String userLoginId, String artifactId, Permission permission) {
		// TODO Auto-generated method stub
		
	}

	public void updateUser(String userLoginId, String password) {
		// TODO Auto-generated method stub
		
	}

	public void updateUserGroup(String userGroupId, String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void clearUserData(GenericValue userLogin) {
        super.clearUserData(userLogin);
        userPermCache.remove(userLogin.getString("userLogin"));
    }

    public AccessController getAccessController() throws AccessControlException {
        String userLoginId = ThreadContext.getUserLogin().getString("userLoginId");
        return getAccessController(userLoginId);
	}

    protected static AccessController getAccessController(String userLoginId) throws AccessControlException {
        AccessController accessController = userPermCache.get(userLoginId);
        if (accessController != null) {
            return accessController;
        }
        synchronized (userPermCache) {
            try {
                ThreadContext.runUnprotected();
                Delegator delegator = ThreadContext.getDelegator();
                PathNode node = PathNode.getInstance(ArtifactPath.PATH_ROOT);
                // Process group membership permissions first
                List<GenericValue> groupMemberships = delegator.findList("UserToUserGroupRel", EntityCondition.makeCondition(UtilMisc.toMap("userLoginId", userLoginId)), null, null, null, false);
                for (GenericValue userGroup : groupMemberships) {
                    processGroupPermissions(userGroup.getString("groupId"), node, delegator);
                }
                // Process user permissions last
                List<GenericValue> permissionValues = delegator.findList("UserToArtifactPermRel", EntityCondition.makeCondition(UtilMisc.toMap("userLoginId", userLoginId)), null, null, null, false);
                setPermissions(userLoginId, node, permissionValues);
                accessController = new AccessControllerImpl(node);
                userPermCache.put(userLoginId, accessController);
            } catch (GenericEntityException e) {
                throw new AccessControlException(e.getMessage());
            } finally {
                ThreadContext.endRunUnprotected();
            }
        }
	    return accessController;
	}

    protected static void processGroupPermissions(String groupId, PathNode node, Delegator delegator) throws AccessControlException {
        try {
            // Process this group's memberships first
            List<GenericValue> parentGroups = delegator.findList("UserGroupRelationship", EntityCondition.makeCondition(UtilMisc.toMap("toGroupId", groupId)), null, null, null, false);
            for (GenericValue parentGroup : parentGroups) {
                processGroupPermissions(parentGroup.getString("fromGroupId"), node, delegator);
            }
            // Process this group's permissions
            List<GenericValue> permissionValues = delegator.findList("UserGrpToArtifactPermRel", EntityCondition.makeCondition(UtilMisc.toMap("groupId", groupId)), null, null, null, false);
            setPermissions(groupId, node, permissionValues);
        } catch (GenericEntityException e) {
            throw new AccessControlException(e.getMessage());
        }
    }

    protected static void setPermissions(String id, PathNode node, List<GenericValue> permissionValues) {
        for (GenericValue value : permissionValues) {
            String artifactPath = value.getString("artifactPath");
            OFBizPermission target = new OFBizPermission(id + "@" + artifactPath);
            String[] pair = value.getString("permissionValue").split("=");
            if ("filter".equalsIgnoreCase(pair[0])) {
                target.addFilter(pair[1]);
            } else if ("service".equalsIgnoreCase(pair[0])) {
                target.addService(pair[1]);
            } else {
                Permission permission = BasicPermissions.ConversionMap.get(pair[0].toUpperCase());
                if (permission != null) {
                    if ("true".equalsIgnoreCase(pair[1])) {
                        target.includePermissions.getPermissionsSet().add(permission);
                    } else {
                        target.excludePermissions.getPermissionsSet().add(permission);
                    }
                } else {
                    throw new AccessControlException("Invalid permission: " + pair[0]);
                }
            }
            node.setPermissions(new ArtifactPath(artifactPath), target);
        }
    }

}
