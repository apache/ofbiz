package org.ofbiz.entity.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ModelField {

    public void addValidator(String validator);

    /** The col-name of the Field */
	public String getColName();

    /** The description for documentation purposes */
    public String getDescription();

    public boolean getEnableAuditLog();

	public boolean getEncrypt();

	public boolean getIsAutoCreatedInternal();

	public boolean getIsNotNull();

	/** boolean which specifies whether or not the Field is a Primary Key */
	public boolean getIsPk();

	public ModelEntity getModelEntity();

	/** The name of the Field */
	public String getName();

	/** The type of the Field */
	public String getType();

	/** validators to be called when an update is done */
	public String getValidator(int index);

	public int getValidatorsSize();

	public String removeValidator(int index);

	public void setColName(String colName);

	public void setDescription(String description);

	public void setEncrypt(boolean encrypt);

	public void setIsAutoCreatedInternal(boolean isAutoCreatedInternal);

	public void setIsNotNull(boolean isNotNull);

	public void setIsPk(boolean isPk);

	public void setName(String name);

	public void setType(String type);

	public Element toXmlElement(Document document);

}