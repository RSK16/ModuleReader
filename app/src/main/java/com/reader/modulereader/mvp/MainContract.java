package com.reader.modulereader.mvp;

import com.reader.modulereader.entity.Course;
import com.reader.modulereader.entity.Notice;
import com.reader.modulereader.entity.OrderDetails;
import com.reader.modulereader.entity.PayInfo;
import com.reader.modulereader.exception.ApiHttpException;
import com.reader.modulereader.http.IBaseView;
import com.reader.modulereader.http.IPresenter;

/**
 * 作者：zhaokang on 2017/12/23 14:01
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：
 */

public interface MainContract {
    interface IMainView extends IBaseView {

        void getNoticeJsonServletSuccess(Notice notice);

        void getNoticeJsonServletError(ApiHttpException e);

        void getCourseJsonServletSuccess(Course course);

        void getCourseJsonServletError(ApiHttpException e);

        void addlnglatJsonServletSuccess();

        void addlnglatJsonServletError(ApiHttpException e);

    }

    interface IMainPresenter extends IPresenter {
        void getNoticeJsonServlet();

        void getCourseJsonServlet();

        void addlnglatJsonServlet(String lng,String lat);


    }
}
