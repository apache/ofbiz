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

import javolution.text.TextBuilder;
import javolution.util.FastList;

import org.ofbiz.context.PathNode.BranchNode;
import org.ofbiz.context.PathNode.SubstitutionNode;
import org.ofbiz.context.PathNode.WildCardNode;

public class TreeStringBuilder implements PathNodeVisitor {
    protected final FastList<PathNode> currentPath = FastList.newInstance();
    protected final PathNode node;
    protected final TextBuilder stringBuilder = TextBuilder.newInstance();

    public TreeStringBuilder(PathNode node) {
        this.node = node;
    }

    protected void buildNodeString(PathNode node) {
        if (node.childNodes != null) {
            Collection<PathNode> childNodes = node.childNodes.values();
            for (PathNode childNode : childNodes) {
                childNode.accept(this);
            }
        }
    }

    @Override
    public String toString() {
        this.node.accept(this);
        return this.stringBuilder.toString();
    }

    @Override
    public void visit(BranchNode node) {
        this.currentPath.addLast(node);
        if (node.permission != null) {
            for (PathNode pathNode: this.currentPath) {
                this.stringBuilder.append("/");
                this.stringBuilder.append(pathNode.nodeName);
            }
            this.stringBuilder.append("[");
            this.stringBuilder.append(node.permission);
            this.stringBuilder.append("]\n");
        }
        if (node.substitutionNode != null) {
            node.substitutionNode.accept(this);
        }
        if (node.wildCardNode != null) {
            node.wildCardNode.accept(this);
        }
        this.buildNodeString(node);
        this.currentPath.removeLast();
    }

    @Override
    public void visit(SubstitutionNode node) {
        this.currentPath.addLast(node);
        this.buildNodeString(node);
        this.currentPath.removeLast();
    }

    @Override
    public void visit(WildCardNode node) {
        this.currentPath.addLast(node);
        this.buildNodeString(node);
        this.currentPath.removeLast();
    }

}
