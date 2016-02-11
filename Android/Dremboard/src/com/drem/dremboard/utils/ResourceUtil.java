package com.drem.dremboard.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


public class ResourceUtil {
	/*
	 * Delete File
	 */
	public static boolean DeleteFile(File f) {
		if (f != null && f.exists() && !f.isDirectory()) {
			return f.delete();
		}
		return false;
	}

	public static boolean DeleteFile(String f) {
		if (!TextUtils.isEmpty(f)) {
			return DeleteFile(new File(f));
		}
		return false;
	}

	/*
	 * File
	 */
	public static String getCameraFilePath(Context context) {
		return getResourcePath("camera.jpg");
	}
	
	public static String getVideoFilePath(Context context) {
		return getResourcePath("video.mp4");
	}

	public static String getAvatarFilePath(Context context) {
		return getResourcePath("avatar.jpg");
	}
	
	public static String getCompetitorAvatarFilePath(Context context) {
		return getResourcePath("competitor_avatar.jpg");
	}
	
	public static String getEventImageFilePath(Context context) {
		return getResourcePath("event.jpg");
	}
	
	public static String getNewImageFilePath(Context context) {
		return getResourcePath(DateTimeUtils.dateToString(new Date(), "yyyyMMddhhmmss")+".jpg");
	}
	
	private static String getResourcePath(String fileName) {
		String tempDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dremboard/";

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + fileName);
		if (!tempFile.exists())
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tempDirPath + fileName;
	}
}
