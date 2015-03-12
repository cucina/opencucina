
package org.cucina.engine.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.cucina.engine.schedule.CronSlot;
import org.junit.Before;
import org.junit.Test;


/**
 * Ensure CronSlotConverter works as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CronSlotConverterTest {
    private static final String NAME = "name";
    private static final String WORKFLOW_ID = "workflow";
    private static final String TRANSITION = "transition";
    private static final String CRON = "* * * * * ?";
    private CronSlotConverter converter;

    /**
     * Creates CronSlot without fuss
     */
    @Test
    public void converts() {
        CronSlot slot = converter.convert(NAME + ":" + WORKFLOW_ID + ":" + TRANSITION + ":" + CRON);

        assertNotNull("Should have created wrapper", slot);
        assertEquals("Incorrect name", NAME, slot.getName());
        assertEquals("Incorrect workflow id", WORKFLOW_ID, slot.getWorkflowId());
        assertEquals("Incorrect transition", TRANSITION, slot.getTransitionId());
        assertEquals("Should have correct cron expression", CRON, slot.getCronExpression());
    }

    /**
     * ingores null value
     */
    @Test
    public void ignoresNull() {
        assertNull("Shouldn't have created wrapper", converter.convert(null));
    }

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        converter = new CronSlotConverter();
    }

    /**
     * Should barf if not minimum arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void tooFewArguments() {
        converter.convert(NAME + ":" + WORKFLOW_ID + ":" + TRANSITION);
    }
}
