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

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generic Entity - Relation model class
 *
 */
public interface ModelRelationInterface {

    public String getCombinedName();

    /** the title, gives a name/description to the relation */
    public String getTitle();

    public void setTitle(String title);

    /** the type: either "one" or "many" or "one-nofk" */
    public String getType();

    public void setType(String type);

    /** the name of the related entity */
    public String getRelEntityName();

    public void setRelEntityName(String relEntityName);

    public String getFkName();

    public void setFkName(String fkName);

    /** keyMaps defining how to lookup the relatedTable using columns from this table */
    public Iterator<ModelKeyMapInterface> getKeyMapsIterator();

    public List<ModelKeyMapInterface> getKeyMapsClone();

    public int getKeyMapsSize();

    public ModelKeyMapInterface getKeyMap(int index);

    /** Find a KeyMap with the specified fieldName */
    public ModelKeyMapInterface findKeyMap(String fieldName);

    /** Find a KeyMap with the specified relFieldName */
    public ModelKeyMapInterface findKeyMapByRelated(String relFieldName);

    /**
     * @return Returns the isAutoRelation.
     */
    public boolean isAutoRelation();
    /**
     * @param isAutoRelation The isAutoRelation to set.
     */
    public void setAutoRelation(boolean isAutoRelation);

    public Element toXmlElement(Document document);
}
