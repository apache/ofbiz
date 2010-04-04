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
import java.util.List;
import java.util.Set;

import javolution.util.FastSet;

/**
 * A <code>Set</code> of permissions.
 */
@SuppressWarnings("serial")
public abstract class PermissionsSet extends BasicPermission {

    protected final Set<Permission> permissionsSet = FastSet.newInstance();

    public PermissionsSet(String setName) {
        super(setName);
    }

    public PermissionsSet(String setName, List<Permission> permissionsList) {
        super(setName);
        this.permissionsSet.addAll(permissionsList);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        try {
            PermissionsSet that = (PermissionsSet) obj;
            return this.permissionsSet.equals(that.permissionsSet);
        } catch (ClassCastException e) {}
        return false;
    }

    @Override
    public String getActions() {
        return null;
    }

    @Override
    public int hashCode() {
        return permissionsSet.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Permission perm : this.permissionsSet) {
            sb.append(perm);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public Set<Permission> getPermissionsSet() {
        return this.permissionsSet;
    }
}
