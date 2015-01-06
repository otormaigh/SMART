package com.team3.smartapp;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ServiceProvider {
	private int id;
	private Date date;
	private String time;
	//private Date dateNew;
	//private Time time;
	private int serviceProviderId;
	private int serviceUserId;
	private String priority;		//Enum Priority.java
	private String visitType;		//Enum VisitType.java
	private int serviceOptionId;
	
	public ServiceProvider(){
	}
	
	public ServiceProvider(int id, Date date, String time, int serviceProviderId,
						   int serviceUserId, String priority, String visitType, int serviceOptionId) {
		this.id = id;
		this.date = date;
		this.time = time;
		this.serviceProviderId = serviceProviderId;
		this.serviceUserId = serviceUserId;
		this.priority = priority;
		this.visitType = visitType;
		this.serviceOptionId = serviceOptionId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(String theDate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dateInString = theDate;	 
		try {	 
			Date date = formatter.parse(dateInString);
			System.out.println(date);
			System.out.println(formatter.format(date));
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(int serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
	public int getServiceUserId() {
		return serviceUserId;
	}
	public void setServiceUserId(int serviceUserId) {
		this.serviceUserId = serviceUserId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public int getServiceOptionId() {
		return serviceOptionId;
	}
	public void setServiceOptionId(int serviceOptionId) {
		this.serviceOptionId = serviceOptionId;
	}
	public String toString(){
		return "ID: " + id + "\n" +
			   "Date: " + date + "\n" +
			   "Time: " + time + "\n" +
			   "Provider ID: " + serviceProviderId + "\n" +
			   "User ID: " + serviceUserId + "\n" +
			   "Priority: " + priority + "\n" +
			   "Visit Type: " + visitType + "\n" +
			   "Service Option ID: " + serviceOptionId + "\n";
	}
}