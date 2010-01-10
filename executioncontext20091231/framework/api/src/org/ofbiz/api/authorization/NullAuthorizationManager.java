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
package org.ofbiz.api.authorization;

import java.security.AccessControlException;
import java.security.Permission;

public class NullAuthorizationManager implements AuthorizationManager {

    public static final String module = NullAuthorizationManager.class.getName();

    protected final AccessController accessController;
    
    public NullAuthorizationManager(AccessController accessController) {
        this.accessController = accessController;
    }
    
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

    public AccessController getAccessController() throws AccessControlException {
        return this.accessController;
    }
}
