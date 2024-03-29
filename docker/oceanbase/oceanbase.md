# oceanbase

## 查看/修改配置
show parameters like '%mem%'
alter system set memory_limit = '30G' 

## 查看/修改变量
show variables like '%paral%';
set global parallel_servers_target=90;

## olap配置
```conf
ALTER SYSTEM SET memory_limit='30G';
ALTER SYSTEM SET datafile_size='128G';
ALTER SYSTEM SET log_disk_size='5G';
ALTER SYSTEM SET cpu_count=0;

ALTER SYSTEM SET trace_log_slow_query_watermark='10s';
ALTER SYSTEM SET memstore_limit_percentage=50;
ALTER SYSTEM SET large_query_worker_percentage=20;
ALTER SYSTEM SET net_thread_count=24;
ALTER SYSTEM SET rpc_timeout=1000000000;
ALTER SYSTEM SET micro_block_merge_verify_level=0;
ALTER SYSTEM SET enable_sql_audit=False;
ALTER SYSTEM SET enable_perf_event=False;
ALTER SYSTEM SET cache_wash_threshold='30GB';
ALTER SYSTEM SET undo_retention=900;
SET GLOBAL ob_query_timeout=10000000000;
SET GLOBAL ob_trx_timeout=100000000000;
SET GLOBAL ob_sql_work_area_percentage=70;
SET GLOBAL parallel_servers_target=90;
SET GLOBAL max_allowed_packet=67108864;
```
参考 https://www.oceanbase.com/docs/enterprise-oceanbase-database-cn-10000000000881526#c9c74ad4-1b1b-462c-9002-f8b2f9aa7f9a

## 租户管理
创建租户： https://www.oceanbase.com/docs/common-oceanbase-database-cn-0000000001594479#581e4ff1-add3-4059-a64a-d0bdab546321
```sql
-- 查看租户名称和资源ID
select a.tenant_name, b.unit_config_id  from oceanbase.dba_ob_tenants a,oceanbase.dba_ob_resource_pools b where b.tenant_id=a.tenant_id;
-- 查看租户资源
select * from oceanbase.dba_ob_unit_configs
-- 修改租户资源大小
alter resource unit test_unit memory_size 2147483648,max_cpu 1,min_cpu 1
alter resource unit sys_unit_config memory_size 28240000000,max_cpu 15,min_cpu 15,max_iops 500000,min_iops 150000
```

## 选型
https://open.oceanbase.com/blog/27200135

## 部署
https://www.modb.pro/db/606070

https://blog.csdn.net/chrisy521/article/details/127660668

## 配置
https://github.com/oceanbase/obdeploy/tree/master/example/autodeploy

## 监控，告警
