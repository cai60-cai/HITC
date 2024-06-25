package com.cxs.client.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class LocationResp implements Serializable {

    /**
     * address : CN|甘肃|兰州|None|CERNET|0|0
     * content : {"address_detail":{"province":"甘肃省","city":"兰州市","district":"","street":"","street_number":"","city_code":36},"address":"甘肃省兰州市","point":{"y":"4284074.36","x":"11557683.25"}}
     * status : 0
     */

    private String address;
    private ContentBean content;
    private Integer status;

    @NoArgsConstructor
    @Data
    public static class ContentBean implements Serializable {
        /**
         * address_detail : {"province":"甘肃省","city":"兰州市","district":"","street":"","street_number":"","city_code":36}
         * address : 甘肃省兰州市
         * point : {"y":"4284074.36","x":"11557683.25"}
         */

        private AddressDetailBean addressDetail;
        private String address;
        private PointBean point;

        @NoArgsConstructor
        @Data
        public static class AddressDetailBean implements Serializable {
            /**
             * province : 甘肃省
             * city : 兰州市
             * district :
             * street :
             * street_number :
             * city_code : 36
             */

            private String province;
            private String city;
            private String district;
            private String street;
            private String streetNumber;
            private Integer cityCode;
        }

        @NoArgsConstructor
        @Data
        public static class PointBean implements Serializable {
            /**
             * y : 4284074.36
             * x : 11557683.25
             */

            private String y;
            private String x;
        }
    }
}
