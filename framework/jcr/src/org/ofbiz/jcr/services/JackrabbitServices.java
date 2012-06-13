/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ofbiz.jcr.services;

import java.util.Date;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.VersionManager;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.jcr.access.jackrabbit.JackrabbitRepositoryAccessor;
import org.ofbiz.jcr.loader.JCRFactoryUtil;
import org.ofbiz.jcr.orm.jackrabbit.data.JackrabbitNews;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;

public class JackrabbitServices {

    private static String module = JackrabbitServices.class.getName();

    public static Map<String, Object> determineJackrabbitRepositorySpeed(DispatchContext ctx, Map<String, Object> context) throws UnsupportedRepositoryOperationException, RepositoryException {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Integer maxNodes = (Integer) context.get("maxNodes");

        Long start = 0l;
        Long diff = 0l;

        Session session = JCRFactoryUtil.getSession(ctx.getDelegator());
        VersionManager vm = session.getWorkspace().getVersionManager();
        start = new Date().getTime();
        for (int i = 0; i <= maxNodes; i++) {
            try {
                // add a node
                Node n = session.getRootNode().addNode("__Speedtest_Node-" + i);
                n.addMixin("mix:versionable");
                n.setProperty("anyProperty", "Blah");
                session.save();
                vm.checkin(n.getPath());

                vm.checkout("/__Speedtest_Node-" + i);
                // remove the node
                session.removeItem("/__Speedtest_Node-" + i);
                session.save();
            } catch (Exception e) {
                Debug.logError(e, module);
            }
        }

        session.logout();
        diff = (new Date().getTime() - start);
        result.put("repositoryDirectAccessTime", diff.toString());

        JackrabbitRepositoryAccessor access = new JackrabbitRepositoryAccessor(userLogin, ctx.getDelegator());
        start = new Date().getTime();
        for (int i = 0; i <= maxNodes; i++) {
            try {
                JackrabbitNews news = new JackrabbitNews("/__Speedtest_Node-" + i, "de", "", null, "");
                access.storeContentObject(news);
                access.removeContentObject("/__Speedtest_Node-" + i);
            } catch (Exception e) {
                Debug.logError(e, module);
            }

        }

        access.closeAccess();
        diff = (new Date().getTime() - start);
        result.put("repositoryOcmAccessTime", diff.toString());

        return result;
    }
}
