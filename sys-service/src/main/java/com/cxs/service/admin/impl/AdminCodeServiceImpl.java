package com.cxs.service.admin.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.cxs.base.BaseResult;
import com.cxs.bo.TableInfoBo;
import com.cxs.config.CommonConfig;
import com.cxs.config.MyDataSourceConfig;
import com.cxs.dto.CodeGenerateDTO;
import com.cxs.dto.admin.GetTableListDTO;
import com.cxs.mapper.TablesMapper;
import com.cxs.service.admin.AdminCodeService;
import com.cxs.utils.CodeGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class AdminCodeServiceImpl implements AdminCodeService {

    @Autowired
    private TablesMapper tablesMapper;

    @Autowired
    private MyDataSourceConfig myDataSourceConfig;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private CodeGenerateUtil codeGenerateUtil;

    @Override
    public void getTableList(GetTableListDTO dto, BaseResult result) {
        try {
            List<TableInfoBo> tableInfoList = tablesMapper.selectTables(myDataSourceConfig.getDatabase(), dto.getKeyword());
            result.setData(tableInfoList);
        } catch (Exception e) {

        }
    }

    @Override
    public void codeGenerate(CodeGenerateDTO dto, HttpServletResponse response) {
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String basepath = commonConfig.getTempDir() + File.separator + format.format(date);
            dto.setBasePath(basepath);
            codeGenerateUtil.generate(dto);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + format.format(date) + ".zip");
            ServletOutputStream oos = response.getOutputStream();
            File zipFile = ZipUtil.zip(new File(basepath), StandardCharsets.UTF_8);
            FileCopyUtils.copy(new FileInputStream(zipFile), oos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
