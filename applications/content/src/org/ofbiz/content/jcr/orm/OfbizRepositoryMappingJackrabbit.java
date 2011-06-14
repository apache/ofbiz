package org.ofbiz.content.jcr.orm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;

import javolution.util.FastList;
import javolution.util.FastMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public class OfbizRepositoryMappingJackrabbit implements OfbizRepositoryMapping {

    private static String module = OfbizRepositoryMappingJackrabbit.class.getName();

    private enum PROPERTY_FIELDS {
        MESSAGE("jcr:message"), FILE(NodeType.NT_FILE), FOLDER(NodeType.NT_FOLDER), RESOURCE(NodeType.NT_RESOURCE), DATA("jcr:data"), UNSTRUCTURED(NodeType.NT_UNSTRUCTURED), MIMETYPE("jcr:mimeType"), REPROOT("rep:root"), mixInLANGUAGE("mix:language"), mixInVERSIONING(
                "mix:versionable"), mixInTITLE("mix:title"), LANGUAGE("jcr:language"), TITLE("jcr:title"), DESCRIPTION("jcr:description");

        String type = null;

        PROPERTY_FIELDS(String type) {
            this.type = type;
        }

        String getType() {
            return this.type;
        }

    };

    public enum NODE_TYPE {
        FILE, DATA;
    };

    private Delegator delegator = null;
    private GenericValue content = null;
    // private GenericValue contentAssoc = null;
    private Session session = null;
    private Node node = null;
    private volatile VersionManager versionManager = null;
    private volatile String selectedLanguage = UtilProperties.getPropertyValue("general", "locale.properties.fallback");
    private List<Node> checkedOutNodeStore = Collections.synchronizedList(new ArrayList<Node>());

    /**
     * The OfbizContentMapping constructor loads the node and related content
     * data from the DB. You can pass a contentId *OR* a node path. Primary the
     * constructor will take a contentId, get the content information from the
     * database and read the related repository node from the database. If you
     * pass a repository node path, the method will look in the database for a
     * related content entry.
     *
     * @param delegator
     * @param session
     * @param contentId
     * @param repositoryNode
     * @param type
     *            indicates if a file or data content should be added
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public OfbizRepositoryMappingJackrabbit(Delegator delegator, Session session, String contentId, String repositoryNode, NODE_TYPE type) throws RepositoryException, GenericEntityException {
        if (session == null) {
            Debug.logWarning("A repository session is needed to create an OfbizContentMapping Object.", module);
            return;
        } else if (UtilValidate.isEmpty(contentId) && UtilValidate.isEmpty(repositoryNode)) {
            Debug.logWarning("There should be either a contentId or a repositoryNode", module);
            return;
        }

        // get the version manager from the workspace
        versionManager = session.getWorkspace().getVersionManager();
        this.delegator = delegator;
        this.session = session;

        // check if the node path is an absolute path
        if (!repositoryNode.startsWith("/")) {
            repositoryNode = "/" + repositoryNode;
        }

        if (UtilValidate.isNotEmpty(contentId)) {
            this.content = delegator.findOne("Content", true, UtilMisc.toMap("contentId", contentId));
            this.node = getRepositoryNode(content.getString("repositoryNode"));

        } else if (UtilValidate.isNotEmpty(repositoryNode)) {

            List<GenericValue> contentList = delegator.findByAndCache("Content", UtilMisc.toMap("repositoryNode", repositoryNode));

            // if the List is Empty there might be no repository node with
            // this path information, so we have to create a new one
            if (UtilValidate.isNotEmpty(contentList)) {
                this.content = EntityUtil.getFirst(contentList);
                this.node = getRepositoryNode(repositoryNode);
            } else {
                // decide if we create a file content or a data content and set
                // the primary node type
                String primType = null;
                if (type == NODE_TYPE.FILE) {
                    primType = PROPERTY_FIELDS.FOLDER.getType();
                } else {
                    primType = PROPERTY_FIELDS.UNSTRUCTURED.getType();
                }

                Map<String, Object> newRepositoryEntry = createNewRepositoryNode(repositoryNode, primType);
                this.node = (Node) newRepositoryEntry.get("node");
                this.content = (GenericValue) newRepositoryEntry.get("content");
                saveSessionAndCheckinNode();
            }

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getDelegator()
     */
    @Override
    public Delegator getDelegator() {
        return delegator;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getSelctedLanguage()
     */
    @Override
    public String getSelctedLanguage() {
        return this.selectedLanguage;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getCurrentVersion()
     */
    @Override
    public String getCurrentBaseVersion() {
        try {
            return versionManager.getBaseVersion(this.node.getPath()).getName();
        } catch (UnsupportedRepositoryOperationException e) {
            Debug.logWarning(e, module);
        } catch (RepositoryException e) {
            Debug.logWarning(e, module);
        }

        // 0 means not versined
        return "0.0";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getCurrentVersion()
     */
    @Override
    public String getCurrentLanguageVersion() {
        try {
            Node languageNode = this.node;
            if (this.node.hasNode(selectedLanguage)) {
                languageNode = this.node.getNode(selectedLanguage);
            }

            return versionManager.getBaseVersion(languageNode.getPath()).getName();
        } catch (UnsupportedRepositoryOperationException e) {
            Debug.logWarning(e, module);
        } catch (RepositoryException e) {
            Debug.logWarning(e, module);
        }

        // 0 means not versioned
        return "0.0";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getAllLanguageVersions()
     */
    @Override
    public List<String> getAllLanguageVersions() {
        List<String> versionList = new ArrayList<String>();
        try {
            Node languageNode = this.node;
            if (this.node.hasNode(selectedLanguage)) {
                languageNode = this.node.getNode(selectedLanguage);
            }

            VersionHistory vh = versionManager.getVersionHistory(languageNode.getPath());
            VersionIterator vi = vh.getAllVersions();
            while (vi.hasNext()) {
                String v = vi.nextVersion().getName();
                // exclude the 'jcr:rootVersion'
                if (!v.startsWith("jcr:")) {
                    versionList.add(v);
                }
            }

        } catch (UnsupportedRepositoryOperationException e) {
            Debug.logWarning(e, module);
        } catch (RepositoryException e) {
            Debug.logWarning(e, module);
        }

        return versionList;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#closeSession()
     */
    @Override
    public void closeSession() {
        if (session != null && session.isLive()) {
            session.logout();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.OfbizContentMapping#updateTextData(java.lang.String)
     */
    @Override
    public Version updateOrStoreTextData(String message) throws RepositoryException, GenericEntityException {
        return updateOrStoreTextData(message, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.OfbizContentMapping#updateTextData(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Version updateOrStoreTextData(String message, String language) throws RepositoryException, GenericEntityException {
        // if no language is passed store the content under the ofbiz default
        // language
        if (UtilValidate.isEmpty(language)) {
            language = UtilProperties.getPropertyValue("general", "locale.properties.fallback");
        }

        // create a sub node for the language if the current node haven't the
        // language property
        // TODO Refactor code fragment
        Node languageNode = node;
        if (!this.node.hasProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType())) {
            if (session.nodeExists(this.node.getPath()) && session.nodeExists(this.node.getPath() + "/" + language)) {
                languageNode = this.node.getNode(language);
                checkOutNode(languageNode);
            } else {
                languageNode = (Node) this.createNewRepositoryNode(this.node.getPath() + "/" + language).get("node");
                languageNode.setProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType(), language);
            }
        } else {
            checkOutNode(languageNode);
        }
        languageNode.setProperty(PROPERTY_FIELDS.MESSAGE.getType(), message);
        languageNode.addMixin(PROPERTY_FIELDS.mixInVERSIONING.getType());

        saveSessionAndCheckinNode();

        return versionManager.getBaseVersion(languageNode.getPath());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getContent()
     */
    @Override
    public GenericValue getContentObject() {
        return content;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getNode()
     */
    @Override
    public Node getNode() {
        return node;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.OfbizContentMapping#getNodeProperty(java.lang.String)
     */
    @Override
    public Property getNodeProperty(String propertyName) {
        try {
            if (this.node.hasProperty(propertyName)) {
                return this.node.getProperty(propertyName);
            }
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getContentId()
     */
    @Override
    public String getContentId() {
        if (content != null) {
            return content.getString("contentId");
        } else {
            return new String();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getNodePath()
     */
    @Override
    public String getNodePath() {
        try {
            return node.getPath();
        } catch (RepositoryException e) {
            return new String();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#removeRepositoryNode()
     */
    @Override
    public void removeRepositoryNode() throws RepositoryException, GenericEntityException {
        checkOutRelatedNodes(node);

        node.remove();
        try {
            GenericValue content = delegator.findOne("Content", false, UtilMisc.toMap("contentId", this.content.getString("contentId")));
            List<GenericValue> relatedContents = getAllRelatedContents(content);
            // TODO We should decide if we set a thru date or delete the
            // resource
            if (UtilValidate.isNotEmpty(relatedContents)) {
                delegator.removeAll(relatedContents);
            }
            saveSessionAndCheckinNode();
        } catch (GenericEntityException e) {
            throw new GenericEntityException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getStringContent()
     */
    @Override
    public String getStringContent() throws PathNotFoundException, RepositoryException {
        return getStringContent(null);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.OfbizContentMapping#getStringContent(java.lang.String)
     */
    @Override
    public String getStringContent(String language) throws PathNotFoundException, RepositoryException {
        return getStringContent(language, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.OfbizContentMapping#getStringContent(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getStringContent(String language, String version) throws PathNotFoundException, RepositoryException {
        // the default language is set here, because we need to indicate if we
        // try to use the default language or if a specific
        boolean useDefaultLanguage = Boolean.FALSE;
        if (UtilValidate.isEmpty(language)) {
            language = UtilProperties.getPropertyValue("general", "locale.properties.fallback");
            useDefaultLanguage = Boolean.TRUE;
        }

        Node langNode = null;
        if (this.node.hasProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType())) {
            langNode = this.node;
        } else if (this.node.hasNode(language)) {
            // if a language is set check if the content exists in this
            // language, else take the system default language
            if (this.node.hasNode(language)) {
                langNode = this.node.getNode(language);
            }
        } else if (!this.node.hasNode(language) && useDefaultLanguage) {
            // NOTE: This will only executed if we want to load the content by
            // the (system) default language and if for this default language no
            // node exist.

            // When the method is called with a specific language and this
            // language is
            // not present the user should get a hint that he is looking for a
            // not existing language. In the other case the user looks for any
            // language first we try the default language and if the default not
            // exist we use the first language which was found.
            if (useDefaultLanguage) {
                NodeIterator ni = this.node.getNodes();
                while (ni.hasNext()) {
                    Node n = ni.nextNode();
                    if (n.hasProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType())) {
                        langNode = n;
                        break;
                    }
                }

            }
        }
        // if nothing was found throw a new PathNotFoundException for an
        // empty node
        if (langNode == null) {
            Debug.logWarning("No text content exist for this node. You should create a text content.", module);
            throw new PathNotFoundException("No text content exist for this node. You should create a text content.");
        }

        // restore another version if needed
        if (UtilValidate.isNotEmpty(version)) {
            VersionHistory vh = versionManager.getVersionHistory(langNode.getPath());
            Version v = vh.getVersion(version);
            versionManager.restore(v, true);
        }

        // set the current language selection
        selectedLanguage = langNode.getProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType()).getString();

        Property property = null;
        if (langNode.hasProperty(PROPERTY_FIELDS.MESSAGE.getType())) {
            property = langNode.getProperty(PROPERTY_FIELDS.MESSAGE.getType());
        }

        if (property == null || property.getType() != 1) {
            Debug.logWarning("The content from the node:" + node.getPath() + " is not a String content.", module);
            return new String();
        }

        return property.getString();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getLanguage()
     */
    @Override
    public List<String> getAvailableLanguages() throws PathNotFoundException, RepositoryException {
        List<String> availableLanguages = FastList.newInstance();
        NodeIterator ni = this.node.getNodes();

        while (ni.hasNext()) {
            Node subNode = (Node) ni.next();
            if (subNode.hasProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType())) {
                availableLanguages.add(subNode.getProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType()).getString());
            }
        }

        return availableLanguages;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#uploadFileData()
     */
    @Override
    public void uploadFileData(InputStream file, String fileName) throws PathNotFoundException, RepositoryException, GenericEntityException {
        uploadFileData(file, fileName, null, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#uploadFileData()
     */
    @Override
    public void uploadFileData(InputStream file, String fileName, String language, String description) throws PathNotFoundException, RepositoryException, GenericEntityException {
        // if no language is passed store the content under the ofbiz default
        // language
        if (UtilValidate.isEmpty(language)) {
            language = UtilProperties.getPropertyValue("general", "locale.properties.fallback");
        }

        Node folder = null;
        // set an indicator if the file should be updated or not
        Boolean update = Boolean.FALSE;
        // check if the node already exists, if not create else update
        if (!this.node.hasNode(fileName)) {
            folder = (Node) createNewRepositoryNode(this.node.getPath() + "/" + fileName, PROPERTY_FIELDS.FILE.getType()).get("node");
            folder.addMixin(PROPERTY_FIELDS.mixInLANGUAGE.getType());
            folder.addMixin(PROPERTY_FIELDS.mixInTITLE.getType());
        } else {
            folder = this.node.getNode(fileName);
            checkOutNode(folder);
            update = Boolean.TRUE;
        }


        // set additional file informations
        folder.setProperty(PROPERTY_FIELDS.LANGUAGE.getType(), language);
        folder.setProperty(PROPERTY_FIELDS.TITLE.getType(), fileName);
        if (UtilValidate.isNotEmpty(description)) {
            folder.setProperty(PROPERTY_FIELDS.DESCRIPTION.getType(), description);
        }

        Node resource = null;
        if(!update) {
            resource = (Node) createNewRepositoryNode(folder.getPath() + "/jcr:content", PROPERTY_FIELDS.RESOURCE.getType()).get("node");
        } else {
            resource = folder.getNode("jcr:content");
            checkOutNode(resource);
        }

        String mimeType = getMimeTypeFromInputStream(file);
        resource.setProperty(PROPERTY_FIELDS.MIMETYPE.getType(), mimeType);
        // resource.setProperty("jcr:encoding", "");

        Binary binary = this.session.getValueFactory().createBinary(file);
        resource.setProperty(PROPERTY_FIELDS.DATA.getType(), binary);
        resource.addMixin(PROPERTY_FIELDS.mixInVERSIONING.getType());
        saveSessionAndCheckinNode();

        return;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getFileContent()
     */
    @Override
    public InputStream getFileContent(String fileName) throws RepositoryException {
        if (!this.node.getPrimaryNodeType().isNodeType(PROPERTY_FIELDS.FOLDER.getType())) {
            Debug.logWarning("The Node: " + this.node.getPath() + " is not a node from type: " + PROPERTY_FIELDS.FOLDER.getType() + ". No OutputStream can retunred.", module);
            return null;
        }

        if (!this.node.hasNode(fileName)) {
            throw new RepositoryException("This file does not exists in the folder");
        }

        Node fileNode = this.node.getNode(fileName);
        // read file language
        if (fileNode.hasProperty(PROPERTY_FIELDS.LANGUAGE.getType())) {
            selectedLanguage = fileNode.getProperty(PROPERTY_FIELDS.LANGUAGE.getType()).getString();
        }

        Node jcrContent = fileNode.getNode("jcr:content");
        if (!jcrContent.hasProperty(PROPERTY_FIELDS.DATA.getType())) {
            Debug.logWarning("No File Content found in repository node.", module);
            return null;
        }

        return jcrContent.getProperty(PROPERTY_FIELDS.DATA.getType()).getBinary().getStream();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getFileContent()
     */
    @Override
    public InputStream getFileContent() throws RepositoryException {
        if (!this.node.getPrimaryNodeType().isNodeType(PROPERTY_FIELDS.FILE.getType())) {
            Debug.logWarning("The Node: " + this.node.getPath() + " is not a node from type: " + PROPERTY_FIELDS.FILE.getType() + ". No OutputStream can retunred.", module);
            return null;
        }

        Node jcrContent = this.node.getNode("jcr:content");
        if (!jcrContent.hasProperty(PROPERTY_FIELDS.DATA.getType())) {
            Debug.logWarning("No File Content found in repository node.", module);
            return null;
        }

        return jcrContent.getProperty(PROPERTY_FIELDS.DATA.getType()).getBinary().getStream();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getJsonFileTree()
     */
    @Override
    public JSONArray getJsonFileTree() throws RepositoryException {
        return getJsonFileChildNodes(this.node);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getJsonDataTree()
     */
    @Override
    public JSONArray getJsonDataTree() throws RepositoryException {
        return getJsonDataChildNodes(this.node);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getNodeName()
     */
    @Override
    public String getNodeName() {
        try {
            return this.node.getName();
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            return new String();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.OfbizContentMapping#getFileMimeType()
     */
    @Override
    public String getFileMimeType() throws RepositoryException {
        String mimeType = new String();

        if (!this.node.getPrimaryNodeType().isNodeType(PROPERTY_FIELDS.FILE.getType())) {
            Debug.logWarning("The Node: " + this.node.getPath() + " is not a node from type: " + PROPERTY_FIELDS.FILE.getType() + ". No OutputStream can retunred.", module);
            return mimeType;
        }

        Node jcrContent = this.node.getNode("jcr:content");
        if (!jcrContent.hasProperty(PROPERTY_FIELDS.DATA.getType())) {
            Debug.logWarning("No File Content found in repository node.", module);
            return mimeType;
        }

        if (jcrContent.hasProperty(PROPERTY_FIELDS.MIMETYPE.getType())) {
            mimeType = jcrContent.getProperty(PROPERTY_FIELDS.MIMETYPE.getType()).getString();
        }

        return mimeType;
    }

    private void saveSessionAndCheckinNode() {
        try {
            this.session.save();

            for (Node node : checkedOutNodeStore) {
                // add the new resource content to the version history
                if (versionManager.isCheckedOut(node.getPath())) {
                    versionManager.checkin(node.getPath());
                }
            }

            // reset the node store after everything is checked in
            checkedOutNodeStore = new ArrayList<Node>();
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }
    }

    private void checkOutNode(Node node) {
        try {
            versionManager.checkout(node.getPath());
            checkedOutNodeStore.add(node);
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }
    }

    /**
     * Returns a JSON Array with the repository folder structure. The JSON array
     * is directly build for the jsTree jQuery plugin.
     *
     * @param startNode
     * @return
     * @throws RepositoryException
     */
    private JSONArray getJsonFileChildNodes(Node startNode) throws RepositoryException {
        NodeIterator nodeIterator = startNode.getNodes();

        JSONArray folderStrucutre = new JSONArray();
        JSONObject attr = new JSONObject();

        while (nodeIterator.hasNext()) {
            JSONObject folder = new JSONObject();
            Node node = nodeIterator.nextNode();

            if (node.getPrimaryNodeType().isNodeType(PROPERTY_FIELDS.FOLDER.getType()) || node.getPrimaryNodeType().isNodeType(PROPERTY_FIELDS.UNSTRUCTURED.getType())) {
                attr.element("title", node.getName());
                folder.element("data", attr);

                attr = new JSONObject();
                attr.element("NodePath", node.getPath());
                attr.element("NodeType", node.getPrimaryNodeType().getName());
                folder.element("attr", attr);

                folder.element("children", getJsonFileChildNodes(node).toString());

                folderStrucutre.element(folder);
            } else if (node.getPrimaryNodeType().isNodeType(PROPERTY_FIELDS.FILE.getType())) {
                attr = new JSONObject();
                attr.element("title", node.getName());
                folder.element("data", attr);

                attr = new JSONObject();
                attr.element("NodePath", node.getPath());
                attr.element("NodeType", node.getPrimaryNodeType().getName());
                folder.element("attr", attr);

                folderStrucutre.element(folder);
            }

        }

        return folderStrucutre;
    }

    /**
     * Checks out recursively all related nodes (parent, all child's (if exists)
     * and the node itself)
     *
     * @param startNode
     * @throws RepositoryException
     */
    private void checkOutRelatedNodes(Node startNode) throws RepositoryException {
        List<Node> nodesToCheckOut = new ArrayList<Node>();
        nodesToCheckOut.add(startNode);
        nodesToCheckOut.add(startNode.getParent());
        if (startNode.hasNodes()) {
            nodesToCheckOut.addAll(getAllChildNodes(startNode));
        }

        for (Node node : nodesToCheckOut) {
            checkOutNode(node);
        }

    }

    /**
     * Return recursively all child nodes
     *
     * @param startNode
     * @return
     * @throws RepositoryException
     */
    private List<Node> getAllChildNodes(Node startNode) throws RepositoryException {
        List<Node> nodes = new ArrayList<Node>();
        NodeIterator ni = startNode.getNodes();
        while (ni.hasNext()) {
            Node nextNode = ni.nextNode();
            if (nextNode.hasNodes()) {
                nodes.addAll(getAllChildNodes(nextNode));
            }

            nodes.add(nextNode);
        }

        return nodes;
    }

    /**
     * Returns a JSON Array with the repository text data structure. The JSON
     * array is directly build for the jsTree jQuery plugin.
     *
     * @param startNode
     * @return
     * @throws RepositoryException
     */
    private JSONArray getJsonDataChildNodes(Node startNode) throws RepositoryException {
        NodeIterator nodeIterator = startNode.getNodes();

        JSONArray folderStrucutre = new JSONArray();
        JSONObject attr = new JSONObject();

        while (nodeIterator.hasNext()) {
            JSONObject folder = new JSONObject();
            Node node = nodeIterator.nextNode();

            //
            if (node.getPrimaryNodeType().isNodeType(PROPERTY_FIELDS.UNSTRUCTURED.getType()) && !node.hasProperty(PROPERTY_FIELDS.mixInLANGUAGE.getType())) {
                attr.element("title", node.getName());
                folder.element("data", attr);

                attr = new JSONObject();
                attr.element("NodePath", node.getPath());
                attr.element("NodeType", node.getPrimaryNodeType().getName());
                folder.element("attr", attr);

                folder.element("children", getJsonDataChildNodes(node).toString());

                folderStrucutre.element(folder);
            }
        }

        return folderStrucutre;
    }

    /**
     * Create a new node in the repository. The new node will be linked with an
     * content object. The method can create recursive node structures.
     * Recursive node associations will be stored in the ContentAssoc table
     *
     * @param newNodePath
     *            - have to be an absolute path
     * @return returned a map with the last created content and node object. The
     *         objects are stored with the key "content" and "node"
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    private Map<String, Object> createNewRepositoryNode(String newNodePath) throws RepositoryException, GenericEntityException {
        return createNewRepositoryNode(newNodePath, null);
    }

    /**
     * Create a new node in the repository. The new node will be linked with an
     * content object. The method can create recursive node structures.
     * Recursive node associations will be stored in the ContentAssoc table
     *
     * @param newNodePath
     *            - have to be an absolute path
     * @param type
     *            - defines a primary type for the new node
     * @return returned a map with the last created content and node object. The
     *         objects are stored with the key "content" and "node"
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    private Map<String, Object> createNewRepositoryNode(String newNodePath, String type) throws RepositoryException, GenericEntityException {
        Map<String, Object> returnMap = createNodeStructure(newNodePath, type);
        return returnMap;
    }

    /**
     * Returns a list of all (recursive) related content to the base content.
     * The return list contains the ContentAssoc Objects as well as the Content
     * Objects.
     *
     * @param content
     * @return
     */
    private List<GenericValue> getAllRelatedContents(GenericValue content) {
        List<GenericValue> returnList = null;

        try {
            // find all content assoc links where the current content object is
            returnList = delegator.findByAnd("ContentAssoc", UtilMisc.toMap("contentId", content.getString("contentId")));

            if (UtilValidate.isNotEmpty(returnList)) {
                List<GenericValue> tmpReturnList = returnList;
                for (GenericValue c : tmpReturnList) {
                    returnList.addAll(getAllRelatedContents(delegator.findOne("Content", false, UtilMisc.toMap("contentId", c.getString("contentIdTo")))));
                }
            }
            returnList.addAll(delegator.findByAnd("ContentAssoc", UtilMisc.toMap("contentIdTo", content.getString("contentId"))));
            returnList.addAll(delegator.findByAnd("ContentKeyword", UtilMisc.toMap("contentId", content.getString("contentId"))));
            returnList.add(content);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return null;
        }

        return returnList;
    }

    /**
     * Here we create a new node Structure, if you pass a node path like
     * "/foo/baa/node" It will create first "/foo" and "/baa" as parent node and
     * than node.
     *
     * @param newNode
     *            Path
     * @return returned a map with the last created content and node object. The
     *         objects are stored with the key "content" and "node"
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    private Map<String, Object> createNodeStructure(String newNodes, String type) throws RepositoryException, GenericEntityException {
        Map<String, Object> returnMap = FastMap.newInstance();
        Node newNodeParent = this.session.getRootNode();
        String assocContentId = null;
        String parentContentId = null;
        String[] nodes = newNodes.split("/");
        for (String node : nodes) {
            if (UtilValidate.isEmpty(node)) {
                continue;
            } else if (newNodeParent.hasNode(node)) {
                newNodeParent = newNodeParent.getNode(node);
                continue;
            }

            // If the current node is not the root node and has a parent node
            // search for the parent content id.
            if (!newNodeParent.getPath().equals("/") && UtilValidate.isEmpty(parentContentId) && UtilValidate.isEmpty(assocContentId) && (newNodeParent.getParent() != null)) {
                parentContentId = getParentNodeContentId(newNodeParent);
            }

            // if we want to add a child node the parent node have to be checked
            // out to
            if (!versionManager.isCheckedOut(newNodeParent.getPath())) {
                checkOutNode(newNodeParent);
            }

            if (UtilValidate.isEmpty(type)) {
                // If the nodeType is empty, add a node with the same node type
                // as the parent node.
                // Only when the parent node is the overall repository root
                // node, we have to create a node without parent type.
                String parentNodeType = newNodeParent.getPrimaryNodeType().getName();
                if (!PROPERTY_FIELDS.REPROOT.getType().equals(parentNodeType)) {
                    newNodeParent = newNodeParent.addNode(node, newNodeParent.getPrimaryNodeType().getName());
                } else {
                    newNodeParent = newNodeParent.addNode(node);
                }
            } else {
                newNodeParent = newNodeParent.addNode(node, type);
            }

            GenericValue newContent = nodeContentDatabaseConnection(newNodeParent);
            returnMap.put("content", newContent);
            assocContentId = newContent.getString("contentId");

            newNodeParent.addMixin(PROPERTY_FIELDS.mixInVERSIONING.getType());
            // the new node should be add to the nodeStore List to check it in
            // when the session is stored.
            checkedOutNodeStore.add(newNodeParent);

            // create the content assoc entry for the new node parent - child
            // relation
            if (UtilValidate.isNotEmpty(assocContentId) && UtilValidate.isNotEmpty(parentContentId)) {
                nodeContentAssoc(assocContentId, parentContentId);
                parentContentId = assocContentId;
            }

        }

        // only the last node which is created will be returned
        returnMap.put("node", newNodeParent);
        return returnMap;
    }

    /**
     * Creates the database relationship between two nodes.
     *
     * @param assocContentId
     * @param parentContentId
     */
    private void nodeContentAssoc(String assocContentId, String parentContentId) {
        GenericValue contentAssoc = delegator.makeValue("ContentAssoc");

        contentAssoc.set("contentId", parentContentId);
        contentAssoc.set("contentIdTo", assocContentId);
        contentAssoc.set("contentAssocTypeId", "REPOSITORY");
        contentAssoc.set("fromDate", UtilDateTime.nowTimestamp());

        try {
            this.delegator.createOrStore(contentAssoc);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        }

    }

    /**
     * Returns the contentId to a node.
     *
     * @param newNodeParent
     * @return
     */
    private String getParentNodeContentId(Node newNodeParent) {
        List<GenericValue> list = null;
        String parentNodeContentId = null;
        try {
            list = delegator.findByAndCache("Content", UtilMisc.toMap("repositoryNode", newNodeParent.getPath()));
            if (UtilValidate.isNotEmpty(list)) {
                parentNodeContentId = EntityUtil.getFirst(list).getString("contentId");
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }

        return parentNodeContentId;
    }

    /**
     * Creates a database relation to a repository node. Returns the new
     * contentId.
     *
     * @param newNode
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    private GenericValue nodeContentDatabaseConnection(Node newNode) throws RepositoryException, GenericEntityException {
        // create the content database connection
        GenericValue content = null;
        content = this.delegator.makeValue("Content");
        String primaryKey = this.delegator.getNextSeqId("Content");
        content.set("contentId", primaryKey);
        content.set("contentTypeId", "REPOSITORY");

        content.set("repositoryNode", newNode.getPath());

        GenericValue newContent = null;
        try {
            newContent = this.delegator.createOrStore(content);
            // only save the session when a new content value is created
        } catch (GenericEntityException e) {
            throw new GenericEntityException(e);
        }

        return newContent;
    }

    /**
     * Get the node object from the repository. If an exceptions raises null
     * will be returned.
     *
     * @param nodePath
     * @return
     */
    private Node getRepositoryNode(String nodePath) {
        try {
            return session.getNode(nodePath);
        } catch (PathNotFoundException e) {
            Debug.logError(e, module);
            return null;
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            return null;
        }
    }

    private String getMimeTypeFromInputStream(InputStream is) {
        if (!TikaInputStream.isTikaInputStream(is)) {
            is = TikaInputStream.get(is);
        }
        Tika tika = new Tika();
        try {
            return tika.detect(is);
        } catch (IOException e) {
            Debug.logError(e, module);
            return "application/octet-stream";
        }
    }

}
