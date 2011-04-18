package org.ofbiz.jcr.helper;

import java.io.InputStream;

import javax.jcr.RepositoryException;

import net.sf.json.JSONArray;

import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

public abstract class JcrFileHelperAbstract extends JcrHelperAbstract {

    public abstract GenericValue uploadFileData(byte[] file, String fileName) throws RepositoryException, GenericEntityException;

    public abstract GenericValue uploadFileData(InputStream file, String fileName) throws RepositoryException, GenericEntityException;

    public abstract InputStream getFileContent(String fileName) throws RepositoryException;

    public abstract InputStream getFileContent() throws RepositoryException;

    public abstract String getFileMimeType() throws RepositoryException;

    public abstract JSONArray getJsonFileTree() throws RepositoryException;

    public abstract JcrFileHelper addNewNode(String newNode) throws RepositoryException, GenericEntityException;
}
