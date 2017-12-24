package com.reader.modulereader.entity;

import java.util.List;

/**
 * Created by 赵康 on 2017/12/24.
 */

public class Notice {

    /**
     * re_tsg : true
     * notices : [{"createTime":"2017-12-23","datakey":"学习","description":"一样一样","id":"da00931f06d14f77aafa0778875d8f73","isValid":"1","name":"明天带"},{"createTime":"2017-12-23","datakey":"学习","description":"啊啊啊啊","id":"f4eaea48d6ea45809201f5f1357badd8","isValid":"1","name":"在哪里"},{"createTime":"2017-12-19","datakey":"顶顶顶顶","description":"sss","id":"d0298455ecb8434084176b205dd65426","isValid":"1","name":"大叔大婶大  "}]
     */

    public String re_tsg;
    public List<NoticesBean> notices;


    public static class NoticesBean {
        /**
         * createTime : 2017-12-23
         * datakey : 学习
         * description : 一样一样
         * id : da00931f06d14f77aafa0778875d8f73
         * isValid : 1
         * name : 明天带
         */

        public String createTime;
        public String datakey;
        public String description;
        public String id;
        public String isValid;
        public String name;

       
    }
}
