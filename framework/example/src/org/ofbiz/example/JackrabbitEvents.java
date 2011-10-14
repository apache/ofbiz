package org.ofbiz.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.nodetype.NodeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.FileUtil;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.RepositoryAccess;
import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;
import org.ofbiz.jcr.api.JcrArticleHelper;
import org.ofbiz.jcr.api.JcrContentHelper;
import org.ofbiz.jcr.api.JcrFileHelper;
import org.ofbiz.jcr.orm.OfbizRepositoryMapping;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitArticle;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFile;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFolder;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitHierarchyNode;
import org.ofbiz.jcr.util.jackrabbit.JcrUtilJackrabbit;

public class JackrabbitEvents {

    public static final String module = JackrabbitEvents.class.getName();

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String addNewTextMessageToJcrRepository(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        JcrArticleHelper articleHelper = new JcrArticleHelper(userLogin);

        String contentPath = request.getParameter("path");
        String language = request.getParameter("msgLocale");
        String title = request.getParameter("title");
        Calendar pubDate = new GregorianCalendar(); // TODO
        String content = request.getParameter("message");

        try {
            articleHelper.storeContentInRepository(contentPath, language, title, content, pubDate);
        } catch (ObjectContentManagerException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (ItemExistsException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } finally {
            articleHelper.closeContentSession();
        }

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String scanRepositoryStructure(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        try {
            List<Map<String, String>> listIt = JcrUtilJackrabbit.getRepositoryNodes(userLogin, "");
            request.setAttribute("listIt", listIt);
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String getNodeContent(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String contentPath = request.getParameter("path");

        String version = request.getParameter("versions");
        String language = request.getParameter("language");

        if (UtilValidate.isEmpty(contentPath)) {
            String msg = "A node path is missing, please pass the path to the node which should be read from the repository."; // TODO
            Debug.logError(msg, module);
            request.setAttribute("_ERROR_MESSAGE_", msg);
            return "error";
        }

        JcrArticleHelper articleHelper = new JcrArticleHelper(userLogin);
        OfbizRepositoryMappingJackrabbitArticle ormArticle = null;
        if (UtilValidate.isEmpty(version)) {
            ormArticle = articleHelper.readContentFromRepository(contentPath, language);
        } else {
            ormArticle = articleHelper.readContentFromRepository(contentPath, language, version);
        }

        request.setAttribute("path", ormArticle.getPath());
        request.setAttribute("language", ormArticle.getLanguage());
        request.setAttribute("title", ormArticle.getTitle());
        request.setAttribute("version", ormArticle.getVersion());
        request.setAttribute("versionList", articleHelper.getVersionListForCurrentArticle());
        request.setAttribute("languageList", articleHelper.getAvailableLanguageList());
        request.setAttribute("createDate", ormArticle.getCreationDate());
        request.setAttribute("content", ormArticle.getContent());

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String updateRepositoryData(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String contentPath = request.getParameter("path");
        JcrArticleHelper articleHelper = new JcrArticleHelper(userLogin);

        OfbizRepositoryMappingJackrabbitArticle ormArticle = articleHelper.readContentFromRepository(contentPath);

        // news.setLanguage(request.getParameter("language"));
        ormArticle.setTitle(request.getParameter("title"));
        ormArticle.setContent(request.getParameter("content"));
        // request.getParameter("pubDate")
        // request.getParameter("createDate")

        try {
            articleHelper.updateContentInRepository(ormArticle);
        } catch (ObjectContentManagerException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
        } finally {
            articleHelper.closeContentSession();
        }

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String removeRepositoryNode(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String contentPath = request.getParameter("path");

        JcrContentHelper helper = new JcrContentHelper(userLogin);
        helper.removeContentObject(contentPath);

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String uploadFileData(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        ServletFileUpload fu = new ServletFileUpload(new DiskFileItemFactory(10240, FileUtil.getFile("runtime/tmp")));
        List<FileItem> list = null;
        Map<String, String> passedParams = FastMap.newInstance();

        try {
            list = UtilGenerics.checkList(fu.parseRequest(request));
        } catch (FileUploadException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        }

        byte[] file = null;
        for (FileItem fi : list) {
            String fieldName = fi.getFieldName();

            if (fi.isFormField()) {
                String fieldStr = fi.getString();
                passedParams.put(fieldName, fieldStr);
            } else if (fieldName.startsWith("fileData")) {
                passedParams.put("completeFileName", fi.getName());
                file = fi.get();
            }
        }

        JcrFileHelper fileHelper = new JcrFileHelper(userLogin);

        try {

            fileHelper.storeContentInRepository(file, passedParams.get("completeFileName"), passedParams.get("path"));

        } catch (ObjectContentManagerException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
        } catch (ItemExistsException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
        } finally {
            fileHelper.closeContentSession();
        }

        return "success";
    }

    /**
     * Creates the FILE Tree as JSON Object
     *
     * @param request
     * @param response
     * @return
     */
    public static String getRepositoryFileTree(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        RepositoryAccess repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        try {
            JSONArray fileTree = repositoryAccess.getJsonFileTree();
            request.setAttribute("fileTree", StringUtil.wrapString(fileTree.toString()));
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("dataTree", new JSONArray());
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        } finally {
            repositoryAccess.closeAccess();
        }

        return "success";
    }

    /**
     * Creates the DATA (TEXT) Tree as JSON Object
     *
     * @param request
     * @param response
     * @return
     */
    public static String getRepositoryDataTree(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        RepositoryAccessJackrabbit repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        try {
            JSONArray fileTree = repositoryAccess.getJsonDataTree();
            request.setAttribute("dataTree", StringUtil.wrapString(fileTree.toString()));
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("dataTree", new JSONArray());
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            repositoryAccess.closeAccess();
            return "error";
        }

        List<String> contentList = new ArrayList<String>();
        Map<String, List<String>> languageList = FastMap.newInstance();
        Session session = repositoryAccess.getSession();
        Node root;
        try {
            root = session.getRootNode();
            getContentList(root, contentList);
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            repositoryAccess.closeAccess();
            return "error";
        } finally {
        }

        try {
        for (String path : contentList) {
            Node parent = session.getNode(path);
            NodeIterator ni = parent.getNodes();
            List<String> language = new ArrayList<String>();
            while (ni.hasNext()) {
                Node t = ni.nextNode();
                    if (t.hasProperty("localized") && t.getProperty("localized").getBoolean()) {
                        String l = t.getPath();
                        language.add(l.substring(l.lastIndexOf("/") + 1));
                    }
            }
            languageList.put(path, language);
        }
        } catch (ValueFormatException e) {
            Debug.logError(e, module);
        } catch (PathNotFoundException e) {
            Debug.logError(e, module);
        } catch (RepositoryException e) {
            Debug.logError(e, module);
        } finally {
            repositoryAccess.closeAccess();
        }

        request.setAttribute("contentList", contentList);
        JSONObject jo = new JSONObject();
        jo.putAll(languageList);
        request.setAttribute("languageList", jo);

        return "success";
    }

    public static String getFileFromRepository(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String contentPath = request.getParameter("path");

        if (UtilValidate.isEmpty(contentPath)) {
            String msg = "A node path is missing, please pass the path to the node which should be read from the repository."; // TODO
            Debug.logError(msg, module);
            request.setAttribute("_ERROR_MESSAGE_", msg);
            return "error";
        }

        JcrFileHelper fileHelper = new JcrFileHelper(userLogin);
        OfbizRepositoryMappingJackrabbitHierarchyNode orm = fileHelper.getRepositoryContent(contentPath);

        if (fileHelper.isFileContent()) {
            OfbizRepositoryMappingJackrabbitFile file = (OfbizRepositoryMappingJackrabbitFile) orm;
            InputStream fileStream = file.getResource().getData();

            String fileName = file.getPath();
            if (fileName.indexOf("/") != -1) {
                fileName = fileName.substring(fileName.indexOf("/") + 1);
            }

            try {
                UtilHttp.streamContentToBrowser(response, IOUtils.toByteArray(fileStream), file.getResource().getMimeType(), fileName);
            } catch (IOException e) {
                Debug.logError(e, module);
                request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                return "error";
            }
        } else {
            Debug.logWarning("This content is no file content, the content is from the type: " + orm.getClass().getName(), module);
        }
        return "success";
    }

    public static String getFileInformation(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String contentPath = request.getParameter("path");

        JcrFileHelper fileHelper = new JcrFileHelper(userLogin);
        OfbizRepositoryMapping orm = fileHelper.getRepositoryContent(contentPath);

        // Here we can differentiate between a file or folder content
        if (fileHelper.isFileContent()) {
            OfbizRepositoryMappingJackrabbitFile file = (OfbizRepositoryMappingJackrabbitFile) orm;
            request.setAttribute("fileName", file.getPath());
            request.setAttribute("fileLastModified", file.getResource().getLastModified().getTime());
            request.setAttribute("fileMimeType", file.getResource().getMimeType());
            request.setAttribute("fileCreationDate", file.getCreationDate().getTime());
        } else if (fileHelper.isFolderContent()) {
            OfbizRepositoryMappingJackrabbitFolder folder = (OfbizRepositoryMappingJackrabbitFolder) orm;
            request.setAttribute("fileName", folder.getPath());
            request.setAttribute("fileCreationDate", folder.getCreationDate().getTime());
        }

        return "success";
    }

    private static void getContentList(Node startNode, List<String> contentList) throws RepositoryException {
        NodeIterator ni = startNode.getNodes();
        while (ni.hasNext()) {
            Node tmpNode = ni.nextNode();
            if (tmpNode.getPrimaryNodeType().isNodeType(NodeType.NT_UNSTRUCTURED) && (!tmpNode.hasProperty("localized") || !tmpNode.getProperty("localized").getBoolean())) {
                contentList.add(tmpNode.getPath());
                if (tmpNode.hasNodes()) {
                    getContentList(tmpNode, contentList);
                }
            }
        }
    }
}