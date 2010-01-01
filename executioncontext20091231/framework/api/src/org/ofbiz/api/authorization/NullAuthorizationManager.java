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
import java.util.List;
import java.util.ListIterator;

import org.ofbiz.api.context.ThreadContext;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;

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
public class NullAuthorizationManager implements AuthorizationManager {

    protected static final String module = NullAuthorizationManager.class.getName();
    protected static final AccessController nullAccessController = new NullAccessController();

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
        return nullAccessController;
    }

    /** An implementation of the <code>AccessController</code> interface
     * that allows unrestricted access to all security-aware artifacts.
     */
    protected static class NullAccessController implements AccessController {

        // Temporary - will be removed later
        protected boolean verbose = false;
        protected NullAccessController() {
            this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.verbose"));
        }

        public <E> List<E> applyFilters(List<E> list) {
            return list;
        }

        public <E> ListIterator<E> applyFilters(ListIterator<E> list) {
            return list;
        }

        public void checkPermission(Permission permission) throws AccessControlException {
            if (this.verbose) {
                Debug.logInfo("Checking permission: " + ThreadContext.getExecutionPath() + "[" + permission + "]", module);
                Debug.logInfo("Found permission(s): " + 
                        "null-access-controller@" + ThreadContext.getExecutionPath() + "[admin=true]", module);
            }
        }
    }

}
