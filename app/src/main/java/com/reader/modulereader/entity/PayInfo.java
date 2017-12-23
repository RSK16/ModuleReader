package com.reader.modulereader.entity;


import java.util.List;

/**
 * 作者：zhaokang on 2017/6/23 16:26
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：支付信息
 */

public class PayInfo {


    /**
     * data : {"pay_infos":[{"shop_id":0,"img_url":"","pay_name":"支付宝","id":15,"sort":null},{"shop_id":0,"img_url":"","pay_name":"微信","id":16,"sort":null},{"shop_id":0,"img_url":"","pay_name":"现金","id":19,"sort":null},{"shop_id":0,"img_url":"","pay_name":"其它","id":20,"sort":null}]}
     * ret_msg : SUCCESS
     * ret_code : 0
     */

    public DataBean data;
    public String ret_msg;
    public int ret_code;
    public static class DataBean {
        public List<PayInfosBean> pay_infos;
        public static class PayInfosBean {
            /**
             * shop_id : 0
             * img_url :
             * pay_name : 支付宝
             * id : 15
             * sort : null
             */

            public int shop_id;
            public String img_url;
            public String pay_name;
            public int id;
            public String sort;
            public boolean clickable;
        }
    }
}
