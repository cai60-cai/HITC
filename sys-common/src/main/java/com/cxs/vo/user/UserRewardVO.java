package com.cxs.vo.user;

import lombok.Data;

@Data
public class UserRewardVO {
    private String userId;

    /**
     * 微信收款码
     */
    private String weixinImg;

    /**
     * 微信边框颜色
     */
    private String weixinBorderColor;

    /**
     * 支付宝收款码
     */
    private String zhifubaoImg;

    /**
     * 支付宝边框颜色
     */
    private String zhifubaoBorderColor;
}
