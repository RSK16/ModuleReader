package com.reader.modulereader.http;


import com.reader.modulereader.exception.ApiHttpException;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/9/21 09:13   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface IBaseView {
	/**
	 * 切换视图
	 *
	 *
	 */
	void showView(int viewState);
	/**
	 * 切换视图
	 *
	 */
	void showView(int viewState, ApiHttpException e);
}
