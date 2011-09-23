package org.ofbiz.jcr.access;

import javax.jcr.RepositoryException;

import net.sf.json.JSONArray;

import org.ofbiz.jcr.orm.OfbizRepositoryMapping;


public interface ContentReader {

    /**
     * Return an OfbizRepositoryMapping Object from the JCR Repository.
     *
     * @param nodePath
     * @return
     */
    OfbizRepositoryMapping getContentObject(String nodePath);

    /**
     * Return an OfbizRepositoryMapping Object in the specified language and version from the JCR Repository.
     *
     * @param nodePath
     * @param language
     * @param version
     * @return
     */
    OfbizRepositoryMapping getContentObject(String nodePath, String version);

    /**
     * Returns a tree of all content nodes (except folders and files) in the repository.
     *
     * @return
     * @throws RepositoryException
     */
    JSONArray getJsonDataTree() throws RepositoryException;

    /**
     * Returns a tree of all folder/file nodes in the repository.
     *
     * @return
     * @throws RepositoryException
     */
    JSONArray getJsonFileTree() throws RepositoryException;
}
