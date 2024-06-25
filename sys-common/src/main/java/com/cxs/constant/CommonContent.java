package com.cxs.constant;

public interface CommonContent {

    String FILE_ACCESS_PREFIX = "/upload";
    String ACCESS_TOKEN = "access_token";
    String ADMIN_TOKEN = "Admin-Token";
    String WEBSOCKET_PROTOCOL = "Sec-Websocket-Protocol";
    // 反馈一天有效期
    Long ONE_DAY_LONG = 1 * 24 * 60 * 60L;
    //  验证码的有效期
    Long CODE_EXPIRE = 60L;
    // 重置的用户密码
    String RESET_PASSWORD = "1234567890abc!";
    // 允许上传文件的后缀
    String[] IMG_LIST = {".jpg",".png",".jpeg"};
    // 允许上传的文件大小
    Long FILE_MAX_SIZE = 2000 * 1024 * 1024L;

    String LOGIN_REQUEST = "/auth/login";

    // 操作类型
    Integer ORDER_TOPPING_CODE = 1;
    Integer ORDER_UP_CODE = 2;
    Integer ORDER_DOWN_CODE = 3;
    Integer ORDER_DEL_CODE = 4;

    // 系统角色
    String SUPER_ADMIN_FLAG = "ROLE_super_admin";
    String ADMIN_FLAG = "ROLE_admin";
    String USER_FLAG = "ROLE_user";
    String SYS_VIEW = "ROLE_sys_view";

    // 操作结果
    Integer OPERA_SUCCESS = 1;
    Integer OPERA_FAILARE = 2;

    // 文章状态 0待审核 1已通过 2已拒绝
    Integer ARTICLE_PASS = 1;
    Integer ARTICLE_WAITING = 0;
    Integer ARTICLE_REJECT = 2;

    // 积分 0扣除 1增加
    Integer POINT_INCR = 1;
    Integer POINT_DECR = 0;
}
