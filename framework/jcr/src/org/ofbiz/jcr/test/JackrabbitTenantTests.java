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

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.api.JcrDataHelper;
import org.ofbiz.jcr.api.jackrabbit.JackrabbitArticleHelper;
import org.ofbiz.jcr.orm.jackrabbit.data.JackrabbitArticle;
import org.ofbiz.service.testtools.OFBizTestCase;

public class JackrabbitTenantTests extends OFBizTestCase {

	private GenericValue userLogin = null;

	public JackrabbitTenantTests(String name) {
		super(name);
	}

	@Override
	public void setUp() throws Exception {
		userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), true);
	}

	public void testTenant() throws Exception {

		JcrDataHelper helper = new JackrabbitArticleHelper(userLogin, delegator);
		helper.storeContentInRepository("news/article", "en", "News Of Today", "Hello World", new GregorianCalendar());

		JackrabbitArticle content = helper.readContentFromRepository("news/article");
		assertEquals("Hello World", content.getContent());
		helper.closeContentSession();

		String delegatorName = delegator.getDelegatorBaseName() + "#DEMO1";
		Delegator tenantDelegator = DelegatorFactory.getDelegator(delegatorName);

		JcrDataHelper tenantHelper = new JackrabbitArticleHelper(userLogin, tenantDelegator);
		try {
			tenantHelper.readContentFromRepository("news/article");
			fail("PathNotFoundException expected");
		} catch (PathNotFoundException e) {
			assertTrue("Caught a PathNotFoundExcpetion as expected.", true);
		}
		tenantHelper.closeContentSession();
	}
}
