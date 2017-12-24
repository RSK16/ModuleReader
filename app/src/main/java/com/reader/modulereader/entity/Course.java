package com.reader.modulereader.entity;

import java.util.List;

/**
 * Created by 赵康 on 2017/12/24.
 */

public class Course {


    /**
     * re_tsg : true
     * course : [{"allDay":0,"color":"0","end":"2017-12-20 10:30:00 11:40:00","id":60,"start":"2017-12-20 08:00:00 03:30:00","title":"历史>"},{"allDay":0,"color":"0","end":"2017-12-24 10:00:00","id":79,"start":"2017-12-24 09>:00:00","title":"数学>"},{"allDay":0,"color":"0","end":"2017-12-24 10:00:00","id":82,"start":"2017-12-24 09:00:00","title":"数学"},{"allDay":0,"color":"0","end":"2017-12-24 11:00:00","id":83,"start":"2017-12-24 10:00:00","title":"语文"},{"allDay":0,"color":"0","end":"2017-12-24 12:00:00","id":84,"start":"2017-12-24 11:00:00","title":"英语"},{"allDay":0,"color":"0","end":"2017-12-24 15:00:00","id":85,"start":"2017-12-24 14:00:00","title":"音乐"},{"allDay":0,"color":"0","end":"2017-12-24 16:00:00","id":86,"start":"2017-12-24 15:00:00","title":"科学"},{"allDay":0,"color":"0","end":"2017-12-24 17:00:00","id":87,"start":"2017-12-24 16:00:00","title":"自然"},{"allDay":0,"color":"0","end":"2017-12-24 17:00:00","id":89,"start":"2017-12-24 17>:00:00","title":"语文阅读>"}]
     */

    public String re_tsg;
    public List<CourseBean> course;

    public static class CourseBean {
        /**
         * allDay : 0
         * color : 0
         * end : 2017-12-20 10:30:00 11:40:00
         * id : 60
         * start : 2017-12-20 08:00:00 03:30:00
         * title : 历史>
         */
        public boolean readed;
        public int allDay;
        public String color;
        public String end;
        public String id;
        public String start;
        public String title;

      
    }
}
