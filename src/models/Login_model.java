package models;

public class Login_model {
	private static String username;
	private static String password;
	private static String token;
	private static String db_name;
	private static String db_username;
	private static String db_password;

	public Login_model() {
	}

	public static String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public static String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static String getDb_username() {
		return db_username;
	}

	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}

	public static String getDb_password() {
		return db_password;
	}

	public void setDb_password(String db_password) {
		this.db_password = db_password;
	}
}