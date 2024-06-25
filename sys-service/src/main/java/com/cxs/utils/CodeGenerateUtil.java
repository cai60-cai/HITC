package com.cxs.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.cxs.config.MyDataSourceConfig;
import com.cxs.dto.CodeGenerateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CodeGenerateUtil {

    @Autowired
    private MyDataSourceConfig myDataSourceConfig;

    public void generate(CodeGenerateDTO dto){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        GlobalConfig gc = new GlobalConfig();
        String projectPath = dto.getBasePath();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(dto.getAuthor());
        gc.setOpen(false);
        gc.setIdType(IdType.AUTO);
        gc.setServiceName("%sService");
        gc.setFileOverride(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(myDataSourceConfig.getUrl());
        dsc.setDriverName(myDataSourceConfig.getDriverClassName());
        dsc.setUsername(myDataSourceConfig.getUsername());
        dsc.setPassword(myDataSourceConfig.getPassword());
//        dsc.setUrl("jdbc:mysql://120.48.3.188:10086/public_database?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
//        dsc.setDriverName("com.mysql.jdbc.Driver");
//        dsc.setUsername("public_user");
//        dsc.setPassword("cxs2022");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(dto.getParentPackageName());
        pc.setMapper(dto.getMapperPackageName());
        pc.setXml(dto.getMapperXmlPackageName());
        pc.setService(dto.getServicePackageName());
        pc.setServiceImpl(dto.getServiceImplPackageName());
        pc.setEntity(dto.getPojoPackageName());
        pc.setController(dto.getControllerPackageName());
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(dto.getTableNameList().toArray(new String[0]));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(dto.getIgnoreTablePrefix());
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
