/*
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
 */
package org.ofbiz.context.entity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javolution.lang.Reusable;

import org.ofbiz.base.util.collections.LocalizedMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generic Entity Value Object - Handles persistence for any defined entity.
 * <p>Note that this class extends <code>Observable</code> to achieve change notification for
 * <code>Observer</code>s. Whenever a field changes the name of the field will be passed to
 * the <code>notifyObservers()</code> method, and through that to the <code>update()</code> method of each
 * <code>Observer</code>.
 *
 */
public interface GenericEntity extends Map<String, Object>, LocalizedMap<Object>, Serializable, Comparable<GenericEntity>, Cloneable, Reusable {

    public void refreshFromValue(GenericEntity newValue) throws GenericEntityException;

    public boolean isModified();
    public void synchronizedWithDatasource();
    public void removedFromDatasource();

    public boolean isMutable();
    public void setImmutable();

    /**
     * @return Returns the isFromEntitySync.
     */
    public boolean getIsFromEntitySync();

    /**
     * @param isFromEntitySync The isFromEntitySync to set.
     */
    public void setIsFromEntitySync(boolean isFromEntitySync);

    public String getEntityName();

    public ModelEntityInterface getModelEntity();

    /** Get the GenericDelegator instance that created this value object and that is responsible for it.
     *@return GenericDelegator object
     */
    public GenericDelegator getDelegator();

    public String getDelegatorName();

    /** Set the GenericDelegator instance that created this value object and that is responsible for it. */
    public void setDelegator(GenericDelegator internalDelegator);

    public Object get(String name);

    /** Returns true if the entity contains all of the primary key fields, but NO others. */
    public boolean isPrimaryKey();
    public boolean isPrimaryKey(boolean requireValue);
    
    /** Returns true if the entity contains all of the primary key fields. */
    public boolean containsPrimaryKey();
    public boolean containsPrimaryKey(boolean requireValue);

    public String getPkShortValueString();

    /** Sets the named field to the passed value, even if the value is null
     * @param name The field name to set
     * @param value The value to set
     */
    public void set(String name, Object value);

    /** Sets the named field to the passed value. If value is null, it is only
     *  set if the setIfNull parameter is true. This is useful because an update
     *  will only set values that are included in the HashMap and will store null
     *  values in the HashMap to the datastore. If a value is not in the HashMap,
     *  it will be left unmodified in the datastore.
     * @param name The field name to set
     * @param value The value to set
     * @param setIfNull Specifies whether or not to set the value if it is null
     */
    public Object set(String name, Object value, boolean setIfNull);

    public void dangerousSetNoCheckButFast(ModelFieldInterface modelField, Object value);

    public Object dangerousGetNoCheckButFast(ModelFieldInterface modelField);

    /** Sets the named field to the passed value, converting the value from a String to the corrent type using <code>Type.valueOf()</code>
     * @param name The field name to set
     * @param value The String value to convert and set
     */
    public void setString(String name, String value);

    /** Sets a field with an array of bytes, wrapping them automatically for easy use.
     * @param name The field name to set
     * @param bytes The byte array to be wrapped and set
     */
    public void setBytes(String name, byte[] bytes);

    public void setNextSeqId();
    
    public Boolean getBoolean(String name);

    public String getString(String name);

    public java.sql.Timestamp getTimestamp(String name);

    public java.sql.Time getTime(String name);

    public java.sql.Date getDate(String name);

    public Integer getInteger(String name);

    public Long getLong(String name);

    public Float getFloat(String name);

    public Double getDouble(String name);

    public BigDecimal getBigDecimal(String name);

    public byte[] getBytes(String name);

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
    public Object get(String name, Locale locale);

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
    public Object get(String name, String resource, Locale locale);

    public GenericPK getPrimaryKey();

    /** go through the pks and for each one see if there is an entry in fields to set */
    public void setPKFields(Map<? extends Object, ? extends Object> fields);

    /** go through the pks and for each one see if there is an entry in fields to set */
    public void setPKFields(Map<? extends Object, ? extends Object> fields, boolean setIfEmpty);

    /** go through the non-pks and for each one see if there is an entry in fields to set */
    public void setNonPKFields(Map<? extends Object, ? extends Object> fields);

    /** go through the non-pks and for each one see if there is an entry in fields to set */
    public void setNonPKFields(Map<? extends Object, ? extends Object> fields, boolean setIfEmpty);


    /** Intelligently sets fields on this entity from the Map of fields passed in
     * @param fields The fields Map to get the values from
     * @param setIfEmpty Used to specify whether empty/null values in the field Map should over-write non-empty values in this entity
     * @param namePrefix If not null or empty will be pre-pended to each field name (upper-casing the first letter of the field name first), and that will be used as the fields Map lookup name instead of the field-name
     * @param pks If null, get all values, if TRUE just get PKs, if FALSE just get non-PKs
     */
    public void setAllFields(Map<? extends Object, ? extends Object> fields, boolean setIfEmpty, String namePrefix, Boolean pks);

    /** Returns keys of entity fields
     * @return java.util.Collection
     */
    public Collection<String> getAllKeys();

    /** Returns key/value pairs of entity fields
     * @return java.util.Map
     */
    public Map<String, Object> getAllFields();

    /** Used by clients to specify exactly the fields they are interested in
     * @param keysofFields the name of the fields the client is interested in
     * @return java.util.Map
     */
    public Map<String, Object> getFields(Collection<String> keysofFields);

    /** Used by clients to update particular fields in the entity
     * @param keyValuePairs java.util.Map
     */
    public void setFields(Map<? extends String, ? extends Object> keyValuePairs);

    public boolean matchesFields(Map<String, ? extends Object> keyValuePairs);

    /** Used to indicate if locking is enabled for this entity
     * @return True if locking is enabled
     */
    public boolean lockEnabled();

    /** Makes an XML Element object with an attribute for each field of the entity
     *@param document The XML Document that the new Element will be part of
     *@return org.w3c.dom.Element object representing this generic entity
     */
    public Element makeXmlElement(Document document);

    /** Makes an XML Element object with an attribute for each field of the entity
     *@param document The XML Document that the new Element will be part of
     *@param prefix A prefix to put in front of the entity name in the tag name
     *@return org.w3c.dom.Element object representing this generic entity
     */
    public Element makeXmlElement(Document document, String prefix);

    /** Writes XML text with an attribute or CDATA element for each field of the entity
     *@param writer A PrintWriter to write to
     *@param prefix A prefix to put in front of the entity name in the tag name
     */
    public void writeXmlText(PrintWriter writer, String prefix);

    /**
     * Creates a String for the entity, overrides the default toString
     * This method is NOT secure, it WILL display encrypted fields
     *
     *@return String corresponding to this entity
     */
    public String toStringInsecure();

    /** Compares this GenericEntity to the passed object
     *@param that Object to compare this to
     *@return int representing the result of the comparison (-1,0, or 1)
     */
    public int compareTo(GenericEntity that);

    public static interface NULL {
    }
}
