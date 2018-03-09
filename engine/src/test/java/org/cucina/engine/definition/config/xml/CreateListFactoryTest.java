package org.cucina.engine.definition.config.xml;

import org.apache.commons.digester3.Digester;
import org.junit.Test;
import org.xml.sax.Attributes;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CreateListFactoryTest {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void createList()
			throws Exception {
		ListWrapper wrapper = new ListWrapper();

		Digester digester = mock(Digester.class);

		when(digester.peek()).thenReturn(wrapper);

		Attributes attributes = mock(Attributes.class);

		when(attributes.getValue("name")).thenReturn("list");

		CreateListFactory factory = new CreateListFactory();

		factory.setDigester(digester);

		List<Object> list = factory.createObject(attributes);

		assertNotNull("Should have set list", list);
		assertEquals("Should have set same list", list, wrapper.getList());
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createListNoAttribute()
			throws Exception {
		ListWrapper wrapper = new ListWrapper();
		Digester digester = mock(Digester.class);

		when(digester.peek()).thenReturn(wrapper);

		Attributes attributes = mock(Attributes.class);

		when(attributes.getValue("name")).thenReturn(null);

		CreateListFactory factory = new CreateListFactory();

		factory.setDigester(digester);
		factory.createObject(attributes);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createListNoPropertyWithName()
			throws Exception {
		ListWrapper wrapper = new ListWrapper();
		Digester digester = mock(Digester.class);

		when(digester.peek()).thenReturn(wrapper);

		Attributes attributes = mock(Attributes.class);

		when(attributes.getValue("name")).thenReturn("rabbitch");

		CreateListFactory factory = new CreateListFactory();

		factory.setDigester(digester);
		factory.createObject(attributes);
	}

	public class ListWrapper {
		private List<Object> list;

		public List<Object> getList() {
			return list;
		}

		public void setList(List<Object> list) {
			this.list = list;
		}
	}
}
