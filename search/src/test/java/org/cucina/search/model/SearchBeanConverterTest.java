package org.cucina.search.model;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.search.query.SearchBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchBeanConverterTest {
	private static final String SEARCH_MARSHALLED = "blahblahblah";
	@Mock
	private BeanFactory bf;
	@Mock
	private JacksonMarshaller marshaller;
	private SearchBeanConverter handler = new SearchBeanConverter();

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
		SearchBean bean = new SearchBean();

		when(marshaller.marshall(bean)).thenReturn(SEARCH_MARSHALLED);

		assertEquals("Incorrect value", SEARCH_MARSHALLED,
				handler.convertToDatabaseColumn(bean));
		verify(marshaller).marshall(bean);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToDatasetValueNull()
			throws Exception {
		assertNull("Incorrect value", handler.convertToDatabaseColumn(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToObjectValue()
			throws Exception {
		SearchBean bean = new SearchBean();

		when(marshaller.unmarshall(SEARCH_MARSHALLED + "\n", SearchBean.class)).thenReturn(bean);
		assertEquals("Should have returned bean", bean,
				handler.convertToEntityAttribute(SEARCH_MARSHALLED));
		verify(marshaller).unmarshall(SEARCH_MARSHALLED + "\n", SearchBean.class);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void toObjectValueNull() {
		assertNull(handler.convertToEntityAttribute(null));
	}
}
