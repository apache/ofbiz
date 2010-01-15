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
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

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
        String artifactId = toString(params.get("artifactId"));
        if (artifactId == null) {
            Debug.logError("artifactId parameter not found, unable to execute transform", module);
            return;
        }
        String permStr = toString(params.get("permission"));
        if (permStr == null) {
            Debug.logError("permission parameter not found, unable to execute transform", module);
            return;
        }
        Permission permission = BasicPermissions.ConversionMap.get(permStr.toUpperCase());
        if (permission == null) {
            Debug.logError("Unknown permission \"" + permStr + "\", unable to execute transform", module);
            return;
        }
        ThreadContext.pushExecutionArtifact(module, artifactId);
        try {
            ThreadContext.getAccessController().checkPermission(permission);
            body.render(env.getOut());
        } catch (AccessControlException e) {
        } finally {
            ThreadContext.popExecutionArtifact();
        }
    }

    protected static String toString(Object freeMarkerObject) {
        String result = null;
        if (freeMarkerObject != null) {
            try {
                StringModel modelObj = (StringModel) freeMarkerObject;
                result = modelObj.getAsString();
            } catch (Exception e) {
                try {
                    SimpleScalar scalarObj = (SimpleScalar) freeMarkerObject;
                    result = scalarObj.getAsString();
                } catch (Exception e2) {}
            }
            if (result != null) {
                result = result.replace("&#47;", "");
            }
        }
        return result;
    }
}
