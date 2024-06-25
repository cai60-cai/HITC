package com.cxs.enums;

import com.cxs.vo.admin.report.GetReportHandleTypeVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

  
public enum ReportHandleEnum {
    ARTICLE_NO_RESULT("1-0", 1, "举报不实,不做处理"),
    ARTICLE_DELETE("1-1", 1, "删除该帖子"),
    ARTICLE_DELETE_AND_BAN_PUBLISH("1-2", 1, "删除该帖子,禁止该用户发布文章权限"),
    ARTICLE_BAN_UPLOAD("1-3", 1, "禁止该用户上传文件权限"),
    COMMENT_NO_RESULT("2-0", 2, "举报不实,不做处理"),
    COMMENT_DELETE("2-1", 2, "删除该评论"),
    COMMENT_DELETE_AND_BAN_COMMENT_LINSHI("2-2", 2, "删除该评论,禁止该用户发言权限"),
    COMMENT_BAN_COMMENT("2-3", 2, "禁止该用户发言权限"),

    ;
    @Setter
    @Getter
    private String handleNo;

    @Setter
    @Getter
    private Integer handleType;

    @Setter
    @Getter
    private String handleDesc;

    ReportHandleEnum(String handleNo, Integer handleType, String handleDesc) {
        this.handleNo = handleNo;
        this.handleType = handleType;
        this.handleDesc = handleDesc;
    }

    public static List<GetReportHandleTypeVO> getReportHandleTypeVOList(Integer handleType){
        ReportHandleEnum[] values = ReportHandleEnum.values();
        List<GetReportHandleTypeVO> list = new ArrayList<>();
        if (null == handleType) {
            return list;
        }
        for (ReportHandleEnum value : values) {
            if (value.getHandleType().equals(handleType)){
                GetReportHandleTypeVO vo = new GetReportHandleTypeVO();
                vo.setHandleNo(value.getHandleNo());
                vo.setHandleDesc(value.getHandleDesc());
                list.add(vo);
            }
        }
        return list;
    }

    public static String getReportHandleDesc(String handleNo){
        ReportHandleEnum[] values = ReportHandleEnum.values();
        String result = null;
        if (!StringUtils.hasLength(handleNo)) {
            return result;
        }
        for (ReportHandleEnum value : values) {
            if (value.getHandleNo().equals(handleNo)){
                result = value.getHandleDesc();
            }
        }
        return result;
    }
}
