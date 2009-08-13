package org.ofbiz.entity.model;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntity;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.config.DatasourceInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ModelEntity extends Comparable<ModelEntity> {

    /** The name of the time stamp field for locking/synchronization */
    public static final String STAMP_FIELD = "lastUpdatedStamp";
    public static final String STAMP_TX_FIELD = "lastUpdatedTxStamp";
    public static final String CREATE_STAMP_FIELD = "createdStamp";
    public static final String CREATE_STAMP_TX_FIELD = "createdTxStamp";

    public void addExtendEntity(ModelReader reader, Element extendEntityElement);

    public void addField(ModelField field);

    public void addRelation(ModelRelation relation);

    public void addViewEntity(ModelViewEntity view);

    public boolean areFields(Collection<String> fieldNames);

    public String classNameString(List<ModelField> flds);

    public String classNameString(List<ModelField> flds, String separator,
			String afterLast);

    public String classNameString(ModelField... flds);

	public String classNameString(String separator, String afterLast,
			ModelField... flds);

	public String colNameString(List<ModelField> flds);

	public String colNameString(List<ModelField> flds, String separator,
			String afterLast, boolean alias);

	public String colNameString(ModelField... flds);

	public String colNameString(String separator, String afterLast,
			boolean alias, ModelField... flds);

	public boolean containsAllPkFieldNames(Set<String> fieldNames);

	public void convertFieldMapInPlace(Map<String, Object> inContext,
			GenericDelegator delegator);

	public void convertFieldMapInPlace(Map<String, Object> inContext,
			ModelFieldTypeReader modelFieldTypeReader);

	public Object convertFieldValue(ModelField modelField, Object value,
			GenericDelegator delegator);

	/** Convert a field value from one Java data type to another. This is the preferred method -
	 * which takes into consideration the user's locale and time zone (for conversions that
	 * require them).
	 * @return the converted value
	 */
	public Object convertFieldValue(ModelField modelField, Object value,
			GenericDelegator delegator, Map<String, ? extends Object> context);

	/** Convert a field value from one Java data type to another. This is the preferred method -
	 * which takes into consideration the user's locale and time zone (for conversions that
	 * require them).
	 * @return the converted value
	 */
	public Object convertFieldValue(ModelField modelField, Object value,
			ModelFieldTypeReader modelFieldTypeReader,
			Map<String, ? extends Object> context);

	public Object convertFieldValue(String fieldName, Object value,
			GenericDelegator delegator);

	public List<? extends Map<String, Object>> convertToViewValues(
			String viewEntityName, GenericEntity entity);

	public Map<String, Object> createEoModelMap(String entityPrefix,
			String helperName, Set<String> entityNameIncludeSet,
			ModelReader entityModelReader) throws GenericEntityException;

	public String fieldNameString();

	public String fieldNameString(String separator, String afterLast);

	public String fieldsStringList(List<ModelField> flds, String eachString,
			String separator);

	public String fieldsStringList(List<ModelField> flds, String eachString,
			String separator, boolean appendIndex);

	public String fieldsStringList(List<ModelField> flds, String eachString,
			String separator, boolean appendIndex, boolean onlyNonPK);

	public String fieldsStringList(String eachString, String separator,
			boolean appendIndex, boolean onlyNonPK, ModelField... flds);

	public String fieldsStringList(String eachString, String separator,
			boolean appendIndex, ModelField... flds);

	public String fieldsStringList(String eachString, String separator,
			ModelField... flds);

	public String fieldTypeNameString();

	public String finderQueryString(List<ModelField> flds);

	public String finderQueryString(ModelField... flds);

	public List<String> getAllFieldNames();

	/** The author for documentation purposes */
    public String getAuthor();

	public boolean getAutoClearCache();

	/** The col-name of the Field, the alias of the field if this is on a view-entity */
	public String getColNameOrAlias(String fieldName);

	/** The copyright for documentation purposes */
    public String getCopyright();

	/** The default-resource-name of the Entity */
	public String getDefaultResourceName();

	/** The entity-name of the Entity that this Entity is dependent on, if empty then no dependency */
	public String getDependentOn();

	/** The description for documentation purposes */
    public String getDescription();

	/** An indicator to specify if this entity requires locking for updates */
	public boolean getDoLock();

	/** The entity-name of the Entity */
	public String getEntityName();

	/**
	 * @deprecated
	 */
	@Deprecated
	public ModelField getField(int index);

	public ModelField getField(String fieldName);

	public List<String> getFieldNamesFromFieldVector(
			List<ModelField> modelFields);

	public List<String> getFieldNamesFromFieldVector(ModelField... modelFields);

	/**
	 * @deprecated Use getFieldsUnmodifiable instead.
	 */
	@Deprecated
	public List<ModelField> getFieldsCopy();

	public Iterator<ModelField> getFieldsIterator();

	public int getFieldsSize();

	public List<ModelField> getFieldsUnmodifiable();

	public String getFirstPkFieldName();

	public boolean getHasFieldWithAuditLog();

	public ModelIndex getIndex(int index);

	public ModelIndex getIndex(String indexName);

	public Iterator<ModelIndex> getIndexesIterator();

	public int getIndexesSize();

	/* Get the location of this entity's definition */
	public String getLocation();

	public ModelReader getModelReader();

	/** An indicator to specify if this entity is never cached.
	 * If true causes the delegator to not clear caches on write and to not get
	 * from cache on read showing a warning messages to that effect
	 */
	public boolean getNeverCache();

	/**
	 * @return Returns the noAutoStamp.
	 */
	public boolean getNoAutoStamp();

	/**
	 * @deprecated
	 */
	@Deprecated
	public ModelField getNopk(int index);

	public List<String> getNoPkFieldNames();

	public List<ModelField> getNopksCopy();

	public Iterator<ModelField> getNopksIterator();

	public int getNopksSize();

	public ModelField getOnlyPk();

	/** The package-name of the Entity */
	public String getPackageName();

	/**
	 * @deprecated
	 */
	@Deprecated
	public ModelField getPk(int index);

	public List<String> getPkFieldNames();

	public List<ModelField> getPkFieldsUnmodifiable();

	/**
	 * @deprecated Use getPkFieldsUnmodifiable instead.
	 */
	@Deprecated
	public List<ModelField> getPksCopy();

	public Iterator<ModelField> getPksIterator();

	public int getPksSize();

	/** The plain table-name of the Entity without a schema name prefix */
	public String getPlainTableName();

	public ModelRelation getRelation(int index);

	public ModelRelation getRelation(String relationName);

	public Iterator<ModelRelation> getRelationsIterator();

	public List<ModelRelation> getRelationsList(boolean includeOne,
			boolean includeOneNoFk, boolean includeMany);

	public List<ModelRelation> getRelationsManyList();

	public List<ModelRelation> getRelationsOneList();

	public int getRelationsOneSize();

	public int getRelationsSize();

	public Integer getSequenceBankSize();

	/** The table-name of the Entity including a Schema name if specified in the datasource config */
	public String getTableName(DatasourceInfo datasourceInfo);

	/** The table-name of the Entity including a Schema name if specified in the datasource config */
	public String getTableName(String helperName);

	/** The title for documentation purposes */
    public String getTitle();

	/** The version for documentation purposes */
    public String getVersion();

	public Iterator<Map.Entry<String, ModelViewEntity>> getViewConvertorsIterator();

	public int getViewEntitiesSize();

	public ModelViewEntity getViewEntity(String viewEntityName);

	public String httpArgList(List<ModelField> flds);

	public String httpArgList(ModelField... flds);

	public String httpArgListFromClass(List<ModelField> flds);

	public String httpArgListFromClass(List<ModelField> flds,
			String entityNameSuffix);

	public String httpArgListFromClass(ModelField... flds);

	public String httpArgListFromClass(String entityNameSuffix,
			ModelField... flds);

	public String httpRelationArgList(List<ModelField> flds,
			ModelRelation relation);

	public String httpRelationArgList(ModelRelation relation,
			ModelField... flds);

	public boolean isField(String fieldName);

	public boolean lock();

	public String nameString(List<ModelField> flds);

	public String nameString(List<ModelField> flds, String separator,
			String afterLast);

	public String nonPkNullList();

	public String pkNameString();

	public String pkNameString(String separator, String afterLast);

	public String primKeyClassNameString();

	public ModelField removeField(int index);

	public ModelField removeField(String fieldName);

	public ModelIndex removeIndex(int index);

	public ModelRelation removeRelation(int index);

	public ModelViewEntity removeViewEntity(ModelViewEntity viewEntity);

	public ModelViewEntity removeViewEntity(String viewEntityName);

	public void setAutoClearCache(boolean autoClearCache);

	public void setDefaultResourceName(String defaultResourceName);

	public void setDependentOn(String dependentOn);

	public void setDoLock(boolean doLock);

	public void setEntityName(String entityName);

	/* Set the location of this entity's definition */
	public void setLocation(String location);

	public void setNeverCache(boolean neverCache);

	/**
	 * @param noAutoStamp The noAutoStamp to set.
	 */
	public void setNoAutoStamp(boolean noAutoStamp);

	public void setPackageName(String packageName);

	public void setTableName(String tableName);

	public String toString();

	public Element toXmlElement(Document document);

	public Element toXmlElement(Document document, String packageName);

	public String typeNameString(List<ModelField> flds);

	public String typeNameString(ModelField... flds);

	public String typeNameStringRelatedAndMain(List<ModelField> flds,
			ModelRelation relation);

	public String typeNameStringRelatedAndMain(ModelRelation relation,
			ModelField... flds);

	public String typeNameStringRelatedNoMapped(List<ModelField> flds,
			ModelRelation relation);

	/*
	 public String httpRelationArgList(ModelRelation relation) {
	 String returnString = "";
	 if (relation.keyMaps.size() < 1) { return ""; }
	
	 int i = 0;
	 for(; i < relation.keyMaps.size() - 1; i++) {
	 ModelKeyMap keyMap = (ModelKeyMap)relation.keyMaps.get(i);
	 if (keyMap != null)
	 returnString = returnString + "\"" + tableName + "_" + keyMap.relColName + "=\" + " + ModelUtil.lowerFirstChar(relation.mainEntity.entityName) + ".get" + ModelUtil.upperFirstChar(keyMap.fieldName) + "() + \"&\" + ";
	 }
	 ModelKeyMap keyMap = (ModelKeyMap)relation.keyMaps.get(i);
	 returnString = returnString + "\"" + tableName + "_" + keyMap.relColName + "=\" + " + ModelUtil.lowerFirstChar(relation.mainEntity.entityName) + ".get" + ModelUtil.upperFirstChar(keyMap.fieldName) + "()";
	 return returnString;
	 }
	 */
	public String typeNameStringRelatedNoMapped(ModelRelation relation,
			ModelField... flds);

	public void updatePkLists();

	/**
	 * Writes entity model information in the Apple EOModelBundle format.
	 *
	 * For document structure and definition see: http://developer.apple.com/documentation/InternetWeb/Reference/WO_BundleReference/Articles/EOModelBundle.html
	 *
	 * For examples see the JavaRealEstate.framework and JavaBusinessLogic.framework packages which are in the /Library/Frameworks directory after installing the WebObjects Examples package (get latest version of WebObjects download for this).
	 *
	 * This is based on examples and documentation from WebObjects 5.4, downloaded 20080221.
	 *
	 * @param writer
	 * @param entityPrefix
	 * @param helperName
	 */
	public void writeEoModelText(PrintWriter writer, String entityPrefix,
			String helperName, Set<String> entityNameIncludeSet,
			ModelReader entityModelReader) throws GenericEntityException;

}