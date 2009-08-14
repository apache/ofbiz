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
import org.ofbiz.entity.model.ModelViewEntityImpl.*;

import org.w3c.dom.Element;

public class ModelFactoryImpl implements ModelObjectFactory {

    public static final String module = ModelFactoryImpl.class.getName();

    public ComplexAlias createComplexAlias(Element complexAliasElement) {
        return new ComplexAliasImpl(complexAliasElement);
    }

    public ComplexAlias createComplexAlias(String operator) {
        return new ComplexAliasImpl(operator);
    }

    public ComplexAliasMember createComplexAliasField(Element complexAliasFieldElement) {
        return new ComplexAliasField(complexAliasFieldElement);
	}

    public ComplexAliasMember createComplexAliasField(String entityAlias, String field, String defaultValue, String function) {
        return new ComplexAliasField(entityAlias, field, defaultValue, function);
	}

    public DynamicViewEntity createDynamicViewEntity() {
    	return new DynamicViewEntityImpl();
    }

    public ModelKeyMap createModelKeyMap(String fieldName, String relFieldName) {
		return new ModelKeyMapImpl(fieldName, relFieldName);
    }

    public List<ModelKeyMap> makeKeyMapList(String fieldName1) {
        return UtilMisc.toList((ModelKeyMap)new ModelKeyMapImpl(fieldName1, null));
    }

	public List<ModelKeyMap> makeKeyMapList(String fieldName1, String relFieldName1) {
        return UtilMisc.toList((ModelKeyMap)new ModelKeyMapImpl(fieldName1, relFieldName1));
    }

	public List<ModelKeyMap> makeKeyMapList(String fieldName1, String relFieldName1, String fieldName2, String relFieldName2) {
        return UtilMisc.toList((ModelKeyMap)new ModelKeyMapImpl(fieldName1, relFieldName1),
        		(ModelKeyMap)new ModelKeyMapImpl(fieldName2, relFieldName2));
    }

	public List<ModelKeyMap> makeKeyMapList(String fieldName1, String relFieldName1, String fieldName2, String relFieldName2, String fieldName3, String relFieldName3) {
    	return UtilMisc.toList((ModelKeyMap)new ModelKeyMapImpl(fieldName1, relFieldName1),
    			(ModelKeyMap)new ModelKeyMapImpl(fieldName2, relFieldName2),
    			(ModelKeyMap)new ModelKeyMapImpl(fieldName3, relFieldName3));
    }
}
