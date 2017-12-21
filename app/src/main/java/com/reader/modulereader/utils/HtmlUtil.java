package com.reader.modulereader.utils;

import android.os.Build;
import android.text.Html;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/9/26 14:51   <br/><br/>
 * 描述:	      ${TODO}
 */
public class HtmlUtil {
	/**
	 * Html解码
	 *
	 * @param input 待解码的字符串
	 * @return Html解码后的字符串
	 */
	@SuppressWarnings("deprecation")
	public static CharSequence htmlDecode(final String input) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
		} else {
			return Html.fromHtml(input);
		}
	}
}
