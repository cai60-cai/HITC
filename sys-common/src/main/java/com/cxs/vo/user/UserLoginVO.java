package com.cxs.vo.user;

import com.cxs.base.Token;
import lombok.Data;


@Data
public class UserLoginVO {
    private UserVO user;
    private Token tokenInfo;
}
