package com.drem.dremboard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.TypedValue;

public class Utility {

	public static Uri createSaveCropFile(){
		Uri uri;
		String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		return uri;
	}

	public static File getImageFile(Context context, Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		if (uri == null) {
			uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}

		Cursor mCursor = context.getContentResolver().query(uri, projection, null, null, 
				MediaStore.Images.Media.DATE_MODIFIED + " desc");
		if(mCursor == null || mCursor.getCount() < 1) {
			return null; // no cursor or no record
		}
		int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		mCursor.moveToFirst();

		String path = mCursor.getString(column_index);

		if (mCursor !=null ) {
			mCursor.close();
			mCursor = null;
		}

		return new File(path);
	}

	public static boolean copyFile(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = copyToFile(in, destFile);
			} finally  {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	public static boolean checkString (String str)
	{
		if (str == null || str.isEmpty())
			return false;

		return true;
	}

	/**
	 * Copy data from a source stream to destFile.
	 * Return true if succeed, return false if failed.
	 */
	private static boolean copyToFile(InputStream inputStream, File destFile) {
		try {
			OutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static long getValueFromTimestamp(String timestamp)
	{
		long ret = 0;
		String tmp = new String(timestamp);
		tmp = tmp.replace("T", " ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		java.util.Date date = null;
		try {
			date = sdf.parse(timestamp);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return 0;
		}

		ret = date.getTime();

		return ret;
	}

	public static String getDateStringFromTimestamp(String timestamp)
	{
		String ret = "";
		String tmp = new String(timestamp);
		tmp = tmp.replace("T", " ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		java.util.Date date = null;
		try {
			date = sdf.parse(timestamp);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return ret;
		}

		SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM yyyy");

		ret = outFormat.format(date);

		return ret;
	}

	public static String getRelativeDateStrFromTime (String inputStr)
	{
		String outStr = "";

		if (inputStr == null)
			return outStr;
		
		String dates = inputStr;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date date;
		try {
			date = simpleDateFormat.parse(dates);

			CharSequence dateString = DateUtils.getRelativeTimeSpanString(date.getTime(), new Date().getTime(), DateUtils.SECOND_IN_MILLIS);
			
			outStr = dateString.toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outStr;
	}

	public static boolean isEmailValid (String email)
	{
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Method for return file path of Gallery image 
	 * 
	 * @param context
	 * @param uri
	 * @return path of the selected image file from gallery
	 */
	public static String getPath(final Context context, final Uri uri) 
	{

		//check here to KITKAT or new version
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}	

	public static int dip2pix(int sizeInDip, Context context){
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDip, context.getResources().getDisplayMetrics());
		return padding;
	}
	
	public static String getFriendshipStr (String status)
	{
		String retStr = "";
		
		if (status == null)
			return  retStr;
		
		if (status.equals("pending")) {
			retStr = "Cancel\nFriend Request";
		} else if (status.equals("not_friends")) {
			retStr = "Add Friend";
		} else if (status.equals("awaiting_response")) {
			retStr = "Friendship Requested";
		} else if (status.equals("is_friend")) {
			retStr = "Cancel Friendship";
		} else {
			retStr = "";
		}
		
		return retStr;
	}

	public static String getFriendshipAction (String status)
	{
		String retStr = "";
		
		if (status == null)
			return  retStr;
		
		if (status.equals("pending")) {
			retStr = "cancel";
		} else if (status.equals("not_friends")) {
			retStr = "add-friend";
		} else if (status.equals("is_friend")) {
			retStr = "remove-friend";
		} else {
			retStr = "";
		}
		
		return retStr;
	}	
	
	public static String getFamilyshipStr (String status)
	{
		String retStr = "";
		
		if (status == null)
			return  retStr;
		
		if (status.equals("pending")) {
			retStr = "Cancel\nFamily Request";
		} else if (status.equals("not_familys")) {
			retStr = "Add Family";
		} else if (status.equals("awaiting_response")) {
			retStr = "Familyship Requested";
		} else if (status.equals("is_family")) {
			retStr = "Cancel Familyship";
		} else {
			retStr = "";
		}
		
		return retStr;
	}

	public static String getFamilyshipAction (String status)
	{
		String retStr = "";
		
		if (status == null)
			return  retStr;
		
		if (status.equals("pending")) {
			retStr = "cancel";
		} else if (status.equals("not_familys")) {
			retStr = "add-family";
		} else if (status.equals("is_family")) {
			retStr = "remove-family";
		} else {
			retStr = "";
		}
		
		return retStr;
	}
}
