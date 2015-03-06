package org.cucina.core.model.eclipselink;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.spring.SingletonBeanFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class JsonMapConverterTest {
    private static final String MARSHALLED = "blahblahblah";
    @Mock
    private BeanFactory bf;
    @Mock
    private JacksonMarshaller marshaller;
    private JsonMapConverter handler = new JsonMapConverter();

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(bf.getBean(JacksonMarshaller.class)).thenReturn(marshaller);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToDatasetValue()
        throws Exception {
        Map<String, String> bean = new HashMap<String, String>();

        when(marshaller.marshall(bean)).thenReturn(MARSHALLED);
        assertEquals("Incorrect value", MARSHALLED,
            handler.convertObjectValueToDataValue(bean, null));
        verify(marshaller).marshall(bean);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToDatasetValueNull()
        throws Exception {
        assertNull("Incorrect value", handler.convertObjectValueToDataValue(null, null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToObjectValue()
        throws Exception {
        Map<String, String> bean = new HashMap<String, String>();

        when(marshaller.unmarshall(MARSHALLED + "\n", Map.class)).thenReturn(bean);
        assertEquals("Should have returned bean", bean,
            handler.convertDataValueToObjectValue(MARSHALLED, null));
        verify(marshaller).unmarshall(MARSHALLED + "\n", Map.class);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToObjectValueNull()
        throws Exception {
        assertNull("Should just return and not barf",
            handler.convertDataValueToObjectValue(null, null));
    }
}
