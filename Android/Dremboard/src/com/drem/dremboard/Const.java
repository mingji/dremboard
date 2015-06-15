package com.drem.dremboard;

public class Const {

	// For Intent
	public	static	final	int		REQUESTCODE_LOGIN_GOOGLE	= 1;
	public	static	final	int		REQUESTCODE_LOGIN_FACEBOOK	= 2;
	public	static	final	int		REQUESTCODE_LOGIN_TWITTER	= 3;
	
	// Twitter
	public	static	final	String	TWITTER_CONSUMER_KEY		=		"kDxijlc2lL4vdXq13EhsXHrEh";
	public	static	final	String	TWITTER_CONSUMER_SECRET		=		"1Q167iKv1F6GF9ajwMXff4lZk08ByQAYSI7mFaW1EyOZYptVum";
	public	static	final	String	TWITTER_CALLBACK_URL		=		"oauth://twitter_callback";

	public	static	final	String	KEY_TWITTER_CALLBACK_URL		= "URL_CALLBACK";
	public	static	final	String	KEY_TWITTER_AUTHENTICATION_URL	= "URL_AUTHENTICATION";
	
	// Login to Main Intent
	public	static	final	String	KEY_LOGINTYPE	= "TYPELOGIN";
	public	static	final	String	KEY_USERID		= "USERID";
	public	static	final	String	KEY_USERNAME	= "USERNAME";
	public	static	final	String	KEY_AVATAR		= "AVATAR";

	// Login Type
	public	static	final	int		TYPE_LOGIN_NORMAL		= 0;
	public	static	final	int		TYPE_LOGIN_FACEBOOK		= 1;
	public	static	final	int		TYPE_LOGIN_TWITTER		= 2;
	public	static	final	int		TYPE_LOGIN_GOOGLE		= 3;
}
