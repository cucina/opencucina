package org.cucina.engine.definition.config.xml;

import org.apache.commons.digester3.Digester;
import org.junit.Test;
import org.xml.sax.Attributes;

import java.util.Map;

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
public class CreateMapFactoryTest {
	/**
	 * Creates map and sets it on top stack object.
	 *
	 * @throws Exception.
	 */
	@Test
	public void createMap()
			throws Exception {
		MapWrapper wrapper = new MapWrapper();

		Digester digester = mock(Digester.class);

		when(digester.peek()).thenReturn(wrapper);

		Attributes attributes = mock(Attributes.class);

		when(attributes.getValue("name")).thenReturn("map");

		CreateMapFactory factory = new CreateMapFactory();

		factory.setDigester(digester);

		Map<Object, Object> map = factory.createObject(attributes);

		assertNotNull("Should have set map", map);
		assertEquals("Should have set same list", map, wrapper.getMap());
	}

	/**
	 * Barfs if name not correct
	 *
	 * @throws Exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createMapInvalidPropertyName()
			throws Exception {
		MapWrapper wrapper = new MapWrapper();

		Digester digester = mock(Digester.class);

		when(digester.peek()).thenReturn(wrapper);

		Attributes attributes = mock(Attributes.class);

		when(attributes.getValue("name")).thenReturn("rabbitch");

		CreateMapFactory factory = new CreateMapFactory();

		factory.setDigester(digester);
		factory.createObject(attributes);
	}

	/**
	 * Barfs if name not set
	 *
	 * @throws Exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createMapNoAttribute()
			throws Exception {
		MapWrapper wrapper = new MapWrapper();

		Digester digester = mock(Digester.class);

		when(digester.peek()).thenReturn(wrapper);

		Attributes attributes = mock(Attributes.class);

		when(attributes.getValue("name")).thenReturn(null);

		CreateMapFactory factory = new CreateMapFactory();

		factory.setDigester(digester);
		factory.createObject(attributes);
	}

	public class MapWrapper {
		private Map<Object, Object> map;

		public Map<Object, Object> getMap() {
			return map;
		}

		public void setMap(Map<Object, Object> map) {
			this.map = map;
		}
	}
}
