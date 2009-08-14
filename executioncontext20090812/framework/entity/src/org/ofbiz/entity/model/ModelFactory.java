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
package org.ofbiz.entity.model;

import java.util.List;

import org.ofbiz.base.util.*;
import org.ofbiz.entity.model.ModelViewEntity.*;

import org.w3c.dom.Element;

public class ModelFactory {

    public static final String module = ModelFactory.class.getName();
    
    public static ModelObjectFactory modelFactory = null;

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
        	modelFactory = (ModelObjectFactory) loader.loadClass("org.ofbiz.entity.model.ModelFactoryImpl").newInstance();
		} catch (Exception e) {
            Debug.logError(e, module);
		}
    }

    public static List<ModelKeyMap> makeKeyMapList(String fieldName1) {
        return modelFactory.makeKeyMapList(fieldName1);
    }

    public static List<ModelKeyMap> makeKeyMapList(String fieldName1, String relFieldName1) {
        return modelFactory.makeKeyMapList(fieldName1, relFieldName1);
    }

    public static List<ModelKeyMap> makeKeyMapList(String fieldName1, String relFieldName1, String fieldName2, String relFieldName2) {
        return modelFactory.makeKeyMapList(fieldName1, relFieldName1, fieldName2, relFieldName2);
    }

    public static List<ModelKeyMap> makeKeyMapList(String fieldName1, String relFieldName1, String fieldName2, String relFieldName2, String fieldName3, String relFieldName3) {
        return modelFactory.makeKeyMapList(fieldName1, relFieldName1, fieldName2, relFieldName2, fieldName3, relFieldName3);
    }

	public static ModelKeyMap createModelKeyMap(String fieldName, String relFieldName) {
		return modelFactory.createModelKeyMap(fieldName, relFieldName);
    }

	public static DynamicViewEntity createDynamicViewEntity() {
    	return modelFactory.createDynamicViewEntity();
    }

    public static ComplexAlias createComplexAlias(String operator) {
        return modelFactory.createComplexAlias(operator);
    }

    public static ComplexAlias createComplexAlias(Element complexAliasElement) {
        return modelFactory.createComplexAlias(complexAliasElement);
    }

    public static ComplexAliasMember createComplexAliasField(Element complexAliasFieldElement) {
        return modelFactory.createComplexAliasField(complexAliasFieldElement);
	}

    public static ComplexAliasMember createComplexAliasField(String entityAlias, String field, String defaultValue, String function) {
        return modelFactory.createComplexAliasField(entityAlias, field, defaultValue, function);
	}

}
