package org.ofbiz.entity;

import java.util.Map;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.model.ModelEntity;

public class EntityFactory {

	public static interface NULL {}

    public static class NullField implements NULL, Comparable<NullField> {
        protected NullField() { }

        @Override
        public String toString() {
            return "[null-field]";
        }

        public int compareTo(NullField other) {
            return this != other ? -1 : 0;
        }
    }

	protected static EntityObjectFactory entityFactory = null;

    public static final String module = EntityFactory.class.getName();

    public static GenericEntity NULL_ENTITY = null;

    public static final NullField NULL_FIELD = new NullField();

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
			entityFactory = (EntityObjectFactory) loader.loadClass("org.ofbiz.entity.EntityFactoryImpl").newInstance();
		} catch (Exception e) {
            Debug.logError(e, module);
		}
    	NULL_ENTITY = entityFactory.createNullEntity();
    }

	/** Copy Factory Method: Creates new GenericEntity from existing GenericEntity */
    public static GenericEntity createGenericEntity(GenericEntity value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot create a GenericEntity with a null value parameter");
        }
        return entityFactory.createGenericEntity(value);
    }

	/** Creates new GenericEntity */
    public static GenericEntity createGenericEntity(ModelEntity modelEntity) {
        if (modelEntity == null) {
            throw new IllegalArgumentException("Cannot create a GenericEntity with a null modelEntity parameter");
        }
        return entityFactory.createGenericEntity(modelEntity);
    }

	/** Creates new GenericEntity from existing Map */
    public static GenericEntity createGenericEntity(ModelEntity modelEntity, Map<String, ? extends Object> fields) {
        if (modelEntity == null) {
            throw new IllegalArgumentException("Cannot create a GenericEntity with a null modelEntity parameter");
        }
        return entityFactory.createGenericEntity(modelEntity, fields);
    }

    /** Creates new GenericPK */
    public static GenericPK createGenericPK(ModelEntity modelEntity) {
        return entityFactory.createGenericPK(modelEntity);
    }

    /** Creates new GenericPK from existing Map */
    public static GenericPK createGenericPK(ModelEntity modelEntity, Map<String, ? extends Object> fields) {
        return entityFactory.createGenericPK(modelEntity, fields);
    }

    /** Creates new GenericPK from existing Map */
    public static GenericPK createGenericPK(ModelEntity modelEntity, Object singlePkValue) {
        return entityFactory.createGenericPK(modelEntity, singlePkValue);
    }

    /** Creates new GenericPK from existing GenericPK */
    public static GenericPK createGenericPK(GenericPK value) {
        return entityFactory.createGenericPK(value);
    }
}