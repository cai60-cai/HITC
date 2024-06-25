package com.cxs.dto.feedback;

import com.cxs.valid.annotation.ParamLengthValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class AddFeedbackDTO {

    /**
     * 反馈类型
     */
    @NotBlank(message = "feedbackType为必传项")
    @ParamLengthValid(max = 50, message = "feedbackType最大50字符")
    private String feedbackType;

    /**
     * 反馈内容
     */
    @NotBlank(message = "feedbackContent为必传项")
    @ParamLengthValid(max = 1000, message = "feedbackContent最大1000字符")
    private String feedbackContent;

    /**
     * 反馈图片
     */
    private List<String> feedbackImages;

    /**
     * 用户接收邮箱
     */
    private String feedbackEmail;
}
