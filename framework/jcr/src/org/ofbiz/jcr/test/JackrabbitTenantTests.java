package org.ofbiz.jcr.test;

import java.util.GregorianCalendar;

import javax.jcr.PathNotFoundException;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.api.JcrDataHelper;
import org.ofbiz.jcr.api.jackrabbit.JackrabbitArticleHelper;
import org.ofbiz.service.testtools.OFBizTestCase;

public class JackrabbitTenantTests extends OFBizTestCase {

    private GenericValue userLogin = null;
    private Delegator tenantDelegator = null;
    private GenericValue tenantUserLogin = null;

    public JackrabbitTenantTests(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "admin"));

        // tenant delegator
        String delegatorName = delegator.getDelegatorBaseName() + "#DEMO1";
        tenantDelegator = DelegatorFactory.getDelegator(delegatorName);
        assertNotNull(tenantDelegator);

        tenantUserLogin = tenantDelegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "admin"));
        assertNotNull(tenantUserLogin);

    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testTenantAccess() throws Exception {
        // Store data with normal user
        JcrDataHelper helper = new JackrabbitArticleHelper(userLogin, delegator);
        helper.storeContentInRepository("news/article", "en", "News Of Today", "Hello World", new GregorianCalendar());
        helper.closeContentSession();

        // check if content is available for tenant user (shouldn't)
        JcrDataHelper tenantHelper = new JackrabbitArticleHelper(tenantUserLogin, tenantDelegator);
        try {
            tenantHelper.readContentFromRepository("news/article");
            fail("Expected a PathNotFoundException.");
        } catch (PathNotFoundException pe) {
            assertTrue("A PathNotFoundException is catched as expected.", true);
        }

        tenantHelper.closeContentSession();
    }

    public void testTenantAccess_2() throws Exception {
        // check if content is available for tenant user (shouldn't)
        JcrDataHelper tenantHelper = new JackrabbitArticleHelper(tenantUserLogin, tenantDelegator);
        tenantHelper.storeContentInRepository("tenant/article", "de", "tenant", "Foo", new GregorianCalendar());
        tenantHelper.closeContentSession();

        // Store data with normal user
        JcrDataHelper helper = new JackrabbitArticleHelper(userLogin, delegator);
        try {
            helper.readContentFromRepository("tenant/article");
            fail("Expected a PathNotFoundException.");
        } catch (PathNotFoundException pe) {
            assertTrue("A PathNotFoundException is catched as expected.", true);
        }
        helper.closeContentSession();
    }

}