package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 问题回答表
 * @TableName t_realtion_question
 */
@TableName(value ="t_realtion_question")
@Data
public class RealtionQuestion implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String questionAnswer;

    /**
     * 是否展示
     */
    private Boolean isShow;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
