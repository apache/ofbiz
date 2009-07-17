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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generic Entity - Field model class
 *
 */
public interface ModelFieldInterface {

    /** The name of the Field */
    public String getName();

    public void setName(String name);

    /** The type of the Field */
    public String getType();

    public void setType(String type);

    /** The col-name of the Field */
    public String getColName();

    public void setColName(String colName);

    /** boolean which specifies whether or not the Field is a Primary Key */
    public boolean getIsPk();

    public void setIsPk(boolean isPk);

    public boolean getIsNotNull();

    public void setIsNotNull(boolean isNotNull);

    public boolean getEncrypt();

    public void setEncrypt(boolean encrypt);

    public boolean getEnableAuditLog();

    public boolean getIsAutoCreatedInternal();

    public void setIsAutoCreatedInternal(boolean isAutoCreatedInternal);

    /** validators to be called when an update is done */
    public String getValidator(int index);

    public int getValidatorsSize();

    public void addValidator(String validator);

    public String removeValidator(int index);

    public Element toXmlElement(Document document);
}
