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

/** ExecutionArtifact interface. Artifacts in the program's execution
 * path (services, screen widgets, form widgets, entities) should implement
 * this interface.
 */
public interface ExecutionArtifact {

    /**
     * Returns the location of this artifact.
     *
     * @return Location of this artifact
     */
    public String getLocation();

    /**
     * Returns the name of this artifact.
     *
     * @return Name of this artifact
     */
    public String getName();
}
