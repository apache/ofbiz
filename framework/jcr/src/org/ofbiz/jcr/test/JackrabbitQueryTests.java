/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */
package org.ofbiz.jcr.test;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.testtools.OFBizTestCase;

public class JackrabbitQueryTests extends OFBizTestCase {

    private GenericValue userLogin = null;

    public JackrabbitQueryTests(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "system"));

    }

    @Override
    protected void tearDown() throws Exception {
    }

    /*
    public void testAccessorQuery() throws RepositoryException {
        JcrRepositoryAccessor accessor = new JackrabbitRepositoryAccessor(userLogin);
        QueryResult results = accessor.queryForRepositoryData("SELECT * FROM [rep:root]");

        assertNotNull(results);
        assertEquals(1, results.getNodes().getSize());

        accessor.closeAccess();
    }*/

    /*
    public void testQuery() throws Exception {
        JcrDataHelper helper = new JackrabbitArticleHelper(userLogin);

        helper.storeContentInRepository("/query", "en", "query", "query test", new GregorianCalendar());

        List<Map<String, String>> queryResult = helper.queryData("SELECT * FROM [nt:unstructured]");

        assertEquals(3, queryResult.size()); // the list should contain 3 result
                                             // sets

        assertEquals("/", queryResult.get(0).get("path"));
        assertEquals("/query", queryResult.get(1).get("path"));
        assertEquals("/query/en", queryResult.get(2).get("path"));

        helper.removeContentObject("query");

        helper.closeContentSession();

    }*/
}