package org.ofbiz.jcr.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFile;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFolder;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitHierarchyNode;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitResource;
import org.ofbiz.jcr.util.jackrabbit.JcrUtilJackrabbit;

/**
 * This Helper class encapsulate the jcr file content bean. it provide all
 * attributes and operations which are necessary to work with the content
 * repository.
 *
 * The concrete implementations covers the different content use case related
 * workflows. I.E. Different behavior for File/Folder or Text content.
 *
 * The Helper classes should be build on top of the generic JCR implementation
 * in the Framework.
 *
 */
public class JcrFileHelper extends AbstractJcrHelper {

    private final static String module = JcrFileHelper.class.getName();

    private OfbizRepositoryMappingJackrabbitHierarchyNode hierarchy = null;

    public JcrFileHelper(GenericValue userLogin) {
        access = new RepositoryAccessJackrabbit(userLogin);
    }

    /**
     * Returns a content file object from the repository. Throws an Exception
     * when the read content type is not an article content type.
     *
     * @param contentPath
     * @return
     * @throws
     */
    public OfbizRepositoryMappingJackrabbitHierarchyNode getRepositoryContent(String contentPath) throws ClassCastException {
        OfbizRepositoryMapping orm = access.getContentObject(contentPath);

        if (orm instanceof OfbizRepositoryMappingJackrabbitFile) {
            OfbizRepositoryMappingJackrabbitFile fileObj = (OfbizRepositoryMappingJackrabbitFile) orm;
            hierarchy = fileObj;
            return fileObj;
        } else if (orm instanceof OfbizRepositoryMappingJackrabbitFolder) {
            OfbizRepositoryMappingJackrabbitFolder fileObj = (OfbizRepositoryMappingJackrabbitFolder) orm;
            hierarchy = fileObj;
            return fileObj;
        }

        throw new ClassCastException("The content object for the path: " + contentPath + " is not a file content object. This Helper can only handle content objects with the type: " + OfbizRepositoryMappingJackrabbitFile.class.getName());
    }

    /**
     * Returns a content file object in the passed version from the repository.
     * Throws an Exception when the read content type is not an article content
     * type.
     *
     * @param contentPath
     * @return
     * @throws
     */
    public OfbizRepositoryMappingJackrabbitHierarchyNode getRepositoryContent(String contentPath, String version) throws ClassCastException {
        OfbizRepositoryMapping orm = access.getContentObject(contentPath, version);

        if (orm instanceof OfbizRepositoryMappingJackrabbitFile) {
            OfbizRepositoryMappingJackrabbitFile fileObj = (OfbizRepositoryMappingJackrabbitFile) orm;
            hierarchy = fileObj;
            return fileObj;
        } else if (orm instanceof OfbizRepositoryMappingJackrabbitFolder) {
            OfbizRepositoryMappingJackrabbitFile fileObj = (OfbizRepositoryMappingJackrabbitFile) orm;
            hierarchy = fileObj;
            return fileObj;
        }

        throw new ClassCastException("The content object for the path: " + contentPath + " is not a file content object. This Helper can only handle content objects with the type: " + OfbizRepositoryMappingJackrabbitFile.class.getName());
    }

    /**
     * Stores a new file content object in the repository.
     *
     * @param fileData
     * @param fileName
     * @param folderPath
     * @param mimeType
     * @throws ObjectContentManagerException
     * @throws RepositoryException
     */
    public void storeContentInRepository(byte[] fileData, String fileName, String folderPath) throws ObjectContentManagerException, RepositoryException {

        // create an ORM Resource Object
        OfbizRepositoryMappingJackrabbitResource ormResource = new OfbizRepositoryMappingJackrabbitResource();
        ormResource.setData(new ByteArrayInputStream(fileData));
        ormResource.setMimeType(getMimeTypeFromInputStream(new ByteArrayInputStream(fileData)));
        ormResource.setLastModified(new GregorianCalendar());

        // create an ORM File Object
        OfbizRepositoryMappingJackrabbitFile ormFile = new OfbizRepositoryMappingJackrabbitFile();
        ormFile.setCreationDate(new GregorianCalendar());
        ormFile.setResource(ormResource);
        ormFile.setPath(fileName);

        // Create the folder if necessary, otherwise we just update the folder content
        folderPath = JcrUtilJackrabbit.createAbsoluteNodePath(folderPath);
        if (access.getSession().itemExists(folderPath)) {
            OfbizRepositoryMapping orm = access.getContentObject(folderPath);
            if (orm instanceof OfbizRepositoryMappingJackrabbitFolder) {
                OfbizRepositoryMappingJackrabbitFolder ormFolder = (OfbizRepositoryMappingJackrabbitFolder) orm;
                ormFolder.addChild(ormFile);
                access.updateContentObject(ormFolder);
            }
        } else {
            // create the ORM folder Object
            OfbizRepositoryMappingJackrabbitFolder ormFolder = new OfbizRepositoryMappingJackrabbitFolder();
            ormFolder.addChild(ormFile);
            ormFolder.setPath(folderPath);

            access.storeContentObject(ormFolder);
        }


    }

    /**
     * Returns TRUE if the current content is a file content (Type: OfbizRepositoryMappingJackrabbitFile)
     * @return
     */
    public boolean isFileContent() {
        return (hierarchy instanceof OfbizRepositoryMappingJackrabbitFile);
    }

    /**
     * Returns TRUE if the current content is a folder content (Type: OfbizRepositoryMappingJackrabbitFolder)
     * @return
     */
    public boolean isFolderContent() {
        return (hierarchy instanceof OfbizRepositoryMappingJackrabbitFolder);
    }

    private static String getMimeTypeFromInputStream(InputStream is) {
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
