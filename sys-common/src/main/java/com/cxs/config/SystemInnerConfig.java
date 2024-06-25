package com.cxs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Configuration
public class SystemInnerConfig {

    @Autowired
    private CommonConfig commonConfig;

    private List<String> innerUserList = new ArrayList<>(0);

    private List<String> innerSuperAdminList = new ArrayList<>(0);

    private List<Integer> innerRoleList = new ArrayList<>(0);

    private List<Integer> innerMenuList = new ArrayList<>(0);

    private Integer superAdminRoleId;
    private String superAdminRoleName;
    private Integer adminRoleId;
    private String adminRoleName;
    private Integer userRoleId;
    private String userRoleName;

    @PostConstruct
    public void initMethod(){
        String innerUser = commonConfig.getInnerUser();
        String innerSuperAdmin = commonConfig.getInnerSuperAdmin();
        String innerRole = commonConfig.getInnerRole();
        String innerMenu = commonConfig.getInnerMenu();
        this.superAdminRoleId = commonConfig.getSuperAdminRoleId();
        this.adminRoleId = commonConfig.getAdminRoleId();
        this.superAdminRoleName = commonConfig.getSuperAdminRoleName();
        this.adminRoleName = commonConfig.getAdminRoleName();
        this.userRoleId = commonConfig.getUserRoleId();
        this.userRoleName = commonConfig.getUserRoleName();
        if (StringUtils.hasLength(innerUser)) {
            this.innerUserList = Arrays.asList(innerUser.split(","));
        }

        if (StringUtils.hasLength(innerSuperAdmin)) {
            this.innerSuperAdminList = Arrays.asList(innerSuperAdmin.split(","));
        }

        if (StringUtils.hasLength(innerRole)) {
            this.innerRoleList = Arrays.stream(innerRole.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        }

        if (StringUtils.hasLength(innerMenu)) {
            this.innerMenuList = Arrays.stream(innerMenu.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        }
    }
}
