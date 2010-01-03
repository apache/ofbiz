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

import java.util.Arrays;
import java.util.Iterator;

/** Artifact path class. */
public class ArtifactPath {

    public static final String ELEMENT_SEPARATOR = "/";
    protected String currentPathElement = null;
    protected Iterator<String> pathIterator;

    public ArtifactPath(String artifactPath) {
        String[] strArray = artifactPath.split(ELEMENT_SEPARATOR);
        this.currentPathElement = strArray[0];
        this.pathIterator = Arrays.asList(strArray).iterator();
    }

    public String getCurrentPathElement() {
        return this.currentPathElement;
    }

    public String getNextPathElement() {
        this.currentPathElement = this.pathIterator.next();
        return this.currentPathElement;
    }

    public boolean hasMoreElements() {
        return this.pathIterator.hasNext();
    }
}
