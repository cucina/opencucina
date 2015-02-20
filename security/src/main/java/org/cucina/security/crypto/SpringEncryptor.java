
package org.cucina.security.crypto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SpringEncryptor
    implements Encryptor {
    private TextEncryptor encryptor;

    /**
     * Creates a new SpringEncryptor object.
     *
     * @param password JAVADOC.
     */
    public SpringEncryptor(String password) {
        this.encryptor = Encryptors.text(password, SALT);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param text JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String decrypt(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        return encryptor.decrypt(text);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param text JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String encrypt(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        return encryptor.encrypt(text);
    }
}
