package com.team3.smartapp.model;

public enum PriorityEnum_model {
	OTHER("other");
	
	private String priority;
	
	private PriorityEnum_model(String priority){
		this.priority = priority;
	}
	public String toString(){
		return this.priority;
	}
}