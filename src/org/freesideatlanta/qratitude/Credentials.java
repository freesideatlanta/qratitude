package org.freesideatlanta.qratitude;

public class Credentials {
	public static final String EXTRA_USERNAME = "org.freesideatlanta.qratitude.USERNAME";
	public static final String EXTRA_PASSWORD = "org.freesideatlanta.qratitude.PASSWORD";

	private String username;
	public String getUsername() {
		return this.username;
	}

	private String password;
	public String getPassword() {
		return this.password;
	}

	public Credentials(String u, String p) {
		this.username = u;
		this.password = p;
	}
}
