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
package org.ofbiz.base.json;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@Deprecated
public final class JSON {
    private final org.ofbiz.base.lang.JSON json;

    private JSON(org.ofbiz.base.lang.JSON json) {
        this.json = json;
    }

    // these methods are left out; there is no good way to implement
    // them with the replacement json library

    //public JSON(String filename) {
    //}

    public JSON(InputStream in) throws IOException {
        this(org.ofbiz.base.lang.JSON.from(in));
    }

    public JSON(InputStream in, String encoding) throws IOException {
        this(new InputStreamReader(in, encoding));
    }

    public JSON(Reader reader) throws IOException {
        this(org.ofbiz.base.lang.JSON.from(reader));
    }

    //public Object JSONValue() {
    //}

    //public Object JSONItem() {
    //}

    //public Object JSONResolve() {
    //}

    private <T> T toObject(Class<T> targetClass) {
        try {
            return json.toObject(targetClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String JSONString() {
        return toObject(String.class);
    }

    public Double JSONFloat() {
        return toObject(Double.class);
    }

    public Long JSONLong() {
        return toObject(Long.class);
    }

    public Map JSONObject() {
        return toObject(Map.class);
    }

    //public void JSONObjectEntry(Map<String, Object> map) {
    //}

    public List JSONArray() {
        return toObject(List.class);
    }

    //public Boolean True() {
    //}

    //public Boolean False() {
    //}

    //public Object Null() {
    //}
}
