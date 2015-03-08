
package org.cucina.search.marshall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.cucina.search.query.criterion.BooleanSearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.junit.Before;
import org.junit.Test;


/**
 * Test that BooleanSearchCriterionMarshaller functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BooleanSearchCriterionMarshallerTest {
    private static final String NAME = "name";
    private static final String ALIAS = "alias";
    private static final String ROOT_ALIAS = "root";
    private static final boolean VALUE = true;
    private BooleanSearchCriterionMarshaller marshaller;

    /**
     * Expects alias
     */
    @Test(expected = IllegalArgumentException.class)
    public void checksAlias() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        BooleanSearchCriterion criterion = new BooleanSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        marshaller.marshall(null, null, criterion, marshalledCriterion);
    }

    /**
     * Expects criterion
     */
    @Test(expected = IllegalArgumentException.class)
    public void checksCriterion() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        marshaller.marshall(ALIAS, null, null, marshalledCriterion);
    }

    /**
     * Expects marshalledCriterion
     */
    @Test(expected = IllegalArgumentException.class)
    public void checksMarshalledCriterion() {
        BooleanSearchCriterion criterion = new BooleanSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        marshaller.marshall(ALIAS, null, criterion, null);
    }

    /**
     * Marshalls value
     */
    @Test
    public void marshallsValue() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        BooleanSearchCriterion criterion = new BooleanSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        Boolean value = (Boolean) marshalledCriterion.get(ALIAS);

        assertEquals("Should have got value", VALUE, value);
    }

    /**
     * Set up for test
     */
    @Before
    public void setup() {
        marshaller = new BooleanSearchCriterionMarshaller();
    }

    /**
     * Check supports for right type
     */
    @Test
    public void supports() {
        assertTrue("Should support BooleanSearchCriterionMarshaller",
            marshaller.supports(BooleanSearchCriterion.class));
        assertFalse("Shouldn't support NumberSearchCriterion",
            marshaller.supports(NumberSearchCriterion.class));
    }
}
