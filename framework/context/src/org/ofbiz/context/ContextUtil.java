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

import static org.ofbiz.api.authorization.BasicPermissions.Access;

import java.security.AccessControlException;
import java.util.List;

import javolution.util.FastList;

import org.ofbiz.api.authorization.AccessController;
import org.ofbiz.api.context.ArtifactPath;
import org.ofbiz.api.context.ThreadContext;
import org.ofbiz.base.component.ComponentConfig;
import org.ofbiz.base.component.ComponentConfig.WebappInfo;

/**
 * ExecutionContext utility methods.
 *
 */
public class ContextUtil {

    public static List<WebappInfo> getAppBarWebInfos(String serverName, String menuName) {
        List<WebappInfo> webInfos = ComponentConfig.getAppBarWebInfos(serverName, menuName);
        String [] pathArray = {ArtifactPath.PATH_ROOT_NODE_NAME, null};
        ArtifactPath artifactPath = new ArtifactPath(pathArray);
        AccessController accessController = ThreadContext.getAccessController();
        List<WebappInfo> resultList = FastList.newInstance();
        for (WebappInfo webAppInfo : webInfos) {
            pathArray[1] = webAppInfo.getContextRoot().replace("/", "");
            artifactPath.saveState();
            try {
                accessController.checkPermission(Access, artifactPath);
                resultList.add(webAppInfo);
            } catch (AccessControlException e) {
                // This exception is expected - do nothing
            }
            artifactPath.restoreState();
        }
        return resultList;
    }

}
