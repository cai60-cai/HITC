package com.cxs.client.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Pay1Req {

    @JSONField(name = "primary_industry_name")
    private String primaryIndustryName = "IT科技/IT软件与服务";
    @JSONField(name = "primary_industry_code")
    private String primaryIndustryCode = "10001/20102";
    @JSONField(name = "secondary_industry_code")
    private String secondaryIndustryCode = "10001/20102";
    @JSONField(name = "secondary_industry_name")
    private String secondaryIndustryName = "IT科技/IT软件与服务";
}
