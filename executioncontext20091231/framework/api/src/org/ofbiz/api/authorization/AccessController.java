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
package org.ofbiz.api.authorization;

import java.security.AccessControlException;
import java.security.Permission;
import java.util.List;
import java.util.ListIterator;

import org.ofbiz.api.context.ArtifactPath;

/** AccessController interface. This interface is intended to
 * separate the permissions-checking logic from the artifacts
 * that use it.
 */
public interface AccessController  {

    /** Returns silently if the user has been granted <code>permission</code>
     * access for the current artifact, throws <code>AccessControlException</code>
     * otherwise.<p>Security-aware artifacts call this
     * method with the desired permission. If access is granted the
     * method returns, otherwise it throws an unchecked exception.
     * Higher level code can catch the exception and handle it accordingly.</p>
     * 
     * @param permission The permission to check
     * @throws AccessControlException
     */
    public void checkPermission(Permission permission) throws AccessControlException;

    /** Returns silently if the user has been granted <code>permission</code>
     * access for the specified artifact, throws <code>AccessControlException</code>
     * otherwise.<p>Client code can call this method when an artifact other
     * than the current one needs to be checked. If access is granted the
     * method returns, otherwise it throws an unchecked exception.
     * Higher level code can catch the exception and handle it accordingly.</p>
     * 
     * @param permission The permission to check
     * @throws AccessControlException
     */
    public void checkPermission(Permission permission, ArtifactPath artifactPath) throws AccessControlException;

    /** Applies permission filters to a <code>List</code>. The
     * returned <code>List</code> is security-aware, so methods
     * that return an <code>Object</code> will return only the
     * objects the user has permission to access.
     * 
     * @param list The <code>List</code> to apply filters to
     * @return A security-aware <code>List</code> if filters
     * were specified for the current artifact, or the original
     * <code>List</code> otherwise
     */
    public <E> List<E> applyFilters(List<E> list);

    /** Applies permission filters to a <code>ListIterator</code>. The
     * returned <code>ListIterator</code> is security-aware, so methods
     * that return an <code>Object</code> will return only the
     * objects the user has permission to access.
     * 
     * @param list The <code>ListIterator</code> to apply filters to
     * @return A security-aware <code>ListIterator</code> if filters
     * were specified for the current artifact, or the original
     * <code>ListIterator</code> otherwise
     */
    public <E> ListIterator<E> applyFilters(ListIterator<E> list);

}
