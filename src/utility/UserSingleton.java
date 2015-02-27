package utility;

public class UserSingleton {
	private static UserSingleton user;
	private String username;
	private String password;
	private String token;
	private int id;
	private boolean isLoggedIn;
	
	private UserSingleton(){
	}
	
	public static UserSingleton getUserSingleton() {
		if(user == null) {
			user = new UserSingleton();
		}
		return user;
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
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
}