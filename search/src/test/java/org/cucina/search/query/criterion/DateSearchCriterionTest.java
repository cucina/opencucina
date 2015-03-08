package org.cucina.search.query.criterion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.utils.NameUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests DateSearchCriterion functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateSearchCriterionTest {
    private static final Date FROM = new GregorianCalendar(2012, 01, 02).getTime();
    private static final Date TO = new GregorianCalendar(2012, 01, 03).getTime();
    private static final String PARENT_ALIAS = "parent";
    private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.DateSearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"from\":[\"java.util.Date\"," +
        FROM.getTime() + "],\"to\":[\"java.util.Date\"," + TO.getTime() + "]}";
    private static final String PROPERTY = "name";
    private static final String ROOT_ALIAS = "root";
    private AbstractDateSearchCriterion criterion;
    private Marshaller marshaller;

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void marshalls() {
        assertEquals("Should have marshalled correctly", MARSHALLED_PROJECTION,
            marshaller.marshall(criterion));
    }

    /**
     * Sets up test
     */
    @Before
    public void setup() {
        criterion = new DateSearchCriterion(PROPERTY, null, ROOT_ALIAS, FROM, TO);
        criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        marshaller = new JacksonMarshaller(null, null);
    }

    /**
     * alias is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoAliasSupplied() {
        new DateSearchCriterion(PROPERTY, null, null, FROM, TO);
    }

    /**
     * from or to are required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoFromOrToSupplied() {
        new DateSearchCriterion(PROPERTY, null, ROOT_ALIAS, null, null);
    }

    /**
     * property is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoPropertySupplied() {
        new DateSearchCriterion(null, null, ROOT_ALIAS, FROM, TO);
    }

    /**
     * Tests restriction is correct when from only is set
     */
    @Test
    public void testRestrictionFromSet() {
        AbstractDateSearchCriterion localCriterion = new DateSearchCriterion(PROPERTY, null,
                ROOT_ALIAS, FROM, null);

        localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " >= ? ",
            localCriterion.getRestriction());

        localCriterion.setBooleanNot(true);
        assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " < ? ",
            localCriterion.getRestriction());
    }

    /**
     * Tests restriction is correct when from and to are equal
     */
    @Test
    public void testRestrictionFromToEqualNot() {
        AbstractDateSearchCriterion localCriterion = new DateSearchCriterion(PROPERTY, null,
                ROOT_ALIAS, TO, TO);

        localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        localCriterion.setBooleanNot(true);
        assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " != ? ",
            localCriterion.getRestriction());
        assertEquals("Should only have one 'value'", 1, localCriterion.getValues().size());
    }

    /**
     * Tests restriction is correct
     */
    @Test
    public void testRestrictionFromToNot() {
        criterion.setBooleanNot(true);
        assertEquals("Incorrect restriction",
            NameUtils.concat(PARENT_ALIAS, PROPERTY) + " not between ? and ? ",
            criterion.getRestriction());
    }

    /**
     * Tests restriction is correct
     */
    @Test
    public void testRestrictionFromToSet() {
        assertEquals("Incorrect restriction",
            NameUtils.concat(PARENT_ALIAS, PROPERTY) + " between ? and ? ",
            criterion.getRestriction());
    }

    /**
     * Tests restriction is correct when from and to are equal
     */
    @Test
    public void testRestrictionFromToSetEqual() {
        AbstractDateSearchCriterion localCriterion = new DateSearchCriterion(PROPERTY, null,
                ROOT_ALIAS, TO, TO);

        localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " = ? ",
            localCriterion.getRestriction());
        assertEquals("Should only have one 'value'", 1, localCriterion.getValues().size());
    }

    /**
     * Tests restriction is correct when to only is set
     */
    @Test
    public void testRestrictionToSet() {
        AbstractDateSearchCriterion localCriterion = new DateSearchCriterion(PROPERTY, null,
                ROOT_ALIAS, null, TO);

        localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " <= ? ",
            localCriterion.getRestriction());

        localCriterion.setBooleanNot(true);
        assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " > ? ",
            localCriterion.getRestriction());
    }

    /**
     * Test values returned are as expected when only from set
     */
    @Test
    public void testValuesFromSet() {
        AbstractDateSearchCriterion localCriterion = new DateSearchCriterion(PROPERTY, null,
                ROOT_ALIAS, FROM, null);

        localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        List<Object> values = localCriterion.getValues();

        assertNotNull("values cannot be null", values);
        assertEquals("Incorrect number of values", 1, values.size());
        assertEquals("Incorrect value", FROM, values.get(0));
    }

    /**
     * Test values returned are as expected
     */
    @Test
    public void testValuesFromToSet() {
        List<Object> values = criterion.getValues();

        assertNotNull("values cannot be null", values);
        assertEquals("Incorrect number of values", 2, values.size());
        assertEquals("Incorrect value", FROM, values.get(0));
        assertEquals("Incorrect value", TO, values.get(1));
    }

    /**
     * Test values returned are as expected when only to set
     */
    @Test
    public void testValuesToSet() {
        AbstractDateSearchCriterion localCriterion = new DateSearchCriterion(PROPERTY, null,
                ROOT_ALIAS, null, TO);
        List<Object> values = localCriterion.getValues();

        assertNotNull("values cannot be null", values);
        assertEquals("Incorrect number of values", 1, values.size());
        assertEquals("Incorrect value", TO, values.get(0));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void unmarshalls() {
        DateSearchCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
                DateSearchCriterion.class);

        assertNotNull("Should have returned projection", unmarshalledCrit);
        assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
        assertEquals("Should have set from", FROM, unmarshalledCrit.getFrom());
        assertEquals("Should have set to", TO, unmarshalledCrit.getTo());
        assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
    }
}
