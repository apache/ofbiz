package org.ofbiz.jcr.access.jackrabbit;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import net.sf.json.JSONArray;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.ContentReader;
import org.ofbiz.jcr.access.ContentWriter;
import org.ofbiz.jcr.access.RepositoryAccess;
import org.ofbiz.jcr.loader.JCRFactoryUtil;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitArticle;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitLocalizedContent;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFile;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFolder;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitHierarchyNode;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitNews;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitResource;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitUnstructured;

public class RepositoryAccessJackrabbit implements RepositoryAccess {

    private static String module = RepositoryAccessJackrabbit.class.getName();

    Session session = null;
    ObjectContentManagerImpl ocm = null;

    /**
     * Create a repository Access object based on the userLogin.
     *
     * @param userLogin
     */
    public RepositoryAccessJackrabbit(GenericValue userLogin) {
        // TODO pass the userLogin to the getSession() method and perform some
        this(JCRFactoryUtil.getSession());
    }

    /**
     * Create a repository Access object based on a JCR Session.
     *
     * @param userLogin
     */
    public RepositoryAccessJackrabbit(Session session) {
        if (session == null) {
            Debug.logWarning("A repository session is needed to create an OfbizContentMapping Object.", module);
            return;
        }

        this.session = session;

        List<Class> classes = new ArrayList<Class>();
        // put this in an xml configuration file
        // should the ocm classes be loaded in during the container startup?
        classes.add(OfbizRepositoryMappingJackrabbitUnstructured.class);
        classes.add(OfbizRepositoryMappingJackrabbitHierarchyNode.class);
        classes.add(OfbizRepositoryMappingJackrabbitNews.class);
        classes.add(OfbizRepositoryMappingJackrabbitFile.class);
        classes.add(OfbizRepositoryMappingJackrabbitFolder.class);
        classes.add(OfbizRepositoryMappingJackrabbitResource.class);
        classes.add(OfbizRepositoryMappingJackrabbitLocalizedContent.class);
        classes.add(OfbizRepositoryMappingJackrabbitArticle.class);

        Mapper mapper = new AnnotationMapperImpl(classes);
        this.ocm = new ObjectContentManagerImpl(session, mapper);

        return;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.RepositoryAccess#closeAccess()
     */
    @Override
    public void closeAccess() {
        if (this.ocm != null && this.ocm.getSession().isLive()) {
            this.ocm.logout();
        }
        this.ocm = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.RepositoryAccess#getContentObject(java.lang.String)
     */
    @Override
    public OfbizRepositoryMapping getContentObject(String nodePath) {
        ContentReader contentReader = new ContentReaderJackrabbit(this.ocm);
        return contentReader.getContentObject(nodePath);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.RepositoryAccess#storeContentObject(org.ofbiz.jcr.orm
     * .OfbizRepositoryMapping)
     */
    @Override
    public void storeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException, ItemExistsException {
        ContentWriter contentWriter = new ContentWriterJackrabbit(this.ocm);
        contentWriter.storeContentObject(orm);

        return;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.RepositoryAccess#updateContentObject(org.ofbiz.jcr.
     * orm.OfbizRepositoryMapping)
     */
    @Override
    public void updateContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException {
        ContentWriter contentWriter = new ContentWriterJackrabbit(this.ocm);
        contentWriter.updateContentObject(orm);

        return;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.RepositoryAccess#removeContentObject(java.lang.String)
     */
    @Override
    public void removeContentObject(String nodePath) throws ObjectContentManagerException {
        ContentWriter contentWriter = new ContentWriterJackrabbit(this.ocm);
        contentWriter.removeContentObject(nodePath);

        return;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ofbiz.jcr.orm.RepositoryAccess#removeContentObject(org.ofbiz.jcr.
     * orm.OfbizRepositoryMapping)
     */
    @Override
    public void removeContentObject(OfbizRepositoryMapping orm) throws ObjectContentManagerException {
        removeContentObject(orm.getPath());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ofbiz.jcr.orm.RepositoryAccess#getJsonFileTree()
     */
    @Override
    public JSONArray getJsonDataTree() throws RepositoryException {
        ContentReader contentReader = new ContentReaderJackrabbit(this.ocm);
        return contentReader.getJsonDataTree();
    }

    /*
     * (non-Javadoc)
     * @see org.ofbiz.jcr.access.RepositoryAccess#getJsonFileTree()
     */
    @Override
    public JSONArray getJsonFileTree() throws RepositoryException {
        ContentReader contentReader = new ContentReaderJackrabbit(this.ocm);
        return contentReader.getJsonFileTree();
    }
}
