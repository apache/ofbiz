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

public class JackrabbitFilesTests extends OFBizTestCase {

    private GenericValue userLogin = null;

    public JackrabbitFilesTests(String name) {
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
    public void testAccessorFileTree() throws RepositoryException {
        JcrRepositoryAccessor accessor = new JackrabbitRepositoryAccessor(userLogin);

        JSONArray array = accessor.getJsonFileTree();
        assertEquals(0, array.size()); // should be 0 because there are no
                                       // entries in the repository yet
        accessor.closeAccess();
    }*/

    /*
     * Test the File upload
     */ /*
    public void testCreateRepositoryFileNode() throws Exception {
        File f = new File("stopofbiz.sh");
        File f2 = new File("README");
        assertTrue(f.exists() && f2.exists());

        InputStream file = new FileInputStream(f);

        JcrFileHelper helper = new JackrabbitFileHelper(userLogin);
        helper.storeContentInRepository(file, f.getName(), "/fileHome");

        assertNotNull(helper.getRepositoryContent("/fileHome/" + f.getName()));

        // add a second file to the same folder
        file = new FileInputStream(f2);

        helper.storeContentInRepository(file, f2.getName(), "/fileHome");
        assertNotNull(helper.getRepositoryContent("/fileHome/" + f2.getName()));

        // remove all files in folder
        helper.removeContentObject("/fileHome");

        helper.closeContentSession();
    }*/
}