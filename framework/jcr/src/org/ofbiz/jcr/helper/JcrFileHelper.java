package org.ofbiz.jcr.helper;

import java.io.InputStream;

import javax.jcr.RepositoryException;

import net.sf.json.JSONArray;

import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

public interface JcrFileHelper {

    /**
     * Upload a file to the repository file tree.
     *
     * @param file
     * @param fileName
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public GenericValue uploadFileData(byte[] file, String fileName) throws RepositoryException, GenericEntityException;

    /**
     * Upload a file to the repository file tree.
     *
     * @param file
     * @param fileName
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public GenericValue uploadFileData(InputStream file, String fileName) throws RepositoryException, GenericEntityException;

    /**
     * Return the file passed file from the current folder node.
     *
     * @return
     * @throws RepositoryException
     */
    public InputStream getFileContent(String fileName) throws RepositoryException;

    /**
     * Return the file from the current file node.
     *
     * @return
     * @throws RepositoryException
     */
    public InputStream getFileContent() throws RepositoryException;

    /**
     * Add a child node to the current node. All Files are stored under a file
     * root directory which will add automatically. The new JcrFileHelper object
     * will be returned.
     *
     * @param newNode
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public JcrFileHelper addNewNode(String newNode) throws RepositoryException, GenericEntityException;

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
     * Returns the Repository File Tree as JSON Object
     *
     * @return
     * @throws RepositoryException
     */
    public JSONArray getJsonFileTree() throws RepositoryException;

    /**
     * Returns the name of the node.
     *
     * @return
     */
    public String getNodeName();

    /**
     * Returns the content type of the file. An empty String will be returned if
     * the node is not a file node or no mimeType exists.
     *
     * @return
     * @throws RepositoryException
     */
    public String getFileMimeType() throws RepositoryException;

	/**
	 * Get the current selected content language.
	 *
	 * @return
	 */
	public String getSelctedLanguage();


}