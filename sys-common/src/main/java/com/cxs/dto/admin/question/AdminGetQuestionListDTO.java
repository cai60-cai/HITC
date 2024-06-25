package com.cxs.dto.admin.question;

import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import lombok.Data;

  
@Data
public class AdminGetQuestionListDTO extends BaseRequest {

    private String keyword;
}
