package com.reader.modulereader.http;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 创建者：    金国栋     <br/><br/>
 * 创建时间:   2016/12/16 10:56   <br/><br/>
 * 描述:	      基于Rx的Presenter封装,控制订阅的生命周期
 */
public abstract class RxPresenter<T extends IBaseView> implements IPresenter {

	protected CompositeSubscription mCompositeSubscription;

	public RxPresenter(T view) {
		this.view = view;
	}

	public T view;

	protected void unSubscribe() {
		if (mCompositeSubscription != null) {
			mCompositeSubscription.unsubscribe();
		}
	}

	public void addSubscribe(Subscription subscription) {

		if (mCompositeSubscription == null) {
			mCompositeSubscription = new CompositeSubscription();
		}

		if (subscription == null)
			return;

		mCompositeSubscription.add(subscription);
	}

	public void removeSubscribe(Subscription subscription) {
		if (mCompositeSubscription != null && subscription != null) {
			mCompositeSubscription.remove(subscription);
		}
	}

	@Override
	public void detachView() {
		this.view = null;
		unSubscribe();
	}
}
