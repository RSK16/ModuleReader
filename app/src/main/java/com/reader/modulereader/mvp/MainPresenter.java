package com.reader.modulereader.mvp;

import com.reader.modulereader.entity.BaseCourse;
import com.reader.modulereader.entity.Course;
import com.reader.modulereader.entity.Notice;
import com.reader.modulereader.entity.Response;
import com.reader.modulereader.exception.ApiHttpException;
import com.reader.modulereader.http.HttpRequest;
import com.reader.modulereader.http.HttpSubscriber;
import com.reader.modulereader.http.RxPresenter;
import com.reader.modulereader.utils.RxUtil;

/**
 * 作者：zhaokang on 2017/12/23 14:01
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：
 */

public class MainPresenter extends RxPresenter<MainContract.IMainView> implements MainContract.IMainPresenter {

    public MainPresenter(MainContract.IMainView view) {
        super(view);
    }

    @Override
    public void loadDataFromServer() {

    }

    @Override
    public void getNoticeJsonServlet() {
        HttpSubscriber<Notice> subscriber = new HttpSubscriber<Notice>() {
            @Override
            protected void onError(ApiHttpException exception) {
                view.getNoticeJsonServletError(exception);
            }

            @Override
            public void onNext(Notice notice) {
                if ("true".equals(notice.re_tsg)) {
                    view.getNoticeJsonServletSuccess(notice);
                } else {
                    onError(new ApiHttpException("接口请求失败",-1));
                }
            }
        };
        HttpRequest.getInstance().getNoticeJsonServlet()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(subscriber);

        addSubscribe(subscriber);
    }

    @Override
    public void getCourseJsonServlet() {
        HttpSubscriber<Course> subscriber = new HttpSubscriber<Course>() {
            @Override
            protected void onError(ApiHttpException exception) {
                view.getCourseJsonServletError(exception);
            }

            @Override
            public void onNext(Course course) {
                if ("true".equals(course.re_tsg)) {
                    view.getCourseJsonServletSuccess(course);
                } else {
                    onError(new ApiHttpException("接口请求失败",-1));
                }
            }
        };
        HttpRequest.getInstance().getCourseJsonServlet()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(subscriber);

        addSubscribe(subscriber);
    }

    @Override
    public void addlnglatJsonServlet(String lng, String lat) {
        HttpSubscriber<Response> subscriber = new HttpSubscriber<Response>() {
            @Override
            protected void onError(ApiHttpException exception) {
                view.addlnglatJsonServletError(exception);
            }

            @Override
            public void onNext(Response response ) {

            }
        };
        HttpRequest.getInstance().addlnglatJsonServlet(lng,lat)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(subscriber);

        addSubscribe(subscriber);
    }

    @Override
    public void GetBaseCourseJsonServlet() {
        HttpSubscriber<BaseCourse> subscriber = new HttpSubscriber<BaseCourse>() {
            @Override
            protected void onError(ApiHttpException exception) {
                view.GetBaseCourseJsonServletError(exception);
            }

            @Override
            public void onNext(BaseCourse baseCourse ) {
                if ("true".equals(baseCourse.re_tsg)) {
                    view.GetBaseCourseJsonServletSuccess(baseCourse);
                } else {
                    onError(new ApiHttpException("接口请求失败",-1));
                }
            }
        };
        HttpRequest.getInstance().getBaseCourseJsonServlet()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(subscriber);

        addSubscribe(subscriber);
    }

}
