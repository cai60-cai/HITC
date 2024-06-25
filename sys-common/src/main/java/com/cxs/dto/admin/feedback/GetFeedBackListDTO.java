package com.cxs.dto.admin.feedback;

import com.cxs.base.BaseRequest;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Data
public class GetFeedBackListDTO extends BaseRequest {

    @ParamRangeValid(ranges = {"1", "0"}, message = "feedbackStatus范围不允许")
    private Integer feedbackStatus;

    public static void main(String[] args) {
        System.out.println( 0x00000001 );
    }
}
