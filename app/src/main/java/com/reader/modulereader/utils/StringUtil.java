package com.reader.modulereader.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 创建者     金国栋              <br/><br/>
 * 创建时间    2016/09/21 10:20   <br/><br/>
 * 描述	     字符串处理工具类
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 *
	 * @param str 需要判断的字符串
	 * @return boolean 为空则返回true，不为空返回false
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static byte[] getByte(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	public static byte[] getByte_GBK(String str) {
		try {
			return str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	public static String byteToString(byte[] bytes) {
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String byteToString_GBK(byte[] bytes) {
		try {
			return new String(bytes, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String reserve2(double data) {
		String s = String.valueOf(data);
		if (!s.contains(".")) {
			return s + ".00";
		}
		String[] split = s.split("\\.");
		if (split[1].length() < 2) {
			return s + "0";
		}
		return s;
	}


	/**
	 * 将String转换成MD5值
	 *
	 * @param string 需要转换的String数据
	 * @return String MD5值
	 */
	public static String String2Md5(String string) {
		String result = null;
		try {
			char[] charArray = string.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++) {
				byteArray[i] = (byte) charArray[i];
			}

			StringBuffer hexValue = new StringBuffer();
			byte[] md5Bytes = MessageDigest.getInstance("MD5")
					.digest(byteArray);
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}

			result = hexValue.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断内容是否是数字
	 *
	 * @param str
	 * @return 正确返回ture
	 */
	public static boolean isNumber(String str) {
		return str.matches("\\d+");
	}

	/**
	 * double出现科学计数法转换为显示正常
	 */
	public static String doubleParse(double value) {
		String temp = String.valueOf(value);
		if (temp.matches("\\d+\\.\\d+")) {
			//不包含A-z说明不需要转换
			return temp;
		}
		String format = String.format("%.4f", value);
		return format.replaceAll("(\\d+\\.[1-9]+)", "$1 ");
	}

	/**
	 *截取指定字节长度的字符串，不能返回半个汉字</b>
	 * 例如：
	 * 如果网页最多能显示17个汉字，那么 length 则为 34
	 * StringTool.getSubString(str, 34);
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String getSubString(String str, int length) {
		int count = 0;
		int offset = 0;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] > 256) {
				offset = 2;
				count += 2;
			} else {
				offset = 1;
				count++;
			}
			if (count == length) {
				return str.substring(0, i + 1);
			}
			if ((count == length + 1 && offset == 2)) {
				return str.substring(0, i);
			}
		}
		return "";
	}
}
