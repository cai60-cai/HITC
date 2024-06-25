package com.cxs.client;

import com.cxs.client.req.LocationReq;
import com.cxs.client.resp.LocationResp;

public interface LocationClient {
    LocationResp getAddr(LocationReq req);
}
