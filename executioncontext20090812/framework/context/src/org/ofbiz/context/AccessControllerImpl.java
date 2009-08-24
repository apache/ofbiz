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
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.cache.UtilCache;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.ExecutionContext;

public class AccessControllerImpl<E> implements AccessController<E> {

    public static final String module = AccessControllerImpl.class.getName();
    protected static UtilCache<String, Permission> userGroupPermCache = new UtilCache<String, Permission>("authorization.UserGroupPermissions");
    protected static UtilCache<String, Permission> userPermCache = new UtilCache<String, Permission>("authorization.UserPermissions");
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
