package org.ofbiz.jcr.access.jackrabbit;

import javax.jcr.nodetype.NodeType;

import org.ofbiz.jcr.access.Constants;

public class ConstantsJackrabbit implements Constants {
    protected enum PROPERTY_FIELDS {
        MESSAGE("jcr:message"), FILE(NodeType.NT_FILE), FOLDER(NodeType.NT_FOLDER), RESOURCE(NodeType.NT_RESOURCE), DATA("jcr:data"), UNSTRUCTURED(NodeType.NT_UNSTRUCTURED), MIMETYPE("jcr:mimeType"), REPROOT("rep:root"), mixInLANGUAGE("mix:language"), mixInVERSIONING(
                "mix:versionable"), mixInTITLE("mix:title"), LANGUAGE("jcr:language"), TITLE("jcr:title"), DESCRIPTION("jcr:description");

        String type = null;

        PROPERTY_FIELDS(String type) {
            this.type = type;
        }

        String getType() {
            return this.type;
        }

    };

    public static String ROOTPATH = "/";
    public static String FILEROOT = ROOTPATH + "fileHome";
}
