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
package org.ofbiz.jcr.api;

import java.io.InputStream;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.ofbiz.jcr.orm.jackrabbit.file.JackrabbitHierarchyNode;

public interface JcrFileHelper extends JcrHelper{

    /**
     * Returns a content file object from the repository. Throws an Exception
     * when the read content type is not an article content type.
     *
     * @param contentPath
     * @return
     * @throws ClassCastException
     * @throws PathNotFoundException
     */
    public abstract JackrabbitHierarchyNode getRepositoryContent(String contentPath) throws ClassCastException, PathNotFoundException;

    /**
     * Returns a content file object in the passed version from the repository.
     * Throws an Exception when the read content type is not an article content
     * type.
     *
     * @param contentPath
     * @return
     * @throws ClassCastException
     * @throws PathNotFoundException
     */
    public abstract JackrabbitHierarchyNode getRepositoryContent(String contentPath, String version) throws ClassCastException, PathNotFoundException;

    /**
     * Stores a new file content object in the repository.
     *
     * @param fileData
     * @param fileName
     * @param folderPath
     * @param mimeType
     * @throws ObjectContentManagerException
     * @throws RepositoryException
     */
    public abstract void storeContentInRepository(byte[] fileData, String fileName, String folderPath) throws ObjectContentManagerException, RepositoryException;

    /**
     * Stores a new file content object in the repository.
     *
     * @param fileData
     * @param fileName
     * @param folderPath
     * @param mimeType
     * @throws ObjectContentManagerException
     * @throws RepositoryException
     */
    public abstract void storeContentInRepository(InputStream fileData, String fileName, String folderPath) throws ObjectContentManagerException, RepositoryException;

    /**
     * Returns TRUE if the current content is a file content (Type:
     * OfbizRepositoryMappingJackrabbitFile)
     *
     * @return
     */
    public abstract boolean isFileContent();

    /**
     * Returns TRUE if the current content is a folder content (Type:
     * OfbizRepositoryMappingJackrabbitFolder)
     *
     * @return
     */
    public abstract boolean isFolderContent();

}