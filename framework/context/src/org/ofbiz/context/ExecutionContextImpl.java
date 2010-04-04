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

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javolution.util.FastList;

import org.ofbiz.base.authorization.AccessController;
import org.ofbiz.base.authorization.AuthorizationManager;
import org.ofbiz.base.authorization.AuthorizationManagerException;
import org.ofbiz.base.authorization.NullAuthorizationManager;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.ExecutionContext;
import org.ofbiz.service.GenericDispatcher;
import org.ofbiz.service.LocalDispatcher;

/** An implementation of the <code>ExecutionContext</code> interface. */
public class ExecutionContextImpl extends org.ofbiz.base.context.AbstractExecutionContext implements ExecutionContext {

    public static final String module = ExecutionContextImpl.class.getName();
    protected static final AccessController accessDeniedController = new AccessDeniedController();
    protected static final AuthorizationManager unrestrictedAuthorizationManager = new NullAuthorizationManager(new AccessGrantedController());
    /** Used by <code>runUnprotected</code> and <code>endRunUnprotected</code>
     * to save/restore the original <code>AuthorizationManager</code> instance.
     */
    protected final FastList<AuthorizationManager> managerList = FastList.newInstance();
    protected Delegator delegator = null;
    protected LocalDispatcher dispatcher = null;
    protected AuthorizationManager security = null;
    protected GenericValue userLogin = null;

    @Override
    public void clearUserData() {
        this.userLogin = null;
        this.resetUserPreferences();
    }

    @Override
    public void endRunUnprotected() {
        if (!this.managerList.isEmpty()) {
            this.setSecurity(this.managerList.removeLast());
        }
    }

    @Override
    public AccessController getAccessController() {
        try {
            return this.getSecurity().getAccessController();
        } catch (AuthorizationManagerException e) {
            Debug.logError(e, module);
        }
        return accessDeniedController;
    }

    @Override
    public Delegator getDelegator() {
        if (this.delegator == null) {
            this.delegator = DelegatorFactory.getDelegator("default");
        }
        return this.delegator;
    }

    @Override
    public LocalDispatcher getDispatcher() {
        if (this.dispatcher == null) {
            this.dispatcher = GenericDispatcher.getLocalDispatcher("ExecutionContext", this.getDelegator());
        }
        return this.dispatcher;
    }

    @Override
    public AuthorizationManager getSecurity() {
        if (this.security == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String className = UtilProperties.getPropertyValue("api.properties", "authorizationManager.class");
            if (this.verbose) {
                Debug.logInfo("Loading Authorization Manager " + className, module);
            }
            try {
                this.security = (AuthorizationManager) loader.loadClass(className).newInstance();
            } catch (Exception e) {
                Debug.logError(e, module);
            }
        }
        return this.security;
    }

    @Override
    public GenericValue getUserLogin() {
        if (this.userLogin == null) {
            Delegator localDelegator = this.getDelegator();
            try {
                this.userLogin = localDelegator.findOne("UserLogin", false, "userLoginId", "NOT_LOGGED_IN");
            } catch (GenericEntityException e) {
                Debug.logError(e, "Error while getting NOT_LOGGED_IN user: ", module);
            }
            if (this.userLogin == null) {
                this.userLogin = localDelegator.makeValue("UserLogin");
                this.userLogin.set("userLoginId", "NOT_LOGGED_IN");
            }
        }
        return this.userLogin;
    }

    @Override
    public void initializeContext(Map<String, ? extends Object> params) {
        this.setDelegator((Delegator) params.get("delegator"));
        this.setDispatcher((LocalDispatcher) params.get("dispatcher"));
        this.setSecurity((AuthorizationManager) params.get("security"));
        this.setUserLogin((GenericValue) params.get("userLogin"));
        this.setLocale((Locale) params.get("locale"));
        this.setTimeZone((TimeZone) params.get("timeZone"));
    }

    @Override
    public void reset() {
        super.reset();
        this.managerList.clear();
        this.delegator = null;
        this.dispatcher = null;
        this.security = null;
        this.userLogin = null;
    }

    protected void resetUserPreferences() {
        if (this.userLogin != null) {
            this.setLocale(userLogin.getString("lastLocale"));
            this.setLocale(userLogin.getString("lastTimeZone"));
        } else {
            this.locale = Locale.getDefault();
            this.timeZone = TimeZone.getDefault();
        }
    }

    @Override
    public void runUnprotected() {
        this.managerList.addLast(getSecurity());
        this.setSecurity(unrestrictedAuthorizationManager);
    }

    @Override
    public void setDelegator(Delegator delegator) {
        if (delegator != null) {
            this.delegator = delegator;
        }
    }

    @Override
    public void setDispatcher(LocalDispatcher dispatcher) {
        if (dispatcher != null) {
            this.dispatcher = dispatcher;
        }
    }

    @Override
    public void setSecurity(AuthorizationManager security) {
        if (security != null) {
            this.security = security;
        }
    }

    @Override
    public void setUserLogin(GenericValue userLogin) {
        if (userLogin != null) {
            this.userLogin = userLogin;
            this.resetUserPreferences();
        }
    }
}
