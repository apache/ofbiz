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
 * Generic permission class. Similar to java.security.BasicPermission.
 */
@SuppressWarnings("serial")
public class BasicPermission extends Permission {

    protected final String permissionString;

    public BasicPermission(String permissionString) {
        super(permissionString);
        this.permissionString = permissionString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        try {
            BasicPermission that = (BasicPermission) obj;
            return this.permissionString.equals(that.permissionString);
        } catch (Exception e) {}
        return false;
    }

    @Override
    public String getActions() {
        return null;
    }

    @Override
    public int hashCode() {
        return this.permissionString.hashCode();
    }

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
        } catch (Exception e) {}
        try {
            PermissionsIntersection permissionsIntersection = (PermissionsIntersection) permission;
            for (Permission perm : permissionsIntersection.getPermissionsSet()) {
                if (!this.implies(perm)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {}
        return this.equals(permission);
    }

    @Override
    public String toString() {
        return this.permissionString;
    }
}
