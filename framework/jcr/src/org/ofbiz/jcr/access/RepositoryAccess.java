package org.ofbiz.jcr.access;

import javax.jcr.ItemExistsException;
import javax.jcr.RepositoryException;

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
     * Returns a tree of all content nodes (except folders and files) in the repository.
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
}
