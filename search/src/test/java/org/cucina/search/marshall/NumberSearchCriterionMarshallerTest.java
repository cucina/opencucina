
package org.cucina.search.marshall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.cucina.search.query.criterion.DateSearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.junit.Before;
import org.junit.Test;


/**
 * Test that NumberSearchCriterionMarshaller functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NumberSearchCriterionMarshallerTest {
    private static final String NAME = "name";
    private static final String ALIAS = "alias";
    private static final String ROOT_ALIAS = "root";
    private static final Long FROM = 12L;
    private static final Long TO = 14L;
    private NumberSearchCriterionMarshaller marshaller;

    /**
     * Expects alias
     */
    @Test(expected = IllegalArgumentException.class)
    public void checksAlias() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        NumberSearchCriterion criterion = new NumberSearchCriterion(NAME, null, ROOT_ALIAS, FROM, TO);

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
        NumberSearchCriterion criterion = new NumberSearchCriterion(NAME, null, ROOT_ALIAS, FROM, TO);

        marshaller.marshall(ALIAS, null, criterion, null);
    }

    /**
     * Marshalls from and to
     */
    @Test
    public void marshallsCollection() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        NumberSearchCriterion criterion = new NumberSearchCriterion(NAME, null, ROOT_ALIAS, FROM, TO);

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        @SuppressWarnings("unchecked")
        Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

        assertEquals("Should have set from time", FROM,
            criteria.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", TO,
            criteria.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Marshalls from and to
     */
    @Test
    public void marshallsFromAndTo() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        NumberSearchCriterion criterion = new NumberSearchCriterion(NAME, null, ROOT_ALIAS, FROM, TO);

        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        @SuppressWarnings("unchecked")
        Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

        assertEquals("Should have set from time", FROM,
            criteria.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", TO,
            criteria.get(SearchCriterionMarshaller.TO_PROPERTY));
    }

    /**
     * Marshalls from and to
     */
    @Test
    public void marshallsNotCollection() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        NumberSearchCriterion criterion = new NumberSearchCriterion(NAME, null, ROOT_ALIAS, FROM, TO);

        criterion.setBooleanNot(true);
        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        @SuppressWarnings("unchecked")
        Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

        assertEquals("Should have set from time", FROM,
            criteria.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", TO,
            criteria.get(SearchCriterionMarshaller.TO_PROPERTY));
        assertEquals("Should have negated search", true,
            criteria.get(SearchCriterionMarshaller.NOT_PROPERTY));
    }

    /**
     * Marshalls from and to
     */
    @Test
    public void marshallsNotFromAndTo() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        NumberSearchCriterion criterion = new NumberSearchCriterion(NAME, null, ROOT_ALIAS, FROM, TO);

        criterion.setBooleanNot(true);
        marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

        @SuppressWarnings("unchecked")
        Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

        assertEquals("Should have set from time", FROM,
            criteria.get(SearchCriterionMarshaller.FROM_PROPERTY));
        assertEquals("Should have set to time", TO,
            criteria.get(SearchCriterionMarshaller.TO_PROPERTY));
        assertEquals("Should have negated search", true,
            criteria.get(SearchCriterionMarshaller.NOT_PROPERTY));
    }

    /**
     * Set up for test
     */
    @Before
    public void setup() {
        marshaller = new NumberSearchCriterionMarshaller();
    }

    /**
     * Check supports for right type
     */
    @Test
    public void supports() {
        assertTrue("Should support NumberSearchCriterion",
            marshaller.supports(NumberSearchCriterion.class));
        assertFalse("Shouldn't support DateSearchCriterion",
            marshaller.supports(DateSearchCriterion.class));
    }
}
