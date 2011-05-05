package org.ofbiz.jcr.helper;

import java.util.List;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import net.sf.json.JSONArray;

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
     * @throws GenericEntityException
     */
    public String storeNewTextData(String message) throws RepositoryException, GenericEntityException;

    /**
     * Store new text content in the text content tree. Returns the contentId of
     * the database content object
     *
     * @param message
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public String storeNewTextData(String message, String language) throws RepositoryException, GenericEntityException;

    /**
     * Get the String content to the current node, returns the system default language.
     *
     * @return
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public String getTextData() throws PathNotFoundException, RepositoryException;

    /**
     * Get the String content to the current node.
     *
     * @param language
     * @return
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public String getTextData(String language) throws PathNotFoundException, RepositoryException;

    /**
     * Update the text content of a current text node.
     *
     * @param message
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public String updateTextData(String message) throws RepositoryException, GenericEntityException;

    /**
     * Update the text content of a current text node.
     *
     * @param message
     * @param language
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public String updateTextData(String message, String language) throws RepositoryException, GenericEntityException;

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

    /**
     * Returns a list of available languages for the node content.
     *
     * @return
     */
    public List<String> getAvailableLanguages() throws PathNotFoundException, RepositoryException;

	/**
	 * Get the current selected content language.
	 *
	 * @return
	 */
	public String getSelctedLanguage();

    /**
     * Returns the Repository Data (including all Text contents) Tree as JSON Object
     *
     * @return
     * @throws RepositoryException
     */
    public JSONArray getJsonDataTree() throws RepositoryException;

}