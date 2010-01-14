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

import java.security.Permission;

/**
 * An object used for managing users, user groups, and permissions.
 */
public interface AuthorizationManager {

    void assignGroupPermission(String userGroupId, String artifactId, Permission permission) throws AuthorizationManagerException;

    void assignGroupToGroup(String childGroupId, String parentGroupId) throws AuthorizationManagerException;

    void assignUserPermission(String userLoginId, String artifactId, Permission permission) throws AuthorizationManagerException;

    void assignUserToGroup(String userLoginId, String userGroupId) throws AuthorizationManagerException;

    void createUser(String userLoginId, String password) throws AuthorizationManagerException;

    String createUserGroup(String description) throws AuthorizationManagerException;

    void deleteGroupFromGroup(String childGroupId, String parentGroupId) throws AuthorizationManagerException;

    void deleteGroupPermission(String userGroupId, String artifactId, Permission permission) throws AuthorizationManagerException;

    void deleteUser(String userLoginId) throws AuthorizationManagerException;

    void deleteUserFromGroup(String userLoginId, String userGroupId) throws AuthorizationManagerException;

    void deleteUserGroup(String userGroupId) throws AuthorizationManagerException;

    void deleteUserPermission(String userLoginId, String artifactId, Permission permission) throws AuthorizationManagerException;

    AccessController getAccessController() throws AuthorizationManagerException;

    void updateUser(String userLoginId, String password) throws AuthorizationManagerException;

    void updateUserGroup(String userGroupId, String description) throws AuthorizationManagerException;
}
