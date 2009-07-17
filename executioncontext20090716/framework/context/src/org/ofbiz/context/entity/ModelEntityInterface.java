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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ModelEntityInterface extends Serializable {

    public boolean containsAllPkFieldNames(Set<String> fieldNames);

    // ===== GETTERS/SETTERS =====
    /** The entity-name of the Entity */
    public String getEntityName();

    public void setEntityName(String entityName);

    /** The plain table-name of the Entity without a schema name prefix */
    public String getPlainTableName();

    /** The table-name of the Entity including a Schema name if specified in the datasource config */
    public String getTableName(String helperName);

    public void setTableName(String tableName);

    /** The package-name of the Entity */
    public String getPackageName();

    public void setPackageName(String packageName);

    /** The default-resource-name of the Entity */
    public String getDefaultResourceName();

    public void setDefaultResourceName(String defaultResourceName);

    /** The entity-name of the Entity that this Entity is dependent on, if empty then no dependency */
    public String getDependentOn();

    public void setDependentOn(String dependentOn);

    /** An indicator to specify if this entity is never cached.
     * If true causes the delegator to not clear caches on write and to not get
     * from cache on read showing a warning messages to that effect
     */
    public boolean getNeverCache();

    public void setNeverCache(boolean neverCache);

    public boolean getAutoClearCache();

    public void setAutoClearCache(boolean autoClearCache);

    public boolean getHasFieldWithAuditLog();

    /* Get the location of this entity's definition */
    public String getLocation();

    /* Set the location of this entity's definition */
    public void setLocation(String location);

    /** An indicator to specify if this entity requires locking for updates */
    public boolean getDoLock();

    public void setDoLock(boolean doLock);

    public boolean lock();

    public Integer getSequenceBankSize();

    public void updatePkLists();

    public boolean isField(String fieldName);

    public boolean areFields(Collection<String> fieldNames);

    public int getPksSize();

    public ModelFieldInterface getOnlyPk();

    public Iterator<ModelFieldInterface> getPksIterator();

    public List<ModelFieldInterface> getPkFieldsUnmodifiable();

    public String getFirstPkFieldName();

    public int getNopksSize();

    public Iterator<ModelFieldInterface> getNopksIterator();

    public List<ModelFieldInterface> getNopksCopy();

    public int getFieldsSize();

    public Iterator<ModelFieldInterface> getFieldsIterator();

    public List<ModelFieldInterface> getFieldsUnmodifiable();

    /** The col-name of the Field, the alias of the field if this is on a view-entity */
    public String getColNameOrAlias(String fieldName);

    public ModelFieldInterface getField(String fieldName);

    public List<String> getAllFieldNames();

    public List<String> getPkFieldNames();

    public List<String> getNoPkFieldNames();

    public int getRelationsSize();

    public int getRelationsOneSize();

    public ModelRelationInterface getRelation(int index);

    public Iterator<ModelRelationInterface> getRelationsIterator();

    public List<ModelRelationInterface> getRelationsList(boolean includeOne, boolean includeOneNoFk, boolean includeMany);

    public List<ModelRelationInterface> getRelationsOneList();

    public List<ModelRelationInterface> getRelationsManyList();

    public ModelRelationInterface getRelation(String relationName);

    /**
     * @return Returns the noAutoStamp.
     */
    public boolean getNoAutoStamp();

    /**
     * @param noAutoStamp The noAutoStamp to set.
     */
    public void setNoAutoStamp(boolean noAutoStamp);

    public Element toXmlElement(Document document);
}
