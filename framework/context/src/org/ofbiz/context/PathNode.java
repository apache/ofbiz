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

/** A node in a permissions tree.
 */
public abstract class PathNode {

    public static final String SUBSTITUTION_CHARACTER = "?";
    public static final String WILDCARD_CHARACTER = "*";

    public static PathNode getInstance(ArtifactPath artifactPath) {
        String currentPathElement = artifactPath.getCurrentPathElement().intern();
        if (SUBSTITUTION_CHARACTER.equals(currentPathElement)) {
            return new SubstitutionNode();
        }
        if (WILDCARD_CHARACTER.equals(currentPathElement)) {
            return new WildCardNode();
        }
        return new BranchNode(currentPathElement);
    }

    protected Map<String, PathNode> childNodes = null;
    protected String nodeName = null;

    protected PathNode(String nodeName) {
        this.nodeName = nodeName;
    }

    protected void buildNodeString(FastList<PathNode> currentPath, StringBuilder result) {
        if (this.childNodes != null) {
            Collection<PathNode> childNodes = this.childNodes.values();
            for (PathNode childNode : childNodes) {
                childNode.buildNodeString(currentPath, result);
            }
        }
    }

    protected void getChildNodePermissions(String key, ArtifactPath artifactPath, OFBizPermission permission) {
        if (this.childNodes != null) {
            PathNode node = this.childNodes.get(key.toUpperCase());
            if (node != null) {
                node.getPermissions(artifactPath, permission);
            }
        }
    }

    public abstract void getPermissions(ArtifactPath artifactPath, OFBizPermission permission);

    protected void setChildNodePermissions(String key, ArtifactPath artifactPath, OFBizPermission permission) {
        if (this.childNodes == null) {
            this.childNodes = FastMap.newInstance();
        }
        key = key.toUpperCase();
        PathNode node = this.childNodes.get(key);
        if (node == null) {
            node = PathNode.getInstance(artifactPath);
            this.childNodes.put(key, node);
        }
        node.setPermissions(artifactPath, permission);
    }

    public abstract void setPermissions(ArtifactPath artifactPath, OFBizPermission permission);

    @Override
    public String toString() {
        FastList<PathNode> currentPath = FastList.newInstance();
        StringBuilder result = new StringBuilder();
        buildNodeString(currentPath, result);
        return result.toString();
    }

    protected static class BranchNode extends PathNode {
        protected OFBizPermission permission = null;
        protected SubstitutionNode substitutionNode = null;
        protected WildCardNode wildCardNode = null;

        protected BranchNode(String nodeName) {
            super(nodeName);
        }

        @Override
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
            if (this.substitutionNode != null) {
                this.substitutionNode.buildNodeString(currentPath, result);
            }
            if (this.wildCardNode != null) {
                this.wildCardNode.buildNodeString(currentPath, result);
            }
            super.buildNodeString(currentPath, result);
            currentPath.removeLast();
        }

        @Override
        public void getPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
            permission.accumulatePermissions(this.permission);
            if (artifactPath.hasNext()) {
                String key = artifactPath.next();
                if (this.substitutionNode != null) {
                    this.substitutionNode.getPermissions(artifactPath.clone(), permission);
                }
                if (this.wildCardNode != null) {
                    this.wildCardNode.getPermissions(artifactPath.clone(), permission);
                }
                this.getChildNodePermissions(key, artifactPath, permission);
            }
        }

        @Override
        public void setPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
            if (!artifactPath.hasNext()) {
                if (this.permission == null) {
                    this.permission = permission;
                } else {
                    this.permission.accumulatePermissions(permission);
                }
                return;
            }
            String key = artifactPath.next();
            if (SUBSTITUTION_CHARACTER.equals(key)) {
                if (this.substitutionNode == null) {
                    this.substitutionNode = new SubstitutionNode();
                }
                this.substitutionNode.setPermissions(artifactPath, permission);
                return;
            }
            if (WILDCARD_CHARACTER.equals(key)) {
                if (this.wildCardNode == null) {
                    this.wildCardNode = new WildCardNode();
                }
                this.wildCardNode.setPermissions(artifactPath, permission);
                return;
            }
            this.setChildNodePermissions(key, artifactPath, permission);
        }
    }

    protected static class SubstitutionNode extends PathNode {

        protected SubstitutionNode() {
            super(SUBSTITUTION_CHARACTER);
        }

        @Override
        protected void buildNodeString(FastList<PathNode> currentPath, StringBuilder result) {
            currentPath.add(this);
            super.buildNodeString(currentPath, result);
            currentPath.removeLast();
        }

        @Override
        public void getPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
            if (artifactPath.hasNext()) {
                this.getChildNodePermissions(artifactPath.next(), artifactPath, permission);
            }
        }

        @Override
        public void setPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
            if (artifactPath.hasNext()) {
                this.setChildNodePermissions(artifactPath.next(), artifactPath, permission);
            }
        }
    }

    protected static class WildCardNode extends PathNode {

        protected WildCardNode() {
            super(WILDCARD_CHARACTER);
        }

        @Override
        protected void buildNodeString(FastList<PathNode> currentPath, StringBuilder result) {
            currentPath.add(this);
            super.buildNodeString(currentPath, result);
            currentPath.removeLast();
        }

        public void getPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
            if (artifactPath.hasNext() && this.childNodes != null) {
                artifactPath.next();
                String currentPath = artifactPath.getCurrentPath().toUpperCase();
                for (Map.Entry<String, PathNode> entry : this.childNodes.entrySet()) {
                    if (currentPath.endsWith(entry.getKey())) {
                        entry.getValue().getPermissions(artifactPath, permission);
                        return;
                    }
                }
            }
        }

        @Override
        public void setPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
            if (artifactPath.hasNext()) {
                artifactPath.next();
                this.setChildNodePermissions(artifactPath.getCurrentPath(), artifactPath, permission);
            }
        }
    }
}
