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
package org.ofbiz.jcr.access;

import javax.jcr.ItemExistsException;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public interface ContentWriter {

    /**
     * Stores the OfbizRepositoryMapping Class in the content repository.
     *
     * @param orm
     * @throws ObjectContentManagerException
     * @throws ItemExistsException
     */
    public void storeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException, ItemExistsException;

    /**
     * Update the OfbizRepositoryMapping Class in the content repository.
     *
     * @param orm
     * @throws ObjectContentManagerException
     */
    public void updateContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException;

    /**
     * Remove the passed node from the content repository. The node path should be absolute.
     *
     * @param nodePath
     * @throws ObjectContentManagerException
     */
    public void removeContentObject(String nodePath) throws ObjectContentManagerException;


}
