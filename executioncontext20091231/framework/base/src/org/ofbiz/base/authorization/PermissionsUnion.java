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
package org.ofbiz.base.authorization;

import java.security.Permission;
import java.util.Arrays;
import java.util.List;

/**
 * A <code>Set</code> of permissions that represent a union.
 */
@SuppressWarnings("serial")
public class PermissionsUnion extends PermissionsSet {

    public PermissionsUnion(String listName) {
        super(listName);
    }

    public PermissionsUnion(String listName, List<Permission> permissionsList) {
        super(listName, permissionsList);
    }

    public PermissionsUnion(String listName, Permission... permissions) {
        super(listName, Arrays.asList(permissions));
    }

    /** Returns <code>true</code> if any of the contained permissions
     * returns <code>true</code>.
     */
    @Override
    public boolean implies(Permission permission) {
        try {
            PermissionsUnion permissionsUnion = (PermissionsUnion) permission;
            for (Permission perm : permissionsUnion.getPermissionsSet()) {
                if (this.implies(perm)) {
                    return true;
                }
            }
            return false;
        } catch (ClassCastException e) {}
        try {
            PermissionsIntersection permissionsIntersection = (PermissionsIntersection) permission;
            for (Permission perm : permissionsIntersection.getPermissionsSet()) {
                if (!this.implies(perm)) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {}
        for (Permission perm : this.permissionsSet) {
            if (perm.implies(permission)) {
                return true;
            }
        }
        return false;
    }
}
