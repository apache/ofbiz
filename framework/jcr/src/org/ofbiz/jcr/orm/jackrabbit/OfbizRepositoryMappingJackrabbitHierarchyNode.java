package org.ofbiz.jcr.orm.jackrabbit;

import java.util.Calendar;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.ofbiz.jcr.access.jackrabbit.ConstantsJackrabbit;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;

@Node(jcrType = "nt:hierarchyNode")
public class OfbizRepositoryMappingJackrabbitHierarchyNode implements OfbizRepositoryMapping {
    @Field(path = true, id = true, jcrProtected = true)
    protected String path;
    @Field(jcrName = "jcr:created")
    private Calendar creationDate;

    public String getPath() {
        return path;
    }

    public void setPath(String nodePath) {
        // check if the node path is an absolute path
        if (!nodePath.startsWith(ConstantsJackrabbit.ROOTPATH)) {
            nodePath = ConstantsJackrabbit.ROOTPATH + nodePath;
        }

        this.path = nodePath;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }
}
