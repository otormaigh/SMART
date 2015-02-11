package models;

public class Login_model {
	private static String username;
	private static String password;
	private static String token;
	private static int userId;

	public Login_model() {
	}
	public static String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		Login_model.username = username;
	}
	public static String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		Login_model.password = password;
	}
	public static String getToken() {
		return token;
	}
	public void setToken(String token) {
		Login_model.token = token;
	}
	public static int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		Login_model.userId = userId;
	}
}