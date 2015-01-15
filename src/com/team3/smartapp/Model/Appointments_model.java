package com.team3.smartapp.Model;

public class Appointments_model {
	private String id;
	private String date;
	private String time;
	private String serviceProvderId;
	private String serviceUserId;
	private boolean priority;
	private String vistType;
	private String serviceOptionId;
	

	public Appointments_model (){	
	}
	public String getId(){ 
		return id;
	}	
	public String getDate(){
		return date;
	}	
	public String getTime(){
		return time;
	}	
	public String getServiceProvderrId(){
		return serviceProvderId;
	}	
	public String getServiceUserId(){
		return serviceUserId;
	}	
	public Boolean getPriority(){
		return priority;
	}
	public String getVistType(){
		return vistType;
	}
	public String getServiceOptionId(){
		return serviceOptionId;
	}	
	public void setId( String newId){
		this.id = newId;
	}	
	public void setDate( String newDate){
		this.date = newDate;
	}	
	public void settime( String newTime){
		this.time = newTime;
	}	
	public void setserviceProvderId( String newServiceProvderId){
		this.serviceProvderId = newServiceProvderId;
	}
	public void setServiceUserId( String serviceUserId){
		this.serviceUserId = serviceUserId;
	}	
	public void setPriority( boolean Priority){
		this.priority = Priority;
	}
	public void setVistType( String VistType){
		this.vistType = VistType;
	}	
	public void setServiceOptionId (String ServiceOptionId){
		this.serviceOptionId = ServiceOptionId;
	}
}