package org.ofbiz.content.jcr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastMap;
import net.sf.json.JSONArray;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.FileUtil;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.RepositoryAccess;
import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFile;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitFolder;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitNews;
import org.ofbiz.jcr.orm.jackrabbit.OfbizRepositoryMappingJackrabbitResource;
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
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");

        String nodePath = request.getParameter("path");
        String language = request.getParameter("msgLocale");
        String title = request.getParameter("title");
        Calendar pubDate = new GregorianCalendar(); // TODO
        String content = request.getParameter("message");

        OfbizRepositoryMappingJackrabbitNews orm = new OfbizRepositoryMappingJackrabbitNews(nodePath, language, title, pubDate, content);

        RepositoryAccess repositoryAccess = null;
        try {
            repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
            repositoryAccess.storeContentObject(orm);
        } catch (ObjectContentManagerException ocme) {
            Debug.logError(ocme, module);
            request.setAttribute("_ERROR_MESSAGE_", ocme.toString());
            return "error";
        } finally {
            repositoryAccess.closeAccess();
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
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");

        String node = request.getParameter("path");

        if (UtilValidate.isEmpty(node)) {
            String msg = "A node path is missing, please pass the path to the node which should be read from the repository."; // TODO
            Debug.logError(msg, module);
            request.setAttribute("_ERROR_MESSAGE_", msg);
            return "error";
        }

        RepositoryAccess repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        OfbizRepositoryMappingJackrabbitNews news = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject(node);

        request.setAttribute("contentObject", news);
        request.setAttribute("path", news.getPath());
        request.setAttribute("language", news.getLanguage());
        request.setAttribute("pubDate", news.getPubDate());
        request.setAttribute("title", news.getTitle());
        request.setAttribute("version", news.getVersion());
        request.setAttribute("createDate", news.getCreationDate());
        request.setAttribute("content", news.getContent());

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String updateRepositoryData(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");

        String path = request.getParameter("path");

        RepositoryAccess repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        OfbizRepositoryMappingJackrabbitNews news = (OfbizRepositoryMappingJackrabbitNews) repositoryAccess.getContentObject(path);

        news.setLanguage(request.getParameter("language"));
        news.setTitle(request.getParameter("title"));
        news.setContent(request.getParameter("content"));
        // request.getParameter("pubDate")
        // request.getParameter("createDate")

        repositoryAccess.updateContentObject(news);
        repositoryAccess.closeAccess();

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String removeRepositoryNode(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");

        String path = request.getParameter("path");

        RepositoryAccess repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        repositoryAccess.removeContentObject(path);
        repositoryAccess.closeAccess();
        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String cleanJcrRepository(HttpServletRequest request, HttpServletResponse response) {

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String uploadFileData(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");
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

        OfbizRepositoryMappingJackrabbitResource ormResource = new OfbizRepositoryMappingJackrabbitResource();
        ormResource.setData(new ByteArrayInputStream(file));
        ormResource.setMimeType(getMimeTypeFromInputStream(new ByteArrayInputStream(file)));
        ormResource.setLastModified(new GregorianCalendar());

        OfbizRepositoryMappingJackrabbitFile ormFile = new OfbizRepositoryMappingJackrabbitFile();
        ormFile.setCreationDate(new GregorianCalendar());
        ormFile.setResource(ormResource);

        // ormFile.setPath(passedParams.get("path") + "/" +
        // passedParams.get("completeFileName"));
        ormFile.setPath(passedParams.get("completeFileName"));
        // ormFile.setFileName(passedParams.get("completeFileName"));

        OfbizRepositoryMappingJackrabbitFolder ormFolder = new OfbizRepositoryMappingJackrabbitFolder();
        ormFolder.addChild(ormFile);
        ormFolder.setPath(passedParams.get("path"));

        RepositoryAccess repositoryAcces = new RepositoryAccessJackrabbit(userLogin);
        repositoryAcces.storeContentObject(ormFolder);

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
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");

        RepositoryAccess repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        try {
            JSONArray fileTree = repositoryAccess.getJsonFileTree();
            request.setAttribute("fileTree", StringUtil.wrapString(fileTree.toString()));
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("dataTree", new JSONArray());
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
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
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");

        RepositoryAccess repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        try {
            JSONArray fileTree = repositoryAccess.getJsonDataTree();
            request.setAttribute("dataTree", StringUtil.wrapString(fileTree.toString()));
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("dataTree", new JSONArray());
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        }

        return "success";
    }

    public static String getFileFromRepository(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getAttribute("userLogin");

        String node = request.getParameter("path");

        if (UtilValidate.isEmpty(node)) {
            String msg = "A node path is missing, please pass the path to the node which should be read from the repository."; // TODO
            Debug.logError(msg, module);
            request.setAttribute("_ERROR_MESSAGE_", msg);
            return "error";
        }

        RepositoryAccess repositoryAccess = new RepositoryAccessJackrabbit(userLogin);
        OfbizRepositoryMappingJackrabbitFile file = (OfbizRepositoryMappingJackrabbitFile) repositoryAccess.getContentObject(node);

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

        return "success";
    }

    public static String getFileInformation(HttpServletRequest request, HttpServletResponse resposne) {

        return "success";
    }

    private static String getMimeTypeFromInputStream(InputStream is) {
        if (!TikaInputStream.isTikaInputStream(is)) {
            is = TikaInputStream.get(is);
        }
        Tika tika = new Tika();
        try {
            return tika.detect(is);
        } catch (IOException e) {
            Debug.logError(e, module);
            return "application/octet-stream";
        }
    }
}