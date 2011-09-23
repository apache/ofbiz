package org.ofbiz.jcr.orm.jackrabbit;

import java.util.GregorianCalendar;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(isAbstract = true, extend = OfbizRepositoryMappingJackrabbitUnstructured.class)
public abstract class OfbizRepositoryMappingJackrabbitLocalizedContent extends OfbizRepositoryMappingJackrabbitUnstructured {

    @Field
    private String language;

    public OfbizRepositoryMappingJackrabbitLocalizedContent() {
        super();
        this.language = "";
        super.setLocalized(true);
        super.setCreationDate(new GregorianCalendar());
        // create an empty localized object
    }

    /**
     *
     * @param nodePath
     * @param language
     */
    public OfbizRepositoryMappingJackrabbitLocalizedContent(String nodePath, String language) {
        super(nodePath);
        this.language = language;

        // define this node as a localized node
        super.setLocalized(true);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
