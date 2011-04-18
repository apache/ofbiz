package org.ofbiz.jcr.helper;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

public interface JcrTextHelper {

	/**
	 * Store new text content in the text content tree. Returns the contentId of
	 * the database content object
	 *
	 * @param message
	 * @return
	 * @throws RepositoryException
	 */
	public String storeNewTextData(String message) throws RepositoryException;

	/**
	 * Get the String content to the current node.
	 *
	 * @return
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 */
	public String getTextData() throws PathNotFoundException, RepositoryException;

	/**
	 * Update the text content of a current text node.
	 *
	 * @param message
	 * @return
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 */
	public String updateTextData(String message) throws PathNotFoundException, RepositoryException;

	/**
	 * Add a child node to the current node. The new JcrTextHelper object will
	 * be returned.
	 *
	 * @param newNode
	 * @return
	 * @throws RepositoryException
	 * @throws GenericEntityException
	 */
	public JcrTextHelper addNewNode(String newNode) throws RepositoryException, GenericEntityException;

	/**
	 * close the current jcr session.
	 */
	public void closeSession();

	/**
	 * Remove the current repository node. Removes also all child nodes.
	 *
	 * @throws GenericEntityException
	 * @throws RepositoryException
	 */
	public void removeRepositoryNode() throws GenericEntityException, RepositoryException;

	/**
	 * Returns the database content object.
	 *
	 * @return
	 */
	public GenericValue getContentObject();

	/**
	 * Returns the name of the node.
	 *
	 * @return
	 */
	public String getNodeName();

}