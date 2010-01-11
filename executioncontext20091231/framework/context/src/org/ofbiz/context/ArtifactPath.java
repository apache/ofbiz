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

import javolution.text.TextBuilder;
import javolution.util.FastList;

import org.ofbiz.api.context.AbstractExecutionContext;

/** Artifact path class. */
public class ArtifactPath implements Iterator<String> {

    public static final ArtifactPath PATH_ROOT = new ArtifactPath(AbstractExecutionContext.PATH_ROOT_NODE_NAME);

    protected int currentIndex = 0;
    protected final String[] pathElementArray;
    protected FastList<Integer> stack = null;
    protected final TextBuilder stringBuilder = TextBuilder.newInstance();

    public ArtifactPath(String artifactPath) {
        this.pathElementArray = artifactPath.split(AbstractExecutionContext.PATH_ELEMENT_SEPARATOR);
    }
    
    public ArtifactPath(String[] pathElementArray) {
        this.pathElementArray = pathElementArray;
    }

    public String getCurrentPath() {
        if (this.pathElementArray.length == 1 || !this.hasNext()) {
            return this.pathElementArray[this.currentIndex];
        }
        this.stringBuilder.clear();
        for (int i = this.currentIndex; i < this.pathElementArray.length; i++) {
            if (i != this.currentIndex) {
                stringBuilder.append(AbstractExecutionContext.PATH_ELEMENT_SEPARATOR);
            }
            stringBuilder.append(this.pathElementArray[i]);
        }
        return stringBuilder.toString();
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

    public void saveState() {
        if (this.stack == null) {
            this.stack = FastList.newInstance();
        }
        this.stack.addLast(this.currentIndex);
    }

    public void restoreState() {
        if (this.stack != null && !this.stack.isEmpty()) {
            this.currentIndex = this.stack.removeLast();
        }
    }
}
