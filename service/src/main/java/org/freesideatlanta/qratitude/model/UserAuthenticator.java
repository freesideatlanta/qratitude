package org.freesideatlanta.qratitude.model;

import java.net.*;
import java.util.*;

import org.apache.log4j.*;

import org.freesideatlanta.qratitude.data.*;

public class UserAuthenticator {
	private static Logger log = Logger.getLogger(UserAuthenticator.class);
	private UserStore store;

	public UserAuthenticator() {
		this.store = StoreFactory.getUserStore();
	}

	public Token login(String username, String password) throws Exception {
		log.debug("about to query for user: " + username);		
		User user = this.queryUserByUsername(username);
		
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
			log.debug("checking password hash for validity");
			valid = CryptUtil.check(password, hash);
		}

		if (valid) {
			token = new Token();
			log.debug("generating the token for the user");
			token.generate(user);

			// NOTE: the hash of the token string is stored in the database
			String tokenText = token.getToken();
			log.debug("get the salted hash of the token");
			String hash = CryptUtil.getSaltedHash(tokenText);
			user.setToken(hash);
			log.debug("update the user with the token hash value");
			this.store.update(user);
		}

		return token; 
	}

	public void logout(String token) throws Exception {
		User user = this.queryUserByToken(token);
		if (user != null) {
			user.setToken("");
			this.store.update(user);
		}
	}

	public boolean authenticate(String token) throws Exception {
		User user = this.queryUserByToken(token);

		boolean valid = false;
		if (user != null) {
			log.debug("user.getUsername() = " + user.getUsername());
			valid = this.authenticate(user, token);
		}

		return valid;
	}

	public boolean authenticate(User user, String token) throws Exception {
		boolean valid = false;
		if (user != null) {
			String hash = user.getToken();
			log.debug("token = " + token);
			log.debug("hash = " + hash);
			if (hash != null && !hash.isEmpty()) {
				log.debug("checking token against hash stored in the database");
				valid = CryptUtil.check(token, hash);
				log.debug("valid = " + valid);
			}
		}

		return valid;
	}

	private User queryUserByUsername(String username) {
		UserQuery query = new UserUsernameQuery(username);
		Collection<User> matches = this.store.read(query);
		return this.takeOne(matches);
	}

	private User queryUserByToken(String token) {
		UserQuery query = new UserTokenQuery(token);
		Collection<User> matches = this.store.read(query);
		return this.takeOne(matches);
	}

	private User takeOne(Collection<User> matches) {
		User user = null;
		if (matches.size() == 0) {
			log.debug("matches.size() == 0");
			user = null;
		} else if (matches.size() == 1) {
			log.debug("matches.size() == 1");
			Iterator<User> iterator = matches.iterator();
			user = iterator.next();
		} else { // matches.size() > 1
			log.debug("matches.size() > 1");
			// TODO: throw an exception
			user = null;
		}

		return user;
	}
}
