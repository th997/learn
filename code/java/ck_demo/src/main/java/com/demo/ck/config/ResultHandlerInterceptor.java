package com.demo.ck.config;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;

import javax.persistence.Table;
import java.util.Arrays;
import java.util.Properties;

/**
 * mybatis 插件, 自动映射jpa实体类
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class ResultHandlerInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof Executor)) {
            return invocation.proceed();
        }
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        // xml sql 不做处理
        if (ms.getResource().contains(".xml")) {
            return invocation.proceed();
        }
        ResultMap resultMap = ms.getResultMaps().iterator().next();
        // 已有resultMap,不重复设置
        if (!CollectionUtils.isEmpty(resultMap.getResultMappings())) {
            return invocation.proceed();
        }
        Class<?> mapType = resultMap.getType();
        // @Table 注解才自动映射
        if (mapType.isAnnotationPresent(Table.class)) {
            EntityTable entityTable = EntityHelper.getEntityTable(mapType);
            resultMap = entityTable.getResultMap(ms.getConfiguration());
            MetaObject metaObject = MetaObjectUtil.forObject(ms);
            metaObject.setValue("resultMaps", Arrays.asList(resultMap));
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}