package com.team3.smartapp;

public enum PriorityEnum {
	OTHER("other");
	
	private String priority;
	
	private PriorityEnum(String priority){
		this.priority = priority;
	}
	public String toString(){
		return this.priority;
	}
}