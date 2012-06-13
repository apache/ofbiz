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
package org.ofbiz.jcr.access.jackrabbit;

import javax.jcr.nodetype.NodeType;

public class ConstantsJackrabbit {
    // JCR Variables
    public static String MESSAGE = "jcr:message";
    public static String FILE = NodeType.NT_FILE;
    public static String FOLDER = NodeType.NT_FOLDER;
    public static String RESOURCE = NodeType.NT_RESOURCE;
    public static String DATA = "jcr:data";
    public static String UNSTRUCTURED = NodeType.NT_UNSTRUCTURED;
    public static String MIMETYPE = "jcr:mimeType";
    public static String MIXIN_LANGUAGE = "mix:language";
    public static String MIXIN_VERSIONING = "mix:versionable";
    public static String ROOTVERSION = "jcr:rootVersion";

    //
    public static String ROOTPATH = "/";
    public static String FILEROOT = ROOTPATH + "fileHome";
    public static String NODEPATHDELIMITER = "/";
}
