package org.ofbiz.content.jcr.helper;

import java.util.List;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

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
		} catch (RepositoryException re) {
			Debug.logError(re, module);
			return;
		}

		try {
			super.orm = new OfbizRepositoryMappingJackrabbit(delegator, session, contentId, repositoryNode, OfbizRepositoryMappingJackrabbit.NODE_TYPE.DATA);
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			return;
		} catch (RepositoryException e) {
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
		} catch (RepositoryException re) {
			Debug.logError(re, module);
			return;
		}

		String contentId = request.getParameter("contentId");
		String repositoryNode = request.getParameter("repositoryNode");

		Delegator delegator = (Delegator) request.getAttribute("delegator");
		try {
			super.orm = new OfbizRepositoryMappingJackrabbit(delegator, session, contentId, repositoryNode, OfbizRepositoryMappingJackrabbit.NODE_TYPE.DATA);
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
			return;
		} catch (RepositoryException e) {
			Debug.logError(e, module);
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.ofbiz.jcr.helper.JcrTextHelper#storeNewTextData(java.lang.String)
	 */
	@Override
	public String storeNewTextData(String message) throws RepositoryException, GenericEntityException {
		return storeNewTextData(message, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.ofbiz.jcr.helper.JcrTextHelper#storeNewTextData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String storeNewTextData(String message, String language) throws RepositoryException, GenericEntityException {
		orm.updateOrStoreTextData(message, language);

		return orm.getContentId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.ofbiz.jcr.helper.JcrTextHelper#getStringContent()
	 */
	@Override
	public String getTextData() throws PathNotFoundException, RepositoryException {
		return orm.getStringContent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.ofbiz.jcr.helper.JcrTextHelper#getStringContent(java.lang.String)
	 */
	@Override
	public String getTextData(String language) throws PathNotFoundException, RepositoryException {
		return orm.getStringContent(language);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.ofbiz.jcr.helper.JcrTextHelper#updateTextData(java.lang.String)
	 */
	@Override
	public String updateTextData(String message) throws RepositoryException, GenericEntityException {
		orm.updateOrStoreTextData(message);

		return message;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.ofbiz.jcr.helper.JcrTextHelper#updateTextData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String updateTextData(String message, String language) throws RepositoryException, GenericEntityException {
		orm.updateOrStoreTextData(message, language);
		return message;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.ofbiz.jcr.helper.JcrDataHelper#getJsonDataTree()
	 */
	@Override
	public JSONArray getJsonDataTree() throws RepositoryException {
		return orm.getJsonDataTree();
	}

	/*
	 * (non-Javadoc)
	 *
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.ofbiz.jcr.helper.JcrTextHelper#getAvailableLanguages();
	 */
	@Override
	public List<String> getAvailableLanguages() throws PathNotFoundException, RepositoryException {
		return orm.getAvailableLanguages();
	}

}
