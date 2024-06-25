package com.cxs.enums;

import com.cxs.constant.ResponseStateConstant;
import lombok.Getter;
import lombok.Setter;

  
public enum CurrencyErrorEnum {
    REQUEST_PARAMETER_ERROR(ResponseStateConstant.PARAMETER_ERROR, "入参错误"),
    FILE_UPLOAD_FAIL(-100001, "文件上传失败"),
    CODE_SEND_FAIL(-100002, "验证码发送失败"),
    UNAUTHORIZED_BE_OVERDUE(ResponseStateConstant.NO_LOGIN, "用户认证信息已过期"),
    ADMIN_LOGIN_FORBIDDEN(ResponseStateConstant.NO_PROMISSION, "登陆失败,该用户暂未授权管理权限"),
    UNAUTHORIZED(ResponseStateConstant.NO_LOGIN, "用户身份信息认证失败"),
    OPERA_ERROR(ResponseStateConstant.OPERA_FAIL, "操作失败"),
    DATABASE_ERROR(ResponseStateConstant.OPERA_FAIL, "数据库错误"),
    DATABASE_READ_ONLY(ResponseStateConstant.NO_LOGIN, "权限不足,该用户只有读权限"),
    AUTH_LOCK(ResponseStateConstant.OPERA_FAIL, "用户权限被锁定"),
    SYSTEM_ERROR(ResponseStateConstant.SERVER_ERROR, "系统错误"),
    ;

    @Setter
    @Getter
    private Integer code;

    @Setter
    @Getter
    private String msg;

    CurrencyErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
