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

import org.ofbiz.context.PathNode.BranchNode;

public class PermissionTreeBuilder extends TreeBuilder {

    protected OFBizPermission permission;

    public PermissionTreeBuilder(PathNode node) {
        super(node);
    }

    public void build(ArtifactPath artifactPath, OFBizPermission permission) {
        this.permission = permission;
        super.build(artifactPath);
    }

    @Override
    public void visit(BranchNode node) {
        if (!this.artifactPath.hasNext()) {
            if (node.permission == null) {
                node.permission = this.permission;
            } else {
                node.permission.accumulatePermissions(this.permission);
            }
            return;
        }
        super.visit(node);
    }
}
