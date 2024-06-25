package com.cxs.client.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.cxs.client.LocationClient;
import com.cxs.client.req.LocationReq;
import com.cxs.client.resp.LocationResp;
import com.cxs.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
@Slf4j
public class LocationClientImpl implements LocationClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public LocationResp getAddr(LocationReq req) {
        if (req == null || !StringUtils.hasLength(req.getIp())) {
            return null;
        }
        LocationResp locationResp = null;
        try {
            MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
            param.add("ak", commonConfig.getLocationSecret());
            param.add("ip", req.getIp());
            URI uri = UriComponentsBuilder.fromHttpUrl(commonConfig.getLocationUrl()).queryParams(param).build().toUri();
            String result = restTemplate.getForObject(uri, String.class);
            locationResp = JSON.parseObject(result, LocationResp.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[百度地图定位服务]: 定位失败,入参:{}, 出参:{}", req, locationResp);
        }
        return locationResp;
    }
}
