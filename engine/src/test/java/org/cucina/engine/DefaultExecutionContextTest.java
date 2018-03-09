package org.cucina.engine;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * @author vlevine
 */
public class DefaultExecutionContextTest {
	/**
	 *
	 */
	@Test
	public void testAddParameter() {
		DefaultExecutionContext context = new DefaultExecutionContext(null, null, null, null);

		context.addParameter("key", "value");
		assertEquals("value", context.getParameters().get("key"));
	}

	/**
	 *
	 */
	@Test
	public void testSetParameters() {
		DefaultExecutionContext context = new DefaultExecutionContext(null, null, null, null);
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("key", "value");
		context.setParameters(parameters);
		assertEquals(parameters, context.getParameters());
	}
}
