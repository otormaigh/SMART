package ie.teamchile.smartapp.models;

public class ServiceProvider_model {
	private int id;
	private String name;
	private String username;
	private String password;
	private Boolean active;
	private Boolean admin;
	private String jobOccupation; // JobOccupationEnum.java
	private String jobLevel; // JobLevelEnum.java
	private int primaryPhone;
	private int secondaryPhone;

	public ServiceProvider_model() {
	}

	public ServiceProvider_model(int id, String name, String username,
			String password, Boolean active, Boolean admin,
			String jobOccupation, String jobLevel, int primaryPhone,
			int secondaryPhone) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.active = active;
		this.admin = admin;
		this.jobOccupation = jobOccupation;
		this.jobLevel = jobLevel;
		this.primaryPhone = primaryPhone;
		this.secondaryPhone = secondaryPhone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getJobOccupation() {
		return jobOccupation;
	}

	public void setJobOccupation(String jobOccupation) {
		this.jobOccupation = jobOccupation;
	}

	public String getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}

	public int getPrimaryPhone() {
		return primaryPhone;
	}

	public void setPrimaryPhone(int primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	public int getSecondaryPhone() {
		return secondaryPhone;
	}

	public void setSecondaryPhone(int secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}
}