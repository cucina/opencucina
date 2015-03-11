package org.cucina.security.model;

import java.util.Locale;

import org.springframework.beans.factory.BeanFactory;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.security.crypto.Encryptor;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


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

        assertEquals(Locale.ENGLISH, converter.convertToEntityAttribute("en"));
        assertEquals("en", converter.convertToDatabaseColumn(Locale.ENGLISH));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPasswordConverter() {
        User.PasswordConverter converter = new User.PasswordConverter();

        assertEquals("password", converter.convertToEntityAttribute("password"));
        assertEquals("password", converter.convertToDatabaseColumn("password"));
    }
}
