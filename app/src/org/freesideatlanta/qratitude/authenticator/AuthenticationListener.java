package org.freesideatlanta.qratitude.authenticator;

public interface AuthenticationListener {
	void onAuthenticationResult(String token) throws IllegalStateException;
	void onAuthenticationCancel() throws IllegalStateException;
}
