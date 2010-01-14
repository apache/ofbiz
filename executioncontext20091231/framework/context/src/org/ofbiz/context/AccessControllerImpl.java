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
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ThreadContext;
import org.ofbiz.service.ServiceUtil;

/** An implementation of the <code>AccessController</code> interface. */
public class AccessControllerImpl implements AccessController {

    public static final String module = AccessControllerImpl.class.getName();

    protected static boolean securityAuditEnabled() {
        return "true".equals(UtilProperties.getPropertyValue("api.properties", "securityAudit.enabled"));
    }

    /**
     * The root node of the current user's permission tree.
     */
    protected final PathNode node;
    // Temporary - will be removed later
    protected boolean verbose = false;
    protected boolean disabled = false;

    protected AccessControllerImpl(PathNode node) {
        this.node = node;
        this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.verbose"));
        this.disabled = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.disabled"));
        if (this.verbose) {
            Debug.logInfo("Permissions for " + ThreadContext.getUserLogin().getString("userLoginId") + ": \n" + node, module);
        }
    }

    /** Applies permission filters to a <code>List</code>. The
     * returned <code>List</code> will contain only the objects
     * the user has permission to access.
     * 
     * <p>This implementation invokes the specified service
     * with the list as a parameter called <code>candidateList</code>.
     * The service must return a <code>List</code> called
     * <code>candidateList</code>. The service returns only
     * those list elements the user is permitted to access.</p>
     * 
     * @param list The <code>List</code> to apply filters to
     * @return A security-aware <code>List</code> if filters
     * were specified for the current artifact, or the original
     * <code>List</code> otherwise
     */
    public <E> List<E> applyFilters(List<E> list) {
        OFBizPermission permission = new OFBizPermission("applyFilters");
        PermissionsGatherer permissionsGatherer = new PermissionsGatherer(this.node, permission);
        permissionsGatherer.gatherPermissions(ThreadContext.getExecutionPath());
        if (permission.getFilterNames().size() > 0) {
            try {
                LocalDispatcher dispatcher = ThreadContext.getDispatcher();
                DispatchContext ctx = dispatcher.getDispatchContext();
                Map<String, ? extends Object> params = ThreadContext.getParameters();
                for (String serviceName : permission.getFilterNames()) {
                    Debug.logInfo("Applying filter service: " + serviceName, module);
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
                    context.put("candidateList", list);
                    context = modelService.makeValid(context, ModelService.IN_PARAM);
                    Map<String, Object> result = dispatcher.runSync(serviceName, context);
                    if (ServiceUtil.isSuccess(result)) {
                        list = UtilGenerics.cast(result.get("candidateList"));
                    }
                }
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
            }
        }
        return list;
    }

    public <E> ListIterator<E> applyFilters(ListIterator<E> listIterator) {
        if (listIterator instanceof EntityListIterator) {
            // Decorating the EntityListIterator breaks a lot of code.
            return listIterator;
        }
        OFBizPermission permission = new OFBizPermission("applyFilters");
        PermissionsGatherer permissionsGatherer = new PermissionsGatherer(this.node, permission);
        permissionsGatherer.gatherPermissions(ThreadContext.getExecutionPath());
        if (permission.getFilterNames().size() > 0) {
            return new SecurityAwareListIterator<E>(listIterator, permission.getFilterNames());
        }
        return listIterator;
    }

    public void checkPermission(Permission permission) throws AccessControlException {
        checkPermission(permission, ThreadContext.getExecutionPath());
    }

    @Override
    public void checkPermission(Permission permission, ArtifactPath artifactPath) throws AccessControlException {
        if (this.verbose) {
            Debug.logInfo("Checking permission: " + artifactPath + "[" + permission + "]", module);
        }
        OFBizPermission gatheredPermissions = new OFBizPermission("checkPermission");
        PermissionsGatherer permissionsGatherer = new PermissionsGatherer(this.node, gatheredPermissions);
        permissionsGatherer.gatherPermissions(artifactPath);
        if (this.verbose) {
            Debug.logInfo("Found permission(s): " + ThreadContext.getUserLogin().getString("userLoginId") +
                    "@" + artifactPath + "[" + gatheredPermissions + "]", module);
        }
        if (this.disabled) {
            return;
        }
        if (gatheredPermissions.implies(permission) && this.hasServicePermission(gatheredPermissions)) {
            return;
        }
        if (securityAuditEnabled()) {
            AuthorizationManagerImpl.logIncident(permission);
        }
        throw new AccessControlException(ThreadContext.getUserLogin().getString("userLoginId") +
                "@" + artifactPath + "[" + permission + "]");
    }

    protected boolean hasServicePermission(OFBizPermission permission) {
        try {
            if (permission.getServiceNames().size() == 0 || permission.isAdmin()) {
                return true;
            }
            LocalDispatcher dispatcher = ThreadContext.getDispatcher();
            DispatchContext ctx = dispatcher.getDispatchContext();
            Map<String, ? extends Object> params = ThreadContext.getParameters();
            for (String serviceName : permission.getServiceNames()) {
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
}
