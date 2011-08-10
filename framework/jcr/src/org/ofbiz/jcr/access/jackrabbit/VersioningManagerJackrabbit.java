package org.ofbiz.jcr.access.jackrabbit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.VersionManager;

import org.ofbiz.base.util.Debug;
import org.ofbiz.jcr.access.VersioningManager;

public class VersioningManagerJackrabbit implements VersioningManager {

    private static String module = VersioningManagerJackrabbit.class.getName();

    private volatile VersionManager versionManager = null;
    private List<Node> checkedOutNodeStore = Collections.synchronizedList(new ArrayList<Node>());
    private Session session = null;

    VersioningManagerJackrabbit(Session session) throws UnsupportedRepositoryOperationException, RepositoryException {
        versionManager = session.getWorkspace().getVersionManager();
        this.session = session;
    }

    public VersionManager getVersionManager() {
        return this.versionManager;
    }

    public void addNodeToCheckedOutStore(Node node) {
        try {
            if (!versionManager.isCheckedOut(node.getPath())) {
                checkedOutNodeStore.add(node);
            }
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }
    }

    protected void saveSessionAndCheckinNode() {
        try {
            this.session.save();

            for (Node node : checkedOutNodeStore) {
                // add the new resource content to the version history
                if (session.nodeExists(node.getPath()) && versionManager.isCheckedOut(node.getPath())) {
                    versionManager.checkin(node.getPath());
                }
            }

            // reset the node store after everything is checked in
            checkedOutNodeStore = new ArrayList<Node>();
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }
    }

    protected void checkOutNode(Node node) {
        try {
            // make sure we don't checkout the root node, because it's not
            // versionable
            if (!ConstantsJackrabbit.ROOTPATH.equals(node.getPath())) {
                versionManager.checkout(node.getPath());
                checkedOutNodeStore.add(node);
            }
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }
    }

    /**
     * Checks out recursively all related nodes (parent, all child's (if exists)
     * and the node itself)
     *
     * @param startNode
     * @throws RepositoryException
     */
    protected void checkOutRelatedNodes(Node startNode) throws RepositoryException {
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

}
