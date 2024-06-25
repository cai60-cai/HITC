package com.cxs.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.cxs.plugins.SysViewMybatisPlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan(basePackages = "com.cxs.mapper")
public class MybatisConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

    @Bean
    public SysViewMybatisPlugin sysViewMybatisPlugin(){
        return new SysViewMybatisPlugin();
    }
}
