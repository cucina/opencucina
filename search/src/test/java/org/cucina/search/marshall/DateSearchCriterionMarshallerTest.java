
package org.cucina.search.marshall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.cucina.search.query.criterion.AbstractDateSearchCriterion;
import org.cucina.search.query.criterion.DateSearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.junit.Before;
import org.junit.Test;


/**
 * Test that DateSearchCriterionMarshaller functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateSearchCriterionMarshallerTest {
    private static final String NAME = "name";
    private static final String ALIAS = "alias";
    private static final String ROOT_ALIAS = "root";
    private DateSearchCriterionMarshaller marshaller;

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
    @SuppressWarnings("unchecked")
    @Test
    public void marshallsFrom() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -1);

        Date from = cal.getTime();

        AbstractDateSearchCriterion criterion = new DateSearchCriterion(NAME, null, ROOT_ALIAS, from, null);

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

        assertNotNull("Should be criteria", criteria);
        assertEquals("Should have set from time", from.getTime() / 1000,
            criteria.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertNull("Should not have set to time",
            criteria.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Marshalls from and to
     */
    @SuppressWarnings("unchecked")
    @Test
    public void marshallsFromAndTo() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        Calendar cal = Calendar.getInstance();
        Date to = cal.getTime();

        cal.add(Calendar.DATE, -1);

        Date from = cal.getTime();

        AbstractDateSearchCriterion criterion = new DateSearchCriterion(NAME, null, ROOT_ALIAS, from, to);

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

        assertNotNull("Should be criteria", criteria);
        assertEquals("Should have set from time", from.getTime() / 1000,
            criteria.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", to.getTime() / 1000,
            criteria.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Marshalls to only
     */
    @SuppressWarnings("unchecked")
    @Test
    public void marshallsTo() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        Calendar cal = Calendar.getInstance();
        Date to = cal.getTime();

        AbstractDateSearchCriterion criterion = new DateSearchCriterion(NAME, null, ROOT_ALIAS, null, to);

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

        assertNotNull("Should be criteria", criteria);
        assertNull("Should not have set from time",
            criteria.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", to.getTime() / 1000,
            criteria.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Set up for test
     */
    @Before
    public void setup() {
        marshaller = new DateSearchCriterionMarshaller();
    }

    /**
     * Check supports for right type
     */
    @Test
    public void supports() {
        assertTrue("Should support DateSearchCriterion",
            marshaller.supports(DateSearchCriterion.class));
        assertFalse("Shouldn't support NumberSearchCriterion",
            marshaller.supports(NumberSearchCriterion.class));
    }
}
