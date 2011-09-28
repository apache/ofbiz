package org.ofbiz.content.jcr.helper;

import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;

public abstract class AbstractJcrHelper {

    protected static RepositoryAccessJackrabbit access = null;

    /**
     * This will close the connection to the content repository and make sure
     * that all changes a stored successfully.
     */
    public void closeContentSession() {
        access.closeAccess();
        access = null;
    }
}
