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

import java.util.Collection;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

/** Implements a node in a permissions tree.
 */
public class PathNode {

    protected String nodeName = null;
    protected OFBizPermission permission = null;
    protected Map<String, PathNode> childNodes = null;

    public PathNode() {}

    protected PathNode(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setPermissions(String artifactPath, OFBizPermission permission) {
        int pos = artifactPath.indexOf("/");
        if (pos == -1) {
            if (this.permission == null) {
                this.permission = permission;
            } else {
                this.permission.accumulatePermissions(permission);
            }
            if (this.nodeName == null) {
                this.nodeName = artifactPath;
            }
            return;
        }
        String thisNodeName = artifactPath.substring(0, pos);
        if (this.nodeName == null) {
            this.nodeName = thisNodeName;
        }
        artifactPath = artifactPath.substring(pos + 1);
        String nextNodeName = artifactPath;
        pos = artifactPath.indexOf("/");
        if (pos != -1) {
            nextNodeName = artifactPath.substring(0, pos);
        }
        String key = nextNodeName.toUpperCase();
        if (this.childNodes == null) {
            this.childNodes = FastMap.newInstance();
        }
        PathNode node = this.childNodes.get(key);
        if (node == null) {
            node = new PathNode(nextNodeName);
            this.childNodes.put(key, node);
        }
        node.setPermissions(artifactPath, permission);
    }

    public void getPermissions(String artifactPath, OFBizPermission permission) {
        permission.accumulatePermissions(this.permission);
        int pos = artifactPath.indexOf("/");
        if (pos != -1 && this.childNodes != null) {
            artifactPath = artifactPath.substring(pos + 1);
            String nextNodeName = artifactPath;
            pos = artifactPath.indexOf("/");
            if (pos != -1) {
                nextNodeName = artifactPath.substring(0, pos);
            }
            PathNode node = this.childNodes.get(nextNodeName.toUpperCase());
            if (node != null) {
                node.getPermissions(artifactPath, permission);
            }
        }
    }

    @Override
    public String toString() {
        FastList<PathNode> currentPath = FastList.newInstance();
        StringBuilder result = new StringBuilder();
        buildNodeString(currentPath, result);
        return result.toString();
    }

    protected void buildNodeString(FastList<PathNode> currentPath, StringBuilder result) {
        currentPath.add(this);
        if (this.permission != null) {
            for (PathNode pathNode: currentPath) {
                result.append("/");
                result.append(pathNode.nodeName);
            }
            result.append("[");
            result.append(this.permission);
            result.append("]");
            result.append("\n");
        }
        if (this.childNodes != null) {
            Collection<PathNode> childNodes = this.childNodes.values();
            for (PathNode childNode : childNodes) {
                childNode.buildNodeString(currentPath, result);
            }
        }
        currentPath.removeLast();
    }
}
