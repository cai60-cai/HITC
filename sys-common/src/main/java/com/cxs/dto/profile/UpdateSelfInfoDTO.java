package com.cxs.dto.profile;

import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;
import javax.validation.constraints.NotBlank;
  
@Data
public class UpdateSelfInfoDTO {
    /**
     * 用户名
     */
    @NotBlank(message = "userName岂能为空")
    @ParamLengthValid(max = 16, message = "userName最大16个字符")
    private String userName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 昵称
     */
    @NotBlank(message = "nickName岂能为空")
    @ParamLengthValid(max = 10, message = "nickName最大10个字符")
    private String nickName;

    /**
     * 手机号
     */
    @ParamLengthValid(max = 11, message = "phone格式不正确")
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户签名
     */
    @ParamLengthValid(max = 50, message = "autograph最大50字符")
    private String autograph;

    /**
     * 性别 1、男 2、女
     */
    @ParamRangeValid(ranges = {"1", "2"}, message = "性别不允许,1、男 2、女")
    private Integer sex;
}
