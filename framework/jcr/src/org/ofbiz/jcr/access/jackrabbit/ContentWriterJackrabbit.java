package org.ofbiz.jcr.access.jackrabbit;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.jcr.access.ContentWriter;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public class ContentWriterJackrabbit implements ContentWriter {

    private static String module = ContentWriterJackrabbit.class.getName();

    private ObjectContentManager ocm = null;

    public ContentWriterJackrabbit(ObjectContentManager ocm) {
        this.ocm = ocm;
    }

    @Override
    public void storeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException {
        if (orm == null) {
            return;
        }

        // We have to check if the node structure (the sub nodes of the passed
        // ORM Object) exist, otherwise they will be created.
        String path = orm.getPath();
        String[] nodeStructure = path.split("/");
        Node parentNode = null;
        try {
            parentNode = this.ocm.getSession().getRootNode();
        } catch (RepositoryException e) {
            Debug.logError(e, "The new node could not be created: " + orm.getPath(), module);
            return;
        }

        // We loop only over the sub nodes.
        for (int i = 0; i < (nodeStructure.length - 1); i++) {
            String node = nodeStructure[i];
            if (UtilValidate.isEmail(node)) {
                continue;
            }

            try {
                if (parentNode.hasNode(node)) {
                    parentNode = parentNode.getNode(node);
                } else {
                    // create new sub node based on the passed
                    // OrfbizRepositoryMapping object.
                    OfbizRepositoryMapping newNode = orm.getClass().newInstance();
                    String parentNodePath = parentNode.getPath();

                    if (!parentNodePath.endsWith("/")) {
                        parentNodePath = parentNodePath + "/";
                    }
                    newNode.setPath(parentNodePath + node);

                    ocm.insert(newNode);
                    parentNode = parentNode.getNode(node);
                }
            } catch (PathNotFoundException e) {
                Debug.logError(e, "The new node could not be created: " + orm.getPath(), module);
                return;
            } catch (RepositoryException e) {
                Debug.logError(e, "The new node could not be created: " + orm.getPath(), module);
                return;
            } catch (InstantiationException e) {
                Debug.logError(e, "The new node could not be created: " + orm.getPath(), module);
                return;
            } catch (IllegalAccessException e) {
                Debug.logError(e, "The new node could not be created: " + orm.getPath(), module);
                return;
            }

        }

        ocm.insert(orm);
        this.saveState();
    }

    @Override
    public void updateContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException {
        ocm.update(orm);
        this.saveState();
    }

    @Override
    public void removeContentObject(String nodePath) throws ObjectContentManagerException {
        ocm.remove(nodePath);
        this.saveState();
    }

    private void saveState() {
        if (ocm != null) {
            ocm.save();
        }
    }
}
