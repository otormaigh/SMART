package com.team3.smartapp.Model;
	
public class Clinics_model {
	
	private int id;
	private String name;
	private String address;
	private String openingHours;
	private String closingHours;
	private String recurrence;
	private String type;
	private int appointmentIntervals;
	private String days;

	public Clinics_model() {
	}
	public int getId() {
		return id;
	}
	public void setId(int newId) {
		id = newId;
	}
	public String getName() {
		return name;
	}
	public void setName(String newName) {
		name = newName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String newAddress) {
		address = newAddress;
	}
	public String getOpeningHours() {
		return openingHours;
	}
	public void setOpeningHours(String newOpeningHours) {
		openingHours = newOpeningHours;
	}
	public String getClosingHours() {
		return closingHours;
	}
	public void setClosingHours(String newClosingHours) {
		closingHours = newClosingHours;
	}
	public String getRecurrence() {
		return recurrence;
	}
	public void setRecurrence(String newRecurrence) {
		recurrence = newRecurrence;
	}
	public String getType() {
		return type;
	}
	public void setType(String newType) {
		type = newType;
	}
	public int getAppointmentIntervals() {
		return appointmentIntervals;
	}
	public void setAppointmentIntervals(int newAppointmentIntervals) {
		appointmentIntervals = newAppointmentIntervals;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String newDays) {
		days = newDays;
	}		
}