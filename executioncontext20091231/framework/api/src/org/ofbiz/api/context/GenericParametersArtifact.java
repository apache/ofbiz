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

import java.util.Map;

/** A basic implementation of the ParametersArtifact interface. */
public class GenericParametersArtifact extends GenericExecutionArtifact implements ParametersArtifact {

    protected final Map<String, ? extends Object> parameters;

    public GenericParametersArtifact(String location, String name, Map<String, ? extends Object> parameters) {
        super(location, name);
        this.parameters = parameters;
    }

    public GenericParametersArtifact(ExecutionArtifact artifact, Map<String, ? extends Object> parameters) {
        super(artifact.getLocation(), artifact.getName());
        this.parameters = parameters;
    }

    public Map<String, ? extends Object> getParameters() {
        return this.parameters;
    }

    @Override
    public String toString() {
        return "GenericParametersArtifact: location = " + this.location + ", name = " + this.name;
    }
}
