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

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Delegator Interface
 */
public interface GenericDelegator {

    public String getDelegatorName();
    
    /** Gets the name of the server configuration that corresponds to this delegator
     * @return server configuration name
     */
    public String getOriginalDelegatorName();

    public ModelEntityInterface getModelEntity(String entityName);
    
    /** Gets the helper name that corresponds to this delegator and the specified entityName
     *@param entityName The name of the entity to get the helper for
     *@return String with the helper name that corresponds to this delegator and the specified entityName
     */
    public String getEntityGroupName(String entityName);

    /** Gets the helper name that corresponds to this delegator and the specified entityName
     *@param groupName The name of the group to get the helper name for
     *@return String with the helper name that corresponds to this delegator and the specified entityName
     */
    public String getGroupHelperName(String groupName);

    /** Gets the helper name that corresponds to this delegator and the specified entityName
     *@param entityName The name of the entity to get the helper name for
     *@return String with the helper name that corresponds to this delegator and the specified entityName
     */
    public String getEntityHelperName(String entityName);

    /** Creates a Entity in the form of a GenericValue without persisting it */
    public GenericValue makeValue(String entityName);

    /** Creates a Entity in the form of a GenericValue without persisting it */
    public GenericValue makeValue(String entityName, Object... fields);

    /** Creates a Entity in the form of a GenericValue without persisting it */
    public GenericValue makeValue(String entityName, Map<String, ? extends Object> fields);

    /** Creates a Entity in the form of a GenericValue without persisting it */
    public GenericValue makeValueSingle(String entityName, Object singlePkValue);

    /** Creates a Entity in the form of a GenericValue without persisting it; only valid fields will be pulled from the fields Map */
    public GenericValue makeValidValue(String entityName, Object... fields);

    /** Creates a Entity in the form of a GenericValue without persisting it; only valid fields will be pulled from the fields Map */
    public GenericValue makeValidValue(String entityName, Map<String, ? extends Object> fields);

    /** Creates a Primary Key in the form of a GenericPK without persisting it */
    public GenericPK makePK(String entityName);

    /** Creates a Primary Key in the form of a GenericPK without persisting it */
    public GenericPK makePK(String entityName, Object... fields);

    /** Creates a Primary Key in the form of a GenericPK without persisting it */
    public GenericPK makePK(String entityName, Map<String, ? extends Object> fields);

    /** Creates a Primary Key in the form of a GenericPK without persisting it */
    public GenericPK makePKSingle(String entityName, Object singlePkValue);

    /** Creates a Entity in the form of a GenericValue and write it to the datasource
     *@param primaryKey The GenericPK to create a value in the datasource from
     *@return GenericValue instance containing the new instance
     */
    public GenericValue create(GenericPK primaryKey) throws GenericEntityException;

    /** Creates a Entity in the form of a GenericValue and write it to the datasource
     *@param primaryKey The GenericPK to create a value in the datasource from
     *@param doCacheClear boolean that specifies whether to clear related cache entries for this primaryKey to be created
     *@return GenericValue instance containing the new instance
     */
    public GenericValue create(GenericPK primaryKey, boolean doCacheClear) throws GenericEntityException;

    /** Creates a Entity in the form of a GenericValue and write it to the database
     *@return GenericValue instance containing the new instance
     */
    public GenericValue create(String entityName, Object... fields) throws GenericEntityException;

    /** Creates a Entity in the form of a GenericValue and write it to the database
     *@return GenericValue instance containing the new instance
     */
    public GenericValue create(String entityName, Map<String, ? extends Object> fields) throws GenericEntityException;

    /** Creates a Entity in the form of a GenericValue and write it to the database
     *@return GenericValue instance containing the new instance
     */
    public GenericValue createSingle(String entityName, Object singlePkValue) throws GenericEntityException;

    /** Creates a Entity in the form of a GenericValue and write it to the datasource
     *@param value The GenericValue to create a value in the datasource from
     *@return GenericValue instance containing the new instance
     */
    public GenericValue create(GenericValue value) throws GenericEntityException;

    /** Sets the sequenced ID (for entity with one primary key field ONLY), and then does a create in the database
     * as normal. The reason to do it this way is that it will retry and fix the sequence if somehow the sequencer
     * is in a bad state and returning a value that already exists.
     *@param value The GenericValue to create a value in the datasource from
     *@return GenericValue instance containing the new instance
     */
    public GenericValue createSetNextSeqId(GenericValue value) throws GenericEntityException;

    /** Creates a Entity in the form of a GenericValue and write it to the datasource
     *@param value The GenericValue to create a value in the datasource from
     *@param doCacheClear boolean that specifies whether or not to automatically clear cache entries related to this operation
     *@return GenericValue instance containing the new instance
     */
    public GenericValue create(GenericValue value, boolean doCacheClear) throws GenericEntityException;

    /** Creates or stores an Entity
     *@param value The GenericValue instance containing the new or existing instance
     *@param doCacheClear boolean that specifies whether or not to automatically clear cache entries related to this operation
     *@return GenericValue instance containing the new or updated instance
     */
    public GenericValue createOrStore(GenericValue value, boolean doCacheClear) throws GenericEntityException;

    /** Creates or stores an Entity
     *@param value The GenericValue instance containing the new or existing instance
     *@return GenericValue instance containing the new or updated instance
     */
    public GenericValue createOrStore(GenericValue value) throws GenericEntityException;

    /** Remove a Generic Entity corresponding to the primaryKey
     *@param primaryKey  The primary key of the entity to remove.
     *@return int representing number of rows effected by this operation
     */
    public int removeByPrimaryKey(GenericPK primaryKey) throws GenericEntityException;

    /** Remove a Generic Entity corresponding to the primaryKey
     *@param primaryKey  The primary key of the entity to remove.
     *@param doCacheClear boolean that specifies whether to clear cache entries for this primaryKey to be removed
     *@return int representing number of rows effected by this operation
     */
    public int removeByPrimaryKey(GenericPK primaryKey, boolean doCacheClear) throws GenericEntityException;

    /** Remove a Generic Value from the database
     *@param value The GenericValue object of the entity to remove.
     *@return int representing number of rows effected by this operation
     */
    public int removeValue(GenericValue value) throws GenericEntityException;

    /** Remove a Generic Value from the database
     *@param value The GenericValue object of the entity to remove.
     *@param doCacheClear boolean that specifies whether to clear cache entries for this value to be removed
     *@return int representing number of rows effected by this operation
     */
    public int removeValue(GenericValue value, boolean doCacheClear) throws GenericEntityException;

    /** Removes/deletes Generic Entity records found by all of the specified fields (ie: combined using AND)
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return int representing number of rows effected by this operation
     */
    public int removeByAnd(String entityName, Object... fields) throws GenericEntityException;

    /** Removes/deletes Generic Entity records found by all of the specified fields (ie: combined using AND)
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return int representing number of rows effected by this operation
     */
    public int removeByAnd(String entityName, Map<String, ? extends Object> fields) throws GenericEntityException;

    /** Removes/deletes Generic Entity records found by all of the specified fields (ie: combined using AND)
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param doCacheClear boolean that specifies whether to clear cache entries for this value to be removed
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return int representing number of rows effected by this operation
     */
    public int removeByAnd(String entityName, boolean doCacheClear, Object... fields) throws GenericEntityException;

    /** Removes/deletes Generic Entity records found by all of the specified fields (ie: combined using AND)
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@param doCacheClear boolean that specifies whether to clear cache entries for this value to be removed
     *@return int representing number of rows effected by this operation
     */
    public int removeByAnd(String entityName, Map<String, ? extends Object> fields, boolean doCacheClear) throws GenericEntityException;

    /** Removes/deletes Generic Entity records found by the condition
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param condition The condition used to restrict the removing
     *@return int representing number of rows effected by this operation
     */
    public int removeByCondition(String entityName, EntityConditionInterface condition) throws GenericEntityException;

    /** Removes/deletes Generic Entity records found by the condition
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param condition The condition used to restrict the removing
     *@param doCacheClear boolean that specifies whether to clear cache entries for this value to be removed
     *@return int representing number of rows effected by this operation
     */
    public int removeByCondition(String entityName, EntityConditionInterface condition, boolean doCacheClear) throws GenericEntityException;

    /** Remove the named Related Entity for the GenericValue from the persistent store
     *@param relationName String containing the relation name which is the
     *      combination of relation.title and relation.rel-entity-name as
     *      specified in the entity XML definition file
     *@param value GenericValue instance containing the entity
     *@return int representing number of rows effected by this operation
     */
    public int removeRelated(String relationName, GenericValue value) throws GenericEntityException;

    /** Remove the named Related Entity for the GenericValue from the persistent store
     *@param relationName String containing the relation name which is the
     *      combination of relation.title and relation.rel-entity-name as
     *      specified in the entity XML definition file
     *@param value GenericValue instance containing the entity
     *@param doCacheClear boolean that specifies whether to clear cache entries for this value to be removed
     *@return int representing number of rows effected by this operation
     */
    public int removeRelated(String relationName, GenericValue value, boolean doCacheClear) throws GenericEntityException;

    /** Refresh the Entity for the GenericValue from the persistent store
     *@param value GenericValue instance containing the entity to refresh
     */
    public void refresh(GenericValue value) throws GenericEntityException;

    /** Refresh the Entity for the GenericValue from the persistent store
     *@param value GenericValue instance containing the entity to refresh
     *@param doCacheClear boolean that specifies whether or not to automatically clear cache entries related to this operation
     */
    public void refresh(GenericValue value, boolean doCacheClear) throws GenericEntityException;

    /** Refresh the Entity for the GenericValue from the cache
     *@param value GenericValue instance containing the entity to refresh
     */
    public void refreshFromCache(GenericValue value) throws GenericEntityException;

   /** Store a group of values
     *@param entityName The name of the Entity as defined in the entity XML file
     *@param fieldsToSet The fields of the named entity to set in the database
     *@param condition The condition that restricts the list of stored values
     *@return int representing number of rows effected by this operation
     *@throws GenericEntityException
     */
    public int storeByCondition(String entityName, Map<String, ? extends Object> fieldsToSet, EntityConditionInterface condition) throws GenericEntityException;

    /** Store a group of values
     *@param entityName The name of the Entity as defined in the entity XML file
     *@param fieldsToSet The fields of the named entity to set in the database
     *@param condition The condition that restricts the list of stored values
     *@param doCacheClear boolean that specifies whether to clear cache entries for these values
     *@return int representing number of rows effected by this operation
     *@throws GenericEntityException
     */
    public int storeByCondition(String entityName, Map<String, ? extends Object> fieldsToSet, EntityConditionInterface condition, boolean doCacheClear) throws GenericEntityException;

    /** Store the Entity from the GenericValue to the persistent store
     *@param value GenericValue instance containing the entity
     *@return int representing number of rows effected by this operation
     */
    public int store(GenericValue value) throws GenericEntityException;

    /** Store the Entity from the GenericValue to the persistent store
     *@param value GenericValue instance containing the entity
     *@param doCacheClear boolean that specifies whether or not to automatically clear cache entries related to this operation
     *@return int representing number of rows effected by this operation
     */
    public int store(GenericValue value, boolean doCacheClear) throws GenericEntityException;

    /** Store the Entities from the List GenericValue instances to the persistent store.
     *  <br/>This is different than the normal store method in that the store method only does
     *  an update, while the storeAll method checks to see if each entity exists, then
     *  either does an insert or an update as appropriate.
     *  <br/>These updates all happen in one transaction, so they will either all succeed or all fail,
     *  if the data source supports transactions. This is just like to othersToStore feature
     *  of the GenericEntity on a create or store.
     *@param values List of GenericValue instances containing the entities to store
     *@return int representing number of rows effected by this operation
     */
    public int storeAll(List<GenericValue> values) throws GenericEntityException;

    /** Store the Entities from the List GenericValue instances to the persistent store.
     *  <br/>This is different than the normal store method in that the store method only does
     *  an update, while the storeAll method checks to see if each entity exists, then
     *  either does an insert or an update as appropriate.
     *  <br/>These updates all happen in one transaction, so they will either all succeed or all fail,
     *  if the data source supports transactions. This is just like to othersToStore feature
     *  of the GenericEntity on a create or store.
     *@param values List of GenericValue instances containing the entities to store
     *@param doCacheClear boolean that specifies whether or not to automatically clear cache entries related to this operation
     *@return int representing number of rows effected by this operation
     */
    public int storeAll(List<GenericValue> values, boolean doCacheClear) throws GenericEntityException;

    /** Store the Entities from the List GenericValue instances to the persistent store.
     *  <br/>This is different than the normal store method in that the store method only does
     *  an update, while the storeAll method checks to see if each entity exists, then
     *  either does an insert or an update as appropriate.
     *  <br/>These updates all happen in one transaction, so they will either all succeed or all fail,
     *  if the data source supports transactions. This is just like to othersToStore feature
     *  of the GenericEntity on a create or store.
     *@param values List of GenericValue instances containing the entities to store
     *@param doCacheClear boolean that specifies whether or not to automatically clear cache entries related to this operation
     *@param createDummyFks boolean that specifies whether or not to automatically create "dummy" place holder FKs
     *@return int representing number of rows effected by this operation
     */
    public int storeAll(List<GenericValue> values, boolean doCacheClear, boolean createDummyFks) throws GenericEntityException;

    public int removeAll(String entityName) throws GenericEntityException;

    /** Remove the Entities from the List from the persistent store.
     *  <br/>The List contains GenericEntity objects, can be either GenericPK or GenericValue.
     *  <br/>If a certain entity contains a complete primary key, the entity in the datasource corresponding
     *  to that primary key will be removed, this is like a removeByPrimary Key.
     *  <br/>On the other hand, if a certain entity is an incomplete or non primary key,
     *  if will behave like the removeByAnd method.
     *  <br/>These updates all happen in one transaction, so they will either all succeed or all fail,
     *  if the data source supports transactions.
     *@param dummyPKs Collection of GenericEntity instances containing the entities or by and fields to remove
     *@return int representing number of rows effected by this operation
     */
    public int removeAll(List<? extends GenericEntity> dummyPKs) throws GenericEntityException;

    /** Remove the Entities from the List from the persistent store.
     *  <br/>The List contains GenericEntity objects, can be either GenericPK or GenericValue.
     *  <br/>If a certain entity contains a complete primary key, the entity in the datasource corresponding
     *  to that primary key will be removed, this is like a removeByPrimary Key.
     *  <br/>On the other hand, if a certain entity is an incomplete or non primary key,
     *  if will behave like the removeByAnd method.
     *  <br/>These updates all happen in one transaction, so they will either all succeed or all fail,
     *  if the data source supports transactions.
     *@param dummyPKs Collection of GenericEntity instances containing the entities or by and fields to remove
     *@param doCacheClear boolean that specifies whether or not to automatically clear cache entries related to this operation
     *@return int representing number of rows effected by this operation
     */
    public int removeAll(List<? extends GenericEntity> dummyPKs, boolean doCacheClear) throws GenericEntityException;

    // ======================================
    // ======= Find Methods =================
    // ======================================

    /** Find a Generic Entity by its Primary Key
     * NOTE 20080502: 6 references
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return The GenericValue corresponding to the primaryKey
     */
    public GenericValue findOne(String entityName, boolean useCache, Object... fields) throws GenericEntityException;
    
    /** Find a Generic Entity by its Primary Key
     * NOTE 20080502: 6 references
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return The GenericValue corresponding to the primaryKey
     */
    public GenericValue findOne(String entityName, Map<String, ? extends Object> fields, boolean useCache) throws GenericEntityException;

    /** Find a Generic Entity by its Primary Key
     * NOTE 20080502: 550 references (20080503 521 left); needs to be deprecated, should use findOne instead, but lots of stuff to replace!
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return The GenericValue corresponding to the primaryKey
     */
    public GenericValue findByPrimaryKey(String entityName, Map<String, ? extends Object> fields) throws GenericEntityException;

    /** Find a CACHED Generic Entity by its Primary Key
     * NOTE 20080502: 2 references
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return The GenericValue corresponding to the primaryKey
     */
    public GenericValue findByPrimaryKeyCache(String entityName, Object... fields) throws GenericEntityException;

    /** Find a CACHED Generic Entity by its Primary Key
     * NOTE 20080502: 218 references
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return The GenericValue corresponding to the primaryKey
     */
    public GenericValue findByPrimaryKeyCache(String entityName, Map<String, ? extends Object> fields) throws GenericEntityException;

    /** Find a Generic Entity by its Primary Key and only returns the values requested by the passed keys (names)
     * NOTE 20080502: 3 references
     *@param primaryKey The primary key to find by.
     *@param keys The keys, or names, of the values to retrieve; only these values will be retrieved
     *@return The GenericValue corresponding to the primaryKey
     */
    public GenericValue findByPrimaryKeyPartial(GenericPK primaryKey, Set<String> keys) throws GenericEntityException;

    /** Finds Generic Entity records by all of the specified fields (ie: combined using AND)
     * NOTE 20080502: 1 references
     * @param entityName The Name of the Entity as defined in the entity XML file
     * @param fields The fields of the named entity to query by with their corresponding values
     * @return List of GenericValue instances that match the query
     */
    public List<GenericValue> findByAnd(String entityName, Object... fields) throws GenericEntityException;

    /** Finds Generic Entity records by all of the specified fields (ie: combined using AND)
     * NOTE 20080502: 264 references
     * @param entityName The Name of the Entity as defined in the entity XML file
     * @param fields The fields of the named entity to query by with their corresponding values
     * @return List of GenericValue instances that match the query
     */
    public List<GenericValue> findByAnd(String entityName, Map<String, ? extends Object> fields) throws GenericEntityException;

    /** Finds Generic Entity records by all of the specified fields (ie: combined using AND)
     * NOTE 20080502: 72 references
     * @param entityName The Name of the Entity as defined in the entity XML file
     * @param fields The fields of the named entity to query by with their corresponding values
     * @param orderBy The fields of the named entity to order the query by;
     *      optionally add a " ASC" for ascending or " DESC" for descending
     * @return List of GenericValue instances that match the query
     */
    public List<GenericValue> findByAnd(String entityName, Map<String, ? extends Object> fields, List<String> orderBy) throws GenericEntityException;

    /** Finds Generic Entity records by all of the specified fields (ie: combined using AND), looking first in the cache; uses orderBy for lookup, but only keys results on the entityName and fields
     * NOTE 20080502: 91 references
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@return List of GenericValue instances that match the query
     */
    public List<GenericValue> findByAndCache(String entityName, Map<String, ? extends Object> fields) throws GenericEntityException;

    /** Finds Generic Entity records by all of the specified fields (ie: combined using AND), looking first in the cache; uses orderBy for lookup, but only keys results on the entityName and fields
     * NOTE 20080502: 56 references
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     *@param orderBy The fields of the named entity to order the query by; optionally add a " ASC" for ascending or " DESC" for descending
     *@return List of GenericValue instances that match the query
     */
    public List<GenericValue> findByAndCache(String entityName, Map<String, ? extends Object> fields, List<String> orderBy) throws GenericEntityException;

    /** Finds GenericValues by the conditions specified in the EntityCondition object, the the EntityCondition javadoc for more details.
     * NOTE 20080502: 3 references
     *@param entityName The name of the Entity as defined in the entity XML file
     *@param whereEntityCondition The EntityCondition object that specifies how to constrain this query before any groupings are done (if this is a view entity with group-by aliases)
     *@param havingEntityCondition The EntityCondition object that specifies how to constrain this query after any groupings are done (if this is a view entity with group-by aliases)
     *@param fieldsToSelect The fields of the named entity to get from the database; if empty or null all fields will be retreived
     *@param orderBy The fields of the named entity to order the query by; optionally add a " ASC" for ascending or " DESC" for descending
     *@param findOptions An instance of EntityFindOptions that specifies advanced query options. See the EntityFindOptions JavaDoc for more details.
     *@return EntityListIterator representing the result of the query: NOTE THAT THIS MUST BE CLOSED (preferably in a finally block) WHEN YOU ARE
     *      DONE WITH IT, AND DON'T LEAVE IT OPEN TOO LONG BEACUSE IT WILL MAINTAIN A DATABASE CONNECTION.
     */
    public EntityListIterator find(String entityName, EntityConditionInterface whereEntityCondition,
            EntityConditionInterface havingEntityCondition, Set<String> fieldsToSelect, List<String> orderBy, EntityFindOptions findOptions)
            throws GenericEntityException;

    /** Finds GenericValues by the conditions specified in the EntityCondition object, the the EntityCondition javadoc for more details.
     * NOTE 20080502: 12 references
     *@param entityName The name of the Entity as defined in the entity XML file
     *@param entityCondition The EntityCondition object that specifies how to constrain this query before any groupings are done (if this is a view entity with group-by aliases)
     *@param fieldsToSelect The fields of the named entity to get from the database; if empty or null all fields will be retreived
     *@param orderBy The fields of the named entity to order the query by; optionally add a " ASC" for ascending or " DESC" for descending
     *@param findOptions An instance of EntityFindOptions that specifies advanced query options. See the EntityFindOptions JavaDoc for more details.
     *@return List of GenericValue objects representing the result
     */
    public List<GenericValue> findList(String entityName, EntityConditionInterface entityCondition,
            Set<String> fieldsToSelect, List<String> orderBy, EntityFindOptions findOptions, boolean useCache)
            throws GenericEntityException;

    /** Finds GenericValues by the conditions specified in the EntityCondition object, the the EntityCondition javadoc for more details.
     * NOTE 20080502: 9 references
     *@param dynamicViewEntity The DynamicViewEntity to use for the entity model for this query; generally created on the fly for limited use
     *@param whereEntityCondition The EntityCondition object that specifies how to constrain this query before any groupings are done (if this is a view entity with group-by aliases)
     *@param havingEntityCondition The EntityCondition object that specifies how to constrain this query after any groupings are done (if this is a view entity with group-by aliases)
     *@param fieldsToSelect The fields of the named entity to get from the database; if empty or null all fields will be retreived
     *@param orderBy The fields of the named entity to order the query by; optionally add a " ASC" for ascending or " DESC" for descending
     *@param findOptions An instance of EntityFindOptions that specifies advanced query options. See the EntityFindOptions JavaDoc for more details.
     *@return EntityListIterator representing the result of the query: NOTE THAT THIS MUST BE CLOSED WHEN YOU ARE
     *      DONE WITH IT, AND DON'T LEAVE IT OPEN TOO LONG BEACUSE IT WILL MAINTAIN A DATABASE CONNECTION.
     */
    public EntityListIterator findListIteratorByCondition(DynamicViewEntityInterface dynamicViewEntity, EntityConditionInterface whereEntityCondition,
            EntityConditionInterface havingEntityCondition, Collection<String> fieldsToSelect, List<String> orderBy, EntityFindOptions findOptions)
            throws GenericEntityException;

    /**
     * NOTE 20080502: 2 references
     */
    public long findCountByCondition(String entityName, EntityConditionInterface whereEntityCondition,
            EntityConditionInterface havingEntityCondition, EntityFindOptions findOptions) throws GenericEntityException;

    /**
     * Get the named Related Entity for the GenericValue from the persistent store across another Relation.
     * Helps to get related Values in a multi-to-multi relationship.
     * NOTE 20080502: 3 references
     * @param relationNameOne String containing the relation name which is the
     *      combination of relation.title and relation.rel-entity-name as
     *      specified in the entity XML definition file, for first relation
     * @param relationNameTwo String containing the relation name for second relation
     * @param value GenericValue instance containing the entity
     * @param orderBy The fields of the named entity to order the query by; may be null;
     *      optionally add a " ASC" for ascending or " DESC" for descending
     * @return List of GenericValue instances as specified in the relation definition
     */
    public List<GenericValue> getMultiRelation(GenericValue value, String relationNameOne, String relationNameTwo, List<String> orderBy) throws GenericEntityException;

    /** Get the named Related Entity for the GenericValue from the persistent store
     * NOTE 20080502: 5 references
     * @param relationName String containing the relation name which is the
     *      combination of relation.title and relation.rel-entity-name as
     *      specified in the entity XML definition file
     * @param byAndFields the fields that must equal in order to keep; may be null
     * @param orderBy The fields of the named entity to order the query by; may be null;
     *      optionally add a " ASC" for ascending or " DESC" for descending
     * @param value GenericValue instance containing the entity
     * @return List of GenericValue instances as specified in the relation definition
     */
    public List<GenericValue> getRelated(String relationName, Map<String, ? extends Object> byAndFields, List<String> orderBy, GenericValue value) throws GenericEntityException;

    /** Get a dummy primary key for the named Related Entity for the GenericValue
     * NOTE 20080502: 2 references
     * @param relationName String containing the relation name which is the
     *      combination of relation.title and relation.rel-entity-name as
     *      specified in the entity XML definition file
     * @param byAndFields the fields that must equal in order to keep; may be null
     * @param value GenericValue instance containing the entity
     * @return GenericPK containing a possibly incomplete PrimaryKey object representing the related entity or entities
     */
    public GenericPK getRelatedDummyPK(String relationName, Map<String, ? extends Object> byAndFields, GenericValue value) throws GenericEntityException;

    /** Get the named Related Entity for the GenericValue from the persistent store, checking first in the cache to see if the desired value is there
     * NOTE 20080502: 4 references
     * @param relationName String containing the relation name which is the
     *      combination of relation.title and relation.rel-entity-name as
     *      specified in the entity XML definition file
     * @param value GenericValue instance containing the entity
     * @return List of GenericValue instances as specified in the relation definition
     */
    public List<GenericValue> getRelatedCache(String relationName, GenericValue value) throws GenericEntityException;

    /** Get related entity where relation is of type one, uses findByPrimaryKey
     * NOTE 20080502: 7 references
     * @throws IllegalArgumentException if the list found has more than one item
     */
    public GenericValue getRelatedOne(String relationName, GenericValue value) throws GenericEntityException;

    /** Get related entity where relation is of type one, uses findByPrimaryKey, checking first in the cache to see if the desired value is there
     * NOTE 20080502: 1 references
     * @throws IllegalArgumentException if the list found has more than one item
     */
    public GenericValue getRelatedOneCache(String relationName, GenericValue value) throws GenericEntityException;


    // ======================================
    // ======= Cache Related Methods ========
    // ======================================

    /** This method is a shortcut to completely clear all entity engine caches.
     * For performance reasons this should not be called very often.
     */
    public void clearAllCaches();

    public void clearAllCaches(boolean distribute);

    /** Remove all CACHED Generic Entity (List) from the cache
     *@param entityName The Name of the Entity as defined in the entity XML file
     */
    public void clearCacheLine(String entityName);

    /** Remove a CACHED Generic Entity (List) from the cache, either a PK, ByAnd, or All
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     */
    public void clearCacheLine(String entityName, Object... fields);

    /** Remove a CACHED Generic Entity (List) from the cache, either a PK, ByAnd, or All
     *@param entityName The Name of the Entity as defined in the entity XML file
     *@param fields The fields of the named entity to query by with their corresponding values
     */
    public void clearCacheLine(String entityName, Map<String, ? extends Object> fields);

    /** Remove a CACHED Generic Entity from the cache by its primary key.
     * Checks to see if the passed GenericPK is a complete primary key, if
     * it is then the cache line will be removed from the primaryKeyCache; if it
     * is NOT a complete primary key it will remove the cache line from the andCache.
     * If the fields map is empty, then the allCache for the entity will be cleared.
     *@param dummyPK The dummy primary key to clear by.
     */
    public void clearCacheLineFlexible(GenericEntity dummyPK);

    public void clearCacheLineFlexible(GenericEntity dummyPK, boolean distribute);

    public void clearCacheLineByCondition(String entityName, EntityConditionInterface condition);

    public void clearCacheLineByCondition(String entityName, EntityConditionInterface condition, boolean distribute);

    /** Remove a CACHED Generic Entity from the cache by its primary key, does NOT
     * check to see if the passed GenericPK is a complete primary key.
     * Also tries to clear the corresponding all cache entry.
     *@param primaryKey The primary key to clear by.
     */
    public void clearCacheLine(GenericPK primaryKey);

    public void clearCacheLine(GenericPK primaryKey, boolean distribute);

    /** Remove a CACHED GenericValue from as many caches as it can. Automatically
     * tries to remove entries from the all cache, the by primary key cache, and
     * the by and cache. This is the ONLY method that tries to clear automatically
     * from the by and cache.
     *@param value The GenericValue to clear by.
     */
    public void clearCacheLine(GenericValue value);

    public void clearCacheLine(GenericValue value, boolean distribute);

    public void clearAllCacheLinesByDummyPK(Collection<GenericPK> dummyPKs);

    public void clearAllCacheLinesByValue(Collection<GenericValue> values);

    public GenericValue getFromPrimaryKeyCache(GenericPK primaryKey);

    public void putInPrimaryKeyCache(GenericPK primaryKey, GenericValue value);

    public void putAllInPrimaryKeyCache(List<GenericValue> values);

    // ======= XML Related Methods ========
    public List<GenericValue> readXmlDocument(URL url) throws SAXException, ParserConfigurationException, java.io.IOException;

    public List<GenericValue> makeValues(Document document);

    public GenericPK makePK(Element element);

    public GenericValue makeValue(Element element);

    // ======= Misc Methods ========

    public void setEntityEcaHandler(EntityEcaHandler entityEcaHandler);

    public EntityEcaHandler getEntityEcaHandler();

    /** Get the next guaranteed unique seq id from the sequence with the given sequence name;
     * if the named sequence doesn't exist, it will be created
     *@param seqName The name of the sequence to get the next seq id from
     *@return String with the next sequenced id for the given sequence name
     */
    public String getNextSeqId(String seqName);

    /** Get the next guaranteed unique seq id from the sequence with the given sequence name;
     * if the named sequence doesn't exist, it will be created
     *@param seqName The name of the sequence to get the next seq id from
     *@param staggerMax The maximum amount to stagger the sequenced ID, if 1 the sequence will be incremented by 1, otherwise the current sequence ID will be incremented by a value between 1 and staggerMax
     *@return Long with the next seq id for the given sequence name
     */
    public String getNextSeqId(String seqName, long staggerMax);

    /** Get the next guaranteed unique seq id from the sequence with the given sequence name;
     * if the named sequence doesn't exist, it will be created
     *@param seqName The name of the sequence to get the next seq id from
     *@return Long with the next sequenced id for the given sequence name
     */
    public Long getNextSeqIdLong(String seqName);

    /** Get the next guaranteed unique seq id from the sequence with the given sequence name;
     * if the named sequence doesn't exist, it will be created
     *@param seqName The name of the sequence to get the next seq id from
     *@param staggerMax The maximum amount to stagger the sequenced ID, if 1 the sequence will be incremented by 1, otherwise the current sequence ID will be incremented by a value between 1 and staggerMax
     *@return Long with the next seq id for the given sequence name
     */
    public Long getNextSeqIdLong(String seqName, long staggerMax);

    /** Refreshes the ID sequencer clearing all cached bank values. */
    public void refreshSequencer();


    /** Look at existing values for a sub-entity with a sequenced secondary ID, and get the highest plus 1 */
    public void setNextSubSeqId(GenericValue value, String seqFieldName, int numericPadding, int incrementBy);

    public void encryptFields(List<? extends GenericEntity> entities) throws GenericEntityException;

    public void encryptFields(GenericEntity entity) throws GenericEntityException;

    public Object encryptFieldValue(String entityName, Object fieldValue) throws GenericEntityException;

    public void decryptFields(List<? extends GenericEntity> entities) throws GenericEntityException;

    public void decryptFields(GenericEntity entity) throws GenericEntityException;

    public GenericDelegator cloneDelegator(String delegatorName);

    public GenericDelegator cloneDelegator();
}
