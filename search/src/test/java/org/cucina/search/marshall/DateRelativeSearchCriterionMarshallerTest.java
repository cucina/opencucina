
package org.cucina.search.marshall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.cucina.search.query.criterion.AbstractDateSearchCriterion;
import org.cucina.search.query.criterion.DateRelativeSearchCriterion;
import org.cucina.search.query.criterion.DateSearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.junit.Before;
import org.junit.Test;


/**
 * Test that DateRelativeSearchCriterion functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateRelativeSearchCriterionMarshallerTest {
    private static final String NAME = "name";
    private static final String ALIAS = "alias";
    private static final String ROOT_ALIAS = "root";
    private DateRelativeSearchCriterionMarshaller marshaller;

    /**
     * Expects alias
     */
    @Test(expected = IllegalArgumentException.class)
    public void checksAlias() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();
        Calendar cal = Calendar.getInstance();
        Date to = cal.getTime();

        cal.add(Calendar.DATE, -1);

        Date from = cal.getTime();
        AbstractDateSearchCriterion criterion = new DateSearchCriterion(NAME, null, ROOT_ALIAS, from, to);

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
        Calendar cal = Calendar.getInstance();
        Date to = cal.getTime();

        cal.add(Calendar.DATE, -1);

        Date from = cal.getTime();
        AbstractDateSearchCriterion criterion = new DateSearchCriterion(NAME, null, ROOT_ALIAS, from, to);

        marshaller.marshall(ALIAS, null, criterion, null);
    }

    /**
     * Marshalls from only
     */
    @Test
    public void marshallsFrom() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        DateRelativeSearchCriterion criterion = new DateRelativeSearchCriterion(NAME, null, ROOT_ALIAS,
                "+1d", null);

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        @SuppressWarnings("unchecked")
        Map<String, String> crit = (Map<String, String>) marshalledCriterion.get(ALIAS);

        assertEquals("Should have set from time", "+1d",
            crit.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertNull("Should not have set to time", crit.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Marshalls from and to
     */
    @Test
    public void marshallsFromAndTo() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        DateRelativeSearchCriterion criterion = new DateRelativeSearchCriterion(NAME, null, ROOT_ALIAS,
                "+1d", "+3d");

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        @SuppressWarnings("unchecked")
        Map<String, String> crit = (Map<String, String>) marshalledCriterion.get(ALIAS);

        assertEquals("Should have set from time", "+1d",
            crit.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", "+3d",
            crit.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Marshalls to only
     */
    @Test
    public void marshallsTo() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        DateRelativeSearchCriterion criterion = new DateRelativeSearchCriterion(NAME, null, ROOT_ALIAS,
                null, "+3d");

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        @SuppressWarnings("unchecked")
        Map<String, String> crit = (Map<String, String>) marshalledCriterion.get(ALIAS);

        assertNull("Should not have set from time",
            crit.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", "+3d",
            crit.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Set up for test
     */
    @Before
    public void setup() {
        marshaller = new DateRelativeSearchCriterionMarshaller();
    }

    /**
     * Check supports for right type
     */
    @Test
    public void supports() {
        assertTrue("Should support DateRelativeSearchCriterion",
            marshaller.supports(DateRelativeSearchCriterion.class));
        assertFalse("Shouldn't support NumberSearchCriterion",
            marshaller.supports(NumberSearchCriterion.class));
    }
}
