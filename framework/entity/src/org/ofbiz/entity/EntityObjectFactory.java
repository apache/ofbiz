package org.ofbiz.entity;

import java.util.Map;

import org.ofbiz.entity.model.ModelEntity;

public interface EntityObjectFactory {

	/** Copy Factory Method: Creates new GenericEntity from existing GenericEntity */
	public GenericEntity createGenericEntity(GenericEntity value);

	/** Creates new GenericEntity */
	public GenericEntity createGenericEntity(ModelEntity modelEntity);

	/** Creates new GenericEntity from existing Map */
	public GenericEntity createGenericEntity(ModelEntity modelEntity,
			Map<String, ? extends Object> fields);

	public GenericEntity createNullEntity();

    /** Creates new GenericPK */
    public GenericPK createGenericPK(ModelEntity modelEntity);

    /** Creates new GenericPK from existing Map */
    public GenericPK createGenericPK(ModelEntity modelEntity, Map<String, ? extends Object> fields);

    /** Creates new GenericPK from existing Map */
    public GenericPK createGenericPK(ModelEntity modelEntity, Object singlePkValue);

    /** Creates new GenericPK from existing GenericPK */
    public GenericPK createGenericPK(GenericPK value);
}