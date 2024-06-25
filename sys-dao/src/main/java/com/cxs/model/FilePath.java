package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文件资源表
 * @TableName t_file_path
 */
@TableName(value ="t_file_path")
@Data
public class FilePath implements Serializable {
    /**
     * 文件id
     */
    @TableId
    private String fileId;

    /**
     * 源文件名
     */
    private String fileOrignName;

    /**
     * 文件真实路径
     */
    private String fileRealPath;

    /**
     * 文件访问路径
     */
    private String fileAccessUrl;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件结构
     */
    private String fileStructure;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}