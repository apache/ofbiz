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

import java.util.GregorianCalendar;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import net.sf.json.JSONArray;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.ContentWriter;
import org.ofbiz.jcr.access.JcrRepositoryAccessor;
import org.ofbiz.jcr.access.jackrabbit.ContentWriterJackrabbit;
import org.ofbiz.jcr.access.jackrabbit.JackrabbitRepositoryAccessor;
import org.ofbiz.jcr.api.JcrDataHelper;
import org.ofbiz.jcr.api.jackrabbit.JackrabbitArticleHelper;
import org.ofbiz.jcr.loader.JCRFactoryUtil;
import org.ofbiz.jcr.loader.jackrabbit.JCRFactoryImpl;
import org.ofbiz.jcr.orm.jackrabbit.data.JackrabbitArticle;
import org.ofbiz.service.testtools.OFBizTestCase;

public class JackrabbitDataTests extends OFBizTestCase {

    private GenericValue userLogin = null;

    public JackrabbitDataTests(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), true);

    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testAccessorConstructor() throws RepositoryException {
        JcrRepositoryAccessor accessor = new JackrabbitRepositoryAccessor(userLogin, delegator);

        assertNotNull(accessor);
        assertEquals("/", accessor.getSession().getRootNode().getPath());

        accessor.closeAccess();
    }

    public void testAccessorDataTree() throws RepositoryException {
        JcrRepositoryAccessor accessor = new JackrabbitRepositoryAccessor(userLogin, delegator);

        JSONArray array = accessor.getJsonDataTree();
        // should be 0 because there are no entries in the repository yet
        assertEquals(0, array.size());

        accessor.closeAccess();
    }

    public void testAccessorNodeExist() throws RepositoryException {
        JcrRepositoryAccessor accessor = new JackrabbitRepositoryAccessor(userLogin, delegator);
        assertTrue(accessor.checkIfNodeExist("/"));
        assertFalse(accessor.checkIfNodeExist("/test"));

        accessor.closeAccess();
    }

    public void testWriterConsturctor() throws RepositoryException {

        Session session = JCRFactoryUtil.getSession(delegator);
        ObjectContentManager ocm = new ObjectContentManagerImpl(session, JCRFactoryImpl.getMapper());
        ContentWriter writer = new ContentWriterJackrabbit(ocm);

        assertNotNull(writer);

        ocm.logout();
    }

    public void testCrudArticleNode() throws Exception {
        JcrDataHelper helper = new JackrabbitArticleHelper(userLogin, delegator);
        helper.storeContentInRepository("news/article", "en", "News Of Today", "Hello World", new GregorianCalendar());

        JackrabbitArticle content = helper.readContentFromRepository("news/article");
        assertEquals("Hello World", content.getContent());

        content.setContent("New World!");

        helper.updateContentInRepository(content);

        JackrabbitArticle updatedContent = helper.readContentFromRepository("news/article");
        assertEquals("New World!", updatedContent.getContent());

        helper.removeContentObject("news");

        helper.closeContentSession();
    }

    public void testVersionning() throws Exception {
        JcrDataHelper helper = new JackrabbitArticleHelper(userLogin, delegator);
        helper.storeContentInRepository("news/versionArticle", "en", "News Of Today", "Hello World", new GregorianCalendar());

        JackrabbitArticle content = helper.readContentFromRepository("news/versionArticle");
        assertEquals("1.0", content.getVersion());

        content.setTitle("New Title");
        helper.updateContentInRepository(content);

        content = helper.readContentFromRepository("news/versionArticle");
        assertEquals("1.1", content.getVersion());

        helper.removeContentObject("news");

        helper.closeContentSession();
    }

    public void testLanguageDetermination() throws Exception {
        JcrDataHelper helper = new JackrabbitArticleHelper(userLogin, delegator);

        helper.storeContentInRepository("news/tomorrow", "en", "The news for tomorrow.", "Content.", new GregorianCalendar());
        helper.storeContentInRepository("superhero", "de", "Batman", "The best superhero!", new GregorianCalendar());

        assertEquals("en", helper.readContentFromRepository("/news/tomorrow", "").getLanguage());
        assertEquals("en", helper.readContentFromRepository("/news/tomorrow/en", "").getLanguage());
        assertEquals("en", helper.readContentFromRepository("/news/tomorrow", "de").getLanguage());
        assertEquals("en", helper.readContentFromRepository("/news/tomorrow", "en").getLanguage());

        assertEquals("de", helper.readContentFromRepository("/superhero", "de").getLanguage());
        assertEquals("de", helper.readContentFromRepository("/superhero", "").getLanguage());
        assertEquals("de", helper.readContentFromRepository("/superhero", "fr").getLanguage());

        helper.removeContentObject("/superhero");
        helper.removeContentObject("/news");
        helper.closeContentSession();
    }

    public void testLanguageDeterminationExpectedPathNotFoundException() throws Exception {
        JcrDataHelper helper = new JackrabbitArticleHelper(userLogin, delegator);
        helper.storeContentInRepository("news/tomorrow", "en", "The news for tomorrow.", "Content.", new GregorianCalendar());

        try {
            helper.readContentFromRepository("/news/tomorrow/fr", "").getLanguage();
            // if no exception is thrown, the test should fail
            fail("Expected a PathNotFoundException.");
        } catch (PathNotFoundException pnf) {
            // check if the right exception is thrown (in jUnit 4 this could be
            // replaced by annotations)
            assertTrue("A PathNotFoundException is catched as expected.", true);
        }

        helper.removeContentObject("/news");
        helper.closeContentSession();
    }
}