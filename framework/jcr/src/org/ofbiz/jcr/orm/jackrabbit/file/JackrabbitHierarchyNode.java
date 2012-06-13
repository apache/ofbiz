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

import java.util.Calendar;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;
import org.ofbiz.jcr.util.jackrabbit.JackrabbitUtils;

@Node(jcrType = "nt:hierarchyNode", jcrMixinTypes = "mix:versionable")
public class JackrabbitHierarchyNode implements OfbizRepositoryMapping {
    @Field(path = true, id = true, jcrProtected = true)
    protected String path;
    private String version;
    @Field(jcrName = "jcr:created")
    private Calendar creationDate;

    public String getPath() {
        return path;
    }

    public void setPath(String nodePath) {
        // check if the node path is an absolute path
        this.path = JackrabbitUtils.createAbsoluteNodePath(nodePath);
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

}
