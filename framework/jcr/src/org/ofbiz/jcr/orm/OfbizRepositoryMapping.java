package org.ofbiz.jcr.orm;

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import net.sf.json.JSONArray;

import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

public interface OfbizRepositoryMapping {

	/**
	 * Object delegator reference
	 */
	public Delegator getDelegator();

	/**
	 * Close the current repository session should be used when the operation
	 * with this object are finished.
	 */
	public void closeSession();

	/**
	 * Updates only the node text property data
	 *
	 * @param message
	 * @return
	 * @throws RepositoryException
	 */
	public void updateOrStoreTextData(String message) throws RepositoryException;

	/**
	 * Returns the related Content Object
	 *
	 * @return
	 */
	public GenericValue getContentObject();

	/**
	 * Returns the related Repository Node
	 *
	 * @return
	 */
	public Node getNode();

	/**
	 * Returns the contentId from the related content object. If the content
	 * object is null, an empty string will be retunred.
	 *
	 * @return
	 */
	public String getContentId();

	/**
	 * Returns the absolute path of the node.
	 *
	 * @return
	 */
	public String getNodePath();

	/**
	 * Returns the name of the node.
	 *
	 * @return
	 */
	public String getNodeName();

	/**
	 * Remove a repository Node and the related database entry.
	 *
	 * @throws RepositoryException
	 * @throws GenericEntityException
	 */
	public void removeRepositoryNode() throws RepositoryException, GenericEntityException;

	/**
	 * Returns only the String Content of a node, if none exists an empty String
	 * will be returned
	 *
	 * @return
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 */
	public String getStringContent() throws PathNotFoundException, RepositoryException;

	/**
	 * Upload and store a file in the repository
	 *
	 * @param description
	 * @param file
	 * @return
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 * @throws GenericEntityException
	 */
	public void uploadFileData(InputStream file, String fileName) throws PathNotFoundException, RepositoryException, GenericEntityException;

	/**
	 * Return the file stream from the current node object.
	 *
	 * @param fileName
	 * @return
	 * @throws RepositoryException
	 */
	public InputStream getFileContent(String fileName) throws RepositoryException;

	/**
	 * Returns the content type of the file. An empty String will be returned if the node
	 * is not a file node or no mimeType exists.
	 *
	 * @return
	 * @throws RepositoryException
	 */
	public String getFileMimeType() throws RepositoryException;

	/**
	 * Returns the repository file tree as Json Object.
	 *
	 * @return
	 * @throws RepositoryException
	 */
	public JSONArray getJsonFileTree() throws RepositoryException;

	/**
	 * Get the file stream from the current node.
	 *
	 * @return
	 * @throws RepositoryException
	 */
	public InputStream getFileContent() throws RepositoryException;
}