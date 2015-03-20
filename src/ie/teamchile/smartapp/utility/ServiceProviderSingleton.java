package ie.teamchile.smartapp.utility;

public class ServiceProviderSingleton {
	private static ServiceProviderSingleton user;
	private String username;
	private String password;
	private String token;
	private String id;
	private boolean isLoggedIn;
	
	private ServiceProviderSingleton(){
	}
	
	public static synchronized ServiceProviderSingleton getInstance() {
		if(user == null) {
			user = new ServiceProviderSingleton();
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
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
}