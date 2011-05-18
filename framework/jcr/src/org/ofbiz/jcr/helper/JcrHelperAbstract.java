package org.ofbiz.jcr.helper;

import java.util.List;

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

    /**
     * Get the current version of the node. Only returns the version of the base
     * node not of the current selsected language.
     *
     * @return
     */
    public String getCurrentBaseVersion() {
        return orm.getCurrentBaseVersion();
    }

    /**
     * Get the current version of the node. Returns the version of the currently
     * selected language.
     *
     * @return
     */
    public String getCurrentLanguageVersion() {
        return orm.getCurrentLanguageVersion();
    }

    /**
     * Returns a list of all available Versions for the current selected
     * language.
     *
     * @return
     */
    public List<String> getAllLanguageVersions() {
        return orm.getAllLanguageVersions();
    }
}
