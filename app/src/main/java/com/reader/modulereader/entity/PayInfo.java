package com.reader.modulereader.entity;


import java.util.List;

/**
 * 作者：zhaokang on 2017/6/23 16:26
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：支付信息
 */

public class PayInfo {


    /**
     * re_tsg : true
     * notices : [{"createTime":"2017-12-23","datakey":"学习","description":"一样一样","id":"da00931f06d14f77aafa0778875d8f73","isValid":"1","name":"明天带"},{"createTime":"2017-12-23","datakey":"学习","description":"啊啊啊啊","id":"f4eaea48d6ea45809201f5f1357badd8","isValid":"1","name":"在哪里"},{"createTime":"2017-12-19","datakey":"顶顶顶顶","description":"sss","id":"d0298455ecb8434084176b205dd65426","isValid":"1","name":"大叔大婶大  "}]
     */

    private String re_tsg;
    private List<NoticesBean> notices;


    public static class NoticesBean {
        /**
         * createTime : 2017-12-23
         * datakey : 学习
         * description : 一样一样
         * id : da00931f06d14f77aafa0778875d8f73
         * isValid : 1
         * name : 明天带
         */

        private String createTime;
        private String datakey;
        private String description;
        private String id;
        private String isValid;
        private String name;
    }
}
