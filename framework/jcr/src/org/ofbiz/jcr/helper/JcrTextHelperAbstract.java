package org.ofbiz.jcr.helper;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.ofbiz.entity.GenericEntityException;

public abstract class JcrTextHelperAbstract extends JcrHelperAbstract {

    public abstract String storeNewTextData(String message) throws RepositoryException;

    public abstract String getTextData() throws PathNotFoundException, RepositoryException;

    public abstract String updateTextData(String message) throws PathNotFoundException, RepositoryException;

    public abstract JcrTextHelper addNewNode(String newNode) throws RepositoryException, GenericEntityException;

}
