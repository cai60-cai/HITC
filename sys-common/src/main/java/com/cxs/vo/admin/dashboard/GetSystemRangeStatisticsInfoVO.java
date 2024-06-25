package com.cxs.vo.admin.dashboard;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetSystemRangeStatisticsInfoVO {
    private List<String> weekList;

    private DataInfo lineData;

    @Data
    public static class DataInfo{
        private DataVO newUserInfo;
        private DataVO publishInfo;
        private DataVO activeInfo;
        private DataVO ordersInfo;
    }

    @Data
    public static class DataVO{
        // 期望值
        private List<Integer> expectedData = new ArrayList<>();
        // 实际值
        private List<Integer> actualData = new ArrayList<>();
    }
}
