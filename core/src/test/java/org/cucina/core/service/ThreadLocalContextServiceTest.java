
package org.cucina.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ThreadLocalContextServiceTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testClear() {
        ThreadLocalContextService service = new ThreadLocalContextService();
        Map<Object, Object> map = new HashMap<Object, Object>();

        service.putAll(map);
        service.clear();
        assertNotNull(service.getAll());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPut() {
        ThreadLocalContextService service = new ThreadLocalContextService();

        service.put("key", "value");
        assertEquals("value", service.get("key"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPutAll() {
        ThreadLocalContextService service = new ThreadLocalContextService();
        Map<Object, Object> map = new HashMap<Object, Object>();

        service.putAll(map);
        assertEquals(map, service.getAll());
    }
}
