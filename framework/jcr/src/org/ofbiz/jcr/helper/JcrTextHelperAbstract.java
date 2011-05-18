package org.ofbiz.jcr.helper;

import java.util.List;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.ofbiz.entity.GenericEntityException;

public abstract class JcrTextHelperAbstract extends JcrHelperAbstract {

    public abstract String storeNewTextData(String message) throws RepositoryException, GenericEntityException;

    public abstract String storeNewTextData(String message, String language) throws RepositoryException, GenericEntityException;

    public abstract String getTextData() throws PathNotFoundException, RepositoryException;

    public abstract String getTextData(String language) throws PathNotFoundException, RepositoryException;

    public abstract String getTextData(String language, String version) throws PathNotFoundException, RepositoryException;

    public abstract String updateTextData(String message) throws PathNotFoundException, RepositoryException, GenericEntityException;

    public abstract JcrTextHelper addNewNode(String newNode) throws RepositoryException, GenericEntityException;

    public abstract List<String> getAvailableLanguages() throws PathNotFoundException, RepositoryException;

}
