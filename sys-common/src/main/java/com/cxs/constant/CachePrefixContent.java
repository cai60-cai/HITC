package com.cxs.constant;

public interface CachePrefixContent {
    // 用户token
    String TOKEN_PREFIX = "token:";
    // 首页文章列表
    String INDEX_ARTICLE_LIST = "index:article-list";
    // 导航链接
    String INDEX_NAV_LIST = "index:nav-list";
    // 技术分类
    String INDEX_TECHNOLOGY_LIST = "index:technology-list";
    // 外部链接
    String INDEX_EXTERNALLINK_LIST = "index:externallink-list";
    // 标签
    String INDEX_TAG_LIST = "index:tag-list";
    // 首页系统信息
    String INDEX_SYS_INFO = "index:sys-info";
    // 文章热搜列表
    String INDEX_ARTICLE_HOT_LIST = "index:article-hot-list";
    // 官方推荐文章列表
    String INDEX_SYSTEM_RECOMMEND_LIST = "index:article-system-recommend-list";
    // 邮箱验证码
    String PUBLIC_EMAIL_CODE = "public:email-code";
    String PUBLIC_EMAIL_CODE_TIME = "public:email-code:time";
    // 用户文件下载信息
    String FILE_BEFORE_DOWNLOAD_VALID = "user:download:token:";
    // 签到
    String USER_EVERY_SIGN_PREFIX = "user:sign";
    // 订单前缀
    String POINT_TRADE_PREFIX = "point:trade:order:";
    // 验证码前缀
    String VALIDATE_CODE_PREFIX = "validate:code:";
    // 锁
    String LOCK_PREFIX = "lock:";
}
