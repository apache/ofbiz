Below is a list of issues filed in jira that might be nice to have
implemented in this branch.  This list is not absolute.  What is below
is just a quick run-through of items I saw that seem to be filed
against the entityengine.  Inclusion below does not imply that the
request will be implemented.

Other items could be added in the future; it would be nice to keep
list uptodate as work progresses.


OFBIZ-4857
	Check in GenericEntity.set whether the new value is different from the current one
OFBIZ-4844
	Fields in extended entity do not override attribute enable-audit-log
OFBIZ-4811
	Improving view-entity creation in both XML and DynamicViewEntity to exclude fields declared as Alias from the SQL Select strings
OFBIZ-4781
	entitymodel - entity-condition inside view-link does not work
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
OFBIZ-3959
	Introduce a ModelEntityReaderBuilder to decouple ModelEntity and ModelReader
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
OFBIZ-3520
	revision 897605 breaks certain delegator.find() EntityListIterator calls
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
OFBIZ-1232
	Data filtering in entity views
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
OFBIZ-836
	Bug in SqlJdbcUtil.java regarding outer join in oracle theta join mode
OFBIZ-810
	Improve export/import services
OFBIZ-563
	Entity Model Reader needs reserved word check
OFBIZ-293
	data import - nulling fields impossible

