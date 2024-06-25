package com.cxs.dto.admin.notice;

import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


  
@Data
public class SaveOrUpdateNoticeDTO {

    private Integer id;
    /**
     * 公告标题
     */
    @NotBlank(message = "noticeTitle为必传项")
    @ParamLengthValid(max = 50, message = "公告标题最大50字符")
    private String noticeTitle;

    /**
     * 公告内容
     */
    @NotBlank(message = "noticeContent为必传项")
    private String noticeContent;

    /**
     * 是否首页轮询推送
     */
    @NotNull(message = "isPush为必传项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "isPush范围为{0,1}")
    private Integer isPush;
}
