package org.cucina.security.crypto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import reactor.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;


/**
 * Encryptor to do AES.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class AESEncryptor
		implements Encryptor, InitializingBean {
	private static final String AES = "AES";
	private static final String AES_PAD = "AES/CBC/PKCS5Padding";
	private static final int ITERATIONS = 1000;
	private static final String HASHING_ALGO = "SHA-256";
	private static final Logger LOG = LoggerFactory.getLogger(AESEncryptor.class);

	// TODO pool ciphers, that'll remove a need for synchronized blocks
	private Cipher dec;
	private Cipher enc;
	private SecretKey key;
	private String password;

	/**
	 * Creates a new AESEncryptor object.
	 *
	 * @param password JAVADOC.
	 * @throws UnsupportedEncodingException
	 * @throws GeneralSecurityException
	 */
	public AESEncryptor(String password)
			throws UnsupportedEncodingException, GeneralSecurityException {
		this.password = password;
	}

	private static byte[] append(byte[] array1, byte[] array2) {
		byte[] array3 = new byte[array1.length + array2.length];
		int index = 0;

		for (int i = 0; i < array1.length; i++) {
			array3[index++] = array1[i];
		}

		for (int i = 0; i < array2.length; i++) {
			array3[index++] = array2[i];
		}

		return array3;
	}

	/**
	 * @throws Exception .
	 */
	@Override
	public void afterPropertiesSet()
			throws Exception {
		init();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param text JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public String decrypt(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}

		if (enc == null) {
			try {
				init();
			} catch (Exception e) {
				LOG.error("Oops", e);
			}
		}

		synchronized (dec) {
			try {
				dec.init(Cipher.DECRYPT_MODE, key);

				byte[] r = dec.doFinal(Hex.decode(text));

				return Utf8.decode(r);
			} catch (GeneralSecurityException e) {
				LOG.error("Oops", e);
				throw new IllegalArgumentException("Security exception", e);
			}
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param text JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public String encrypt(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}

		if (enc == null) {
			try {
				init();
			} catch (Exception e) {
				LOG.error("Oops", e);
			}
		}

		synchronized (enc) {
			try {
				enc.init(Cipher.ENCRYPT_MODE, key);

				byte[] r = enc.doFinal(Utf8.encode(text));

				return new String(Hex.encode(r));
			} catch (GeneralSecurityException e) {
				LOG.error("Oops", e);
				throw new IllegalArgumentException("Security exception", e);
			}
		}
	}

	private SecretKey generateKey(byte[] bPassword, byte[] salt)
			throws IllegalArgumentException {
		if ((bPassword == null) || (salt == null)) {
			throw new IllegalArgumentException("One or more of the parameters are Null");
		}

		SecretKey aesKey = null;

		try {
			bPassword = append(bPassword, salt);

			MessageDigest md = MessageDigest.getInstance(HASHING_ALGO);
			byte[] hashedKey = md.digest(bPassword);

			for (int i = 0; i < (ITERATIONS - 1); i++) {
				hashedKey = append(hashedKey, salt);
				hashedKey = md.digest(hashedKey);
			}

			aesKey = new SecretKeySpec(hashedKey, 0, 16, AES);
		} catch (Exception e) {
			LOG.error("Oops", e);
		}

		return aesKey;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	private void init()
			throws Exception {
		byte[] bPassword = password.getBytes("UTF-8");
		byte[] salt = SALT.getBytes("UTF-8");

		key = generateKey(bPassword, salt);
		enc = Cipher.getInstance(AES);

		if (enc == null) {
			LOG.debug("enc is null for " + AES);
			enc = Cipher.getInstance(AES_PAD);
			Assert.notNull(enc, "enc is null for " + AES + " and " + AES_PAD);
			dec = Cipher.getInstance(AES_PAD);
		} else {
			dec = Cipher.getInstance(AES);
		}
	}
}
