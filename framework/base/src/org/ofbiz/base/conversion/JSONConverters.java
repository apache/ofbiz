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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** JSON Converter classes. */
public class JSONConverters implements ConverterLoader {

    public static class JSONToList extends AbstractConverter<JSON, List<Object>> {
        public JSONToList() {
            super(JSON.class, List.class);
        }

        public List<Object> convert(JSON obj) throws ConversionException {
            try {
                return (List<Object>) JSONArray.toCollection((JSONArray)obj);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }
    }

    public static class JSONToMap extends AbstractConverter<JSON, Map<String, Object>> {
        public JSONToMap() {
            super(JSON.class, Map.class);
        }

        public Map<String, Object> convert(JSON obj) throws ConversionException {
            try {
                return (Map<String, Object>) JSONObject.toBean((JSONObject)obj, Map.class);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }
    }

    public static class MapToJSON extends AbstractConverter<Map<String, Object>, JSON> {
        public MapToJSON() {
            super(Map.class, JSON.class);
        }

        public JSON convert(Map<String, Object> obj) throws ConversionException {
            try {
                return JSONObject.fromObject(obj);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }
    }

    public static class ListToJSON extends AbstractConverter<List<Object>, JSON> {
        public ListToJSON() {
            super(List.class, JSON.class);
        }

        public JSON convert(List<Object> obj) throws ConversionException {
            try {
                return JSONArray.fromObject(obj);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }
    }

    public static class JSONToSet extends AbstractConverter<JSON, Set<Object>> {
        public JSONToSet() {
            super(JSON.class, Set.class);
        }

        public Set<Object> convert(JSON obj) throws ConversionException {
            try {
                Set<Object> set = new TreeSet<Object>();
                set.addAll((JSONArray)obj);
                return set;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }
    }

    public void loadConverters() {
        Converters.loadContainedConverters(JSONConverters.class);
    }
}
