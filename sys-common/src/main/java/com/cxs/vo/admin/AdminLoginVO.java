package com.cxs.vo.admin;

import com.cxs.base.Token;
import lombok.Data;


@Data
public class AdminLoginVO {
    private AdminUserVO userInfo;
    private Token tokenInfo;
}
