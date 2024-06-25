package com.cxs.plugins;

import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.exception.CurrencyException;
import com.cxs.utils.UserRoleStatusHolder;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

  
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class SysViewMybatisPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Boolean status = UserRoleStatusHolder.getCurrrentUserRoleStatus();
        if (status != null && status) {
            Object[] args = invocation.getArgs();
            MappedStatement mappedStatement  = (MappedStatement) args[0];
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if (sqlCommandType.equals(SqlCommandType.SELECT)) {
                return invocation.proceed();
            } else {
                throw new CurrencyException(CurrencyErrorEnum.DATABASE_READ_ONLY);
            }
        } else {
            return invocation.proceed();
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
