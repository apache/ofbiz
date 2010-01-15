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
package org.ofbiz.api.context;

/** A basic implementation of the <code>ExecutionArtifact</code> interface. */
public class GenericExecutionArtifact implements ExecutionArtifact {

    protected final String location;
    protected final String name;

    public GenericExecutionArtifact(String location, String name) {
        this.location = location;
        this.name = name;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void run() throws Throwable {}

    @Override
    public String toString() {
        return "GenericExecutionArtifact: location = " + this.location + ", name = " + this.name;
    }
}
