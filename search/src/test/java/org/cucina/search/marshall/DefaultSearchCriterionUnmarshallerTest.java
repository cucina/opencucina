package org.cucina.search.marshall;

import org.cucina.core.InstanceFactory;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.criterion.ForeignKeySearchCriterion;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Test unmarshalling of DefaultSearchCriterionUnmarshaller functions as
 * expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DefaultSearchCriterionUnmarshallerTest {
	private static final String NAME = "name";
	private static final String ALIAS = "alias";
	private static final String ROOT_TYPE = "rootType";
	private static final String ROOT_ALIAS = "rootAlias";
	private static final String VALUE = "value";
	private static final Long FK = 12L;
	private DefaultSearchCriterionUnmarshaller unmarshaller;

	/**
	 * Set up for test.
	 */
	@Before
	public void setup() {
		unmarshaller = new DefaultSearchCriterionUnmarshaller(mock(InstanceFactory.class));
	}

	/**
	 * unmarshall blank foreign key
	 */
	@Test
	public void unmarshallBlank() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		marshalledCriterion.put(ALIAS, null);

		InstanceFactory instanceFactory = mock(InstanceFactory.class);

		when(instanceFactory.isForeignKey(ROOT_TYPE, NAME)).thenReturn(true);

		unmarshaller = new DefaultSearchCriterionUnmarshaller(instanceFactory);

		assertNull("searchCriterion should be null",
				unmarshaller.unmarshall(NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, marshalledCriterion));

		verify(instanceFactory).isForeignKey(ROOT_TYPE, NAME);
	}

	/**
	 * unmarshall foreign key
	 */
	@Test
	public void unmarshallForeignKey() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		marshalledCriterion.put(ALIAS, FK);

		InstanceFactory instanceFactory = mock(InstanceFactory.class);

		when(instanceFactory.isForeignKey(ROOT_TYPE, NAME)).thenReturn(true);

		unmarshaller = new DefaultSearchCriterionUnmarshaller(instanceFactory);

		ForeignKeySearchCriterion searchCriterion = (ForeignKeySearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, marshalledCriterion);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set value correctly", Collections.singleton(FK),
				searchCriterion.getValue());

		verify(instanceFactory).isForeignKey(ROOT_TYPE, NAME);
	}

	/**
	 * unmarshall multiple string
	 */
	@Test
	public void unmarshallInText() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();
		Collection<String> values = Collections.singleton("a");

		marshalledCriterion.put(ALIAS, values);

		InstanceFactory instanceFactory = mock(InstanceFactory.class);

		when(instanceFactory.isForeignKey(ROOT_TYPE, NAME)).thenReturn(false);
		unmarshaller = new DefaultSearchCriterionUnmarshaller(instanceFactory);

		InSearchCriterion searchCriterion = (InSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, marshalledCriterion);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set value correctly", new ArrayList<Object>(values),
				searchCriterion.getValues());
		verify(instanceFactory).isForeignKey(ROOT_TYPE, NAME);
	}

	/**
	 * unmarshall text
	 */
	@Test
	public void unmarshallText() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		marshalledCriterion.put(ALIAS, VALUE);

		InstanceFactory instanceFactory = mock(InstanceFactory.class);

		when(instanceFactory.isForeignKey(ROOT_TYPE, NAME)).thenReturn(false);
		unmarshaller = new DefaultSearchCriterionUnmarshaller(instanceFactory);

		TextSearchCriterion searchCriterion = (TextSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, marshalledCriterion);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set value correctly", VALUE, searchCriterion.getValue());
		verify(instanceFactory).isForeignKey(ROOT_TYPE, NAME);
	}

	/**
	 * unmarshall text
	 */
	@Test
	public void unmarshallTextNot() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		marshalledCriterion.put(ALIAS, VALUE);
		marshalledCriterion.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.NOT_PROPERTY),
				"true");

		InstanceFactory instanceFactory = mock(InstanceFactory.class);

		when(instanceFactory.isForeignKey(ROOT_TYPE, NAME)).thenReturn(false);
		unmarshaller = new DefaultSearchCriterionUnmarshaller(instanceFactory);

		TextSearchCriterion searchCriterion = (TextSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, marshalledCriterion);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set value correctly", VALUE, searchCriterion.getValue());
		assertEquals("should be negated", true, searchCriterion.isBooleanNot());
		verify(instanceFactory).isForeignKey(ROOT_TYPE, NAME);
	}

	/**
	 * unmarshall text
	 */
	@Test
	public void unmarshallTextNotBool() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		marshalledCriterion.put(ALIAS, VALUE);
		marshalledCriterion.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.NOT_PROPERTY),
				true);

		InstanceFactory instanceFactory = mock(InstanceFactory.class);

		when(instanceFactory.isForeignKey(ROOT_TYPE, NAME)).thenReturn(false);
		unmarshaller = new DefaultSearchCriterionUnmarshaller(instanceFactory);

		TextSearchCriterion searchCriterion = (TextSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, marshalledCriterion);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set value correctly", VALUE, searchCriterion.getValue());
		assertEquals("should be negated", true, searchCriterion.isBooleanNot());
		verify(instanceFactory).isForeignKey(ROOT_TYPE, NAME);
	}
}
