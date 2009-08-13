package org.ofbiz.entity;

import java.util.Map;

import javolution.context.ObjectFactory;

import org.ofbiz.entity.model.ModelEntity;

public class EntityFactoryImpl implements EntityObjectFactory {

    @SuppressWarnings("serial")
	public static class NullGenericEntity extends GenericEntityImpl implements EntityFactory.NULL {
        protected NullGenericEntity() { }

        @Override
        public String getEntityName() {
            return "[null-entity]";
        }
        @Override
        public String toString() {
            return "[null-entity]";
        }
    }

    protected static final ObjectFactory<GenericPKImpl> genericPKFactory = new ObjectFactory<GenericPKImpl>() {
        @Override
        protected GenericPKImpl create() {
            return new GenericPKImpl();
        }
    };

    public GenericEntity createGenericEntity(GenericEntity value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot create a GenericEntity with a null value parameter");
        }
        GenericEntityImpl newEntity = new GenericEntityImpl();
        newEntity.init(value);
        return newEntity;
    }

    public GenericEntity createGenericEntity(ModelEntity modelEntity) {
        if (modelEntity == null) {
            throw new IllegalArgumentException("Cannot create a GenericEntity with a null modelEntity parameter");
        }
        GenericEntityImpl newEntity = new GenericEntityImpl();
        newEntity.init(modelEntity);
        return newEntity;
    }

	public GenericEntity createGenericEntity(ModelEntity modelEntity, Map<String, ? extends Object> fields) {
        if (modelEntity == null) {
            throw new IllegalArgumentException("Cannot create a GenericEntity with a null modelEntity parameter");
        }
        GenericEntityImpl newEntity = new GenericEntityImpl();
        newEntity.init(modelEntity, fields);
        return newEntity;
    }

	public GenericEntity createNullEntity() {
		return new NullGenericEntity();
	}

    /** Creates new GenericPK */
    public GenericPK createGenericPK(ModelEntity modelEntity) {
    	GenericPKImpl newPK = genericPKFactory.object();
        newPK.init(modelEntity);
        return newPK;
    }

    /** Creates new GenericPK from existing Map */
    public GenericPK createGenericPK(ModelEntity modelEntity, Map<String, ? extends Object> fields) {
    	GenericPKImpl newPK = genericPKFactory.object();
        newPK.init(modelEntity, fields);
        return newPK;
    }

    /** Creates new GenericPK from existing Map */
    public GenericPK createGenericPK(ModelEntity modelEntity, Object singlePkValue) {
    	GenericPKImpl newPK = genericPKFactory.object();
        newPK.init(modelEntity, singlePkValue);
        return newPK;
    }

    /** Creates new GenericPK from existing GenericPK */
    public GenericPK createGenericPK(GenericPK value) {
    	GenericPKImpl newPK = genericPKFactory.object();
        newPK.init(value);
        return newPK;
    }

}