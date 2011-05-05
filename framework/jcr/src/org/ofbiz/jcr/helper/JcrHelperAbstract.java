package org.ofbiz.jcr.helper;

import javax.jcr.RepositoryException;

import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

/**
 *
 *
 */
public abstract class JcrHelperAbstract {

	public static final String module = JcrHelperAbstract.class.getName();

	protected OfbizRepositoryMapping orm = null;
	protected GenericValue userLogin = null;

	/**
	 * close the current jcr session.
	 */
	public void closeSession() {
		orm.closeSession();
	}

	/**
	 * Remove the current repository node. Removes also all child nodes.
	 *
	 * @throws GenericEntityException
	 * @throws RepositoryException
	 */
	public void removeRepositoryNode() throws GenericEntityException, RepositoryException {
		orm.removeRepositoryNode();
	}

	/**
	 * Returns the database content object.
	 *
	 * @return
	 */
	public GenericValue getContentObject() {
		return orm.getContentObject();
	}

	/**
	 * Returns the name of the node.
	 *
	 * @return
	 */
	public String getNodeName() {
		return orm.getNodeName();
	}

	protected Boolean checkNodeWritePermission(GenericValue userLogin) {
		// TODO have to be specified
		return Boolean.TRUE;
	}

	protected Boolean checkNodeReadPermission(GenericValue userLogin) {
		// TODO have to be specified
		return Boolean.TRUE;
	}

	/**
	 * Get the current selected content language.
	 *
	 * @return
	 */
	public String getSelctedLanguage() {
		return orm.getSelctedLanguage();
	}
}
