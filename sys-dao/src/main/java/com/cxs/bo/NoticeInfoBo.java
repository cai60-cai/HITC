package com.cxs.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

  
@Data
public class NoticeInfoBo {

    private Integer id;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 是否首页轮询推送
     */
    private Integer isPush;

    /**
     * 发布人用户id
     */
    private UserBo user;

    /**
     * 发布时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
