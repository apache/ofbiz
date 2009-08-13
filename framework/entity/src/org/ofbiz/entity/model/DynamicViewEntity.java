package org.ofbiz.entity.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.model.ModelViewEntity.ComplexAliasMember;
import org.ofbiz.entity.model.ModelViewEntity.ModelAlias;
import org.ofbiz.entity.model.ModelViewEntity.ModelAliasAll;
import org.ofbiz.entity.model.ModelViewEntity.ModelMemberEntity;
import org.ofbiz.entity.model.ModelViewEntity.ModelViewLink;

public interface DynamicViewEntity {

	public ModelViewEntity makeModelViewEntity(GenericDelegator delegator);

	public String getOneRealEntityName();

	/** Getter for property entityName.
	 * @return Value of property entityName.
	 *
	 */
	public String getEntityName();

	/** Setter for property entityName.
	 * @param entityName New value of property entityName.
	 *
	 */
	public void setEntityName(String entityName);

	/** Getter for property packageName.
	 * @return Value of property packageName.
	 *
	 */
	public String getPackageName();

	/** Setter for property packageName.
	 * @param packageName New value of property packageName.
	 *
	 */
	public void setPackageName(String packageName);

	/** Getter for property defaultResourceName.
	 * @return Value of property defaultResourceName.
	 *
	 */
	public String getDefaultResourceName();

	/** Setter for property defaultResourceName.
	 * @param defaultResourceName New value of property defaultResourceName.
	 *
	 */
	public void setDefaultResourceName(String defaultResourceName);

	/** Getter for property title.
	 * @return Value of property title.
	 *
	 */
	public String getTitle();

	/** Setter for property title.
	 * @param title New value of property title.
	 *
	 */
	public void setTitle(String title);

	public void addMemberEntity(String entityAlias, String entityName);

	public Iterator<Map.Entry<String, ModelMemberEntity>> getModelMemberEntitiesEntryIter();

	public void addAliasAll(String entityAlias, String prefix);

	public void addAllAliasAllsToList(List<ModelAliasAll> addList);

	public void addAlias(String entityAlias, String name);

	/** Add an alias, full detail. All parameters can be null except entityAlias and name. */
	public void addAlias(String entityAlias, String name, String field,
			String colAlias, Boolean primKey, Boolean groupBy, String function);

	public void addAlias(String entityAlias, String name, String field,
			String colAlias, Boolean primKey, Boolean groupBy, String function,
			ComplexAliasMember complexAliasMember);

	public void addAllAliasesToList(List<ModelAlias> addList);

	public void addViewLink(String entityAlias, String relEntityAlias,
			Boolean relOptional, List<ModelKeyMap> modelKeyMaps);

	public void addAllViewLinksToList(List<ModelViewLink> addList);

	public void addRelation(String type, String title, String relEntityName,
			List<ModelKeyMap> modelKeyMaps);

	public void addAllRelationsToList(List<ModelRelation> addList);

}