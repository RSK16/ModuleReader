package com.reader.modulereader.entity;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/10/20 14:04   <br/><br/>
 * 描述:	      ${TODO}
 */
public class Response<T> {

	public static final int SUCCESS = 0;

	/**
	 * token失效错误码
	 */
	public static final int TOKEN_ERROR = 3;

	public int ret_code;
	public String ret_msg;
	public String re_tsg;
	public String message;
	public T data;

	public boolean isSuccess() {
		return ret_code == SUCCESS;
	}
}
