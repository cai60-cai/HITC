package com.cxs.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PointTradeFlowVO {
    private Integer id;
    /**
     * 变动积分值
     */
    private Integer point;

    /**
     * 积分方向,1增加、0扣除
     */
    private Integer pointType;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 交易描述
     */
    private String tradeDesc;

    /**
     * 交易时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tradeTime;
}
