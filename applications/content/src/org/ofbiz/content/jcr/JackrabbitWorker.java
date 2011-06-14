package org.ofbiz.content.jcr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionManager;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.JCRFactoryUtil;

public class JackrabbitWorker {

    public static final String module = JackrabbitWorker.class.getName();

    /**
     * Just a dummy method to list all nodes in the repository.
     *
     * @param startNodePath
     * @return
     * @throws RepositoryException
     */
    public static List<Map<String, String>> getRepositoryNodes(GenericValue userLogin, String startNodePath) throws RepositoryException {
        List<Map<String, String>> returnList = null;
        Session session = JCRFactoryUtil.getSession();
        try {
            returnList = getRepositoryNodes(session, startNodePath);
        } catch (RepositoryException e) {
            throw new RepositoryException(e);
        } finally {
            session.logout();
        }

        return returnList;
    }

    /**
     * A helper method to check if all repository nodes have a database
     * relation. If a node is found which is not in the database, the node will
     * be deleted.
     *
     * @param userLogin
     * @return
     * @throws RepositoryException
     * @throws GenericEntityException
     */
    public static void cleanJcrRepository(Delegator delegator, GenericValue userLogin) throws RepositoryException, GenericEntityException {
        Session session = JCRFactoryUtil.getSession();
        List<Map<String, String>> nodesList = null;
        try {
            nodesList = getRepositoryNodes(session, null);
            for (Map<String, String> node : nodesList) {
                String nodePath = node.get("repositoryNode");
                if (UtilValidate.isEmpty(nodePath)) {
                    continue;
                }

                // if the node path is a jcr:system node than ignore this
                // entry
                if (nodePath.startsWith("/jcr:system")) {
                    continue;
                }

                List<GenericValue> contentList = delegator.findByAnd("Content", UtilMisc.toMap("repositoryNode", nodePath));

                // if the List is Empty there is node connection between the
                // node and the database, so this node is a dead node an
                // will be deleted.
                if (UtilValidate.isEmpty(contentList)) {
                    Node n = session.getNode(nodePath);
                    checkOutRelatedNodes(n, session);
                    n.remove();
                }

            }
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            throw new RepositoryException(e);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            throw new GenericEntityException(e);
        } finally {
            try {
                session.save();
            } catch (RepositoryException e) {
                Debug.logError(e, module);
                throw new RepositoryException(e);
            }
            session.logout();
        }
    }

    /**
     * Just a dummy method to list all nodes in the repository.
     *
     * @param startNodePath
     * @return
     * @throws RepositoryException
     */
    private static List<Map<String, String>> getRepositoryNodes(Session session, String startNodePath) throws RepositoryException {
        Node node = null;

        List<Map<String, String>> nodeList = FastList.newInstance();
        if (UtilValidate.isEmpty(startNodePath)) {
            node = session.getRootNode();
        } else {
            node = session.getNode(startNodePath);
        }

        NodeIterator nodeIterator = node.getNodes();
        Map<String, String> nodeEntry = null;
        while (nodeIterator.hasNext()) {
            Node n = nodeIterator.nextNode();

            // recursion - get all subnodes and add the results to our nodeList
            if (n.getNodes().hasNext()) {
                nodeList.addAll(getRepositoryNodes(session, n.getPath()));
            }

            nodeEntry = FastMap.newInstance();

            nodeEntry.put("repositoryNode", n.getPath());

            String message = null;
            if (n.hasProperty("jcr:message")) {
                message = n.getProperty("jcr:message").getString();
            } else {
                message = new String();
            }
            nodeEntry.put("nodeContent", message);
            nodeEntry.put("primaryNodeType", n.getPrimaryNodeType().getName());

            nodeList.add(nodeEntry);
        }

        return nodeList;
    }

    private static void checkOutRelatedNodes(Node startNode, Session session) throws RepositoryException {
        List<Node> nodesToCheckOut = new ArrayList<Node>();
        nodesToCheckOut.add(startNode);
        nodesToCheckOut.add(startNode.getParent());
        if (startNode.hasNodes()) {
            nodesToCheckOut.addAll(getAllChildNodes(startNode));
        }

        VersionManager vm = session.getWorkspace().getVersionManager();
        for (Node node : nodesToCheckOut) {
            vm.checkout(node.getPath());
        }

    }

    private static List<Node> getAllChildNodes(Node startNode) throws RepositoryException {
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
