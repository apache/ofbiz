package org.ofbiz.jcr.orm.jackrabbit;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Bean;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.ofbiz.jcr.access.jackrabbit.ConstantsJackrabbit;

@Node(jcrType = "nt:file", extend = OfbizRepositoryMappingJackrabbitHierarchyNode.class)
public class OfbizRepositoryMappingJackrabbitFile extends OfbizRepositoryMappingJackrabbitHierarchyNode {

    @Bean(jcrName = "jcr:content")
    private OfbizRepositoryMappingJackrabbitResource resource;

    public OfbizRepositoryMappingJackrabbitResource getResource() {
        return resource;
    }

    public void setResource(OfbizRepositoryMappingJackrabbitResource resource) {
        this.resource = resource;
    }

    public void setPath(String nodePath) {
        // check that the path don't end with a /
        if (nodePath.endsWith(ConstantsJackrabbit.ROOTPATH)) {
            nodePath = nodePath.substring(0, nodePath.indexOf("/"));
        }

        // check that it is a relative path
        if (nodePath.indexOf("/") != -1) {
            nodePath = nodePath.substring(nodePath.indexOf("/") + 1);
        }

        super.path = nodePath;
    }
}
