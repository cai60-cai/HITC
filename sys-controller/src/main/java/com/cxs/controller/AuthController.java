package com.cxs.controller;

import com.cxs.base.BaseResult;
import com.cxs.dto.UserLoginDTO;
import com.cxs.service.BaseService;
import com.cxs.service.UserService;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
@Api(tags = "用户认证控制器")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private BaseService baseService;

    @PostMapping("/login")
    @ApiOperation("用户登录认证处理器")
    public BaseResult login(@RequestBody @Validated UserLoginDTO dto, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = BaseResult.ok();
        userService.login(dto, request, response, result);
        return result;
    }

    @PostMapping("/admin/login")
    @ApiOperation("管理员登录认证处理器")
    public BaseResult adminLogin(@RequestBody @Validated UserLoginDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.adminLogin(dto, request, result);
        return result;
    }

    @PostMapping("/checkToken")
    @ApiOperation("用户令牌检查处理器")
    public BaseResult checkToken(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.checkToken(request, result);
        return result;
    }

    @GetMapping("/getValidateCode")
    @ApiOperation("获取验证码处理器")
    public BaseResult getValidateCode(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        baseService.getValidateCode(result);
        return result;
    }

    @GetMapping("/getCaptcha")
    @ApiOperation("图片验证码处理器")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置内容类型
        response.setContentType("image/jpeg");
        // 创建验证码文本
        String capText = captchaProducer.createText();
        // 将验证码文本设置到session
        request.getSession().setAttribute("code", capText);
        // 创建验证码图片
        BufferedImage bi = captchaProducer.createImage(capText);
        // 获取响应输出流
        try (ServletOutputStream out = response.getOutputStream()){
            // 将图片验证码数据写到响应输出流
            ImageIO.write(bi, "jpg", out);
            // 推送并关闭响应输出流
            out.flush();
        }
    }
}
