package org.ofbiz.jcr.orm.jackrabbit;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.ofbiz.jcr.access.jackrabbit.ConstantsJackrabbit;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

@Node(isAbstract = true)
public abstract class OfbizRepositoryMappingJackrabbitUnstructured implements OfbizRepositoryMapping {

    protected static String module = OfbizRepositoryMappingJackrabbitUnstructured.class.getName();

    @Field(path = true) private String path;
    @Field private String version;
    @Field(jcrName = "jcr:created") private Calendar creationDate;
    @Field private boolean localized;

    protected OfbizRepositoryMappingJackrabbitUnstructured() {
        // create an empty object
    }

    protected OfbizRepositoryMappingJackrabbitUnstructured(String nodePath) {
        this.setPath(nodePath);
        this.creationDate = new GregorianCalendar();
        this.localized = false;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getLocalized() {
        return localized;
    }

    public void setLocalized(boolean isLocalized) {
        this.localized = isLocalized;
    }

}
