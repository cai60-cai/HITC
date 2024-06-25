package com.cxs.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cxs.constant.CommonContent;
import com.cxs.interceptor.GloableInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class CustomizedWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //删除jackson的消息转换器
        converters.removeIf(item -> item instanceof MappingJackson2HttpMessageConverter);
        //定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullStringAsEmpty);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullNumberAsZero);
        //在converter中添加配置信息
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        //fastjson转换器必须放在StringHttpMessageConverter之后，不然接口返回值如果是字符串，会多加一对双引号
        converters.add(fastJsonHttpMessageConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //映射static路径的请求到static目录下
        registry.addResourceHandler( "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler( "/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler(CommonContent.FILE_ACCESS_PREFIX + "/**")
                .addResourceLocations("file:" + commonConfig.getFileUploadPath() + "/images/");
        System.out.println(commonConfig.getFileUploadPath());
        System.out.println("file:" + commonConfig.getFileUploadPath() + "/images/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GloableInterceptor())
                .addPathPatterns("/**");
    }
}
