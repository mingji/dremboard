package com.drem.dremboard.entity;

import java.util.ArrayList;

public class GlobalValue {

	private static GlobalValue instance = null;
	
	private DremerInfo mDremer;
	private DremboardInfo mDremboard;
	private ArrayList<ProfileItem> mProfiles;
	private String mPwd;
	private DremInfo mDrem;
	private DremcastInfo mDremcast;

	public static synchronized GlobalValue getInstance(){

		if(null == instance){
			instance = new GlobalValue();
		}
		return instance;
	}
	
	public void setCurrentDremer(DremerInfo dremer)
	{
		if (dremer == null)
			return;
		
		this.mDremer = dremer;
	}
	
	public DremerInfo getCurrentDremer()
	{
		if (mDremer == null)
			this.mDremer = new DremerInfo();
		
		return mDremer;
	}
	
	public void setCurrentProfiles(ArrayList<ProfileItem> items)
	{
		if (items == null)
			return;
		
		if (mProfiles == null)
			mProfiles = new ArrayList<ProfileItem>();
		
		mProfiles.clear();
		
		for (ProfileItem item : items)
		{
			mProfiles.add(item);
		}
	}
	
	public ArrayList<ProfileItem> getCurrentProfiles()
	{
		if (mProfiles == null)
			this.mProfiles = new ArrayList<ProfileItem> ();
		
		return mProfiles;
	}
	
	public void setCurrentDremboard(DremboardInfo board)
	{
		if (board == null)
			return;
		
		this.mDremboard = board;
	}
	
	public DremboardInfo getCurrentDremboard()
	{
		if (mDremboard == null)
			this.mDremboard = new DremboardInfo();
		
		return mDremboard;
	}	
	
	public void setCurrentPassword(String pwd)
	{
		if (pwd == null)
			return;
		
		this.mPwd = new String(pwd);
	}
	
	public String getCurrentPassword()
	{
		if (mPwd == null)
			this.mPwd = "";
		
		return mPwd;
	}

	public void setCurrentDrem(DremInfo drem)
	{
		if (drem == null)
			return;
		
		this.mDrem = drem;
	}
	
	public DremInfo getCurrentDrem()
	{
		if (mDrem == null)
			this.mDrem = new DremInfo();
		
		return mDrem;
	}

	public void setCurrentDremcast(DremcastInfo dremcast)
	{
		if (dremcast == null)
			return;
		
		this.mDremcast = dremcast;
	}
	
	public DremcastInfo getCurrentDremcast()
	{
		if (mDremcast == null)
			this.mDremcast = new DremcastInfo();
		
		return mDremcast;
	}

}
