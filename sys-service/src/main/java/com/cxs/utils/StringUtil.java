package com.cxs.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;


public class StringUtil {
    /**
     * 生成32为随机唯一字符串
     * @return
     */
    public static String generatorUniqueSalt(){
        String str = UUID.randomUUID().toString().replaceAll("-", "");
        return str;
    }

    /**
     * 随机生成指定位数验证码
     * @param num
     * @return
     */
    public static String generatorCode(int num){
        StringBuilder sb = new StringBuilder(num);
        Random random = new Random();
        while (num-- > 0){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 30为文件名
     * @return
     */
    public static String generatorFileName(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String filePostfix = format.format(new Date());
        String filePrefix = UUID.randomUUID()
                .toString().replaceAll("-", "")
                .substring(0, 15);
        return filePrefix + "_" + filePostfix;
    }

    /**
     * 分割字符串
     * @param str
     * @param spiltPoint
     * @return
     */
    public static List<String> stringSpiltList(String str, String spiltPoint){
        List<String> result = new ArrayList<>();
        if (!StringUtils.hasLength(str)){
            return result;
        }
        result = Arrays.asList(str.split(spiltPoint));
        return result;
    }

    /**
     * 将列表用 spiltPoint 拼接
     * @param list
     * @param spiltPoint
     * @return
     */
    public static String joinListToString(List<String> list, String spiltPoint){
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(spiltPoint);
        for (String s : list) {
            joiner.add(s);
        }
        return joiner.toString();
    }

}
