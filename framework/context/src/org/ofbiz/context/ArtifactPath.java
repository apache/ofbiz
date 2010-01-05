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

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Artifact path class. */
public class ArtifactPath implements Cloneable, Iterator<String> {

    public static final ArtifactPath PATH_ROOT = new ArtifactPath("ofbiz");
    public static final String ELEMENT_SEPARATOR = "/";

    protected int currentIndex = 0;
    protected final String[] pathElementArray;

    public ArtifactPath(String artifactPath) {
        this.pathElementArray = artifactPath.split(ELEMENT_SEPARATOR);
    }
    
    public ArtifactPath(String[] pathElementArray) {
        this.pathElementArray = pathElementArray;
    }

    @Override
    public ArtifactPath clone() {
        ArtifactPath newPath = new ArtifactPath(this.pathElementArray);
        newPath.currentIndex = this.currentIndex;
        return newPath;
    }

    public String getCurrentPath() {
        StringBuilder sb = new StringBuilder();
        for (int i = this.currentIndex; i < this.pathElementArray.length; i++) {
            if (i != this.currentIndex) {
                sb.append(ELEMENT_SEPARATOR);
            }
            sb.append(this.pathElementArray[i]);
        }
        return sb.toString();
    }

    public String getCurrentPathElement() {
        return this.pathElementArray[this.currentIndex];
    }

    @Override
    public boolean hasNext() {
        return this.currentIndex + 1 < this.pathElementArray.length;
    }

    @Override
    public String next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return this.pathElementArray[++this.currentIndex];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
