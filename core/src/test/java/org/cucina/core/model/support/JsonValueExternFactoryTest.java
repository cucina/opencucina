package org.cucina.core.model.support;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.core.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JsonValueExternFactoryTest {
	private static final String YOHOHO = "yohoho";
	@Mock
	private BeanFactory bf;
	@Mock
	private Foo searchBean;
	@Mock
	private JacksonMarshaller marshaller;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(marshaller.unmarshall(YOHOHO + "\n", Foo.class)).thenReturn(searchBean);
		when(marshaller.marshall(searchBean)).thenReturn(YOHOHO);
		when(bf.getBean(JacksonMarshaller.class)).thenReturn(marshaller);
		((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testFromString() {
		assertEquals(searchBean, JsonMarshallerFactory.fromString(YOHOHO, Foo.class));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = RuntimeException.class)
	public void testFromStringException() {
		when(marshaller.unmarshall(YOHOHO + "\n", Foo.class)).thenThrow(new IOException());
		JsonMarshallerFactory.fromString(YOHOHO, Foo.class);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testFromStringNull() {
		assertNull("Should return null", JsonMarshallerFactory.fromString(null, null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToStringNull() {
		assertNull("Should return null", JsonMarshallerFactory.toString(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToStringSearchBean() {
		assertEquals(YOHOHO, JsonMarshallerFactory.toString(searchBean));
	}
}
