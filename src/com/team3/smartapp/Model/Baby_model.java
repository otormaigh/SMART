package com.team3.smartapp.Model;

public class Baby_model {
	
	private int id;
	private int pregnancy_id;
	private String hospital_number;
	private String gender;
	private double weight;
	private String delivery_date_time;
	private String vitamin_k;
	private String hearing;
	private String newborn_screening_test;
	private String birth_outcome;

	public Baby_model() {		
	}	
	public int getId(){
		return id;
	}	
	public void setId(int id){
		this.id=id;
	}
	public int getPregnancy_id() {
		return pregnancy_id;
	}
	public void setPregnancy_id(int pregnancy_id) {
		this.pregnancy_id = pregnancy_id;
	}
	public String getHospital_number() {
		return hospital_number;
	}
	public void setHospital_number(String hospital_number) {
		this.hospital_number = hospital_number;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDelivery_date_time() {
		return delivery_date_time;
	}
	public void setDelivery_date_time(String delivery_date_time) {
		this.delivery_date_time = delivery_date_time;
	}
	public String getHearing() {
		return hearing;
	}
	public void setHearing(String hearing) {
		this.hearing = hearing;
	}
	public String getVitamin_k() {
		return vitamin_k;
	}
	public void setVitamin_k(String vitamin_k) {
		this.vitamin_k = vitamin_k;
	}
	public String getNewborn_screening_test() {
		return newborn_screening_test;
	}
	public void setNewborn_screening_test(String newborn_screening_test) {
		this.newborn_screening_test = newborn_screening_test;
	}
	public String getBirth_outcome() {
		return birth_outcome;
	}
	public void setBirth_outcome(String birth_outcome) {
		this.birth_outcome = birth_outcome;
	}
}
