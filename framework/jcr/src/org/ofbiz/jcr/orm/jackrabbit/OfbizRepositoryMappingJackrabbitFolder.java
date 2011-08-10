package org.ofbiz.jcr.orm.jackrabbit;

import java.util.ArrayList;
import java.util.List;

import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.NTCollectionConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrType = "nt:folder", extend = OfbizRepositoryMappingJackrabbitHierarchyNode.class)
public class OfbizRepositoryMappingJackrabbitFolder extends OfbizRepositoryMappingJackrabbitHierarchyNode {
    @Collection(autoUpdate = false, elementClassName = OfbizRepositoryMappingJackrabbitHierarchyNode.class, collectionConverter = NTCollectionConverterImpl.class)
    private List<OfbizRepositoryMappingJackrabbitHierarchyNode> children;

    public List<OfbizRepositoryMappingJackrabbitHierarchyNode> getChildren() {
        return children;
    }

    public void setChildren(List<OfbizRepositoryMappingJackrabbitHierarchyNode> children) {
        this.children = children;
    }

    public void addChild(OfbizRepositoryMappingJackrabbitHierarchyNode node) {
        if (children == null) {
            children = new ArrayList<OfbizRepositoryMappingJackrabbitHierarchyNode>();
        }
        children.add(node);
    }

}
