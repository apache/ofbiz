package org.ofbiz.entity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javolution.lang.Reusable;

import org.ofbiz.api.context.ExecutionArtifact;
import org.ofbiz.base.util.collections.LocalizedMap;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.model.ModelField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("unchecked")
public interface GenericEntity extends Map<String, Object>, LocalizedMap, Serializable, Comparable<GenericEntity>, Cloneable, Reusable, ExecutionArtifact {

	public abstract void clear();

	public abstract boolean containsKey(Object key);

	/** Returns true if the entity contains all of the primary key fields. */
	public abstract boolean containsPrimaryKey();

	public abstract boolean containsPrimaryKey(boolean requireValue);

	public abstract boolean containsValue(Object value);

	public abstract Object dangerousGetNoCheckButFast(ModelField modelField);

	public abstract void dangerousSetNoCheckButFast(ModelField modelField,
			Object value);

	public abstract java.util.Set<Map.Entry<String, Object>> entrySet();

	public abstract Object get(Object key);

	/** Checks a resource bundle for a value for this field using the entity name, the field name
	 *    and a composite of the Primary Key field values as a key. If no value is found in the
	 *    resource then the field value is returned. Uses the default-resource-name from the entity
	 *    definition as the resource name. To specify a resource name manually, use the other getResource method.
	 *
	 *  So, the key in the resource bundle (properties file) should be as follows:
	 *    <entity-name>.<field-name>.<pk-field-value-1>.<pk-field-value-2>...<pk-field-value-n>
	 *  For example:
	 *    ProductType.description.FINISHED_GOOD
	 *
	 * @param name The name of the field on the entity
	 * @param locale The locale to use when finding the ResourceBundle, if null uses the default
	 *    locale for the current instance of Java
	 * @return If the corresponding resource is found and contains a key as described above, then that
	 *    property value is returned; otherwise returns the field value
	 */
	public abstract Object get(String name, Locale locale);

	/** Same as the getResource method that does not take resource name, but instead allows manually
	 *    specifying the resource name. In general you should use the other method for more consistent
	 *    naming and use of the corresponding properties files.
	 * @param name The name of the field on the entity
	 * @param resource The name of the resource to get the value from; if null defaults to the
	 *    default-resource-name on the entity definition, if specified there
	 * @param locale The locale to use when finding the ResourceBundle, if null uses the default
	 *    locale for the current instance of Java
	 * @return If the specified resource is found and contains a key as described above, then that
	 *    property value is returned; otherwise returns the field value
	 */
	public abstract Object get(String name, String resource, Locale locale);

	/** Returns key/value pairs of entity fields
	 * @return java.util.Map
	 */
	public abstract Map<String, Object> getAllFields();

	/** Returns keys of entity fields
	 * @return java.util.Collection
	 */
	public abstract Collection<String> getAllKeys();

	public abstract BigDecimal getBigDecimal(String name);

	public abstract Boolean getBoolean(String name);

	public abstract byte[] getBytes(String name);

	public abstract java.sql.Date getDate(String name);

	/** Get the GenericDelegator instance that created this value object and that is responsible for it.
	 *@return GenericDelegator object
	 */
	public abstract GenericDelegator getDelegator();

	public abstract Double getDouble(String name);

	public abstract String getEntityName();

	/** Used by clients to specify exactly the fields they are interested in
	 * @param keysofFields the name of the fields the client is interested in
	 * @return java.util.Map
	 */
	public abstract Map<String, Object> getFields(
			Collection<String> keysofFields);

	public abstract Float getFloat(String name);

	public abstract Integer getInteger(String name);

	/**
	 * @return Returns the isFromEntitySync.
	 */
	public abstract boolean getIsFromEntitySync();

	public abstract String getLocation();

	public abstract Long getLong(String name);

	public abstract ModelEntity getModelEntity();

	public abstract String getName();

	public abstract String getPkShortValueString();

	public abstract GenericPK getPrimaryKey();

	public abstract String getString(String name);

	public abstract java.sql.Time getTime(String name);

	public abstract java.sql.Timestamp getTimestamp(String name);

	/** Creates a hashCode for the entity, using the default String hashCode and Map hashCode, overrides the default hashCode
	 *@return    Hashcode corresponding to this entity
	 */
	public abstract int hashCode();

	public abstract boolean isEmpty();

	public abstract boolean isModified();

	public abstract boolean isMutable();

	/** Returns true if the entity contains all of the primary key fields, but NO others. */
	public abstract boolean isPrimaryKey();

	public abstract boolean isPrimaryKey(boolean requireValue);

	public abstract java.util.Set<String> keySet();

	/** Used to indicate if locking is enabled for this entity
	 * @return True if locking is enabled
	 */
	public abstract boolean lockEnabled();

	/** Makes an XML Element object with an attribute for each field of the entity
	 *@param document The XML Document that the new Element will be part of
	 *@return org.w3c.dom.Element object representing this generic entity
	 */
	public abstract Element makeXmlElement(Document document);

	/** Makes an XML Element object with an attribute for each field of the entity
	 *@param document The XML Document that the new Element will be part of
	 *@param prefix A prefix to put in front of the entity name in the tag name
	 *@return org.w3c.dom.Element object representing this generic entity
	 */
	public abstract Element makeXmlElement(Document document, String prefix);

	public abstract boolean matches(EntityCondition condition);

	public abstract boolean matchesFields(
			Map<String, ? extends Object> keyValuePairs);

	public abstract Object put(String key, Object value);

	public abstract void putAll(
			java.util.Map<? extends String, ? extends Object> map);

	public abstract void refreshFromValue(GenericEntity newValue)
			throws GenericEntityException;

	public abstract Object remove(Object key);

	public abstract void removedFromDatasource();

	public abstract void reset();

	/** Sets the named field to the passed value, even if the value is null
	 * @param name The field name to set
	 * @param value The value to set
	 */
	public abstract void set(String name, Object value);

	/** Sets the named field to the passed value. If value is null, it is only
	 *  set if the setIfNull parameter is true. This is useful because an update
	 *  will only set values that are included in the HashMap and will store null
	 *  values in the HashMap to the datastore. If a value is not in the HashMap,
	 *  it will be left unmodified in the datastore.
	 * @param name The field name to set
	 * @param value The value to set
	 * @param setIfNull Specifies whether or not to set the value if it is null
	 */
	public abstract Object set(String name, Object value, boolean setIfNull);

	/** Intelligently sets fields on this entity from the Map of fields passed in
	 * @param fields The fields Map to get the values from
	 * @param setIfEmpty Used to specify whether empty/null values in the field Map should over-write non-empty values in this entity
	 * @param namePrefix If not null or empty will be pre-pended to each field name (upper-casing the first letter of the field name first), and that will be used as the fields Map lookup name instead of the field-name
	 * @param pks If null, get all values, if TRUE just get PKs, if FALSE just get non-PKs
	 */
	public abstract void setAllFields(
			Map<? extends Object, ? extends Object> fields, boolean setIfEmpty,
			String namePrefix, Boolean pks);

	/** Sets a field with an array of bytes, wrapping them automatically for easy use.
	 * @param name The field name to set
	 * @param bytes The byte array to be wrapped and set
	 */
	public abstract void setBytes(String name, byte[] bytes);

	/** Set the GenericDelegator instance that created this value object and that is responsible for it. */
	public abstract void setDelegator(GenericDelegator internalDelegator);

	/** Used by clients to update particular fields in the entity
	 * @param keyValuePairs java.util.Map
	 */
	public abstract void setFields(
			Map<? extends String, ? extends Object> keyValuePairs);

	public abstract void setImmutable();

	/**
	 * @param isFromEntitySync The isFromEntitySync to set.
	 */
	public abstract void setIsFromEntitySync(boolean isFromEntitySync);

	public abstract void setNextSeqId();

	/** go through the non-pks and for each one see if there is an entry in fields to set */
	public abstract void setNonPKFields(
			Map<? extends Object, ? extends Object> fields);

	/** go through the non-pks and for each one see if there is an entry in fields to set */
	public abstract void setNonPKFields(
			Map<? extends Object, ? extends Object> fields, boolean setIfEmpty);

	/** go through the pks and for each one see if there is an entry in fields to set */
	public abstract void setPKFields(
			Map<? extends Object, ? extends Object> fields);

	/** go through the pks and for each one see if there is an entry in fields to set */
	public abstract void setPKFields(
			Map<? extends Object, ? extends Object> fields, boolean setIfEmpty);

	/** Sets the named field to the passed value, converting the value from a String to the corrent type using <code>Type.valueOf()</code>
	 * @param name The field name to set
	 * @param value The String value to convert and set
	 */
	public abstract void setString(String name, String value);

	public abstract int size();
    public abstract void synchronizedWithDatasource();

    /**
	 * Creates a String for the entity, overrides the default toString
	 * This method is NOT secure, it WILL display encrypted fields
	 *
	 *@return String corresponding to this entity
	 */
	public abstract String toStringInsecure();

    public abstract java.util.Collection<Object> values();

    /** Writes XML text with an attribute or CDATA element for each field of the entity
	 *@param writer A PrintWriter to write to
	 *@param prefix A prefix to put in front of the entity name in the tag name
	 */
	public abstract void writeXmlText(PrintWriter writer, String prefix);


}