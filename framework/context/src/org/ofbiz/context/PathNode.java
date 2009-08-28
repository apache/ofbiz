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

import java.util.Map;

import javolution.util.FastMap;

public class PathNode {

    protected Map<String, PathNode> childNodes = null;
    protected OFBizPermission permission = null;

    public void setPermissions(String artifactPath, OFBizPermission permission) {
        int pos = artifactPath.indexOf("/");
        if (pos == -1) {
            if (this.permission == null) {
                this.permission = permission;
            } else {
                this.permission.accumulatePermissions(permission);
            }
            return;
        }
        String key = artifactPath.substring(0, pos - 1).toUpperCase();
        if (this.childNodes == null) {
            this.childNodes = FastMap.newInstance();
        }
        PathNode node = this.childNodes.get(key);
        if (node == null) {
            node = new PathNode();
            this.childNodes.put(key, node);
        }
        node.setPermissions(artifactPath.substring(pos + 1), permission);
    }

    public void getPermissions(String artifactPath, OFBizPermission permission) {
        permission.accumulatePermissions(this.permission);
        int pos = artifactPath.indexOf("/");
        if (pos == -1) {
            return;
        }
        String key = artifactPath.substring(0, pos - 1).toUpperCase();
        if (this.childNodes != null) {
            PathNode node = this.childNodes.get(key);
            if (node != null) {
                node.getPermissions(artifactPath, permission);
            }
        }
    }
}
