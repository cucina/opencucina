package org.cucina.engine.definition.config.xml;

import org.apache.commons.digester3.Digester;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.Operation;
import org.cucina.engine.definition.ProcessDefinition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.Attributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PropertyCreationFactoryTest {
	@Mock
	private Digester digester;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testCreateMapObject()
			throws Exception {
		PropertyCreationFactory factory = new PropertyCreationFactory();

		factory.setDigester(digester);
		assertEquals(digester, factory.getDigester());

		Attributes attr = mock(Attributes.class);

		when(attr.getValue("value")).thenReturn("100");
		when(attr.getValue("name")).thenReturn("amount");

		MapOperation action = new MapOperation();

		when(digester.peek()).thenReturn(action);

		System.err.println(factory.createObject(attr));
		assertEquals("100", action.get("amount"));
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testCreateSimpleObject()
			throws Exception {
		PropertyCreationFactory factory = new PropertyCreationFactory();

		factory.setDigester(digester);
		assertEquals(digester, factory.getDigester());

		Attributes attr = mock(Attributes.class);

		when(attr.getValue("value")).thenReturn("100");
		when(attr.getValue("name")).thenReturn("amount");

		SimpleOperation action = new SimpleOperation();

		when(digester.peek()).thenReturn(action);

		System.err.println(factory.createObject(attr));
		assertEquals(100, action.getAmount().intValue());
	}

	public final class MapOperation
			implements Operation, Map<String, String> {
		private Map<String, String> innerMap = new HashMap<String, String>();

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEmpty() {
			return innerMap.isEmpty();
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ProcessDefinition getProcessDefinition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setProcessDefinition(ProcessDefinition worklowDefinition) {
			// TODO Auto-generated method stub
		}

		public void clear() {
			innerMap.clear();
		}

		public boolean containsKey(Object key) {
			return innerMap.containsKey(key);
		}

		public boolean containsValue(Object value) {
			return innerMap.containsValue(value);
		}

		public Set<java.util.Map.Entry<String, String>> entrySet() {
			return innerMap.entrySet();
		}

		@Override
		public void execute(ExecutionContext executionContext) {
			// TODO Auto-generated method stub
		}

		public String get(Object key) {
			return innerMap.get(key);
		}

		public Set<String> keySet() {
			return innerMap.keySet();
		}

		public String put(String key, String value) {
			return innerMap.put(key, value);
		}

		public void putAll(Map<? extends String, ? extends String> m) {
			innerMap.putAll(m);
		}

		public String remove(Object key) {
			return innerMap.remove(key);
		}

		public int size() {
			return innerMap.size();
		}

		public Collection<String> values() {
			return innerMap.values();
		}
	}

	public final class SimpleOperation
			implements Operation {
		private Integer amount;

		public Integer getAmount() {
			return amount;
		}

		public void setAmount(Integer name) {
			this.amount = name;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ProcessDefinition getProcessDefinition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setProcessDefinition(ProcessDefinition worklowDefinition) {
			// TODO Auto-generated method stub
		}

		@Override
		public void execute(ExecutionContext executionContext) {
			// TODO Auto-generated method stub
		}
	}
}
