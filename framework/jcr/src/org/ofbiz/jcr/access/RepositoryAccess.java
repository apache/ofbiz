package org.ofbiz.jcr.access;

import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;

import net.sf.json.JSONArray;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public interface RepositoryAccess {

    /**
     * Close the current repository session should be used when the operation
     * with this object are finished.
     */
    public void closeAccess();

    /**
     * Return an OfbizRepositoryMapping Object from the content repository.
     *
     * @param nodePath
     * @return
     */
    OfbizRepositoryMapping getContentObject(String nodePath);

    /**
     * Return an OfbizRepositoryMapping Object in the specified version from the
     * JCR Repository.
     *
     * @param nodePath
     * @param language
     * @param version
     * @return
     */
    OfbizRepositoryMapping getContentObject(String nodePath, String version);

    /**
     * Stores the OfbizRepositoryMapping Class in the content repository.
     *
     * @param orm
     * @throws ObjectContentManagerException
     * @throws ItemExistsException
     */
    void storeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException, ItemExistsException;

    /**
     * Update the passed content object.
     *
     * @param orm
     * @throws ObjectContentManagerException
     */
    public void updateContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException;

    /**
     * Remove the passed node from the content repository
     *
     * @param nodePath
     * @throws ObjectContentManagerException
     */
    public void removeContentObject(String nodePath) throws ObjectContentManagerException;

    /**
     * Remove the passed node from the content repository
     *
     * @param orm
     * @throws ObjectContentManagerException
     */
    public void removeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException;

    /**
     * Returns a tree of all content nodes (except folders and files) in the
     * repository.
     *
     * @return
     * @throws RepositoryException
     */
    JSONArray getJsonDataTree() throws RepositoryException;

    /**
     * Returns a tree of all file/folder nodes in the repository.
     *
     * @return
     * @throws RepositoryException
     */
    JSONArray getJsonFileTree() throws RepositoryException;

    /**
     * Returns a list of the available versions.
     *
     * @param nodePath
     * @return
     */
    public List<String> getVersionList(String nodePath);

    /**
     * Returns the last checked in version.
     *
     * @param nodePath
     * @return
     */
    public String getBaseVersion(String nodePath);

    /**
     * Returns the first checked in version.
     *
     * @param nodePath
     * @return
     */
    public String getRootVersion(String nodePath);

    /**
     * Query for Data in the JCR Repository using the SQL2 or JQOM Query
     * language. Returns the Query result.
     *
     * @param query
     *            either a SQL2 or JQOM statement.
     * @return
     */
    public QueryResult queryForRepositoryData(String query) throws RepositoryException;
}
