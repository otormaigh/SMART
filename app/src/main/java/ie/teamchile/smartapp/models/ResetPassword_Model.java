package ie.teamchile.smartapp.models;

public class ResetPassword_Model {

	private String token;
	private String username;
	private String email;
	private String password;
	private String password_verification;

	public ResetPassword_Model() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword_verification() {
		return password_verification;
	}

	public void setPassword_verification(String password_verification) {
		this.password_verification = password_verification;
	}

}