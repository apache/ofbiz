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
import java.util.GregorianCalendar;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.RepositoryAccess;
import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;
import org.ofbiz.jcr.api.JcrArticleHelper;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFile;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFolder;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitNews;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitResource;
import org.ofbiz.jcr.util.jackrabbit.JcrUtilJackrabbit;
import org.ofbiz.service.ServiceUtil;
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
        // Create New Object
        // path, language, title, publication date, content string
        OfbizRepositoryMappingJackrabbitNews orm = new OfbizRepositoryMappingJackrabbitNews("/news/today", "en", "News of Today", new GregorianCalendar(), "Hello World");
        assertNotNull(orm);

        repositoryAccess.storeContentObject(orm);
    }

    public void testReadRepositoryNewsNode() throws Exception {
        OfbizRepositoryMappingJackrabbitNews orm = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject("/news/today/en");
        assertNotNull(orm);

        assertEquals(orm.getContent(), "Hello World");
    }

    public void testUpdateRepositoryNewsNode() throws Exception {
        OfbizRepositoryMappingJackrabbitNews orm = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject("/news/today/en");
        assertNotNull(orm);

        orm.setContent("Hello Visitors");
        repositoryAccess.updateContentObject(orm);
    }

    public void testVersionning() throws Exception {
        assertEquals("1.1", repositoryAccess.getBaseVersion("/news/today/en"));

        OfbizRepositoryMappingJackrabbitNews orm = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject("/news/today/en");
        orm.setContent("May the force be with you!");
        repositoryAccess.updateContentObject(orm);

        orm = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject("/news/today/en");
        assertEquals("1.2", repositoryAccess.getBaseVersion("/news/today/en"));
    }

    public void testLanguageDetermination() throws Exception {
        JcrArticleHelper helper = new JcrArticleHelper(userLogin);

        helper.storeContentInRepository("news/tomorrow", "en", "The news for tomorrow.", "Content.", new GregorianCalendar());
        helper.storeContentInRepository("superhero", "de", "Batman", "The best superhero!", new GregorianCalendar());

        assertEquals("en", helper.readContentFromRepository("/news/tomorrow", "").getLanguage());
        assertEquals("en", helper.readContentFromRepository("/news/tomorrow", "de").getLanguage());
        assertEquals("en", helper.readContentFromRepository("/news/tomorrow", "en").getLanguage());

        assertEquals("de", helper.readContentFromRepository("/superhero", "de").getLanguage());
        assertEquals("de", helper.readContentFromRepository("/superhero", "").getLanguage());
        assertEquals("de", helper.readContentFromRepository("/superhero", "fr").getLanguage());

        helper.removeContentObject("/superhero");
        helper.closeContentSession();
    }

    public void testRemoveRepositoryNewsNode() throws Exception {
        repositoryAccess.removeContentObject("/news/today");
    }

    /*
     * Test the File upload
     */
    public void testCreateRepositoryFileNode() throws Exception {
        File f = new File("stopofbiz.sh");
        File f2 = new File("README");
        assertTrue(f.exists() && f2.exists());

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

    /*
     * Test the File upload - Add a second file to the same folder
     */
    public void testCreateRepositoryFileNode_2() throws Exception {
        File f = new File("README");
        assertTrue(f.exists());

        InputStream file = new FileInputStream(f);

        OfbizRepositoryMappingJackrabbitResource ormResource = new OfbizRepositoryMappingJackrabbitResource();
        ormResource.setData(file);

        OfbizRepositoryMappingJackrabbitFile ormFile = new OfbizRepositoryMappingJackrabbitFile();
        ormFile.setResource(ormResource);
        // have to be relative
        ormFile.setPath(f.getName());

        OfbizRepositoryMappingJackrabbitFolder ormFolder = (OfbizRepositoryMappingJackrabbitFolder) repositoryAccess.getContentObject("/fileHome");
        ormFolder.addChild(ormFile);

        // When we add a file to an existing folder we have to use the update
        // method - this is something the FileHelper Api is doing for you.
        repositoryAccess.updateContentObject(ormFolder);
    }

    public void testRemoveRepositoryFileNode() throws Exception {
        repositoryAccess.removeContentObject("/fileHome");
    }

    public void testSpeedTestService() throws Exception {
        Map<String, Object> context = FastMap.newInstance();
        context.put("maxNodes", new Integer(10));
        context.put("userLogin", dispatcher.getDelegator().findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "system")));

        Map<String, Object> serviceResult = this.dispatcher.runSync("determineJackrabbitRepositorySpeed", context);

        if (ServiceUtil.isError(serviceResult)) {
            assertFalse(true);
        } else {
            assertTrue(true);
        }

    }

    public void testListRepositoryNodes() throws Exception {
        assertNotNull(JcrUtilJackrabbit.getRepositoryNodes(userLogin, null));
    }

}