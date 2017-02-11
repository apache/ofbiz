-- ofbiz is the name of your database
SELECT ROUND(SUM(data_length+index_length)/1024/1024) AS total_mb,ROUND(SUM(data_length)/1024/1024) AS data_mb,ROUND(SUM(index_length)/1024/1024) AS index_mb FROM INFORMATION_SCHEMA.tables WHERE TABLE_SCHEMA='ofbiz';
