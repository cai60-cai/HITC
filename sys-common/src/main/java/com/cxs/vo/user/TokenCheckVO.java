package com.cxs.vo.user;

import com.cxs.base.Token;
import lombok.Data;


@Data
public class TokenCheckVO {
    private Boolean status;
    private Token token;
}
