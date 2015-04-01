package org.cucina.security.converters;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.cucina.security.crypto.Encryptor;
import org.cucina.security.model.Password;


/**
 *
 *
 * @author vlevine
  */
public class PasswordWriteConverter
    implements Converter<Password, DBObject> {
    private Encryptor encryptor;

    /**
     * Creates a new PasswordWriteConverter object.
     *
     * @param encryptor .
     */
    public PasswordWriteConverter(Encryptor encryptor) {
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
    public DBObject convert(Password source) {
        DBObject dbo = new BasicDBObject();

        dbo.put("encrypted", source.getValue());
System.err.println(dbo);
        return dbo;
    }
}
