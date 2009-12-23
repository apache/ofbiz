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
package org.ofbiz.entity;

import org.ofbiz.entity.util.EntityListIterator;

/**
 * AccessController interface. This interface extends <code>
 * org.ofbiz.api.authorization.AccessController</code> so that
 * the <code>applyFilters</code> method can be overridden to handle
 * <code>EntityListIterator</code>.
 */
public interface AccessController extends org.ofbiz.api.authorization.AccessController {

    /** Applies permission filters to an <code>EntityListIterator</code>. The
     * returned <code>EntityListIterator</code> is security-aware, so methods
     * that return a <code>GenericValue</code> will return only the
     * values the user has permission to access.
     * 
     * @param list The <code>EntityListIterator</code> to apply filters to
     * @return A security-aware <code>EntityListIterator</code> if filters
     * were specified for the current artifact, or the original
     * <code>EntityListIterator</code> otherwise
     */
	public EntityListIterator applyFilters(EntityListIterator listIterator);

}
