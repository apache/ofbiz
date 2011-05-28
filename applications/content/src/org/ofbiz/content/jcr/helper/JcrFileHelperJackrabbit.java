package org.ofbiz.content.jcr.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.content.jcr.orm.OfbizRepositoryMappingJackrabbit;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.JackrabbitContainer;
import org.ofbiz.jcr.helper.JcrFileHelper;
import org.ofbiz.jcr.helper.JcrFileHelperAbstract;

public class JcrFileHelperJackrabbit extends JcrFileHelperAbstract implements JcrFileHelper {

    private static String fileRootNode = "/fileHome";

    /**
     * The JcrHelper constructor loads the node and linked content data from the
     * DB. You can pass a contentId *OR* a node path. Primary the constructor
     * will take a contentId, get the content information from the database and
     * read the linked repository node from the database. If you pass a
     * repository node path, the method will look in the database for a linked
     * content entry. If none is found the constructor creates a *new* content
     * node in the repository. All nodes creates by the JcrFileHelper
     * constructor will be stored under a file home node. This file home node
     * represents the root folder for each file.
     *
     * @param userLogin
     * @param delegator
     * @param contentId
     * @param repositoryNode
     */
    public JcrFileHelperJackrabbit(GenericValue userLogin, Delegator delegator, String contentId, String repositoryNode) {
        super.userLogin = userLogin;
        if (userLogin == null || delegator == null) {
            Debug.logError("You pass null for the UserLogin or Delegator, Object can't be created.", module);
            return;
        }

        Session session = null;
        try {
            session = JackrabbitContainer.getUserSession(userLogin);
        } catch (RepositoryException re) {
            Debug.logError(re, module);
            return;
        }

        // if neither a contentId nor a repositoryNode is set or the repository
        // node is set to the root node
        // we have to point the repository node to our file root node
        if (UtilValidate.isEmpty(contentId) && (UtilValidate.isEmpty(repositoryNode) || "/".equals(repositoryNode))) {
            repositoryNode = fileRootNode;
        }

        // add the file home node to the repository node if necessary
        repositoryNode = addRepositoryFileHomeNode(repositoryNode);

        try {
            super.orm = new OfbizRepositoryMappingJackrabbit(delegator, session, contentId, repositoryNode, OfbizRepositoryMappingJackrabbit.NODE_TYPE.FILE);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return;
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            return;
        }
    }

    /**
     * The JcrHelper constructor loads the node and linked content data from the
     * DB. You can pass a contentId *OR* a node path. Primary the constructor
     * will take a contentId, get the content information from the database and
     * read the related repository node from the database. If you pass a
     * repository node path, the method will look in the database for a linked
     * content entry. If none is found the constructor creates a *new* content
     * node in the repository. The request object should contain the parameters:
     * userLogin (GenericValue), contentId (String) *or* repositoryNode
     * (String). All nodes creates by the JcrFileHelper constructor will be
     * stored under a file home node. This file home node represents the root
     * folder for each file.
     *
     * @param request
     */
    public JcrFileHelperJackrabbit(HttpServletRequest request) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        super.userLogin = userLogin;

        Session session = null;
        try {
            session = JackrabbitContainer.getUserSession(userLogin);
        } catch (RepositoryException re) {
            Debug.logError(re, module);
            return;
        }

        String contentId = request.getParameter("contentId");
        String repositoryNode = request.getParameter("repositoryNode");

        // if neither a contentId nor a repositoryNode is set or the repository
        // node is set to the root node
        // we have to point the repository node to our file root node
        if (UtilValidate.isEmpty(contentId) && (UtilValidate.isEmpty(repositoryNode) || "/".equals(repositoryNode))) {
            repositoryNode = fileRootNode;
        }

        // add the file home node to the repository node if necessary
        repositoryNode = addRepositoryFileHomeNode(repositoryNode);

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        try {
            super.orm = new OfbizRepositoryMappingJackrabbit(delegator, session, contentId, repositoryNode, OfbizRepositoryMappingJackrabbit.NODE_TYPE.FILE);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return;
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            return;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.helper.JcrFileHelper#uploadFileData(byte[],
     * java.lang.String)
     */
    @Override
    public GenericValue uploadFileData(byte[] file, String fileName) throws RepositoryException, GenericEntityException {
        InputStream is = new ByteArrayInputStream(file);

        orm.uploadFileData(is, fileName);
        return orm.getContentObject();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.helper.JcrFileHelper#uploadFileData(byte[],
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public GenericValue uploadFileData(byte[] file, String fileName, String language, String description) throws RepositoryException, GenericEntityException {
        InputStream is = new ByteArrayInputStream(file);

        orm.uploadFileData(is, fileName, language, description);
        return orm.getContentObject();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.helper.JcrFileHelper#uploadFileData(java.io.InputStream,
     * java.lang.String)
     */
    @Override
    public GenericValue uploadFileData(InputStream file, String fileName) throws RepositoryException, GenericEntityException {
        orm.uploadFileData(file, fileName);
        return orm.getContentObject();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.helper.JcrFileHelper#uploadFileData(java.io.InputStream,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public GenericValue uploadFileData(InputStream file, String fileName, String language, String description) throws RepositoryException, GenericEntityException {
        orm.uploadFileData(file, fileName, language, description);
        return orm.getContentObject();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.helper.JcrFileHelper#getFileContent(java.lang.String)
     */
    @Override
    public InputStream getFileContent(String fileName) throws RepositoryException {
        return orm.getFileContent(fileName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.helper.JcrFileHelper#getFileContent()
     */
    @Override
    public InputStream getFileContent() throws RepositoryException {
        return orm.getFileContent();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.helper.JcrFileHelper#addNewNode(java.lang.String)
     */
    @Override
    public JcrFileHelper addNewNode(String newNode) throws RepositoryException, GenericEntityException {
        if (!newNode.startsWith("/")) {
            newNode = "/" + newNode;
        }

        String newAbsoluteNodePath = orm.getNodePath() + newNode;

        return new JcrFileHelperJackrabbit(userLogin, orm.getDelegator(), null, newAbsoluteNodePath);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.helper.JcrFileHelper#getJsonFileTree()
     */
    @Override
    public JSONArray getJsonFileTree() throws RepositoryException {
        return orm.getJsonFileTree();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.helper.JcrFileHelper#getFileMimeType()
     */
    @Override
    public String getFileMimeType() throws RepositoryException {
        return orm.getFileMimeType();
    }

    /**
     * Adds the file home node to the repository node, if not already exists.
     *
     * @param repositoryNode
     * @return
     */
    private String addRepositoryFileHomeNode(String repositoryNode) {
        if (UtilValidate.isEmpty(repositoryNode)) {
            return repositoryNode;
        }

        // check if the node already starts with the home node
        if (repositoryNode.startsWith(fileRootNode)) {
            return repositoryNode;
        }

        if (repositoryNode.startsWith("/")) {
            return repositoryNode = fileRootNode + repositoryNode;
        }

        return fileRootNode + "/" + repositoryNode;
    }

}
