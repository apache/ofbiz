package org.ofbiz.jcr.api;

import org.ofbiz.jcr.access.jackrabbit.JackrabbitRepositoryAccessor;

public abstract class AbstractJcrHelper {

    protected static JackrabbitRepositoryAccessor access = null;

    /**
     * This will close the connection to the content repository and make sure
     * that all changes a stored successfully.
     */
    public void closeContentSession() {
        access.closeAccess();
        access = null;
    }

    /**
     * Remove the passed node from the content repository.
     *
     * @param contentPath
     */
    public void removeContentObject(String contentPath) {
        access.removeContentObject(contentPath);
    }
}
