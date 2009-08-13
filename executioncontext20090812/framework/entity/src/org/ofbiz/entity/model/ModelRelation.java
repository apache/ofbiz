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
package org.ofbiz.entity.model;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generic Entity - Relation model class
 *
 */
public interface ModelRelation {

    /** Find a KeyMap with the specified fieldName */
    public ModelKeyMap findKeyMap(String fieldName);

    /** Find a KeyMap with the specified relFieldName */
    public ModelKeyMap findKeyMapByRelated(String relFieldName);

    public String getCombinedName();

    /** The description for documentation purposes */
    public String getDescription();

    public String getFkName();

    public ModelKeyMap getKeyMap(int index);

    public List<ModelKeyMap> getKeyMapsClone();

    /** keyMaps defining how to lookup the relatedTable using columns from this table */
    public Iterator<ModelKeyMap> getKeyMapsIterator();

    public int getKeyMapsSize();

    /** @deprecated
     * the main entity of this relation */
   @Deprecated
   public ModelEntity getMainEntity();

    /** the name of the related entity */
    public String getRelEntityName();

    /** the title, gives a name/description to the relation */
    public String getTitle();

    /** the type: either "one" or "many" or "one-nofk" */
    public String getType();

    /**
     * @return Returns the isAutoRelation.
     */
    public boolean isAutoRelation();

    /**
     * @param isAutoRelation The isAutoRelation to set.
     */
    public void setAutoRelation(boolean isAutoRelation);

    public void setFkName(String fkName);

    public void setRelEntityName(String relEntityName);

    public void setTitle(String title);
    public void setType(String type);

    public Element toXmlElement(Document document);
}
