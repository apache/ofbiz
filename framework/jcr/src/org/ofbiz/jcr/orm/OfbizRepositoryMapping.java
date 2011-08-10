package org.ofbiz.jcr.orm;


public interface OfbizRepositoryMapping {
    /**
     * Return the Node Path.
     * @return
     */
    String getPath();

    /**
     * Set the Node Path.
     * @param path
     */
    void setPath(String path);
}