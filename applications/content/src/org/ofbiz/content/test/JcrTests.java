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
package org.ofbiz.content.test;

import org.ofbiz.service.testtools.OFBizTestCase;

public class JcrTests extends OFBizTestCase {

    public JcrTests(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

// TODO create new tests :-)
/*
    protected GenericValue userLogin = null;
    protected JcrTextHelper jh = null;

    public JcrTests(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "system"));
        jh = new JcrTextHelperJackrabbit(userLogin, delegator, null, "/unitTestNode");
    }

    @Override
    protected void tearDown() throws Exception {
        jh.closeSession();
    }

    public void testRepositoryConstructor() throws Exception {
        assertNotNull(jh);
        assertNotNull(jh.getContentObject());
    }

    public void testCreateRepositoryNode() throws Exception {
        GenericValue content = jh.getContentObject();
        assertNotNull(content);
        // check if the content object is correctly stored in the database
        GenericValue contentCheck = delegator.findOne("Content", true, UtilMisc.toMap("contentId", content.getString("contentId")));

        assertNotNull(contentCheck);
    }

    public void testAddTextContent() throws Exception {
        jh.storeNewTextData("Hello World!");

        assertEquals("Hello World!", jh.getTextData());
    }

    public void testGetSelectedLanguage() throws PathNotFoundException, RepositoryException {
        assertEquals(UtilProperties.getPropertyValue("general", "locale.properties.fallback"), jh.getSelctedLanguage());
        assertEquals("Hello World!", jh.getTextData(UtilProperties.getPropertyValue("general", "locale.properties.fallback")));
    }

    public void testCheckVersioning() {
        // should 1.1 because it was created (1.0) and a text content was
        // appened (1.1)
        assertEquals("1.1", jh.getCurrentBaseVersion());
        // should be 1.0 because the text node is only created and not updated
        assertEquals("1.0", jh.getCurrentLanguageVersion());
    }

    public void testCreateNewChildNodes() throws Exception {
        JcrTextHelper subnode = jh.addNewNode("subNode");
        assertNotNull(subnode);

        String parentContentId = jh.getContentObject().getString("contentId");
        String childContentId = subnode.getContentObject().getString("contentId");

        List<GenericValue> list = delegator.findByAndCache("ContentAssoc", UtilMisc.toMap("contentId", parentContentId, "contentIdTo", childContentId));
        assertNotNull(list);
        if (UtilValidate.isEmpty(list) || list.size() != 1) {
            assertTrue(false);
        }
        subnode.closeSession();

        JcrTextHelper subsubnode = jh.addNewNode("/subNodeTwo/subSubNodeOne");
        assertNotNull(subsubnode);
        parentContentId = jh.getContentObject().getString("contentId");
        childContentId = subnode.getContentObject().getString("contentId");

        list = delegator.findByAndCache("ContentAssoc", UtilMisc.toMap("contentId", parentContentId, "contentIdTo", childContentId));
        assertNotNull(list);
        if (UtilValidate.isEmpty(list) || list.size() != 1) {
            assertTrue(false);
        }
        subsubnode.closeSession();

    }

    public void testUploadFileToRepository() throws Exception {
        File f = new File("stopofbiz.sh");
        assertTrue(f.exists());

        InputStream file = new FileInputStream(f);

        JcrFileHelper uf = new JcrFileHelperJackrabbit(userLogin, delegator, null, "/unitTestFolder");

        GenericValue newFile = uf.uploadFileData(file, f.getName());
        assertNotNull(newFile);

        InputStream rs = uf.getFileContent(f.getName());
        assertNotNull(rs);

        uf.removeRepositoryNode();

        uf.closeSession();
    }

    public void testListRepositoryNodes() throws Exception {
        assertNotNull(JackrabbitWorker.getRepositoryNodes(userLogin, null));
    }

    public void testCleanRepositoryStructure() throws Exception {
        JackrabbitWorker.cleanJcrRepository(delegator, userLogin);
    }
    */
}