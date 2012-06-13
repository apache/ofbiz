/*
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
 */
package org.ofbiz.jcr.orm.jackrabbit.file;

import java.util.ArrayList;
import java.util.List;

import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.NTCollectionConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrType = "nt:folder", extend = JackrabbitHierarchyNode.class)
public class JackrabbitFolder extends JackrabbitHierarchyNode {
    @Collection(autoUpdate = true, jcrSameNameSiblings = false, elementClassName = JackrabbitHierarchyNode.class, collectionConverter = NTCollectionConverterImpl.class)
    private List<JackrabbitHierarchyNode> children;

    public List<JackrabbitHierarchyNode> getChildren() {
        return children;
    }

    public void setChildren(List<JackrabbitHierarchyNode> children) {
        this.children = children;
    }

    public void addChild(JackrabbitHierarchyNode node) {
        if (children == null) {
            children = new ArrayList<JackrabbitHierarchyNode>();
        }
        children.add(node);
    }

}
