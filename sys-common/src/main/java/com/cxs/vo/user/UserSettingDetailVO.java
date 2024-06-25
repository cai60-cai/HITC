package com.cxs.vo.user;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


@Data
public class UserSettingDetailVO {
    /**
     * 打赏功能,0、未开启 1、已开启
     */
    private Boolean openReward;

    /**
     * 显示积分余额,0、不显示 1、显示
     */
    private Boolean showPoint;

    private Boolean receiveEmailNotice;

    /**
     * 用户打赏信息
     */
    private JSONObject rewardInfo;
}
