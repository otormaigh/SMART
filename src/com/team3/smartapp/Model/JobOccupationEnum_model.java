package com.team3.smartapp.Model;

public enum JobOccupationEnum_model {
	COMMUNITY_MIDWIFE("Community Midwife");
	
	private String jobOccupation;
	
	private JobOccupationEnum_model(String jobOccupation){
		this.jobOccupation = jobOccupation;
	}	
	public String toString(){
		return jobOccupation;
	}
}