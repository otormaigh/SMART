package Enums;

public enum JobLevelEnum {
	RESIDENT("Resident");

	private String jobLevel;

	private JobLevelEnum(String jobLevel) {
		this.jobLevel = jobLevel;
	}

	public String toString() {
		return jobLevel;
	}
}