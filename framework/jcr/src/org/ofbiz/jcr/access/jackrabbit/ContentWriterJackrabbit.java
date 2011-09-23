package org.ofbiz.jcr.access.jackrabbit;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.jcr.access.ContentWriter;
import org.ofbiz.jcr.access.VersioningManager;
import org.ofbiz.jcr.access.jackrabbit.ConstantsJackrabbit.PROPERTY_FIELDS;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

public class ContentWriterJackrabbit implements ContentWriter {

    private static String module = ContentWriterJackrabbit.class.getName();

    private ObjectContentManager ocm = null;
    VersioningManager versioningManager = null;

    /**
     *
     * @param ocm
     */
    public ContentWriterJackrabbit(ObjectContentManager ocm) {
        this.ocm = ocm;
        versioningManager = new VersioningManagerJackrabbit(ocm);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.access.ContentWriter#storeContentObject(org.ofbiz.jcr.orm
     * .OfbizRepositoryMapping)
     */
    @Override
    public void storeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException, ItemExistsException {
        if (orm == null) {
            return;
        }

        // we want to avoid same name sibling (SnS) for each node Type
        try {
            if (this.ocm.getSession().itemExists(orm.getPath())) {
                // we could either throw an exception or call the update method
                throw new ItemExistsException("There already exists an object stored at " + orm.getPath() + ". Please use update if you want to change it.");
            }
        } catch (ItemExistsException e) {
            throw (e);
        } catch (RepositoryException e) {
            Debug.logError(e, module);
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
            if (UtilValidate.isEmpty(node)) {
                continue;
            }

            try {
                if (parentNode.hasNode(node)) {
                    parentNode = parentNode.getNode(node);
                    versioningManager.checkOutContentObject(parentNode.getPath());
                } else {
                    versioningManager.checkOutContentObject(parentNode.getPath());
                    Node newNode = parentNode.addNode(node);
                    newNode.addMixin(PROPERTY_FIELDS.mixInVERSIONING.getType());
                    if (!ConstantsJackrabbit.ROOTPATH.equals(parentNode.getPath())) {
                        newNode.setPrimaryType(parentNode.getPrimaryNodeType().getName());
                    }

                    versioningManager.addContentToCheckInList(newNode.getPath());
                    parentNode = newNode;
                }
            } catch (PathNotFoundException e) {
                Debug.logError(e, "The new node could not be created: " + orm.getPath(), module);
                return;
            } catch (RepositoryException e) {
                Debug.logError(e, "The new node could not be created: " + orm.getPath(), module);
                return;
            }

        }

        ocm.insert(orm);
        versioningManager.addContentToCheckInList(orm.getPath());

        this.saveState();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.access.ContentWriter#updateContentObject(org.ofbiz.jcr.
     * orm.OfbizRepositoryMapping)
     */
    @Override
    public void updateContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException {
        versioningManager.checkOutContentObject(orm.getPath());
        ocm.update(orm);
        this.saveState();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.access.ContentWriter#removeContentObject(java.lang.String)
     */
    @Override
    public void removeContentObject(String nodePath) throws ObjectContentManagerException {
        versioningManager.checkOutContentObject(nodePath, true);

        ocm.remove(nodePath);
        this.saveState();
    }

    private void saveState() {
        versioningManager.checkInContentAndSaveState();
    }
}
