package org.freesideatlanta.qratitude.model;

import java.net.*;
import java.util.*;

import org.freesideatlanta.qratitude.data.*;

public class UserAuthenticator {

	private UserStore store;

	public UserAuthenticator() {
		this.store = StoreFactory.getUserStore();
	}

	public Token login(String username, String password) throws Exception {
		User user = this.queryUser(username);
		
		Token token = null;
		if (user != null) {
			token = this.login(user, password);
		}

		return token;
	}

	public Token login(User user, String password) throws Exception {
		boolean valid = false;
		Token token = null;

		if (user != null) {
			String hash = user.getPassword();
			valid = CryptUtil.check(password, hash);
		}

		if (valid) {
			token = new Token();
			token.generate(user);

			// NOTE: the hash of the token string is stored in the database
			String tokenText = token.getToken();
			String hash = CryptUtil.getSaltedHash(tokenText);
			user.setToken(hash);
			this.store.update(user);
		}

		return token; 
	}

	public void logout(String username) throws Exception {
		User user = this.queryUser(username);
		if (user != null) {
			user.setToken("");
			this.store.update(user);
		}
	}

	public boolean authenticate(String username, String token) throws Exception {
		User user = this.queryUser(username);

		boolean valid = false;
		if (user != null) {
			valid = this.authenticate(user, token);
		}

		return valid;
	}

	public boolean authenticate(User user, String token) throws Exception {
		boolean valid = false;
		if (user != null) {
			String hash = user.getToken();
			if (hash != null && !hash.isEmpty()) {
				valid = CryptUtil.check(token, hash);
			}
		}

		return valid;
	}

	private User queryUser(String username) {
		UserQuery query = new UserQuery(username);
		Collection<User> matches = this.store.read(query);

		User user = null;
		if (matches.size() == 0) {
			user = null;
		} else if (matches.size() == 1) {
			Iterator<User> iterator = matches.iterator();
			user = iterator.next();
		} else { // matches.size() > 1
			// TODO: throw an exception
			user = null;
		}

		return user;
	}
}
