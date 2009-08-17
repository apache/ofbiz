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
import java.util.List;

/**
 * A <code>List</code> of permissions that represent a union.
 */
@SuppressWarnings("serial")
public class PermissionsUnion extends Permission {
	protected final List<Permission> permissionsList;

	public PermissionsUnion(String listName, List<Permission> permissionsList) {
		super(listName);
		this.permissionsList = permissionsList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		try {
			PermissionsUnion that = (PermissionsUnion) obj;
			return this.permissionsList.equals(that.permissionsList);
		} catch (Exception e) {}
		return false;
	}

	@Override
	public String getActions() {
		return null;
	}

	@Override
	public int hashCode() {
		return permissionsList.hashCode();
	}

	/** Returns <code>true</code> if any of the contained permissions
	 * returns <code>true</code>.
	 */
	@Override
	public boolean implies(Permission permission) {
		try {
			PermissionsUnion permissionsUnion = (PermissionsUnion) permission;
			for (Permission perm : permissionsUnion.permissionsList) {
				if (this.implies(perm)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {}
		try {
			PermissionsIntersection permissionsIntersection = (PermissionsIntersection) permission;
			for (Permission perm : permissionsIntersection.permissionsList) {
				if (!this.implies(perm)) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {}
		for (Permission perm : this.permissionsList) {
			if (perm.implies(permission)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Permission perm : this.permissionsList) {
			sb.append(perm);
			sb.append(" ");
		}
		return sb.toString().trim();
	}
}
