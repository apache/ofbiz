package org.ofbiz.jcr.orm.jackrabbit;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.jcr.access.jackrabbit.ConstantsJackrabbit;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

@Node(isAbstract = true)
public abstract class OfbizRepositoryMappingJackrabbitUnstructured implements OfbizRepositoryMapping {

    protected static String module = OfbizRepositoryMappingJackrabbitUnstructured.class.getName();

    @Field(path = true)
    private String path;
    @Field
    private String language;
    @Field
    private String version;
    @Field(jcrName = "jcr:created")
    private Calendar creationDate;

    protected OfbizRepositoryMappingJackrabbitUnstructured() {
        // create an empty object
    }

    protected OfbizRepositoryMappingJackrabbitUnstructured(String nodePath, String language) {
        this.setPath(nodePath);
        this.creationDate = new GregorianCalendar();
        if (UtilValidate.isEmpty(language)) {
            language = UtilProperties.getPropertyValue("general", "locale.properties.fallback");
        }
        this.language = language;
    }

    public String getPath() {
        return path;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public void setPath(String nodePath) {
        // check if the node path is an absolute path
        if (!nodePath.startsWith(ConstantsJackrabbit.ROOTPATH)) {
            nodePath = ConstantsJackrabbit.ROOTPATH + nodePath;
        }

        this.path = nodePath;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
