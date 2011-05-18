package org.ofbiz.content.jcr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.PathNotFoundException;
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
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.FileUtil;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.content.jcr.helper.JcrFileHelperJackrabbit;
import org.ofbiz.content.jcr.helper.JcrTextHelperJackrabbit;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.helper.JcrFileHelper;
import org.ofbiz.jcr.helper.JcrTextHelper;

public class JackrabbitEvents {

    public static final String module = JackrabbitEvents.class.getName();

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String addNewTextMessageToJcrRepository(HttpServletRequest request, HttpServletResponse response) {
        String message = request.getParameter("message");
        String language = request.getParameter("msgLocale");

        JcrTextHelper jackrabbit = new JcrTextHelperJackrabbit(request);

        String newContentId = null;
        try {
            newContentId = jackrabbit.storeNewTextData(message, language);

            if (newContentId == null) {
                request.setAttribute("_ERROR_MESSAGE_", "Couldn't be created, maybe the node already exists. Use another node name to store you're content.");
                return "error";
            }

            request.setAttribute("newContentId", newContentId);
        } catch (PathNotFoundException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage()); // UtilProperties.getMessage(resourceErr,
                                                                     // "idealEvents.problemsGettingMerchantConfiguration",
                                                                     // locale)
            return "error";
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } finally {
            jackrabbit.closeSession();
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
            List<Map<String, String>> listIt = JackrabbitWorker.getRepositoryNodes(userLogin, "");
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
        JcrTextHelper jackrabbit = new JcrTextHelperJackrabbit(request);
        String language = UtilValidate.isNotEmpty(request.getParameter("language")) ? request.getParameter("language") : (String) request.getAttribute("language");
        String version = UtilValidate.isNotEmpty(request.getParameter("version")) ? request.getParameter("version") : (String) request.getAttribute("version");

        try {
            String textContent = null;
            if (UtilValidate.isEmpty(language)) {
                textContent = jackrabbit.getTextData();
            } else {
                if (UtilValidate.isEmpty(version)) {
                    textContent = jackrabbit.getTextData(language);
                } else {
                    textContent = jackrabbit.getTextData(language, version);
                }
            }
            request.setAttribute("message", textContent);
            List<String> availableLanguages = jackrabbit.getAvailableLanguages();
            List<Map<String, String>> langMap = new ArrayList<Map<String, String>>();
            for (String lang : availableLanguages) {
                Map<String, String> tmp = new HashMap<String, String>();
                tmp.put("localeId", lang);
                langMap.add(tmp);
            }

            List<String> availableVersions = jackrabbit.getAllLanguageVersions();
            List<Map<String, String>> versMap = new ArrayList<Map<String, String>>();
            for (String v : availableVersions) {
                Map<String, String> tmp = new HashMap<String, String>();
                tmp.put("version", v);
                versMap.add(tmp);
            }

            request.setAttribute("languageList", langMap);
            request.setAttribute("versionList", versMap);
            request.setAttribute("selectedLanguage", jackrabbit.getSelctedLanguage());
            request.setAttribute("currentLanguageVersion", jackrabbit.getCurrentLanguageVersion());
            request.setAttribute("currentBaseNodeVersion", jackrabbit.getCurrentBaseVersion());

        } catch (PathNotFoundException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } finally {
            jackrabbit.closeSession();
        }

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String updateRepositoryData(HttpServletRequest request, HttpServletResponse response) {
        String message = request.getParameter("message");
        String language = request.getParameter("selectedLanguage");

        JcrTextHelper jackrabbit = new JcrTextHelperJackrabbit(request);

        try {
            String textContent = null;
            if (UtilValidate.isEmpty(language)) {
                textContent = jackrabbit.updateTextData(message);
            } else {
                textContent = jackrabbit.updateTextData(message, language);
                request.setAttribute("language", language);
            }
            request.setAttribute("message", textContent);
        } catch (PathNotFoundException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } finally {
            jackrabbit.closeSession();
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
        JcrTextHelper jackrabbit = new JcrTextHelperJackrabbit(request);

        try {
            jackrabbit.removeRepositoryNode();
        } catch (PathNotFoundException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } finally {
            jackrabbit.closeSession();
        }

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String removeRepositoryFileNode(HttpServletRequest request, HttpServletResponse response) {
        JcrFileHelper jackrabbit = new JcrFileHelperJackrabbit(request);

        try {
            jackrabbit.removeRepositoryNode();
        } catch (PathNotFoundException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } finally {
            jackrabbit.closeSession();
        }

        return "success";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static String cleanJcrRepository(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {
            JackrabbitWorker.cleanJcrRepository(delegator, userLogin);
        } catch (GenericEntityException e) {
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        } catch (RepositoryException e) {
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
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
    public static String uploadFileData(HttpServletRequest request, HttpServletResponse response) {

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

        JcrFileHelper jackrabbit = new JcrFileHelperJackrabbit((GenericValue) request.getSession().getAttribute("userLogin"), (Delegator) request.getAttribute("delegator"), null, passedParams.get("repositoryNode"));

        if (file != null && file.length >= 1) {
            try {
                jackrabbit.uploadFileData(file, passedParams.get("completeFileName"));
            } catch (GenericEntityException e) {
                Debug.logError(e, module);
                request.setAttribute("_ERROR_MESSAGE_", e.toString());
                return "error";
            } catch (RepositoryException e) {
                Debug.logError(e, module);
                request.setAttribute("_ERROR_MESSAGE_", e.toString());
                return "error";
            } finally {
                jackrabbit.closeSession();
            }
        } else {
            jackrabbit.closeSession();
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
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        JcrFileHelper jackrabbit = new JcrFileHelperJackrabbit(userLogin, delegator, null, "/fileHome");

        try {
            JSONArray fileTree = jackrabbit.getJsonFileTree();
            request.setAttribute("fileTree", StringUtil.wrapString(fileTree.toString()));
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        } finally {
            jackrabbit.closeSession();
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
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        JcrTextHelper jackrabbit = new JcrTextHelperJackrabbit(userLogin, delegator, null, "/");

        try {
            JSONArray fileTree = jackrabbit.getJsonDataTree();
            request.setAttribute("dataTree", StringUtil.wrapString(fileTree.toString()));
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        } finally {
            jackrabbit.closeSession();
        }

        return "success";
    }

    public static String getFileFromRepository(HttpServletRequest request, HttpServletResponse response) {

        JcrFileHelper jackrabbit = new JcrFileHelperJackrabbit(request);
        InputStream file = null;
        try {
            file = jackrabbit.getFileContent();
            UtilHttp.streamContentToBrowser(response, IOUtils.toByteArray(file), jackrabbit.getFileMimeType(), jackrabbit.getNodeName());
        } catch (RepositoryException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        } catch (IOException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.toString());
            return "error";
        } finally {
            jackrabbit.closeSession();
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    Debug.logError(e, module);
                    request.setAttribute("_ERROR_MESSAGE_", e.toString());
                    return "error";
                }
            }
        }

        return "success";
    }
}