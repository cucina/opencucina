
package org.cucina.security.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.security.crypto.Encryptor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UserTest {
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private Encryptor encryptor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);

        SingletonBeanFactory ins = (SingletonBeanFactory) SingletonBeanFactory.getInstance();

        ins.setBeanFactory(beanFactory);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDecrypt() {
        assertEquals("password", User.decrypt("password"));
        when(beanFactory.containsBean("encryptor")).thenReturn(true);
        when(beanFactory.getBean("encryptor")).thenReturn(encryptor);
        when(encryptor.decrypt("password")).thenReturn("drowssap");
        assertEquals("drowssap", User.decrypt("password"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEncrypt() {
        assertEquals("password", User.encrypt("password"));
        when(beanFactory.containsBean("encryptor")).thenReturn(true);
        when(beanFactory.getBean("encryptor")).thenReturn(encryptor);
        when(encryptor.encrypt("password")).thenReturn("drowssap");
        assertEquals("drowssap", User.encrypt("password"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLocaleConverter() {
        User.LocaleConverter converter = new User.LocaleConverter();

        assertEquals(Locale.ENGLISH, converter.convertDataValueToObjectValue("en", null));
        assertEquals("en", converter.convertObjectValueToDataValue(Locale.ENGLISH, null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPasswordConverter() {
        User.PasswordConverter converter = new User.PasswordConverter();

        assertEquals("password", converter.convertDataValueToObjectValue("password", null));
        assertEquals("password", converter.convertObjectValueToDataValue("password", null));
    }
}
