package com.reader.modulereader.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/10/9 17:25   <br/><br/>
 * 描述:	      ${TODO}
 */
public class DateUtil {

	/**
	 * 一天时间
	 */
	public static long toDayTime = 1000 * 60 * 60 * 24;

	public static String formatDate(long date, String format) {
		return new SimpleDateFormat(format).format(new Date(date));
	}

	public static String defDateFormat(long date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String defDateFormatHM(long date) {
		return formatDate(date, "yyyy-MM-dd HH:mm");
	}

	public static long getDateFormatHM2Long(long date) {
		return parseDateString2LongHM(defDateFormatHM(date));
	}

	public static long parseDateString2LongHM(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String MMDD_DateFormat(long date) {
		return new SimpleDateFormat("MM.dd").format(new Date(date));
	}

	public static String OsDateFormat(long date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(date));
	}

	public static long OsDateFormatLong(long date) {
		return Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new Date(date)));
	}

	public static String getCurDay() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
	}

	public static String dateToString(){
		Calendar cl = Calendar.getInstance();
		cl.setTime(new Date(System.currentTimeMillis()));
		int i = cl.get(Calendar.DAY_OF_WEEK);
		String week_of_day = "未知";
		switch (i) {
			case Calendar.SATURDAY:
				week_of_day = "星期六";
				break;
			case Calendar.SUNDAY:
				week_of_day = "星期日";
				break;
			case Calendar.MONDAY:
				week_of_day = "星期一";
				break;
			case Calendar.TUESDAY:
				week_of_day = "星期二";
				break;
			case Calendar.WEDNESDAY:
				week_of_day = "星期三";
				break;
			case Calendar.THURSDAY:
				week_of_day = "星期四";
				break;
			case Calendar.FRIDAY:
				week_of_day = "星期五";
				break;
			default:
				break;
		}
		return week_of_day;
	}

	public static long getToDayTime() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(getCurDay()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}


	public static long plusTime(int day) {
		if (day < 0) {
			return System.currentTimeMillis() - Math.abs(day) * toDayTime;
		} else {
			return System.currentTimeMillis() + day * toDayTime;
		}
	}

	public static long plusTime(long date, int day) {
		if (day < 0) {
			return date - Math.abs(day) * toDayTime;
		} else {
			return date + day * toDayTime;
		}
	}

	/**
	 * 获取当前时间
	 */
	public static String getCurDate() {
		return defDateFormat(System.currentTimeMillis());
	}

	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
	}
}
