Below is a list of issues filed in jira that might be nice to have
implemented in this branch.  This list is not absolute.  What is below
is just a quick run-through of items I saw that seem to be filed
against the entityengine.  Inclusion below does not imply that the
request will be implemented.

Other items could be added in the future; it would be nice to keep
list uptodate as work progresses.

Key:

(svn-name) - owner for item
* todo item
+ in-progress
- done

+ (doogie)DynamicViewEntity(dve)
  + (doogie)support for anonymous-views
    this requires being able to add 'ModelEntity' to ModelViewEntity, instead of just entityName.
  + (doogie)distinct, orderBy, condition
    this is just exposing ModelViewEntity.ViewEntityCondition
+ (doogie)QueryBuilder
  ListQuery query = delegator.query(entityName).useCache(true).distinct(true).byAnd([:]).byCondition().byRelation(relationName).orderBy([]);
  query = query.subList(i, i);
  for (GenericValue value: query) {
    // ListQuery implements List<GenericValue>, and doesn't query the database at all until
    // it needs real values.  At any pointed, you can continue to refilter the query, and
    // it will throw away the result values, so that the query will run again.

    // the pattern used for this is a read-only List, and each manipulator method returns
    // a new copy of the ListQuery object.

    // This use-pattern is similiar to perl's DBIx::Class.  I have a tool that was
    // implemented first in perl, but with an ofbiz compatible database, that I then
    // reimplemented in ofbiz entity calls, to get more speed(and better transaction
    // handling).
  }
  * group-by/function
  * complex-alias?
* (doogie)Modify sql code to do something similiar to the above.

+ (doogie)OFBIZ-3959 - Introduce a ModelEntityReaderBuilder to decouple ModelEntity and ModelReader
  I have this already started in another branch, I need to bring it here.
+ (doogie)Combine delegator name fields into a DelegatorName class.
  I have this already started in another branch, I need to bring it here.
+ (doogie)OFBIZ-836 - Bug in SqlJdbcUtil.java regarding outer join in oracle theta join mode
  We have a client that wanted us to use oracle, and I believe this one might be fixed now(in trunk).
+ (doogie)OFBIZ-1232 - Data filtering in entity views
  I am guessing that this is already done, just might need to close the issue.
+ (doogie)OFBIZ-3520 - revision 897605 breaks certain delegator.find() EntityListIterator calls
  This was fixed long ago by David, but the issue never closed.  I actually have a test case in another branch.
* (doogie)OFBIZ-4781 - entitymodel - entity-condition inside view-link does not work


OFBIZ-4857
	Check in GenericEntity.set whether the new value is different from the current one
OFBIZ-4844
	Fields in extended entity do not override attribute enable-audit-log
OFBIZ-4811
	Improving view-entity creation in both XML and DynamicViewEntity to exclude fields declared as Alias from the SQL Select strings
OFBIZ-4346
	Support MySQL and Postgres's LIMIT and OFFSET options
OFBIZ-4310
	Conversion for complex-alias needs to be implemented
OFBIZ-4277
	Performance : In TransactionUtil StackTrace begining tracing should not be enabled by default
OFBIZ-4240
	Override UiLabels / Properties through tenant specific database
OFBIZ-4153
	Make entity engine a standalone database access tool/API
OFBIZ-4053
	Implement an Entity Query Builder
OFBIZ-3946
	add an SQL query manager
OFBIZ-3880
	EntityDateFilterCondition causes the entity cache to return false matches
OFBIZ-3748
	Remove test specific code in the GenericDelegator
OFBIZ-3554
	Delegator removeByXXX do not trigger remove ECA
OFBIZ-3522
	webtools ViewGeneric does not work with entities that have BigDecimal fields
OFBIZ-3421
	Allow data load to support explicit "null" values for entity update 
OFBIZ-2866
	Allow entity-data-reader to indicate if it should insert, update, or upsert records
OFBIZ-1636
	delegator.getNextSubSeqId does not guarantee primary key uniqueness
OFBIZ-1607
	let the ofbiz work with db2
OFBIZ-1254
	XML Data Export All does not include data present in blob fields
OFBIZ-1033
	Ofbiz SQL Integration Features
OFBIZ-1032
	EntitySaxReader mostly-insert (store after create-error) Fix
OFBIZ-1031
	GenericDelegator improvements
OFBIZ-1030
	Ofbiz SQL-Logging Extension
OFBIZ-1029
	TransactionUtil Refactoring
OFBIZ-1026
	EntityOperator IN_SUBQUERY enhancement
OFBIZ-810
	Improve export/import services
OFBIZ-563
	Entity Model Reader needs reserved word check
OFBIZ-293
	data import - nulling fields impossible

