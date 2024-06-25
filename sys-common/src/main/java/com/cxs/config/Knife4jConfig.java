package com.cxs.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //该注解是Springfox-swagger框架提供的使用Swagger注解，该注解必须加
@EnableKnife4j   //该注解是knife4j提供的增强注解,Ui提供了例如动态参数、参数过滤、接口排序等增强功能,如果你想使用这些增强功能就必须加该注解，否则可以不用加
public class Knife4jConfig {

    /**
     * swagger激活环境
     */
    @Value(value = "${swagger.enable}")
    public boolean enable;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("工大圈子接口文档")
                        .description("# cxs-currency-sys-server RESTful APIs")
                        .termsOfServiceUrl("https://www.cxs.plus/")
                        .contact("2014501@163.com")
                        .version("v1.0")
                        .build())
                //分组名称
                .groupName("2.0版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.cxs.controller"))
                .paths(PathSelectors.any())
                .build()
                .enable(enable);
        return docket;
    }
}
