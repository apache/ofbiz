package org.ofbiz.jcr.orm;

import java.io.InputStream;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

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
     * @throws GenericEntityException
     */
    public Version updateOrStoreTextData(String message) throws RepositoryException, GenericEntityException;

    /**
     * Updates only the node text property data
     *
     * @param message
     * @param language
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public Version updateOrStoreTextData(String message, String language) throws RepositoryException, GenericEntityException;

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
     * will be returned.
     *
     * @return
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public String getStringContent() throws PathNotFoundException, RepositoryException;

    /**
     * Returns only the String Content of a node, if none exists an empty String
     * will be returned.
     *
     * @param language
     * @return
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public String getStringContent(String language) throws PathNotFoundException, RepositoryException;

    /**
     * Returns the text content in a defined language and a defined version
     *
     * @param language
     * @param version
     * @return
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public String getStringContent(String language, String version) throws PathNotFoundException, RepositoryException;

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
     * Upload and store a file in the repository und a givven language.
     *
     * @param file
     * @param fileName
     * @param language
     * @param description
     * @throws PathNotFoundException
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    void uploadFileData(InputStream file, String fileName, String language, String description) throws PathNotFoundException, RepositoryException, GenericEntityException;

    /**
     * Return the file stream from the current node object.
     *
     * @param fileName
     * @return
     * @throws RepositoryException
     */
    public InputStream getFileContent(String fileName) throws RepositoryException;

    /**
     * Returns the content type of the file. An empty String will be returned if
     * the node is not a file node or no mimeType exists.
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
     * Returns the repository data (including all text content data) tree as
     * Json Object.
     *
     * @return
     * @throws RepositoryException
     */
    public JSONArray getJsonDataTree() throws RepositoryException;

    /**
     * Get the file stream from the current node.
     *
     * @return
     * @throws RepositoryException
     */
    public InputStream getFileContent() throws RepositoryException;

    /**
     * Returns a List of available languages of the current node content
     *
     * @return
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public List<String> getAvailableLanguages() throws PathNotFoundException, RepositoryException;

    /**
     * Get the current selected content language.
     *
     * @return
     */
    public String getSelctedLanguage();

    /**
     * Returns the current version of the node. If '0' is returned the node is
     * not versinoed.
     *
     * @return
     */
    public String getCurrentBaseVersion();

    /**
     * Get the current version of the node. Returns the version of the
     * currentliy selected language.
     *
     * @return
     */
    public String getCurrentLanguageVersion();

    /**
     * Returns a list of all versions to the current selected version.
     *
     * @return
     */
    public List<String> getAllLanguageVersions();

}