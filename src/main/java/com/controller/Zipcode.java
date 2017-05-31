package com.controller;


public class Zipcode {
	
	   private String zipcode; 
	   private String abr;	   
	   private String latitude;
	   private String longitude;
	   private String city;
	   private String state;
	   
	public String getZipcode() {
		
		return zipcode;
		
	}
	public void setZipcode(String zipcode) {
		
		this.zipcode = zipcode;
		
	}
	public String getAbr() {
		
		return abr;
		
	}
	public void setAbr(String abr) {
		
		this.abr = abr;
		
	}
	public String getLatitude() {
		
		return latitude;
		
	}
	public void setLatitude(String latitude) {
		
		this.latitude = latitude;
		
	}
	public String getLongitude() {
		
		return longitude;
		
	}
	public void setLongitude(String longitude) {
		
		this.longitude = longitude;
		
	}
	public String getCity() {
		
		return city;
		
	}
	public void setCity(String city) {
		
		this.city = city;
		
	}
	public String getState() {
		
		return state;
		
	}
	public void setState(String state) {
		
		this.state = state;
		
	}
	@Override
	public String toString() {
		return "Zipcode [zipcode=" + zipcode + ", abr=" + abr + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", city=" + city + ", state=" + state + "]";
	}
	   
	}
