package com.drem.dremboard.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtils {

	private static final int SECOND = 1;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;
	private static final int WEEK = 7 * DAY;
	private static final int MONTH = 30 * DAY;

	private static final String checkin_time_minus = "Just now";
	private static final String checkin_time_second_ago = "%1$d second ago";
	private static final String checkin_time_seconds_ago = "%1$d seconds ago";
	private static final String checkin_time_minute_ago = "%1$d minute ago";
	private static final String checkin_time_minutes_ago = "%%1$d minutes ago";
	private static final String checkin_time_hour_ago = "%1$d hour ago";
	private static final String checkin_time_hours_ago = "%%1$d hours ago";
	private static final String checkin_time_day_ago = "1$d day ago";
	private static final String checkin_time_days_ago = "%1$d days ago";
	private static final String checkin_time_week_ago = "%1$d week ago";
	private static final String checkin_time_weeks_ago = "%1$d weeks ago";
	private static final String checkin_time_month_ago = "%1$d month ago";
	private static final String checkin_time_months_ago = "%1$d months ago";
	
	/*
	 * Time Format for saving
	 */
	public static String DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";


	public static String dateStringToOtherDateString(String value, String inFormat, String outFormat) {
		SimpleDateFormat format = new SimpleDateFormat(inFormat);
		SimpleDateFormat toformat = new SimpleDateFormat(outFormat);
		Date date;
		String returnValue = null;
		try {  
			date = format.parse(value);
			returnValue = toformat.format(date);
		} catch (Exception e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();
		}
		return returnValue;
	}

	/*
	 * Date -> yyyyMMdd
	 * Date -> MMdd
	 * Date -> yyyy/MM/dd
	 * Date -> dd/MM/yyyy
	 */
	public static String dateToString(Date date, String strformat) {
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		return format.format(date);
	}

	/*
	 * yyyyMMdd -> Date 
	 * MMdd -> Date 
	 * yyyy/MM/dd -> Date 
	 * dd/MM/yyyy -> Date 
	 */
	public static Date stringToDate(String strDate, String strformat) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	@SuppressWarnings("deprecation")
	public static int getAgeFromDateString(String strDate, String strformat) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		try {
			date = format.parse(strDate);
			Calendar cal = Calendar.getInstance();
			Date cur_date = cal.getTime();

			int age = cur_date.getYear() - date.getYear(); // + 1;
			if (age < 1)
				age = 1;
			return age;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;
	}

	public static String convertUTC0TimeToLocalTime(String datetime, String date_string_format) {
		String formattedTime = null;
		DateFormat df = new SimpleDateFormat(date_string_format, Locale.getDefault());
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());

		try {
			Date date = df.parse(datetime);
			formattedTime = dateToString(date, DATE_STRING_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
			formattedTime = "";
		}
		return formattedTime;
	}

	/*
	 *  
	 * "yyyy-MM-dd'T'HH:mm:ss"  ->  "3 hours ago"
	 * 
	 */
	public static String convertDateStringToString(String datetime, String date_string_format) {
		String formattedTime = null;
		DateFormat df = new SimpleDateFormat(date_string_format, Locale.getDefault());
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());

		try {
			Date date = df.parse(datetime);
			formattedTime = convertDateTimeToString(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formattedTime;
	}

	/*
	 *  
	 * Date  ->  "3 hours ago"
	 * 
	 */
	@SuppressLint("DefaultLocale")
	public static String convertDateTimeToString(Date date) {
		String formattedTime = null;
		try {
			long nowTime = new Date().getTime();
			long dateTime = date.getTime();
			if (nowTime < dateTime)
				return checkin_time_minus;
				
			long timeInterval = ((new Date()).getTime() - date.getTime() )/ 1000;

			if (timeInterval < 1*MINUTE) {
				formattedTime = String.format(timeInterval <= 1 ? checkin_time_second_ago : checkin_time_seconds_ago, timeInterval);
			} else if (timeInterval < 1*HOUR) {
				int minutes = (int)Math.floor((double)timeInterval/MINUTE);
				formattedTime = String.format(minutes <= 1 ? checkin_time_minute_ago : checkin_time_minutes_ago, minutes);
			} else if (timeInterval < 1*DAY) {
				int hours = (int) Math.floor((double)timeInterval/HOUR);
				formattedTime = String.format(hours <= 1 ? checkin_time_hour_ago : checkin_time_hours_ago, hours);
			} else if (timeInterval < 1*WEEK) {
				int days = (int) Math.floor((double)timeInterval/DAY);
				formattedTime = String.format(days <= 1 ? checkin_time_day_ago : checkin_time_days_ago, days);
			} else if (timeInterval < 1*MONTH) {
				int weeks = (int) Math.floor((double)timeInterval/WEEK);
				formattedTime = String.format(weeks <= 1 ? checkin_time_week_ago : checkin_time_weeks_ago, weeks);
			} else if (timeInterval < 3*MONTH) {
				int months = (int) Math.floor((double)timeInterval/MONTH);
				formattedTime = String.format(months <= 1 ? checkin_time_month_ago : checkin_time_months_ago, months);
			} else {
				formattedTime = dateToString(date, DATE_STRING_FORMAT);
			}
			
		} catch ( Exception e) {
			e.printStackTrace();
			formattedTime = checkin_time_minus;
		}
		return formattedTime;
	}
	
	public static Date dateTrim(Date date) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
	}
	
	public static Date dateEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);

        return calendar.getTime();
	}
	
	public static Date getDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, date);

        return calendar.getTime();
	}
}
