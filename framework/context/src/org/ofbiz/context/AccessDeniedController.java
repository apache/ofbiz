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
import java.util.ListIterator;

import javolution.util.FastList;

import org.ofbiz.api.authorization.AccessController;
import org.ofbiz.api.context.ArtifactPath;
import org.ofbiz.api.context.ThreadContext;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilGenerics;

/** An implementation of the <code>AccessController</code> interface
 * that denies access to all security-aware artifacts.
 */
public class AccessDeniedController implements AccessController {

    public static final String module = AccessDeniedController.class.getName();
    // Temporary - will be removed later
    protected boolean verbose = false;

    public AccessDeniedController() {
        this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.verbose"));
    }

    @Override
    public <E> List<E> applyFilters(List<E> list) {
        return FastList.newInstance();
    }

    @Override
    public <E> ListIterator<E> applyFilters(ListIterator<E> list) {
        return UtilGenerics.cast(FastList.newInstance().listIterator());
    }

    @Override
    public void checkPermission(Permission permission) throws AccessControlException {
        checkPermission(permission, ThreadContext.getExecutionPath());
    }

    @Override
    public void checkPermission(Permission permission, ArtifactPath artifactPath) throws AccessControlException {
        if (this.verbose) {
            Debug.logInfo("Checking permission: " + artifactPath + "[" + permission + "]", module);
            Debug.logInfo("Found permission(s): " +
                    "access-denied-controller@" + artifactPath + "[]", module);
        }
        throw new AccessControlException(null, permission);
    }
}
