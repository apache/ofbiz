package org.ofbiz.jcr.access.jackrabbit;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.ofbiz.jcr.access.ContentReader;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public class ContentReaderJackrabbit implements ContentReader {

    private static String module = ContentReaderJackrabbit.class.getName();

    private ObjectContentManager ocm = null;

    public ContentReaderJackrabbit(ObjectContentManager ocm) {
        this.ocm = ocm;
    }

    @Override
    public OfbizRepositoryMapping getContentObject(String nodePath) {
        // OfbizRepositoryMappingJackrabbit ofbizRepositoryMapping =
        // (OfbizRepositoryMappingJackrabbit) ocm.getObject(nodePath);
        return (OfbizRepositoryMapping) ocm.getObject(nodePath);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.ContentReader#getJsonDataTree()
     */
    @Override
    public JSONArray getJsonDataTree() throws RepositoryException {
        return getJsonDataChildNodes(ocm.getSession().getRootNode());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.access.ContentReader#getJsonFileTree()
     */
    @Override
    public JSONArray getJsonFileTree() throws RepositoryException {
        return getJsonFileChildNodes(ocm.getSession().getRootNode());
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

            if (node.getPrimaryNodeType().isNodeType(ConstantsJackrabbit.PROPERTY_FIELDS.FOLDER.getType())) {
                attr.element("title", node.getName());
                folder.element("data", attr);

                attr = new JSONObject();
                attr.element("NodePath", node.getPath());
                attr.element("NodeType", node.getPrimaryNodeType().getName());
                folder.element("attr", attr);

                folder.element("children", getJsonFileChildNodes(node).toString());

                folderStrucutre.element(folder);
            } else if (node.getPrimaryNodeType().isNodeType(ConstantsJackrabbit.PROPERTY_FIELDS.FILE.getType())) {
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
            if (node.getPrimaryNodeType().isNodeType(ConstantsJackrabbit.PROPERTY_FIELDS.UNSTRUCTURED.getType()) && !node.hasProperty(ConstantsJackrabbit.PROPERTY_FIELDS.mixInLANGUAGE.getType())) {
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
}
