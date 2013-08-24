package org.freesideatlanta.qratitude.model;

import java.net.*;
import java.util.*;

import org.freesideatlanta.qratitude.data.*;

public class Authenticator {

	private UserStore store;

	public Authenticator() {
		this.store = StoreFactory.getUserStore();
	}

	public User login(String username, String password) throws Exception {
		User user = this.queryUser(username);
		
		User modified = null;
		if (user != null) {
			modified = this.login(user, password);
		}

		return modified;
	}

	public User login(User user, String password) throws Exception {
		boolean valid = false;
		if (user != null) {
			String hash = user.getPassword();
			valid = CryptUtil.check(password, hash);
		}

		if (valid) {
			String token = UUID.randomUUID().toString();
			String hash = CryptUtil.getSaltedHash(token);

			user.setToken(hash);
			this.store.update(user);
		}

		return user; 
	}

	public void logout(String username) {
		User user = this.queryUser(username);
		if (user != null) {
			user.setToken("");
		}
	}

	public boolean authenticate(String username, String token) {
		User user = this.queryUser(username);

		boolean valid = false;
		if (user != null) {
			valid = this.authenticate(user, token);
		}

		return valid;
	}

	public boolean authenticate(User user, String token) {
		boolean valid = false;
		if (user != null) {
			String hash = user.getToken();
			if (hash != null && !hash.isEmpty()) {
				valid = hash.equals(token);
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
