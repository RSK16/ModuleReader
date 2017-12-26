package com.reader.modulereader.entity;

import java.util.List;

/**
 * Created by 赵康 on 2017/12/24.
 */

public class Course {


    /**
     * re_tsg : true
     * course : [{"allDay":0,"color":"0","end":"2017-12-20 10:30:00 11:40:00","id":60,"start":"2017-12-20 08:00:00 03:30:00","title":"历史>"},{"allDay":0,"color":"0","end":"2017-12-24 10:00:00","id":79,"start":"2017-12-24 09>:00:00","title":"数学>"},{"allDay":0,"color":"0","end":"2017-12-25 10:00:00","id":82,"start":"2017-12-25 09:00:00","title":"数学"},{"allDay":0,"color":"0","end":"2017-12-25 11:00:00","id":83,"start":"2017-12-25 10:00:00","title":"语文"},{"allDay":0,"color":"0","end":"2017-12-25 12:00:00","id":84,"start":"2017-12-25 11:00:00","title":"英语"},{"allDay":0,"color":"0","end":"2017-12-25 15:00:00","id":85,"start":"2017-12-25 14:00:00","title":"音乐"},{"allDay":0,"color":"0","end":"2017-12-25 16:00:00","id":86,"start":"2017-12-25 15:00:00","title":"科学"},{"allDay":0,"color":"0","end":"2017-12-25 17:00:00","id":87,"start":"2017-12-25 16:00:00","title":"自然"},{"allDay":0,"color":"0","end":"2017-12-24 17:00:00","id":89,"start":"2017-12-24 17>:00:00","title":"语文阅读>"},{"allDay":0,"color":"0","end":"2017-12-25 11:00:00","id":94,"start":"2017-12-25 10:30:00","title":"语文阅读"},{"allDay":0,"color":"0","end":"2017-12-25 12:00:00","id":95,"start":"2017-12-25 11:30:00","title":"英语阅读"},{"allDay":0,"color":"0","end":"2017-12-25 10:00:00","id":96,"start":"2017-12-25 09:30:00","title":"数学练习册"}]
     * basecourse : [{"EPCID":"E28011606000020AA9CC0123","COURSE":"数学"},{"EPCID":"E28011606000020AA9CBF02E","COURSE":"语文"},{"EPCID":"E28011606000020AA9CBF02F","COURSE":"英语"},{"EPCID":"E28011606000020AA9CBF02C","COURSE":"音乐"},{"EPCID":"E28011606000020AA9CC0125","COURSE":"科学"},{"EPCID":"E28011606000020AA9CBF02A","COURSE":"自然"},{"EPCID":"E28011606000020AA9CBF02B","COURSE":"语文阅读"},{"EPCID":"E28011606000020AA9CC0120","COURSE":"英语阅读"},{"EPCID":"E28011606000020AA9CBF02D","COURSE":"数学练习册"},{"EPCID":"E28011606000020AA9CC0124","COURSE":"品德与社会"}]
     */

    public String re_tsg;
    public List<CourseBean> course;
    public List<BasecourseBean> basecourse;

    public static class CourseBean {

        /**
         * allDay : 0
         * color : 0
         * end : 2017-12-20 10:30:00 11:40:00
         * id : 60
         * start : 2017-12-20 08:00:00 03:30:00
         * title : 历史>
         */

        public int allDay;
        public String color;
        public String end;
        public int id;
        public String start;
        public String title;
        public int readed;

        public CourseBean(int readed, String title) {

            readed = readed;
            title = title;
        }
    }

    public static class BasecourseBean {
        /**
         * EPCID : E28011606000020AA9CC0123
         * COURSE : 数学
         */
        public boolean current_read;
        public String EPCID;
        public String COURSE;
        public int readed;//0 不显示 1 红色 2 绿色 3 橙色
    }
}
