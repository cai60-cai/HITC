package com.cxs.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

  
@Data
public class LogInfoBo {

    private Integer id;

    /**
     * 操作结果 1、成功 2、失败
     */
    private Integer operaResult;

    /**
     * 操作方法名
     */
    private String operaMethod;

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
     * 失败原因描述
     */
    private String operaErrorWhy;

    /**
     * 操作人用户id
     */
    private UserBo operaUser;

    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operaTime;

}
