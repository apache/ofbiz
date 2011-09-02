package org.ofbiz.jcr.orm.jackrabbit;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;

@Node(isAbstract = true, extend = OfbizRepositoryMappingJackrabbitUnstructured.class)
public abstract class OfbizRepositoryMappingJackrabbitLocalizedContent extends OfbizRepositoryMappingJackrabbitUnstructured {

    @Field
    private String language;

    public OfbizRepositoryMappingJackrabbitLocalizedContent() {
        super();
        super.setLocalized(true);
        // create an empty localized object
    }

    /**
     *
     * @param nodePath
     * @param language
     */
    public OfbizRepositoryMappingJackrabbitLocalizedContent(String nodePath, String language) {
        this.language = language;
        setLocalizedPath(nodePath);

        // define this node as a localized node
        super.setLocalized(true);
    }

    /**
     * The path of a localized node is always a combination of path and language
    */
    private void setLocalizedPath(String path) {
        // determine a default language
        if (UtilValidate.isEmpty(language)) {
            language = determindeTheDefaultLanguage();
        }

        path = path + "/" + language;
        super.setPath(path);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;

        // if the language is set after the path make sure the new language fits to the path
        if (!super.getPath().endsWith(language) || !super.getPath().endsWith(language + "/")) {
            String newPath = super.getPath();
            if (newPath.endsWith("/")) {
                // cut a trailing slash /
                newPath = newPath.substring(0, newPath.lastIndexOf("/"));
            }

            // cut the old trailing language
            newPath = newPath.substring(0, newPath.lastIndexOf("/"));
            // write the new path
            setLocalizedPath(newPath);
        }

    }

    private String determindeTheDefaultLanguage() {
        return language = UtilProperties.getPropertyValue("general", "locale.properties.fallback");
    }

}
