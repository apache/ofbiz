package org.ofbiz.jcr.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.jackrabbit.RepositoryAccessJackrabbit;

/**
 * This Helper class encapsulate the jcr content access. It provide all
 * attributes and operations which are necessary to work with the content
 * repository.
 *
 * The concrete implementations covers the different content use case related
 * workflows. I.E. Different behavior for File/Folder or Text content.
 *
 * The Helper classes should be build on top of the generic JCR implementation
 * in the Framework.
 *
 */
public class JcrContentHelper extends AbstractJcrHelper {

    private static String module = JcrContentHelper.class.getName();

    /**
     * Create a default content helper object.
     *
     * @param userLogin
     */
    public JcrContentHelper(GenericValue userLogin) {
        access = new RepositoryAccessJackrabbit(userLogin);
    }

    public List<Map<String, String>> queryData(String query) throws RepositoryException {
        QueryResult qr = access.queryForRepositoryData(query);

        List<Map<String, String>> resultNodePaths = new ArrayList<Map<String, String>>();
        RowIterator rows = qr.getRows();
        while (rows.hasNext()) {
            Row row = rows.nextRow();
            Map<String, String> content = FastMap.newInstance();
            content.put("path", row.getPath());
            content.put("score", String.valueOf(row.getScore()));

            resultNodePaths.add(content);
            if (Debug.isOn(Debug.INFO)) {
                Debug.logInfo("For query: " + query + " found node with path: " + row.getPath(), module);
            }

        }

        return resultNodePaths;
    }
}
