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

import org.ofbiz.api.context.ArtifactPath;

/** A node in a permissions tree. */
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

    public abstract void accept(PathNodeVisitor visitor);

    @Override
    public String toString() {
        TreeStringBuilder tsb = new TreeStringBuilder(this);
        return tsb.toString();
    }

    public static class BranchNode extends PathNode {
        protected OFBizPermission permission = null;
        protected SubstitutionNode substitutionNode = null;
        protected WildCardNode wildCardNode = null;

        protected BranchNode(String nodeName) {
            super(nodeName);
        }

        @Override
        public void accept(PathNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class SubstitutionNode extends PathNode {

        protected SubstitutionNode() {
            super(SUBSTITUTION_CHARACTER);
        }

        @Override
        public void accept(PathNodeVisitor visitor) {
            visitor.visit(this);
        }

    }

    public static class WildCardNode extends PathNode {

        protected WildCardNode() {
            super(WILDCARD_CHARACTER);
        }

        @Override
        public void accept(PathNodeVisitor visitor) {
            visitor.visit(this);
        }
    }
}
