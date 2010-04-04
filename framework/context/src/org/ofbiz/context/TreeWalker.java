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

import org.ofbiz.base.context.ArtifactPath;
import org.ofbiz.context.PathNode.BranchNode;
import org.ofbiz.context.PathNode.SubstitutionNode;
import org.ofbiz.context.PathNode.WildCardNode;

public class TreeWalker implements PathNodeVisitor {
    protected ArtifactPath artifactPath;
    protected final PathNode node;

    public TreeWalker(PathNode node) {
        this.node = node;
    }

    @Override
    public void visit(BranchNode node) {
        if (this.artifactPath.hasNext()) {
            String key = this.artifactPath.next();
            if (node.wildCardNode != null) {
                this.artifactPath.saveState();
                node.wildCardNode.accept(this);
                this.artifactPath.restoreState();
            }
            if (node.substitutionNode != null) {
                this.artifactPath.saveState();
                node.substitutionNode.accept(this);
                this.artifactPath.restoreState();
            }
            this.visitChildNode(node, key);
        }
    }

    @Override
    public void visit(SubstitutionNode node) {
        if (this.artifactPath.hasNext()) {
            this.visitChildNode(node, this.artifactPath.next());
        }
    }

    @Override
    public void visit(WildCardNode node) {
        if (node.childNodes != null) {
            while (this.artifactPath.hasNext()) {
                String key = this.artifactPath.next().toUpperCase();
                PathNode childNode = node.childNodes.get(key);
                if (childNode != null) {
                    childNode.accept(this);
                    return;
                }
            }
        }
    }

    protected void visitChildNode(PathNode node, String key) {
        if (node.childNodes != null) {
            PathNode childNode = node.childNodes.get(key.toUpperCase());
            if (childNode != null) {
                childNode.accept(this);
            }
        }
    }

    public void walkTree(ArtifactPath artifactPath) {
        this.artifactPath = artifactPath;
        this.node.accept(this);
    }

}
