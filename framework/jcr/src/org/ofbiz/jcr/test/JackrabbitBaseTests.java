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

import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import javolution.util.FastMap;

import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.JcrRepositoryAccessor;
import org.ofbiz.jcr.access.jackrabbit.JackrabbitRepositoryAccessor;
import org.ofbiz.jcr.loader.JCRFactory;
import org.ofbiz.jcr.loader.JCRFactoryUtil;
import org.ofbiz.jcr.loader.jackrabbit.JCRFactoryImpl;
import org.ofbiz.jcr.util.jackrabbit.JackrabbitUtils;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.service.testtools.OFBizTestCase;

public class JackrabbitBaseTests extends OFBizTestCase {

    private GenericValue userLogin = null;

    public JackrabbitBaseTests(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "system"));

    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testRepositoryConstructor() throws Exception {
        JcrRepositoryAccessor repositoryAccess = new JackrabbitRepositoryAccessor(userLogin, delegator);
        assertNotNull(repositoryAccess);
        repositoryAccess.closeAccess();
    }

    public void testFactoryGetMapper() {
        assertNotNull(JCRFactoryImpl.getMapper());
        assertTrue(JCRFactoryImpl.getMapper() instanceof Mapper);
    }

    public void testFactoryUtilGetJcrFactory() {
        JCRFactory factory = JCRFactoryUtil.getJCRFactory();
        assertNotNull(factory);
        assertTrue((factory instanceof JCRFactoryImpl));
    }

    public void testUtilGetSession() throws RepositoryException {
        Session session = JCRFactoryUtil.getSession(delegator);
        assertNotNull(session);
        assertTrue((session instanceof Session));
        session.logout();
    }

    public void testCreateAbsoluteAndNormalizedNodePath() {
        String result = JackrabbitUtils.createAbsoluteNodePath("foo/baa");

        assertEquals("/foo/baa", result);
    }

    public void testCheckIfNodePathIsAbsoluteAndNormalized() {
        assertFalse(JackrabbitUtils.checkIfNodePathIsAbsolute("foo/baa"));
        assertFalse(JackrabbitUtils.checkIfNodePathIsAbsolute("foo/baa/"));
        assertTrue(JackrabbitUtils.checkIfNodePathIsAbsolute("/foo/baa/"));
        assertTrue(JackrabbitUtils.checkIfNodePathIsAbsolute("/foo/baa"));
    }

    public void testListRepositoryNodes() throws Exception {
        assertNotNull(JackrabbitUtils.getRepositoryNodes(userLogin, null, delegator));
    }

    public void testDefaultLanguage() {
        assertEquals(UtilProperties.getPropertyValue("general", "locale.properties.fallback"), JackrabbitUtils.determindeTheDefaultLanguage());
    }

    public void testSpeedTestService() throws Exception {
        Map<String, Object> context = FastMap.newInstance();
        context.put("maxNodes", new Integer(10));
        context.put("userLogin", dispatcher.getDelegator().findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), true));

        Map<String, Object> serviceResult = this.dispatcher.runSync("determineJackrabbitRepositorySpeed", context);

        if (ServiceUtil.isError(serviceResult)) {
            fail();
        } else {
            assertTrue(true);
        }

    }
}