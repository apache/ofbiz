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
package org.ofbiz.base.context;

import java.util.Map;

/** ParametersArtifact interface. This interface extends
 * <code>ExecutionArtifact</code> and adds the ability to
 * contain a parameter <code>Map</code>.<p>The purpose of this
 * class is to provide a way for the <code>ExecutionContext</code>
 * to keep track of what parameters are the most recent. For
 * example: Service A calls Service B. Service A has one set of
 * parameters, and Service B has another set of parameters. During
 * program execution, Service A creates a <code>ParametersArtifact</code>
 * instance that contains the service's parameters, then pushes that
 * instance on the <code>ExecutionContext</code> stack. When Service B
 * is called, it does the same thing. When framework code needs to
 * access the current parameters, it calls
 * <code>ExecutionContext.getParameters()</code>. The <code>ExecutionContext</code>
 * will search its stack for the first <code>ParametersArtifact</code> instance -
 * starting at the top of the stack, and return that instance.</p>
 */
public interface ParametersArtifact extends ExecutionArtifact {

    /**
     * Returns the parameters associated with this artifact.
     *
     * @return The parameters associated with this artifact
     */
    Map<String, ? extends Object> getParameters();

}
