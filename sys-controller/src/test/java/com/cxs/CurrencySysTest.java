package com.cxs;

import com.alibaba.fastjson.JSON;
import com.cxs.config.CommonConfig;
import com.cxs.model.PointRechargeType;
import com.cxs.service.PointRechargeTypeService;
import com.cxs.utils.RSAUtil;
import com.cxs.utils.SendMailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;


@SpringBootTest
public class CurrencySysTest {

    @Autowired
    private SendMailUtil sendMailUtil;

    @Autowired
    private PointRechargeTypeService pointRechargeTypeService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CommonConfig commonConfig;


    @Test
    void test1(){
        List<PointRechargeType> list = pointRechargeTypeService.list(null);
        for (PointRechargeType pointRechargeType : list) {
            System.out.println(pointRechargeType.getMoney().multiply(new BigDecimal(pointRechargeType.getDiscount().toString())).divide(new BigDecimal("10")).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    void test2() throws Exception {
        String password = "123456";
        String str = RSAUtil.encrypt(password, commonConfig.getRsaPublicKey());
        System.out.println(str);
        String decrypt = RSAUtil.decrypt(str, commonConfig.getRsaPrivateKey());
        System.out.println(decrypt);
    }
}
