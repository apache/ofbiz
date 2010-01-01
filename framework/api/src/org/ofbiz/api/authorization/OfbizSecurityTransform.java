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

import java.io.IOException;
import java.security.AccessControlException;
import java.security.Permission;
import java.util.Map;

import org.ofbiz.api.context.ThreadContext;
import org.ofbiz.base.util.Debug;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateDirectiveModel;

/**
 * OfbizSecurityTransform - Security-aware Freemarker transform.
 */
public class OfbizSecurityTransform implements TemplateDirectiveModel {

    public final static String module = OfbizSecurityTransform.class.getName();

    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (body == null) {
            return;
        }
        SimpleScalar obj = (SimpleScalar) params.get("artifactId");
        if (obj == null) {
            Debug.logError("artifactId parameter not found, unable to execute transform", module);
            return;
        }
        String artifactId = obj.getAsString();
        obj = (SimpleScalar) params.get("permission");
        if (obj == null) {
            Debug.logError("permission parameter not found, unable to execute transform", module);
            return;
        }
        String permStr = obj.getAsString();
        Permission permission = BasicPermissions.ConversionMap.get(permStr.toUpperCase());
        if (permission == null) {
            Debug.logError("Unknown permission \"" + permStr + "\", unable to execute transform", module);
            return;
        }
        BeanModel contextBean = (BeanModel)env.getVariable("executionContext");
        if (contextBean == null) {
            Debug.logError("ExecutionContext not found, unable to execute transform", module);
            return;
        }
        Template template = env.getTemplate();
        String location = template.getName();
        ThreadContext.pushExecutionArtifact(location, artifactId);
        AccessController accessController = ThreadContext.getAccessController();
        try {
            accessController.checkPermission(permission);
            body.render(env.getOut());
        } catch (AccessControlException e) {}
        ThreadContext.popExecutionArtifact();
    }
}
