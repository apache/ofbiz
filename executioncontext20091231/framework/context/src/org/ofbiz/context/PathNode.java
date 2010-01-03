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

    public static final String PLACEHOLDER_CHARACTER = "?";
    protected String nodeName = null;
    protected OFBizPermission permission = null;
    protected Map<String, PathNode> childNodes = null;
    protected boolean handlePlaceholder = false;

    public PathNode() {}

    protected PathNode(String nodeName) {
        this.nodeName = nodeName;
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

    public void getPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
        permission.accumulatePermissions(this.permission);
        if (artifactPath.hasMoreElements() && this.childNodes != null) {
            String nextNodeName = artifactPath.getNextPathElement();
            if (this.handlePlaceholder) {
                if (!artifactPath.hasMoreElements()) {
                    return;
                }
                nextNodeName = artifactPath.getNextPathElement();
            }
            PathNode node = this.childNodes.get(nextNodeName.toUpperCase());
            if (node != null) {
                node.getPermissions(artifactPath, permission);
            }
        }
    }

    public void setPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
        if (this.nodeName == null) {
            this.nodeName = artifactPath.getCurrentPathElement();
        }
        if (!artifactPath.hasMoreElements()) {
            if (this.permission == null) {
                this.permission = permission;
            } else {
                this.permission.accumulatePermissions(permission);
            }
            return;
        }
        String nextNodeName = artifactPath.getNextPathElement();
        if (PLACEHOLDER_CHARACTER.equals(nextNodeName)) {
            this.handlePlaceholder = true;
            nextNodeName = artifactPath.getNextPathElement();
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

    @Override
    public String toString() {
        FastList<PathNode> currentPath = FastList.newInstance();
        StringBuilder result = new StringBuilder();
        buildNodeString(currentPath, result);
        return result.toString();
    }
}
