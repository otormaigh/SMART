package com.team3.smartapp;

public enum JobOccupationEnum {
	COMMUNITY_MIDWIFE("Community Midwife");
	
	private String jobOccupation;
	
	private JobOccupationEnum(String jobOccupation){
		this.jobOccupation = jobOccupation;
	}	
	public String toString(){
		return jobOccupation;
	}
}