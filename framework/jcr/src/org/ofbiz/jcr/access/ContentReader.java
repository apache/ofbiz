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

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;

import net.sf.json.JSONArray;

import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public interface ContentReader {

    /**
     * Return an OfbizRepositoryMapping Object from the JCR Repository. The node
     * path have to be an absolute path.
     *
     * @param nodePath
     * @return
     * @throws PathNotFoundException
     */
    OfbizRepositoryMapping getContentObject(String nodePath) throws PathNotFoundException;

    /**
     * Return an OfbizRepositoryMapping Object in the specified language and
     * version from the JCR Repository. The Method checks if the requested
     * version for this node exist. If not the latest version of the node will
     * be returned. The node path have to be an absolute path.
     *
     * @param nodePath
     * @param language
     * @param version
     * @return
     * @throws PathNotFoundException
     */
    OfbizRepositoryMapping getContentObject(String nodePath, String version) throws PathNotFoundException;

    /**
     * Returns a tree of all content nodes (except folders and files) in the
     * repository.
     *
     * @return
     * @throws RepositoryException
     */
    JSONArray getJsonDataTree() throws RepositoryException;

    /**
     * Returns a tree of all folder/file nodes in the repository.
     *
     * @return
     * @throws RepositoryException
     */
    JSONArray getJsonFileTree() throws RepositoryException;

    /**
     * Query for Data in the JCR Repository using the SQL2 or JQOM Query
     * language. Returns the Query Result.
     *
     * @param query
     *            either a SQL2 or JQOM statement.
     * @return
     */
    QueryResult queryRepositoryData(String query) throws RepositoryException;
}
