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

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Bean;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.ofbiz.jcr.access.jackrabbit.ConstantsJackrabbit;

@Node(jcrType = "nt:file", extend = JackrabbitHierarchyNode.class)
public class JackrabbitFile extends JackrabbitHierarchyNode {

    @Bean(jcrName = "jcr:content")
    private JackrabbitResource resource;

    public JackrabbitResource getResource() {
        return resource;
    }

    public void setResource(JackrabbitResource resource) {
        this.resource = resource;
    }

    public void setPath(String nodePath) {
        // check that the path don't end with a /
        if (nodePath.endsWith(ConstantsJackrabbit.ROOTPATH)) {
            nodePath = nodePath.substring(0, nodePath.indexOf(ConstantsJackrabbit.NODEPATHDELIMITER));
        }

        // check that it is a relative path
        if (nodePath.indexOf(ConstantsJackrabbit.NODEPATHDELIMITER) != -1) {
            nodePath = nodePath.substring(nodePath.lastIndexOf(ConstantsJackrabbit.NODEPATHDELIMITER) + 1);
        }

        super.path = nodePath;
    }
}
