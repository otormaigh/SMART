package com.team3.smartapp;

public enum VisitTypeEnum {
	ANTENATAL("antenatal"),			//before baby
	POSTNATAL("postnatal");			//after baby
	
	private String visitType;
	
	private VisitTypeEnum(String visitType){
		this.visitType = visitType;
	}
	public String toString(){
		return this.visitType;
	}
}