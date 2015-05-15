package com.drem.dremboard.entity;


public class School {

	public Place googleInfo;

	public School(Place place) {
		super();
		
		this.googleInfo = new Place();
		googleInfo = place;
	}

}
