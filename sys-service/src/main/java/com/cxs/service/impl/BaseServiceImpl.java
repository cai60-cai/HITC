package com.cxs.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.constant.CommonContent;
import com.cxs.constant.ResponseStateConstant;
import com.cxs.dto.SendEmailCodeDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.exception.CurrencyException;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.model.FilePath;
import com.cxs.model.NavLink;
import com.cxs.model.UserAuth;
import com.cxs.service.BaseService;
import com.cxs.service.FilePathService;
import com.cxs.service.UserService;
import com.cxs.utils.FileUploadUtil;
import com.cxs.utils.RedisUtil;
import com.cxs.utils.SendMailUtil;
import com.cxs.utils.StringUtil;
import com.cxs.vo.base.ValidateCodeVO;
import com.cxs.vo.main.NavLinkVO;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipInputStream;


@Service
@Slf4j
public class BaseServiceImpl implements BaseService {

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private FilePathService filePathService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SendMailUtil sendMailUtil;

    @Autowired
    private Producer captchaProducer;

    /**
     * 单图片上传
     *
     * @param file
     * @return
     */
    @Override
    public void imgUploadFile(MultipartFile file, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String[] prefixList = CommonContent.IMG_LIST;
            String originalFilename = file.getOriginalFilename();
            if (!StringUtils.hasLength(originalFilename)) {
                result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("文件不能为空");
            } else {
                String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
                if (!Arrays.asList(prefixList).contains(prefix)) {
                    result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("上传失败,文件类型暂未授权!");
                    log.error("【文件上传】:文件上传失败,类型不支持,{}", prefix);
                } else if (CommonContent.FILE_MAX_SIZE < file.getSize()) {
                    result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("上传失败,文件大小超出限制!");
                    log.error("【文件上传】:文件上传失败,文件大小超出10MB,{}kb", file.getSize() / 1024);
                } else {
                    try {
                        String fileName = StringUtil.generatorFileName() + prefix;
                        String url = fileUploadUtil.uploadAndUrl(fileName, file.getBytes());
                        result.setCode(ResponseStateConstant.OPERA_SUCCESS).setMsg("文件上传成功").setData(url);
                    } catch (Exception e) {
                        log.error("文件上传失败,{}", e);
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文件上传失败");
                    }
                }
            }
        } catch (Exception e) {
            log.error("文件上传失败失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【文件上传接口】【{}ms】 \n入参:{}\n出参:{}", "文件上传", endTime - startTime, file, result);
        }
    }

    @Override
    public void fileUpload(MultipartFile file, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        ZipInputStream zipInputStream = null;
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
            if (!userAuth.getUploadAuth()) {
                result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
                result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法进行文件上传,请联系管理员");
            } else {
                if (CommonContent.FILE_MAX_SIZE < file.getSize()) {
                    result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("上传失败,文件大小超出限制!");
                    log.error("【文件上传】:文件上传失败,文件大小超出20MB,{}kb", file.getSize() / 1024);
                    return;
                }
                String basePath = fileUploadBefore(file, result);
                if (!StringUtils.hasLength(basePath)) {
                    return;
                }
                String originalFilename = file.getOriginalFilename();
                String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
                if (Arrays.asList(commonConfig.getFileUploadImgSuffix().split(",")).contains(prefix)) {
                    String fileName = null;
                    if (!ObjectUtils.isEmpty(userByToken)) {
                        fileName = userByToken.getId() + getDateStr() + prefix;
                    } else {
                        fileName = IdUtil.simpleUUID() + getDateStr() + prefix;
                    }
                    String filePath = basePath + File.separator + fileName;
                    file.transferTo(new File(filePath));
                    result.setData(CommonContent.FILE_ACCESS_PREFIX + "/" + getSimpleDateStr() + "/" + fileName);
                } else if (Arrays.asList(commonConfig.getFileUploadFileSuffix().split(",")).contains(prefix)) {
                    String fileName = null;
                    if (!ObjectUtils.isEmpty(userByToken)) {
                        fileName = userByToken.getId() + getDateStr() + prefix;
                    } else {
                        fileName = IdUtil.simpleUUID() + getDateStr() + prefix;
                    }
                    String filePath = basePath + File.separator + fileName;
                    file.transferTo(new File(filePath));
                    zipInputStream = new ZipInputStream(new FileInputStream(filePath), Charset.forName("GBK"));
                    List list = new ArrayList();
                    FileUploadUtil.getZipTreeUtil(zipInputStream.getNextEntry(), zipInputStream, list, "");
                    FilePath fileModel = new FilePath();
                    fileModel.setFileId(IdUtil.simpleUUID());
                    fileModel.setCreateDate(LocalDateTime.now());
                    fileModel.setFileSize(file.getSize());
                    fileModel.setFileOrignName(originalFilename);
                    fileModel.setFileRealPath(filePath);
                    fileModel.setFileStructure(JSON.toJSONString(list));
                    boolean save = filePathService.save(fileModel);
                    if (save) {
                        result.setData(fileModel);
                    } else {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            log.error("文件上传失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + "," + e.getMessage());
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long endTime = System.currentTimeMillis();
            log.info("【{}】【文件上传接口】【{}ms】 \n入参:{}\n出参:{}", "文件上传", endTime - startTime, file, result);
        }
    }

    @Override
    public void fileDownload(String fileId, HttpServletRequest request, HttpServletResponse response) {
        try {
            FilePath byId = filePathService.getById(fileId);
            File file = new File(byId.getFileRealPath());
            response.setHeader("fileName", byId.getFileOrignName());
            response.setContentType("application/zip");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "attachment;");
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(file);
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendEmailCode(SendEmailCodeDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String string = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.PUBLIC_EMAIL_CODE_TIME, dto.getEmail()));
            if (StringUtils.hasLength(string)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",验证码发送过于频繁,请稍后再试");
            } else {
                String code = StringUtil.generatorCode(6);
                redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.PUBLIC_EMAIL_CODE, dto.getEmail()), code, 3L, TimeUnit.MINUTES);
                sendMailUtil.sendMail(dto.getEmail(), "邮箱验证码", sendMailUtil.buildCodeContent(code));
                redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.PUBLIC_EMAIL_CODE_TIME, dto.getEmail()), dto.getEmail(), 30L, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("邮箱验证码发送失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【邮箱验证码发送接口】【{}ms】 \n入参:{}\n出参:{}", "发送验证码", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getValidateCode(BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            // 创建验证码文本
            ValidateCodeVO vo = new ValidateCodeVO();
            String key = IdUtil.simpleUUID();
            String code = captchaProducer.createText();
            redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.VALIDATE_CODE_PREFIX, key), code, commonConfig.getCodePeriod(), TimeUnit.SECONDS);
            vo.setKey(key);
            vo.setCode(code);
            result.setData(vo);
        } catch (Exception e) {
            log.error("生成验证码失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【生成验证码接口】【{}ms】 \n入参:{}\n出参:{}", "生成验证码", endTime - startTime, null, result);
        }
    }

    private String getDateStr() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    private String getSimpleDateStr() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 上传之前
     *
     * @param file
     * @param result
     * @return
     */
    private String fileUploadBefore(MultipartFile file, BaseResult result) {
        String res = null;
        String originalFilename = file.getOriginalFilename();
        // 创建跟文件
        File baseFile = new File(commonConfig.getFileUploadPath());
        if (!baseFile.exists()) {
            baseFile.mkdir();
        }
        String secondPath = null;
        // 获取后缀
        String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (Arrays.asList(commonConfig.getFileUploadImgSuffix().split(",")).contains(prefix)) {
            secondPath = commonConfig.getFileUploadImgPath();
        } else if (Arrays.asList(commonConfig.getFileUploadFileSuffix().split(",")).contains(prefix)) {
            secondPath = commonConfig.getFileUploadFilePath();
        } else {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文件类型暂不支持");
            return res;
        }

        // 判断二级路径是否存在
        String secondFilePath = commonConfig.getFileUploadPath() + File.separator + secondPath;
        File secondFile = new File(secondFilePath);
        if (!secondFile.exists()) {
            secondFile.mkdir();
        }

        // 创建三级路径
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateStr = format.format(date);
        String thirdFilePath = secondFilePath + File.separator + dateStr;
        File thirdFile = new File(thirdFilePath);
        if (!thirdFile.exists()) {
            thirdFile.mkdir();
        }
        if (thirdFile.exists() && thirdFile.isDirectory()) {
            res = thirdFilePath;
        }
        return res;
    }
}
