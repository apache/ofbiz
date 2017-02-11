SELECT table_schema,engine, count(*) AS tables 
FROM information_schema.tables
WHERE table_schema NOT IN ('INFORMATION_SCHEMA','PERFORMANCE_SCHEMA')
GROUP BY table_schema,engine
ORDER BY 3 DESC;
