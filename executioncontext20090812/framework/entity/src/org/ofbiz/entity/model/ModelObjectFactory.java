package org.ofbiz.entity.model;

import java.util.List;

import org.ofbiz.entity.model.ModelViewEntity.*;
import org.w3c.dom.Element;

public interface ModelObjectFactory {

	public List<ModelKeyMap> makeKeyMapList(String fieldName1);

	public List<ModelKeyMap> makeKeyMapList(String fieldName1,
			String relFieldName1);

	public List<ModelKeyMap> makeKeyMapList(String fieldName1,
			String relFieldName1, String fieldName2, String relFieldName2);

	public List<ModelKeyMap> makeKeyMapList(String fieldName1,
			String relFieldName1, String fieldName2, String relFieldName2,
			String fieldName3, String relFieldName3);

	public ModelKeyMap createModelKeyMap(String fieldName, String relFieldName);

	public DynamicViewEntity createDynamicViewEntity();

	public ComplexAlias createComplexAlias(String operator);

	public ComplexAlias createComplexAlias(Element complexAliasElement);

	public ComplexAliasMember createComplexAliasField(Element complexAliasFieldElement);

	public ComplexAliasMember createComplexAliasField(String entityAlias, String field, String defaultValue, String function);

}