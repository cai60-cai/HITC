package com.cxs.dto.admin.user;

import com.cxs.base.BaseRequest;
import lombok.Data;

  
@Data
public class GetUserListDTO extends BaseRequest {
    private String keyWord;
}
