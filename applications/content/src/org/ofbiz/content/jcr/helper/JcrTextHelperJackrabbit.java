package org.ofbiz.content.jcr.helper;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.ofbiz.base.util.Debug;
import org.ofbiz.content.jcr.orm.OfbizRepositoryMappingJackrabbit;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.JackrabbitContainer;
import org.ofbiz.jcr.helper.JcrTextHelper;
import org.ofbiz.jcr.helper.JcrTextHelperAbstract;

public class JcrTextHelperJackrabbit extends JcrTextHelperAbstract implements JcrTextHelper {

    public JcrTextHelperJackrabbit(GenericValue userLogin, Delegator delegator, String contentId, String repositoryNode) {
        super.userLogin = userLogin;
        if (userLogin == null || delegator == null) {
            Debug.logError("You pass null for the UserLogin or Delegator, Object can't be created.", module);
            return;
        }

        Session session = null;
        try {
            session = JackrabbitContainer.getUserSession(userLogin);
        }
        catch (RepositoryException re) {
            Debug.logError(re, module);
            return;
        }

        try {
            super.orm = new OfbizRepositoryMappingJackrabbit(delegator, session, contentId, repositoryNode);
        }
        catch (GenericEntityException e) {
            Debug.logError(e, module);
            return;
        }
        catch (RepositoryException e) {
            Debug.logError(e, module);
            return;
        }
    }

    public JcrTextHelperJackrabbit(HttpServletRequest request) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        super.userLogin = userLogin;

        Session session = null;
        try {
            session = JackrabbitContainer.getUserSession(userLogin);
        }
        catch (RepositoryException re) {
            Debug.logError(re, module);
            return;
        }

        String contentId = request.getParameter("contentId");
        String repositoryNode = request.getParameter("repositoryNode");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        try {
            super.orm = new OfbizRepositoryMappingJackrabbit(delegator, session, contentId, repositoryNode);
        }
        catch (GenericEntityException e) {
            Debug.logError(e, module);
            return;
        }
        catch (RepositoryException e) {
            Debug.logError(e, module);
            return;
        }
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.helper.JcrTextHelper#storeNewTextData(java.lang.String)
     */
    @Override
    public String storeNewTextData(String message) throws RepositoryException {
        orm.updateOrStoreTextData(message);

        return orm.getContentId();
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.helper.JcrTextHelper#getStringContent()
     */
    @Override
    public String getTextData() throws PathNotFoundException, RepositoryException {
        return orm.getStringContent();
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.helper.JcrTextHelper#updateTextData(java.lang.String)
     */
    @Override
    public String updateTextData(String message) throws PathNotFoundException, RepositoryException {
        orm.updateOrStoreTextData(message);

        return message;
    }

    /*
     * (non-Javadoc)
     * @see org.ofbiz.jcr.helper.JcrTextHelper#addNewNode(java.lang.String)
     */
    @Override
    public JcrTextHelper addNewNode(String newNode) throws RepositoryException, GenericEntityException {
        if (!newNode.startsWith("/")) {
            newNode = "/" + newNode;
        }

        String newAbsoluteNodePath = orm.getNodePath() + newNode;

        return new JcrTextHelperJackrabbit(userLogin, orm.getDelegator(), null, newAbsoluteNodePath);
    }
}
