package ie.teamchile.smartapp.models;

public class Pregnancy_model {
	private int id;
	private int service_user_id;
	private String estimated_delivery_date;
	private String additional_info;
	private String birth_mode;
	private String perineum;
	private String anti_d;
	private String feeding;
	private String last_menstrual_period;
	private String gestation;

	public Pregnancy_model() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEstimated_delivery_date() {
		return estimated_delivery_date;
	}

	public void setEstimated_delivery_date1(String estimated_delivery_date) {
		this.estimated_delivery_date = estimated_delivery_date;
	}

	public int getService_user_id() {
		return service_user_id;
	}

	public void setService_user_id(int service_user_id) {
		this.service_user_id = service_user_id;
	}

	public String getAdditional_info() {
		return additional_info;
	}

	public void setAdditional_info(String additional_info) {
		this.additional_info = additional_info;
	}

	public String getBirth_mode() {
		return birth_mode;
	}

	public void setBirth_mode(String birth_mode) {
		this.birth_mode = birth_mode;
	}

	public String getPerineum() {
		return perineum;
	}

	public void setPerineum(String perineum) {
		this.perineum = perineum;
	}

	public String getAnti_d() {
		return anti_d;
	}

	public void setAnti_d(String anti_d) {
		this.anti_d = anti_d;
	}

	public String getFeeding() {
		return feeding;
	}

	public void setFeeding(String feeding) {
		this.feeding = feeding;
	}

	public String getLast_menstrual_period() {
		return last_menstrual_period;
	}

	public void setLast_menstrual_period(String last_menstrual_period) {
		this.last_menstrual_period = last_menstrual_period;
	}

	public String getGestation() {
		return gestation;
	}

	public void setGestation(String gestation) {
		this.gestation = gestation;
	}

	public void setEstimated_delivery_date(String estimated_delivery_date) {
		this.estimated_delivery_date = estimated_delivery_date;
	}
}