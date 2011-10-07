package org.ofbiz.jcr.api;

import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;

/**
 * This Helper class encapsulate the jcr content access. It provide all
 * attributes and operations which are necessary to work with the content
 * repository.
 *
 * The concrete implementations covers the different content use case related
 * workflows. I.E. Different behavior for File/Folder or Text content.
 *
 * The Helper classes should be build on top of the generic JCR implementation
 * in the Framework.
 *
 */
public class JcrContentHelper extends AbstractJcrHelper{

    /**
     * Create a default content helper object.
     *
     * @param userLogin
     */
    public JcrContentHelper (GenericValue userLogin) {
        access = new RepositoryAccessJackrabbit(userLogin);
    }
}
