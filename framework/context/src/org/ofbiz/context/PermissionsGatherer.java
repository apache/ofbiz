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

import org.ofbiz.context.PathNode.BranchNode;
import org.ofbiz.context.PathNode.SubstitutionNode;
import org.ofbiz.context.PathNode.WildCardNode;

public class PermissionsGatherer implements PathNodeVisitor {
    protected ArtifactPath artifactPath;
    protected final PathNode node;
    protected OFBizPermission permission;

    public PermissionsGatherer(PathNode node) {
        this.node = node;
    }

    public void gatherPermissions(ArtifactPath artifactPath, OFBizPermission permission) {
        this.artifactPath = artifactPath;
        this.permission = permission;
        this.node.accept(this);
    }

    protected void getChildNodePermissions(PathNode node, String key) {
        if (node.childNodes != null) {
            PathNode childNode = node.childNodes.get(key.toUpperCase());
            if (childNode != null) {
                childNode.accept(this);
            }
        }
    }

    @Override
    public void visit(BranchNode node) {
        this.permission.accumulatePermissions(node.permission);
        if (this.artifactPath.hasNext()) {
            String key = this.artifactPath.next();
            if (node.substitutionNode != null) {
                this.artifactPath.saveState();
                node.substitutionNode.accept(this);
                this.artifactPath.restoreState();
            }
            if (node.wildCardNode != null) {
                this.artifactPath.saveState();
                node.wildCardNode.accept(this);
                this.artifactPath.restoreState();
            }
            this.getChildNodePermissions(node, key);
        }
    }

    @Override
    public void visit(SubstitutionNode node) {
        if (this.artifactPath.hasNext()) {
            this.getChildNodePermissions(node, this.artifactPath.next());
        }
    }

    @Override
    public void visit(WildCardNode node) {
        if (this.artifactPath.hasNext() && node.childNodes != null) {
            this.artifactPath.next();
            String currentPath = this.artifactPath.getCurrentPath().toUpperCase();
            for (Map.Entry<String, PathNode> entry : node.childNodes.entrySet()) {
                if (currentPath.endsWith(entry.getKey())) {
                    entry.getValue().accept(this);
                    return;
                }
            }
        }
    }

}
