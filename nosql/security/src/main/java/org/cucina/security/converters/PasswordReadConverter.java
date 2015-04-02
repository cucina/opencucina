package org.cucina.security.converters;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

import org.cucina.security.crypto.Encryptor;
import org.cucina.security.model.Password;


/**
 *
 *
 * @author vlevine
  */
public class PasswordReadConverter
    implements Converter<DBObject, Password> {
    private Encryptor encryptor;

    /**
     * Creates a new PasswordReadConverter object.
     *
     * @param encryptor .
     */
    public PasswordReadConverter(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    /**
     *
     *
     * @param source .
     *
     * @return .
     */
    @Override
    public Password convert(DBObject source) {
        return new Password(encryptor.decrypt((String) source.get("encrypted")));
    }
}
