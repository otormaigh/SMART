package models;

public enum JobLevelEnum_model {
	RESIDENT("Resident");

	private String jobLevel;

	private JobLevelEnum_model(String jobLevel) {
		this.jobLevel = jobLevel;
	}

	public String toString() {
		return jobLevel;
	}
}