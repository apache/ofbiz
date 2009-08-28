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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.security.AuthorizationManager;

/** An implementation of <code>AuthorizationManager</code> that allows
 * unrestricted access to all security-aware artifacts. This class
 * is intended to be used in situations where user permissions are
 * not available or accessible (the initial data load for example).
 * <p>Extreme care should be taken when using this class so that
 * security holes are not introduced. A recommended strategy is:<br><br>
 * <ul>
 * <li>Save the current <code>AuthorizationManager</code> instance in
 * a local variable - using <code>ExecutionContext.getSecurity()</code>.</li>
 * <li>Call <code>ExecutionContext.setSecurity(...)</code> with a
 * <code>NullAuthorizationManager</code> instance.</li>
 * <li>Perform the unrestricted tasks.</li>
 * <li>Restore the original <code>AuthorizationManager</code> by
 * calling <code>ExecutionContext.setSecurity(...)</code> with the
 * saved <code>AuthorizationManager</code> instance.</li>
 * </ul></p>
 * 
 */
public class NullAuthorizationManager<E> implements AuthorizationManager {

    public void assignGroupPermission(String userGroupId, String artifactId,
            Permission permission) {
    }

    public void assignGroupToGroup(String childGroupId, String parentGroupId) {
    }

    public void assignUserPermission(String userLoginId, String artifactId,
            Permission permission) {
    }

    public void assignUserToGroup(String userLoginId, String userGroupId) {
    }

    public void createUser(String userLoginId, String password) {
    }

    public String createUserGroup(String description) {
        return null;
    }

    public void deleteGroupFromGroup(String childGroupId, String parentGroupId) {
    }

    public void deleteGroupPermission(String userGroupId, String artifactId,
            Permission permission) {
    }

    public void deleteUser(String userLoginId) {
    }

    public void deleteUserFromGroup(String userLoginId, String userGroupId) {
    }

    public void deleteUserGroup(String userGroupId) {
    }

    public void deleteUserPermission(String userLoginId, String artifactId,
            Permission permission) {
    }

    public void updateUser(String userLoginId, String password) {
    }

    public void updateUserGroup(String userGroupId, String description) {
    }

    public org.ofbiz.api.authorization.AccessController getAccessController(
            org.ofbiz.api.context.ExecutionContext executionContext)
            throws AccessControlException {
        return AuthorizationManagerImpl.nullAccessController;
    }

    public void clearUserData(GenericValue userLogin) {
    }

    public Iterator<GenericValue> findUserLoginSecurityGroupByUserLoginId(
            String userLoginId) {
        return null;
    }

    public GenericDelegator getDelegator() {
        return null;
    }

    public boolean hasEntityPermission(String entity, String action,
            HttpSession session) {
        return true;
    }

    public boolean hasEntityPermission(String entity, String action,
            GenericValue userLogin) {
        return true;
    }

    public boolean hasPermission(String permission, HttpSession session) {
        return true;
    }

    public boolean hasPermission(String permission, GenericValue userLogin) {
        return true;
    }

    public boolean hasRolePermission(String application, String action,
            String primaryKey, String role, HttpSession session) {
        return true;
    }

    public boolean hasRolePermission(String application, String action,
            String primaryKey, String role, GenericValue userLogin) {
        return true;
    }

    public boolean hasRolePermission(String application, String action,
            String primaryKey, List<String> roles, GenericValue userLogin) {
        return true;
    }

    public boolean hasRolePermission(String application, String action,
            String primaryKey, List<String> roles, HttpSession session) {
        return true;
    }

    public boolean securityGroupPermissionExists(String groupId,
            String permission) {
        return true;
    }

    public void setDelegator(GenericDelegator delegator) {
    }
}
