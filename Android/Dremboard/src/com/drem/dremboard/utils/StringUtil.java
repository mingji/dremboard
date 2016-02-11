package com.drem.dremboard.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;


public class StringUtil {
	/**
	 * @param str
	 * @return returns true if string is empty or null
	 */
	public static boolean isNullOrEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * @param str
	 * @return return true if the string is just spaces or tabs or empty or null
	 */
	public static boolean isNullOrEmptyOrBlank(String str) {
		return isNullOrEmpty(str) || str.trim().length() == 0;
	}


	public static boolean same(String a, String b) {
		return (a == null && b == null) || (a != null && b != null && a.equals(b));
	}

	public static boolean sameIgnoreCase(String a, String b) {
		return (a == null && b == null) || (a != null && b != null && a.equalsIgnoreCase(b));
	}

	public static String dayOfWeekToString(int dayOfWeek) {
		String dayInString = "";
		switch(dayOfWeek) {
		case Calendar.MONDAY :
			dayInString = "MON";
			break;
		case Calendar.TUESDAY :
			dayInString = "TUE";
			break;
		case Calendar.WEDNESDAY :
			dayInString = "WED";
			break;
		case Calendar.THURSDAY :
			dayInString = "THUR";
			break;
		case Calendar.FRIDAY :
			dayInString = "FRI";
			break;
		case Calendar.SATURDAY :
			dayInString = "SAT";
			break;
		case Calendar.SUNDAY :
			dayInString = "SUN";
			break;    		
		}
		return dayInString;
	}

	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public static String getSuitableStringOfView(String orgString, TextView view, int viewWidth) {
		final int PADDING_SIZE = 20;
		if (orgString == null || orgString.length() <= 0 || view == null || viewWidth <= PADDING_SIZE+10)
			return "";

		orgString = orgString.toUpperCase();

		Paint textPaint = view.getPaint();
		int strPixelWidth = (int)textPaint.measureText(orgString);

		String newString = orgString;
		int lenOrgStr = orgString.length();
		int index = (lenOrgStr + 1) / 2;
		while (strPixelWidth > viewWidth - PADDING_SIZE) {
			String frontString = orgString.substring(0, index-1);
			String backString = orgString.substring(lenOrgStr-index, lenOrgStr-1);
			newString = frontString + "..." + backString;
			if (newString.equals("..."))
				break;

			// get pixel width of new string
			strPixelWidth = (int)textPaint.measureText(newString);

			index--;
		}

		return newString;
	}

	public static String removeAllSpace(String str) {
		if (str == null)
			return null;
		return str.replace(" ", "");
	}

	public static String ArrayToString(ArrayList<String> str_array, String str_split) {
		String retVal = "";
		if (str_array == null || str_array.size() <= 0)
			return retVal;
		if (str_split == null)
			str_split = "";

		for (int i = 0; i < str_array.size()-1; i++)
			retVal = retVal + str_array.get(i) +  str_split;
		retVal = retVal + str_array.get(str_array.size()-1);

		return retVal;
	}

	public static ArrayList<String> StringToArray(String str, String str_split) {
		ArrayList<String> retVal = new ArrayList<String>();
		if (str == null || str_split == null)
			return retVal;

		String[] array = str.split(str_split);
		List<String> list = Arrays.asList(array);
		retVal.addAll(list);

		return retVal;
	}
	
	public static String getStringFromBase64String(String inBase64String) {
		if (TextUtils.isEmpty(inBase64String))
			return "";
		
	    byte[] msgByte = Base64.decode(inBase64String, Base64.DEFAULT);
	    String msgData = "";
	    try {
	    	msgData = new String(msgByte, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    
	    return msgData;
	}
}