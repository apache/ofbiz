package org.ofbiz.jcr.access.jackrabbit;

import javax.jcr.nodetype.NodeType;

public enum NodePropertiesJackrabbit {
    MESSAGE("jcr:message"), FILE(NodeType.NT_FILE), FOLDER(NodeType.NT_FOLDER), RESOURCE(NodeType.NT_RESOURCE), DATA("jcr:data"), UNSTRUCTURED(NodeType.NT_UNSTRUCTURED), MIMETYPE("jcr:mimeType"), REPROOT("rep:root"), mixInLANGUAGE("mix:language"), mixInVERSIONING(
            "mix:versionable"), mixInTITLE("mix:title"), LANGUAGE("jcr:language"), TITLE("jcr:title"), DESCRIPTION("jcr:description");

    String type = null;

    NodePropertiesJackrabbit(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
