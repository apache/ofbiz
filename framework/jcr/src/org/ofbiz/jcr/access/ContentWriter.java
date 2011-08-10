package org.ofbiz.jcr.access;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public interface ContentWriter {

    /**
     * Stores the OfbizRepositoryMapping Class in the content repository.
     *
     * @param orm
     * @throws ObjectContentManagerException
     */
    public void storeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException;

    /**
     * Update the OfbizRepositoryMapping Class in the content repository.
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


}
