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

import static org.ofbiz.api.authorization.BasicPermissions.*;

import java.security.Permission;
import java.util.Set;

import javolution.util.FastSet;

import org.ofbiz.api.authorization.PermissionsUnion;

/** OFBizPermission class.
 * <p>This class enforces the security-aware artifact permission
 * checking rules:<br>
 * <ul>
 * <li>If the permissions list contains the admin permission,
 * then access is granted</li>
 * <li>If the permissions list contains the specified permission,
 * then access is granted</li>
 * <li>If services are specified, and all services return
 * <code>hasPermission=true</code>, then access is granted</li>
 * <li>The class contains a list of filters that can be used
 * by filter implementations</li>
 * </ul></p>
 *
 */
@SuppressWarnings("serial")
public class OFBizPermission extends Permission {

    protected Permission adminPermission = null;
    protected final PermissionsUnion includePermissions;
    protected final PermissionsUnion excludePermissions;
    protected final Set<String> filters = FastSet.newInstance();
    protected final Set<String> services = FastSet.newInstance();

    public OFBizPermission(String name) {
        super(name);
        this.includePermissions = new PermissionsUnion(name);
        this.excludePermissions = new PermissionsUnion(name);
    }

    public void accumulatePermissions(OFBizPermission permission) {
        if (permission == null || this.adminPermission != null) {
            return;
        }
        if (permission.includePermissions.getPermissionsSet().contains(Admin)) {
            this.reset();
            this.adminPermission = Admin;
            return;
        }
        this.includePermissions.getPermissionsSet().removeAll(permission.excludePermissions.getPermissionsSet());
        this.excludePermissions.getPermissionsSet().removeAll(permission.includePermissions.getPermissionsSet());
        this.includePermissions.getPermissionsSet().addAll(permission.includePermissions.getPermissionsSet());
        this.excludePermissions.getPermissionsSet().addAll(permission.excludePermissions.getPermissionsSet());
        this.filters.addAll(permission.filters);
        this.services.addAll(permission.services);
    }

    public void addFilter(String filter) {
        this.filters.add(filter);
    }

    public void addService(String service) {
        this.services.add(service);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        try {
            OFBizPermission that = (OFBizPermission) obj;
            return this.getName().equals(that.getName());
        } catch (ClassCastException e) {}
        return false;
    }

    @Override
    public String getActions() {
        return null;
    }

    public Set<String> getFilterNames() {
        return this.filters;
    }

    public Set<String> getServiceNames() {
        return this.services;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean implies(Permission permission) {
        if (this.adminPermission != null) {
            return this.adminPermission.implies(permission);
        }
        return this.includePermissions.implies(permission) && !this.excludePermissions.implies(permission);
    }

    public boolean isAdmin() {
        return this.adminPermission != null;
    }

    public void reset() {
        this.adminPermission = null;
        this.includePermissions.getPermissionsSet().clear();
        this.excludePermissions.getPermissionsSet().clear();
        this.filters.clear();
        this.services.clear();
    }

    @Override
    public String toString() {
        if (this.adminPermission != null) {
            return this.adminPermission.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.includePermissions);
        sb.append(" ");
        if (this.excludePermissions.getPermissionsSet().size() > 0) {
            sb.append("!(");
            sb.append(this.excludePermissions);
            sb.append(")");
        }
        for (String filter : this.filters) {
            sb.append(" filter=");
            sb.append(filter);
        }
        for (String service : this.services) {
            sb.append(" service=");
            sb.append(service);
        }
        return sb.toString().trim();
    }

}
