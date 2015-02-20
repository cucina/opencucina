
package org.cucina.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EmailDescriptorTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetMessageKey() {
        EmailDescriptor descriptor = new EmailDescriptor();

        descriptor.setMessageKey("messageKey");
        assertEquals("messageKey", descriptor.getMessageKey());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetParameters() {
        EmailDescriptor descriptor = new EmailDescriptor();

        Map<Object, Object> parameters = new HashMap<Object, Object>();

        parameters.put("key", new Object());
        descriptor.setParameters(parameters);
        assertEquals(parameters, descriptor.getParameters());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetToUsers() {
        EmailDescriptor descriptor = new EmailDescriptor();

        Object touser = new Object();

        descriptor.addToUser(touser);

        Object ccuser = new Object();

        descriptor.addCcUser(ccuser);

        Object bccuser = new Object();

        descriptor.addBccUser(bccuser);
        assertTrue(descriptor.getToUsers().contains(touser));
        assertTrue(descriptor.getCcUsers().contains(ccuser));
        assertTrue(descriptor.getBccUsers().contains(bccuser));
    }
}
