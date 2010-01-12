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
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.api.authorization.AccessController;
import org.ofbiz.api.context.ArtifactPath;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ThreadContext;

/** An implementation of the <code>AccessController</code> interface. */
public class AccessControllerImpl implements AccessController {

    public static final String module = AccessControllerImpl.class.getName();

    protected final OFBizPermission permission;
    protected final PermissionsGatherer permissionsGatherer;
    // Temporary - will be removed later
    protected boolean verbose = false;
    protected boolean disabled = false;

    protected AccessControllerImpl(PathNode node) {
        this.permission = new OFBizPermission(ThreadContext.getUserLogin().getString("userLoginId"));
        this.permissionsGatherer = new PermissionsGatherer(node, this.permission);
        this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.verbose"));
        this.disabled = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.disabled"));
        if (this.verbose) {
            Debug.logInfo("Permissions for " + ThreadContext.getUserLogin().getString("userLoginId") + ": \n" + node, module);
        }
    }

    public <E> List<E> applyFilters(List<E> list) {
        if (this.permission.getFilterNames().size() > 0) {
            return new SecurityAwareList<E>(list, this.permission.getFilterNames());
        }
        return list;
    }

    public <E> ListIterator<E> applyFilters(ListIterator<E> listIterator) {
        if (listIterator instanceof EntityListIterator) {
            // Decorating the EntityListIterator breaks a lot of code.
            return listIterator;
        }
        if (this.permission.getFilterNames().size() > 0) {
            return new SecurityAwareListIterator<E>(listIterator, this.permission.getFilterNames());
        }
        return listIterator;
    }

    public void checkPermission(Permission permission) throws AccessControlException {
        checkPermission(permission, new ArtifactPath(ThreadContext.getExecutionPathAsArray()));
    }

    protected boolean hasServicePermission() {
        try {
            if (this.permission.getServiceNames().size() == 0) {
                return true;
            }
            LocalDispatcher dispatcher = ThreadContext.getDispatcher();
            DispatchContext ctx = dispatcher.getDispatchContext();
            Map<String, ? extends Object> params = ThreadContext.getParameters();
            for (String serviceName : this.permission.getServiceNames()) {
                ModelService modelService = ctx.getModelService(serviceName);
                Map<String, Object> context = FastMap.newInstance();
                if (params != null) {
                    context.putAll(params);
                }
                if (!context.containsKey("userLogin")) {
                    context.put("userLogin", ThreadContext.getUserLogin());
                }
                if (!context.containsKey("locale")) {
                    context.put("locale", ThreadContext.getLocale());
                }
                if (!context.containsKey("timeZone")) {
                    context.put("timeZone", ThreadContext.getTimeZone());
                }
                context = modelService.makeValid(context, ModelService.IN_PARAM);
                Map<String, Object> result = dispatcher.runSync(serviceName, context);
                Boolean hasPermission = (Boolean) result.get("hasPermission");
                if (hasPermission != null && !hasPermission.booleanValue()) {
                    return false;
                }
            }
        } catch (Exception e) {
            Debug.logError(e, module);
        }
        return true;
    }

    @Override
    public void checkPermission(Permission permission, ArtifactPath artifactPath) throws AccessControlException {
        if (this.verbose) {
            Debug.logInfo("Checking permission: " + artifactPath + "[" + permission + "]", module);
        }
        this.permissionsGatherer.gatherPermissions(artifactPath);
        if (this.verbose) {
            Debug.logInfo("Found permission(s): " + ThreadContext.getUserLogin().getString("userLoginId") +
                    "@" + artifactPath + "[" + this.permission + "]", module);
        }
        if (this.disabled) {
            return;
        }
        if (this.permission.implies(permission) && this.hasServicePermission()) {
            return;
        }
        throw new AccessControlException(ThreadContext.getUserLogin().getString("userLoginId") +
                "@" + artifactPath + "[" + permission + "]");
    }
}
