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
package org.ofbiz.context.entity;

import java.util.List;
/**
 * This class is used for declaring Dynamic View Entities, to be used and thrown away.
 * A special method exists on the GenericDelegator to accept a DynamicViewEntity instead
 * of an entity-name.
 *
 */
public interface DynamicViewEntityInterface {
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

    public void addAliasAll(String entityAlias, String prefix);

    public void addAlias(String entityAlias, String name);

    /** Add an alias, full detail. All parameters can be null except entityAlias and name. */
    public void addAlias(String entityAlias, String name, String field, String colAlias, Boolean primKey, Boolean groupBy, String function);

    public void addViewLink(String entityAlias, String relEntityAlias, Boolean relOptional, List<ModelKeyMapInterface> modelKeyMaps);

    public void addRelation(String type, String title, String relEntityName, List<ModelKeyMapInterface> modelKeyMaps);
}
