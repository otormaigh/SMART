package models;

public class Roster {
	private String serviceProviderId;
	private String date;
	private String clinicId;

	public Roster(String serviceProviderId, String date, String clinicId) {
		this.serviceProviderId = serviceProviderId;
		this.date = date;
		this.clinicId = clinicId;
	}

}
