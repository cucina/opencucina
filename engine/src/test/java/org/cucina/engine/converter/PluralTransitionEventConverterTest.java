
package org.cucina.engine.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.cucina.engine.event.PluralTransitionEvent;
import org.cucina.engine.model.HistoryRecord;
import org.junit.Before;
import org.junit.Test;


/**
 * Ensure BulkTransitionEventConverter works as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PluralTransitionEventConverterTest {
    private static final String TYPE = "type";
    private static final String WORKFLOW_ID = "workflow";
    private static final String TRANSITION = "transition";
    private static final String COMMENT = "My comment";
    private PluralTransitionEventConverter converter;

    /**
     * Creates including comment
     */
    @Test
    public void convertsWithComment() {
        PluralTransitionEvent event = converter.convert(TYPE + ":" + WORKFLOW_ID + ":" + TRANSITION +
                ":" + COMMENT);

        assertNotNull("Should have created event", event);
        assertEquals("Incorrect type", TYPE, event.getApplicationType());
        assertEquals("Incorrect workflowId", WORKFLOW_ID, event.getWorkflowId());
        assertEquals("Incorrect transition", TRANSITION, event.getSource());
        assertEquals("Incorrect comment", COMMENT,
            event.getParameters().get(HistoryRecord.COMMENTS_PROPERTY));
    }

    /**
     * Creates with default comment
     */
    @Test
    public void convertsWithoutComment() {
        PluralTransitionEvent event = converter.convert(TYPE + ":" + WORKFLOW_ID + ":" + TRANSITION);

        assertNotNull("Should have created event", event);
        assertEquals("Incorrect type", TYPE, event.getApplicationType());
        assertEquals("Incorrect workflowId", WORKFLOW_ID, event.getWorkflowId());
        assertEquals("Incorrect transition", TRANSITION, event.getSource());
        assertEquals("Incorrect comment", PluralTransitionEventConverter.DEFAULT_COMMENT,
            event.getParameters().get(HistoryRecord.COMMENTS_PROPERTY));
    }

    /**
     * ingores null value
     */
    @Test
    public void ignoresNull() {
        assertNull("Shouldn't have created event", converter.convert(null));
    }

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        converter = new PluralTransitionEventConverter();
    }

    /**
     * Should barf if not minimum arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void tooFewArguments() {
        converter.convert(TYPE + ":" + WORKFLOW_ID);
    }
}
