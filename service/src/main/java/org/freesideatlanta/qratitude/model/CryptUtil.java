package org.freesideatlanta.qratitude.model;

import java.io.*;
import java.security.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.*;
import org.apache.log4j.*;

public class CryptUtil {
	private static Logger log = Logger.getLogger(CryptUtil.class);

	private static final String CRYPTO_PROPERTIES = "crypto.properties";
	private static final String CRYPTO_ITERATIONS = "crypto.iterations";
	private static final String CRYPTO_SALT_LENGTH = "crypto.salt_length";
	private static final String CRYPTO_KEY_LENGTH = "crypto.key_length";

	private static int iterations = 10 * 1024;
	private static int saltLength = 32;
	private static int keyLength = 256;

	public static String encrypt(String source) throws Exception {
		if (source == null || source.length() == 0)
			throw new IllegalArgumentException("source to be encrypted cannot be empty");
		// TODO: move the instance parameters out to the crypto properties
		loadCryptoProperties();
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength);
		SecretKey key = f.generateSecret(new PBEKeySpec(source.toCharArray(), salt, iterations, keyLength));
		return Base64.encodeBase64String(key.getEncoded());
	}

	public static String getSaltedHash(String source) throws Exception {
		loadCryptoProperties();
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
		loadCryptoProperties();
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey key = f.generateSecret(new PBEKeySpec(source.toCharArray(), salt, iterations, keyLength));
		return Base64.encodeBase64String(key.getEncoded());
	}

	private static void loadCryptoProperties() throws IOException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		InputStream is = cl.getResourceAsStream(CRYPTO_PROPERTIES);
		properties.load(is);

		String v = properties.getProperty(CRYPTO_ITERATIONS);
		log.debug("iterations value = " + v);
		iterations = Integer.parseInt(v);

		v = properties.getProperty(CRYPTO_SALT_LENGTH);
		log.debug("salt_length value = " + v);
		saltLength = Integer.parseInt(v);

		v = properties.getProperty(CRYPTO_KEY_LENGTH);
		log.debug("key_length value = " + v);
		keyLength = Integer.parseInt(v);
	}
}
