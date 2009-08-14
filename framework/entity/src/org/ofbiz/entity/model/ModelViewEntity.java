package org.ofbiz.entity.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ofbiz.entity.condition.EntityCondition;

public interface ModelViewEntity extends ModelEntity {

    public interface ComplexAliasMember {
        public void makeAliasColName(StringBuilder colNameBuffer, StringBuilder fieldTypeBuffer, ModelViewEntity modelViewEntity, ModelReader modelReader);
    }

    public interface ComplexAlias extends ComplexAliasMember {
        public void addComplexAliasMember(ComplexAliasMember complexAliasMember);
    }

    public interface ModelAlias {

        public String getColAlias();

        public String getDescription();

        public String getEntityAlias();

        public String getField();

        public String getFunction();

        public boolean getGroupBy();

        public boolean getIsFromAliasAll();

        public Boolean getIsPk();

        public String getName();

        public boolean isComplexAlias();

        public void makeAliasColName(StringBuilder colNameBuffer, StringBuilder fieldTypeBuffer, ModelViewEntity modelViewEntity, ModelReader modelReader);

        public void setComplexAliasMember(ComplexAliasMember complexAliasMember);

        public void setDescription(String description);
    }

    public interface ModelAliasAll {

        public String getEntityAlias();

        public String getFunction();

        public boolean getGroupBy();

        public String getPrefix();

        public boolean shouldExclude(String fieldName);
    }

    public interface ModelConversion {
        public void addAllAliasConversions(List<String> aliases, String fieldName);

        public void addAllAliasConversions(String fieldName, String... aliases);

        public void addConversion(String fromFieldName, String toFieldName);

        public Map<String, Object> convert(Map<String, Object> values);
    }

    public interface ModelMemberEntity {
        public String getEntityAlias();

        public String getEntityName();
    }

    public interface ModelViewLink {

        public String getEntityAlias();

        public ModelKeyMap getKeyMap(int index);

        public List<ModelKeyMap> getKeyMapsCopy();

        public Iterator<ModelKeyMap> getKeyMapsIterator();

        public int getKeyMapsSize();

        public String getRelEntityAlias();

        public boolean isRelOptional();
    }

    public static interface ViewCondition extends Serializable {
        public EntityCondition createCondition(ModelFieldTypeReader modelFieldTypeReader, List<String> entityAliasStack);
    }

    public interface ViewEntityCondition {
        public EntityCondition getHavingCondition(ModelFieldTypeReader modelFieldTypeReader, List<String> entityAliasStack);
        
        public List<String> getOrderByList();
        
        public EntityCondition getWhereCondition(ModelFieldTypeReader modelFieldTypeReader, List<String> entityAliasStack);
    }

    public void addMemberModelMemberEntity(ModelMemberEntity modelMemberEntity);

    public void addViewLink(ModelViewLink viewLink);

    public List<Map<String, Object>> convert(String fromEntityName, Map<String, Object> data);

    /** List of aliases with information in addition to what is in the standard field list */
    public ModelAlias getAlias(int index);

    public ModelAlias getAlias(String name);

    public ModelEntity getAliasedEntity(String entityAlias, ModelReader modelReader);

    public ModelField getAliasedField(ModelEntity aliasedEntity, String field, ModelReader modelReader);

    public ModelEntity getAliasedModelEntity();

    public List<ModelAlias> getAliasesCopy();

    public Iterator<ModelAlias> getAliasesIterator();

    public int getAliasesSize();

    public List<ModelMemberEntity> getAllModelMemberEntities();

    /** The col-name of the Field, the alias of the field if this is on a view-entity */
    public String getColNameOrAlias(String fieldName);

    public List<ModelField> getGroupBysCopy();

    public List<ModelField> getGroupBysCopy(List<ModelField> selectFields);

    public ModelEntity getMemberModelEntity(String alias);

    public Map<String, ModelMemberEntity> getMemberModelMemberEntities();

    public ModelMemberEntity getMemberModelMemberEntity(String alias);

    /** List of view links to define how entities are connected (or "joined") */
    public ModelViewLink getViewLink(int index);
    
    public List<ModelViewLink> getViewLinksCopy();


    public Iterator<ModelViewLink> getViewLinksIterator();

    public int getViewLinksSize();

    public void populateFields(ModelReader modelReader);

    public void populateFieldsBasic(ModelReader modelReader);

    public void populateReverseLinks();
    
    public void populateViewEntityConditionInformation(ModelFieldTypeReader modelFieldTypeReader, List<EntityCondition> whereConditions, List<EntityCondition> havingConditions, List<String> orderByList, List<String> entityAliasStack);

    public void removeMemberModelMemberEntity(String alias);

}
