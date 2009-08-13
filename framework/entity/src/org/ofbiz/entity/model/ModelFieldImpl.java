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
package org.ofbiz.entity.model;

import java.util.*;
import org.w3c.dom.*;

import org.ofbiz.entity.jdbc.*;
import org.ofbiz.base.util.*;

/**
 * Generic Entity - Field model class
 *
 */
@SuppressWarnings("serial")
public class ModelFieldImpl extends ModelChild implements ModelField {

    /** The name of the Field */
    protected String name = "";

    /** The type of the Field */
    protected String type = "";

    /** The col-name of the Field */
    protected String colName = "";

    /** boolean which specifies whether or not the Field is a Primary Key */
    protected boolean isPk = false;
    protected boolean encrypt = false;
    protected boolean isNotNull = false;
    protected boolean isAutoCreatedInternal = false;
    protected boolean enableAuditLog = false;

    /** validators to be called when an update is done */
    protected List<String> validators = new ArrayList<String>();

    /** Default Constructor */
    public ModelFieldImpl() {}

    /** Fields Constructor */
    public ModelFieldImpl(String name, String type, String colName, boolean isPk) {
        this(name, type, colName, isPk, false, false);
    }

    public ModelFieldImpl(String name, String type, String colName, boolean isPk, boolean encrypt, boolean enableAuditLog) {
        this.name = name;
        this.type = type;
        this.setColName(colName);
        this.isPk = isPk;
        this.encrypt = encrypt;
        this.enableAuditLog = enableAuditLog;
    }

    /** XML Constructor */
    public ModelFieldImpl(Element fieldElement) {
        this.type = UtilXml.checkEmpty(fieldElement.getAttribute("type")).intern();
        this.name = UtilXml.checkEmpty(fieldElement.getAttribute("name")).intern();
        this.setColName(UtilXml.checkEmpty(fieldElement.getAttribute("col-name")).intern());
        this.isPk = false; // is set elsewhere
        this.encrypt = UtilXml.checkBoolean(fieldElement.getAttribute("encrypt"), false);
        this.description = UtilXml.childElementValue(fieldElement, "description");
        this.enableAuditLog = UtilXml.checkBoolean(fieldElement.getAttribute("enable-audit-log"), false);
        this.isNotNull = UtilXml.checkBoolean(fieldElement.getAttribute("not-null"), false);

        NodeList validateList = fieldElement.getElementsByTagName("validate");

        for (int i = 0; i < validateList.getLength(); i++) {
            Element element = (Element) validateList.item(i);

            this.validators.add(UtilXml.checkEmpty(element.getAttribute("name")).intern());
        }
    }

    /** DB Names Constructor */
    public ModelFieldImpl(DatabaseUtil.ColumnCheckInfo ccInfo, ModelFieldTypeReader modelFieldTypeReader) {
        this.colName = ccInfo.columnName;
        this.name = ModelUtil.dbNameToVarName(this.colName);

        // figure out the type according to the typeName, columnSize and decimalDigits
        this.type = ModelUtil.induceFieldType(ccInfo.typeName, ccInfo.columnSize, ccInfo.decimalDigits, modelFieldTypeReader);

        this.isPk = ccInfo.isPk;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getName()
	 */
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#setName(java.lang.String)
	 */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getType()
	 */
    public String getType() {
        return this.type;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#setType(java.lang.String)
	 */
    public void setType(String type) {
        this.type = type;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getColName()
	 */
    public String getColName() {
        return this.colName;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#setColName(java.lang.String)
	 */
    public void setColName(String colName) {
        this.colName = colName;
        if (this.colName == null || this.colName.length() == 0) {
            this.colName = ModelUtil.javaNameToDbName(UtilXml.checkEmpty(this.name));
        }
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getIsPk()
	 */
    public boolean getIsPk() {
        return this.isPk;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#setIsPk(boolean)
	 */
    public void setIsPk(boolean isPk) {
        this.isPk = isPk;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getIsNotNull()
	 */
    public boolean getIsNotNull() {
        return this.isNotNull;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#setIsNotNull(boolean)
	 */
    public void setIsNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getEncrypt()
	 */
    public boolean getEncrypt() {
        return this.encrypt;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#setEncrypt(boolean)
	 */
    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getEnableAuditLog()
	 */
    public boolean getEnableAuditLog() {
        return this.enableAuditLog;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getIsAutoCreatedInternal()
	 */
    public boolean getIsAutoCreatedInternal() {
        return this.isAutoCreatedInternal;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#setIsAutoCreatedInternal(boolean)
	 */
    public void setIsAutoCreatedInternal(boolean isAutoCreatedInternal) {
        this.isAutoCreatedInternal = isAutoCreatedInternal;
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getValidator(int)
	 */
    public String getValidator(int index) {
        return this.validators.get(index);
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#getValidatorsSize()
	 */
    public int getValidatorsSize() {
        return this.validators.size();
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#addValidator(java.lang.String)
	 */
    public void addValidator(String validator) {
        this.validators.add(validator);
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#removeValidator(int)
	 */
    public String removeValidator(int index) {
        return this.validators.remove(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) return false;
        ModelField other = (ModelField) obj;
        return other.getName().equals(getName()) && other.getModelEntity() == getModelEntity();
    }

    @Override
    public int hashCode() {
        return getModelEntity().hashCode() ^ getName().hashCode();
    }

    @Override
    public String toString() {
        return getModelEntity() + "@" + getName();
    }

    /* (non-Javadoc)
	 * @see org.ofbiz.entity.model.ModelField#toXmlElement(org.w3c.dom.Document)
	 */
    public Element toXmlElement(Document document) {
        Element root = document.createElement("field");
        root.setAttribute("name", this.getName());
        if (!this.getColName().equals(ModelUtil.javaNameToDbName(this.getName()))) {
            root.setAttribute("col-name", this.getColName());
        }
        root.setAttribute("type", this.getType());
        if (this.getEncrypt()) {
            root.setAttribute("encrypt", "true");
        }
        if (this.getIsNotNull()) {
            root.setAttribute("not-null", "true");
        }

        Iterator<String> valIter = this.validators.iterator();
        if (valIter != null) {
            while (valIter.hasNext()) {
                String validator = valIter.next();
                Element val = document.createElement("validate");
                val.setAttribute("name", validator);
                root.appendChild(val);
            }
        }

        return root;
    }
}
