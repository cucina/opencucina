package org.cucina.email.event;

import java.util.HashMap;
import java.util.Map;

import org.cucina.email.event.EmailWithAttachmentDto;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EmailWithAttachmentDtoTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetMessageKey() {
        EmailWithAttachmentDto descriptor = new EmailWithAttachmentDto();

        descriptor.setMessageKey("messageKey");
        assertEquals("messageKey", descriptor.getMessageKey());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetParameters() {
        EmailWithAttachmentDto descriptor = new EmailWithAttachmentDto();

        Map<Object, Object> parameters = new HashMap<Object, Object>();

        parameters.put("key", new Object());
        descriptor.setParameters(parameters);
        assertEquals(parameters, descriptor.getParameters());
    }
}
