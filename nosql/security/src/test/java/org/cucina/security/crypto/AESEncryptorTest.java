
package org.cucina.security.crypto;

import static org.junit.Assert.assertEquals;

import java.security.Provider;
import java.security.Security;

import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AESEncryptorTest {
    private static final String TEXT = "admin";
    private static final String PASSWORD = "sprite";
    private static final String LONG_PASSWORD = "password12354";
    private static final String LONG_TEXT = "textmore test and even more testvsf^&*()";
    private AESEncryptor encryptor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
    }

    /**
     * JAVADOC Method Level Comments
     * @throws Exception
     * @throws
     */
    @Test
    public void testCrypt()
        throws Exception {
        encryptor = new AESEncryptor(PASSWORD);
        encryptor.afterPropertiesSet();

        String enct = encryptor.encrypt(TEXT);

        System.err.println(TEXT + "=" + enct);
        assertEquals(TEXT, encryptor.decrypt(enct));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testCryptLong()
        throws Exception {
        encryptor = new AESEncryptor(LONG_PASSWORD);

        encryptor.afterPropertiesSet();

        String enct = encryptor.encrypt(LONG_TEXT);

        System.err.println(enct.length());
        assertEquals(LONG_TEXT, encryptor.decrypt(enct));
    }

    //@Test
    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    public void testDecrypt()
        throws Exception {
        encryptor = new AESEncryptor(PASSWORD);
        encryptor.afterPropertiesSet();
        System.err.println(encryptor.decrypt("b2cfb399ee1a640e134ee97ae185e7dc"));

        //System.err.println(encryptor.decrypt("e8763113673d8b7791637b75050bcfca"));
    }

    /**
     * JAVADOC Method Level Comments
     */

    //@Test
    public void testProviders() {
        for (Provider provider : Security.getProviders()) {
            System.out.println(provider.getName());
            System.out.println(provider.getInfo());

            for (String key : provider.stringPropertyNames())
                if (key.contains("Cipher") && key.contains("AES")) {
                    System.out.println("\t" + key + "\t" + provider.getProperty(key));
                }
        }
    }
}
