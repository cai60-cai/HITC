package com.cxs.vo.admin.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.vo.user.UserVO;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class LogInfoVO {

    private Integer id;

    /**
     * 操作结果 1、成功 2、失败
     */
    private Integer operaResult;

    /**
     * 入参
     */
    private String param;

    /**
     * 出参
     */
    private String response;

    /**
     * 操作说明
     */
    private String operaDesc;

    /**
     * 操作方法名
     */
    private String operaMethod;

    /**
     * 失败原因描述
     */
    private String operaErrorWhy;

    /**
     * 操作人用户id
     */
    private LogUserVO operaUser;

    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operaTime;

    @Data
    public static class LogUserVO{

        private String userId;

        private String nickName;
    }
}
