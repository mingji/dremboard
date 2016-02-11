package com.drem.dremboard;

public class Const {

	// For Intent
	public	static	final	int		REQUESTCODE_LOGIN_GOOGLE	= 1;
	public	static	final	int		REQUESTCODE_LOGIN_FACEBOOK	= 2;
	public	static	final	int		REQUESTCODE_LOGIN_TWITTER	= 3;
	
	// Twitter
	public	static	final	String	TWITTER_CONSUMER_KEY		=		"IjCj8wIfK6gkqFiWelwRy8HV9";
	public	static	final	String	TWITTER_CONSUMER_SECRET		=		"Xb8pJTx6td8sTi5uaufZzUVAoxAm7c5Mq0Fiz9FOwco5P6Crxz";
	public	static	final	String	TWITTER_CALLBACK_URL		=		"oauth://twitter_callback";

	public	static	final	String	KEY_TWITTER_CALLBACK_URL		= "URL_CALLBACK";
	public	static	final	String	KEY_TWITTER_AUTHENTICATION_URL	= "URL_AUTHENTICATION";
	
	// Login to Main Intent
	public	static	final	String	KEY_LOGINTYPE	= "TYPELOGIN";
	public	static	final	String	KEY_USERID		= "USERID";
	public	static	final	String	KEY_USERNAME	= "USERNAME";
	public	static	final	String	KEY_TOKEN		= "TOKEN";
	public	static	final	String	KEY_AVATAR		= "AVATAR";

	// Login Type
	public	static	final	int		TYPE_LOGIN_NORMAL		= 0;
	public	static	final	int		TYPE_LOGIN_FACEBOOK		= 1;
	public	static	final	int		TYPE_LOGIN_TWITTER		= 2;
	public	static	final	int		TYPE_LOGIN_GOOGLE		= 3;
	
	/*
	 * Request code
	 */
	public static final int REQ_IMAGE_FROM_CAMERA = 10001;
	public static final int REQ_IMAGE_FROM_CAMERA_CROP = 10002;
	public static final int REQ_IMAGE_FROM_GALLERY = 10003;
	public static final int REQ_IMAGE_FROM_GALLERY_CROP = 10004;
	public static final int REQ_VIDEO_FROM_CAMERA = 10005;
	public static final int REQ_VIDEO_FROM_GALLERY = 10006;
	public static final int REQ_MAP = 10007;

}
