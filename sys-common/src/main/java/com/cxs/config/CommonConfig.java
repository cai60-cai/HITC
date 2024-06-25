package com.cxs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

  
@Data
@Component
public class CommonConfig {

    @Value("${auth.ignore}")
    private String[] ignoreUrl;

    @Value("${location.baidu.secret}")
    private String locationSecret;

    @Value("${location.baidu.url}")
    private String locationUrl;

    @Value("${spring.application.name}")
    private String application;

    @Value("${auth.jwt.signingKey}")
    private String signingKey;

    @Value("${auth.jwt.validityTime:30}")
    private Long validityTime;

    @Value("${auth.code-period:60}")
    private Long codePeriod;

    @Value("${auth.domain}")
    private String domian;

    @Value("${spring.servlet.tempDir}")
    private String tempDir;

    @Value("${common.dev}")
    private Boolean dev;

    /**
     * 系统内置配置start
     */

    @Value("${common.system.inner-user}")
    private String innerUser;

    @Value("${common.system.inner-super-admin}")
    private String innerSuperAdmin;

    @Value("${common.system.inner-role}")
    private String innerRole;

    @Value("${common.system.inner-menu}")
    private String innerMenu;

    @Value("${common.system.super-admin-role-id}")
    private Integer superAdminRoleId;

    @Value("${common.system.admin-role-id}")
    private Integer adminRoleId;

    @Value("${common.system.super-admin-role-name}")
    private String superAdminRoleName;

    @Value("${common.system.admin-role-name}")
    private String adminRoleName;

    @Value("${common.system.user-role-id}")
    private Integer userRoleId;

    @Value("${common.system.user-role-name}")
    private String userRoleName;


    @Value("${common.file-upload.path}")
    private String fileUploadPath;

    @Value("${common.file-upload.img-suffix}")
    private String fileUploadImgSuffix;
    @Value("${common.file-upload.file-suffix}")
    private String fileUploadFileSuffix;
    @Value("${common.file-upload.img-path}")
    private String fileUploadImgPath;
    @Value("${common.file-upload.file-path}")
    private String fileUploadFilePath;

    @Value("${common.file-access.default}")
    private String defaultImageUrl;
    /**
     * 系统内置配置end
     */

    @Value("${common.default-pwd}")
    private String defaultPwd;

    /**
     * 缓存时间
     */
    @Value("${cache.time.short-time}")
    private Long cacheShortTime;

    @Value("${cache.time.long-time}")
    private Long cacheLongTime;

    /**
     * 文章热搜
     */
    @Value("${article.hot-search.list-num:10}")
    private Integer articleHotSearchlistNum;

    @Value("${article.hot-search.data-space:3}")
    private Integer articleHotSearchDataSpace;

    /**
     * 系统推荐
     */
    @Value("${article.sys-recommend.list-num:10}")
    private Integer articleSysRecommendlistNum;

    /**
     * rsa 加密相关
     */
    // 加密公钥
    @Value("${rsa.public_key}")
    private String rsaPublicKey;
    // 私钥
    @Value("${rsa.private_key}")
    private String rsaPrivateKey;
    /**
     * ********************rsa加密结束
     */


    /**
     * alipay
     */
    // appid
    @Value("${alipay.app-id}")
    private String appId;
    // 网关
    @Value("${alipay.gateway-url}")
    private String gatewayUrl;

    @Value("${alipay.merchant_private_key}")
    private String merchantPrivateKey;

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Value("${alipay.notify_url}")
    private String notifyUrl;

    @Value("${alipay.return_url}")
    private String returnUrl;

    @Value("${alipay.sign_type}")
    private String signType;

    @Value("${alipay.charset}")
    private String charset;
    /**
     * ********************alipay 结束
     */
    @Value("${aes.key}")
    private String aesKey;

}
