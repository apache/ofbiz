/*******************************************************************************
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
 *******************************************************************************/
package org.ofbiz.api.authorization;

import java.security.Permission;
import java.util.Map;

import javolution.util.FastMap;

/**
 * A collection of basic permissions.
 */
public class BasicPermissions {

    public static final Permission Access = new BasicPermission("access=true");
    public static final Permission Admin = new AdminPermission();
    public static final Permission Create = new BasicPermission("create=true");
    public static final Permission Delete = new BasicPermission("delete=true");
    public static final Permission Update = new BasicPermission("update=true");
    public static final Permission View = new BasicPermission("view=true");
    public static final Map<String, Permission> ConversionMap = createConversionMap();

    protected static Map<String, Permission> createConversionMap() {
        Map<String, Permission> conversionMap = FastMap.newInstance();
        conversionMap.put("ACCESS", Access);
        conversionMap.put("ADMIN", Admin);
        conversionMap.put("CREATE", Create);
        conversionMap.put("DELETE", Delete);
        conversionMap.put("UPDATE", Update);
        conversionMap.put("VIEW", View);
        return conversionMap;
    }
}
