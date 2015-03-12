
package org.cucina.engine;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DefaultExecutionContextTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAddParameter() {
        DefaultExecutionContext context = new DefaultExecutionContext(null, null, null, null);

        context.addParameter("key", "value");
        assertEquals("value", context.getParameters().get("key"));
    }

    /**
     * JAVADOC Method Level Comments
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
