package org.ofbiz.jcr.api.jackrabbit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.jackrabbit.ConstantsJackrabbit;
import org.ofbiz.jcr.access.jackrabbit.JackrabbitRepositoryAccessor;
import org.ofbiz.jcr.api.JcrDataHelper;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;
import org.ofbiz.jcr.orm.jackrabbit.JackrabbitArticle;

/**
 * This Helper class encapsulate the jcr article content bean. it provide all
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
public class JackrabbitArticleHelper extends JackrabbitAbstractHelper implements JcrDataHelper {

    private final static String module = JackrabbitArticleHelper.class.getName();

    private static JackrabbitArticle article = null;

    private static List<String> possibleLocales = null;

    static {
        if (UtilValidate.isEmpty(possibleLocales)) {
            possibleLocales = new ArrayList<String>();
            List<Locale> locales = org.ofbiz.base.util.UtilMisc.availableLocales();
            for (Locale locale : locales) {
                possibleLocales.add(locale.toString());
            }
        }
    }

    /**
     * Setup my content Object
     */
    public JackrabbitArticleHelper(GenericValue userLogin) {
        super(new JackrabbitRepositoryAccessor(userLogin));
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.api.jackrabbit.DataHelper#readContentFromRepository(java.lang.String)
     */
    @Override
    public JackrabbitArticle readContentFromRepository(String contentPath) throws ClassCastException {
        return readContentFromRepository(contentPath, "");
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.api.jackrabbit.DataHelper#readContentFromRepository(java.lang.String, java.lang.String)
     */
    @Override
    public JackrabbitArticle readContentFromRepository(String contentPath, String language) throws ClassCastException {
        contentPath = determineContentLanguagePath(contentPath, language);

        OfbizRepositoryMapping orm = super.access.getContentObject(contentPath);

        if (orm instanceof JackrabbitArticle) {
            article = (JackrabbitArticle) orm;
            article.setVersion(super.access.getBaseVersion(contentPath));
            return article;
        } else {
            throw new ClassCastException("The content object for the path: " + contentPath + " is not an article content object. This Helper can only handle content objects with the type: " + JackrabbitArticle.class.getName());
        }
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.api.jackrabbit.DataHelper#readContentFromRepository(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public JackrabbitArticle readContentFromRepository(String contentPath, String language, String version) throws ClassCastException {
        contentPath = determineContentLanguagePath(contentPath, language);
        OfbizRepositoryMapping orm = super.access.getContentObject(contentPath, version);

        if (orm instanceof JackrabbitArticle) {
            article = (JackrabbitArticle) orm;
            article.setPath(contentPath); // the content path must be
                                          // manipulated because, the jackrabbit
                                          // orm returns a full blown path with
                                          // version information.
            return article;
        } else {
            throw new ClassCastException("The content object for the path: " + contentPath + " is not an article content object. This Helper can only handle content objects with the type: " + JackrabbitArticle.class.getName());
        }
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.api.jackrabbit.DataHelper#storeContentInRepository(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Calendar)
     */
    @Override
    public void storeContentInRepository(String contentPath, String language, String title, String content, Calendar publicationDate) throws ObjectContentManagerException, ItemExistsException {
        if (UtilValidate.isEmpty(language)) {
            language = determindeTheDefaultLanguage();
        }

        // construct the content article object
        article = new JackrabbitArticle(contentPath, language, title, content, publicationDate);

        super.access.storeContentObject(article);

    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.api.jackrabbit.DataHelper#updateContentInRepository(org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitArticle)
     */
    @Override
    public void updateContentInRepository(JackrabbitArticle updatedArticle) throws RepositoryException, ObjectContentManagerException {
        // if the item not already exist create it.
        if (!super.access.getSession().itemExists(updatedArticle.getPath())) {
            Debug.logWarning("This content object with the path: " + updatedArticle.getPath() + " doesn't exist in the repository. It will now created.", module);
            this.storeContentInRepository(updatedArticle.getPath(), updatedArticle.getLanguage(), updatedArticle.getTitle(), updatedArticle.getContent(), updatedArticle.getPubDate());
            return;
        }

        super.access.updateContentObject(updatedArticle);
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.api.jackrabbit.DataHelper#getVersionListForCurrentArticle()
     */
    @Override
    public List<String> getVersionListForCurrentArticle() {
        List<String> versions = new ArrayList<String>();
        ;

        if (article != null) {
            versions = super.access.getVersionList(article.getPath());
        } else {
            Debug.logWarning("No Article is loaded from the repository, please load an article first before requesting the version list.", module);
            versions = new ArrayList<String>(1);
        }

        return versions;
    }

    /* (non-Javadoc)
     * @see org.ofbiz.jcr.api.jackrabbit.DataHelper#getAvailableLanguageList()
     */
    @Override
    public List<String> getAvailableLanguageList() {
        List<String> languages = new ArrayList<String>();

        if (article != null && article.getLocalized()) {
            Session session = super.access.getSession();

            try {
                Node node = session.getNode(article.getPath()).getParent();
                NodeIterator nodes = node.getNodes();
                while (nodes.hasNext()) {
                    Node tmpNode = nodes.nextNode();
                    // only use nodes which have the language mix in
                    if (tmpNode.hasProperty("localized") && tmpNode.getProperty("localized").getBoolean()) {
                        String l = tmpNode.getPath();
                        languages.add(l.substring(l.lastIndexOf(ConstantsJackrabbit.NODEPATHDELIMITER) + 1));
                    }
                }

            } catch (PathNotFoundException e) {
                Debug.logError(e, module);
            } catch (RepositoryException e) {
                Debug.logError(e, module);
            }

        } else {
            Debug.logWarning("No Article is loaded from the repository, please load an article first before requesting the version list.", module);
        }

        return languages;
    }

    /**
     * This method should determine the correct language for the content. It
     * covers the case when the passed language is not available.
     *
     * A default (system) language will be taken, if the passed language, does
     * not exist, if no default language node is specified the first language
     * node which will be found will be choose.
     *
     * @param contentPath
     * @param language
     * @return
     */
    private String determineContentLanguagePath(String contentPath, String language) {
        // return if only the root node path is requested
        if (ConstantsJackrabbit.ROOTPATH.equals(contentPath)) {
            return contentPath;
        }

        // we have to check if the content path already contains a language and
        // if the language is already in the path we have to check if it is
        // equal to the passed language

        // we split the path string in chunks
        String[] path = contentPath.split(ConstantsJackrabbit.NODEPATHDELIMITER);

        // chunk if the last chunk contains a language flag
        StringBuffer canonicalizedContentPath = new StringBuffer(ConstantsJackrabbit.NODEPATHDELIMITER);
        if (possibleLocales.contains(path[path.length - 1])) {
            if (UtilValidate.isEmpty(language)) {
                language = path[path.length - 1];
            }
            for (int i = 0; i < path.length - 1; i++) {
                if (UtilValidate.isNotEmpty(path[i])) {
                    canonicalizedContentPath.append(path[i]).append(ConstantsJackrabbit.NODEPATHDELIMITER);
                }
            }
        } else {
            for (String p : path) {
                if (UtilValidate.isNotEmpty(p)) {
                    canonicalizedContentPath.append(p).append(ConstantsJackrabbit.NODEPATHDELIMITER);
                }
            }
        }

        // check if this language exist in the repository
        Session session = super.access.getSession();
        try {
            // check if the node exist OR if the node has NO localized flag OR
            // the localized flag is set to false
            if (!session.itemExists(canonicalizedContentPath.toString() + language)
                    || (!session.getNode(canonicalizedContentPath.toString() + language).hasProperty("localized") || !session.getNode(canonicalizedContentPath.toString() + language).getProperty("localized").getBoolean())) {
                // check for default language
                if (!session.itemExists(canonicalizedContentPath.toString() + determindeTheDefaultLanguage())) {
                    // return the first available language
                    NodeIterator ni = session.getNode(canonicalizedContentPath.toString()).getNodes();
                    while (ni.hasNext()) {
                        Node n = ni.nextNode();
                        if (possibleLocales.contains(n.getName())) {
                            contentPath = n.getPath();
                            break;
                        }
                    }
                    ni = null;
                } else {
                    contentPath = canonicalizedContentPath.toString() + determindeTheDefaultLanguage();
                }
            } else {
                contentPath = canonicalizedContentPath.toString() + language;
            }
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        }

        return contentPath;
    }

    private String determindeTheDefaultLanguage() {
        return UtilProperties.getPropertyValue("general", "locale.properties.fallback");
    }
}
