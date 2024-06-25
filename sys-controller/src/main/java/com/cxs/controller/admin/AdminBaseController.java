package com.cxs.controller.admin;

import com.cxs.service.admin.AdminBaseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/base")
@Api(tags = "管理员公共控制器")
public class AdminBaseController {

    @Autowired
    private AdminBaseService adminBaseService;

}
