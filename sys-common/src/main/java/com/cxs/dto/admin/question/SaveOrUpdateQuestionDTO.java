package com.cxs.dto.admin.question;

import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class SaveOrUpdateQuestionDTO {

    private Integer id;

    /**
     * 问题
     */
    @NotBlank(message = "question为必传项")
    private String question;

    /**
     * 答案
     */
    @NotBlank(message = "questionAnswer为必传项")
    private String questionAnswer;

    /**
     * 是否展示
     */
    @NotNull(message = "isShow为必传项")
    private Boolean isShow;
}
