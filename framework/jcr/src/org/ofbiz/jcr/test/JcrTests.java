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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.RepositoryAccess;
import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFile;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFolder;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitNews;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitResource;
import org.ofbiz.jcr.util.jackrabbit.JcrUtilJackrabbit;
import org.ofbiz.service.testtools.OFBizTestCase;

public class JcrTests extends OFBizTestCase {

    protected GenericValue userLogin = null;
    RepositoryAccess repositoryAccess = null;

    public JcrTests(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "system"));
        repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
    }

    @Override
    protected void tearDown() throws Exception {
        repositoryAccess.closeAccess();
    }

    public void testRepositoryConstructor() throws Exception {
        assertNotNull(repositoryAccess);
    }

    public void testCreateRepositoryNewsNode() throws Exception {
        OfbizRepositoryMappingJackrabbitNews orm = new OfbizRepositoryMappingJackrabbitNews();
        assertNotNull(orm);
        orm.setPath("/news/today");
        orm.setLanguage("en");
        orm.setContent("Hello World");
        orm.setTitle("News of Today");

        repositoryAccess.storeContentObject(orm);
    }

    public void testReadRepositoryNewsNode() throws Exception {
        OfbizRepositoryMappingJackrabbitNews orm = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject("/news/today");
        assertNotNull(orm);

        assertEquals(orm.getContent(), "Hello World");
    }

    public void testUpdateRepositoryNewsNode() throws Exception {
        OfbizRepositoryMappingJackrabbitNews orm = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject("/news/today");
        assertNotNull(orm);

        orm.setContent("Hello Visitors");
        repositoryAccess.updateContentObject(orm);
    }

    public void testRemoveRepositoryNewsNode() throws Exception {
        repositoryAccess.removeContentObject("/news/today");
    }

    /*
     * Test the File upload
     */
    public void testCreateRepositoryFileNode() throws Exception {
        File f = new File("stopofbiz.sh");
        assertTrue(f.exists());

        InputStream file = new FileInputStream(f);

        OfbizRepositoryMappingJackrabbitResource ormResource = new OfbizRepositoryMappingJackrabbitResource();
        ormResource.setData(file);

        OfbizRepositoryMappingJackrabbitFile ormFile = new OfbizRepositoryMappingJackrabbitFile();
        ormFile.setResource(ormResource);
        // have to be relative
        ormFile.setPath("testFile");

        OfbizRepositoryMappingJackrabbitFolder ormFolder = new OfbizRepositoryMappingJackrabbitFolder();
        ormFolder.setPath("/fileHome");
        ormFolder.addChild(ormFile);

        repositoryAccess.storeContentObject(ormFolder);
    }

    public void testRemoveRepositoryFileNode() throws Exception {
        repositoryAccess.removeContentObject("/fileHome");
    }

    public void testListRepositoryNodes() throws Exception {
        assertNotNull(JcrUtilJackrabbit.getRepositoryNodes(userLogin, null));
    }

}