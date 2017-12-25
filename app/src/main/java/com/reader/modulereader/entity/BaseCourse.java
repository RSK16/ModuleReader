package com.reader.modulereader.entity;

import java.util.List;

/**
 * 作者：zhaokang on 2017/12/25 10:04
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：
 */

public class BaseCourse {

    /**
     * re_tsg : true
     * basecourse : [{"EPCID":"E28011606000020AA9CC0123","COURSE":"数学"},{"EPCID":"E28011606000020AA9CBF02E","COURSE":"语文"},{"EPCID":"E28011606000020AA9CBF02F","COURSE":"英语"},{"EPCID":"E28011606000020AA9CBF02C","COURSE":"音乐"},{"EPCID":"E28011606000020AA9CC0125","COURSE":"科学"},{"EPCID":"E28011606000020AA9CBF02A","COURSE":"自然"},{"EPCID":"E28011606000020AA9CBF02B","COURSE":"语文阅读"},{"EPCID":"E28011606000020AA9CC0120","COURSE":"英语阅读"},{"EPCID":"E28011606000020AA9CBF02D","COURSE":"数学练习册"},{"EPCID":"E28011606000020AA9CC0124","COURSE":"品德与社会"}]
     */

    public String re_tsg;
    public List<BasecourseBean> basecourse;

    public static class BasecourseBean {
        /**
         * EPCID : E28011606000020AA9CC0123
         * COURSE : 数学
         */

        public String EPCID;
        public String COURSE;

    }
}
