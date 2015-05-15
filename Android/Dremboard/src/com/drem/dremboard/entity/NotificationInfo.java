package com.drem.dremboard.entity;


public class NotificationInfo {

	public int id;
	public String desc;
	public String since;
	public String type;

	public NotificationInfo () {
		super();
		
		this.id = -1; // -1 : invalid user id
	}
}
