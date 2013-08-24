package org.freesideatlanta.qratitude.model;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.*;

public class CryptUtil {

	private static final int iterations = 10 * 1024;
	private static final int saltLength = 32;
	private static final int keyLength = 256;

	public static String getSaltedHash(String source) throws Exception {
		byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength);
		return Base64.encodeBase64String(salt) + "$" + hash(source, salt);
	}

	public static boolean check(String source, String stored) throws Exception {
		String[] saltAndPass = stored.split("\\$");
		if (saltAndPass.length != 2)
			return false;
		String hashOfInput = hash(source, Base64.decodeBase64(saltAndPass[0]));
		return hashOfInput.equals(saltAndPass[1]);
	}

	private static String hash(String source, byte[] salt) throws Exception {
		if (source == null || source.length() == 0)
			throw new IllegalArgumentException("source to be hashed cannot be empty");
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey key = f.generateSecret(new PBEKeySpec(source.toCharArray(), salt, iterations, keyLength));
		return Base64.encodeBase64String(key.getEncoded());
	}
}
