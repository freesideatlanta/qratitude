package org.freesideatlanta.qratitude;

public interface AuthenticationListener {
	void onAuthenticationResult(String token) throws IllegalStateException;
	void onAuthenticationCancel() throws IllegalStateException;
}
