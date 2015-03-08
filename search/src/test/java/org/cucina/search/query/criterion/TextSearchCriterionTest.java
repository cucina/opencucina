package org.cucina.search.query.criterion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.utils.NameUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests TextSearchCriterion functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TextSearchCriterionTest {
    private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.TextSearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"value\":\"hi\"}";
    private static final String WILDCARD = "%";
    private static final String PROPERTY = "name";
    private static final String TEXT = "hi";
    private static final String ROOT_ALIAS = "root";
    private static final String PARENT_ALIAS = "parent";
    private Marshaller marshaller;
    private TextSearchCriterion criterion;

    /**
     * Tests marshalls values correctly
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
        criterion = new TextSearchCriterion(PROPERTY, null, ROOT_ALIAS, TEXT);
        criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
        marshaller = new JacksonMarshaller(null, null);
    }

    /**
     * alias is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoAliasSupplied() {
        new TextSearchCriterion(PROPERTY, null, null, TEXT);
    }

    /**
     * match is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoMatchSupplied() {
        new TextSearchCriterion(PROPERTY, null, ROOT_ALIAS, null);
    }

    /**
     * property is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoPropertySupplied() {
        new TextSearchCriterion(null, null, ROOT_ALIAS, TEXT);
    }

    /**
     * Tests restriction is correct
     */
    @Test
    public void testRestriction() {
        assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " like ?",
            criterion.getRestriction());
    }

    /**
     * Ok, this one applies to an object rather than String
     */
    @Test
    public void testSearchPropertyNameIsAlias() {
        TextSearchCriterion localCriterion = new TextSearchCriterion(PARENT_ALIAS, null,
                ROOT_ALIAS, TEXT);

        localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        assertEquals("Should return just parent", PARENT_ALIAS + " like ?",
            localCriterion.getRestriction());
    }

    /**
     * Tests property is nested two levels, should return final nested level as alias and
     * property
     */
    @Test
    public void testSearchPropertyNameNestedTwoLevels() {
        TextSearchCriterion localCriterion = new TextSearchCriterion("grandParent.parent." +
                PROPERTY, null, ROOT_ALIAS, TEXT);

        localCriterion.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

        assertEquals("Should return just parent",
            NameUtils.concat(PARENT_ALIAS, PROPERTY) + " like ?", localCriterion.getRestriction());
    }

    /**
     * Tests property is at root level
     */
    @Test
    public void testSearchPropertyNameRootLevel() {
        TextSearchCriterion localCriterion = new TextSearchCriterion(PROPERTY, null, ROOT_ALIAS,
                TEXT);

        localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

        assertEquals("Should return just root",
            NameUtils.concat(PARENT_ALIAS, PROPERTY) + " like ?", localCriterion.getRestriction());
    }

    /**
     * Tests property is nested one level, should return property provided
     */
    @Test
    public void testSearchPropertyNestedOneLevels() {
        TextSearchCriterion localCriterion = new TextSearchCriterion("parent." + PROPERTY, null,
                ROOT_ALIAS, TEXT);

        localCriterion.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

        assertEquals("Should return just root",
            NameUtils.concat(PARENT_ALIAS, PROPERTY) + " like ?", localCriterion.getRestriction());
    }

    /**
     * Test values returned are as expected
     */
    @Test
    public void testValues() {
        List<Object> values = criterion.getValues();

        assertNotNull("values cannot be null", values);
        assertEquals("Incorrect number of values", 1, values.size());
        assertEquals("Incorrect value", WILDCARD + TEXT + WILDCARD, values.get(0));
    }

    /**
     * Test values returned are as expected when exact match required
     */
    @Test
    public void testValuesExactMatch() {
        criterion.setExact(true);

        List<Object> values = criterion.getValues();

        assertNotNull("values cannot be null", values);
        assertEquals("Incorrect number of values", 1, values.size());
        assertEquals("Incorrect value", TEXT, values.get(0));
    }

    /**
     * unmarshalls values correctly
     */
    @Test
    public void unmarshalls() {
        TextSearchCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
                TextSearchCriterion.class);

        assertNotNull("Should have returned projection", unmarshalledCrit);
        assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
        assertEquals("Should have set text", TEXT, unmarshalledCrit.getValue());
        assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
    }
}
