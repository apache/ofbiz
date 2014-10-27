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
package org.ofbiz.base.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.ofbiz.base.lang.ThreadSafe;
import org.ofbiz.base.util.Assert;

/** A JSON object. */
@ThreadSafe
public final class JSON {

    public static JSON from(InputStream inStream) throws IOException {
        Assert.notNull("inStream", inStream);
        String jsonString = IOUtils.toString(inStream, "UTF-8");
        return from(jsonString);
    }

    public static JSON from(Object object) {
        Assert.notNull("object", object);
        // TODO: Finish implementation.
        return null;
    }

    public static JSON from(Reader reader) throws IOException {
        Assert.notNull("reader", reader);
        String jsonString = IOUtils.toString(reader);
        return from(jsonString);
    }

    public static JSON from(String jsonString) {
        Assert.notNull("jsonString", jsonString);
        // TODO: Validate String
        return new JSON(jsonString);
    }

    private final String jsonString;

    private JSON(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public boolean equals(Object obj) {
        return jsonString.equals(obj);
    }

    @Override
    public int hashCode() {
        return jsonString.hashCode();
    }

    public <T> T toObject() {
        // TODO: Finish implementation.
        return null;
    }

    @Override
    public String toString() {
        return jsonString.toString();
    }
}
