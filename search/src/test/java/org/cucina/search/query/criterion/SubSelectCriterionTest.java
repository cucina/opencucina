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
 * Tests SubSelectSearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SubSelectCriterionTest {
    private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.SubSelectCriterion\",\"name\":\"name\"," +
        "\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"subSelect\":\"select name from Foo\"}";
    private static final String PROPERTY = "name";
    private static final String SUBSELECT = "select name from Foo";
    private static final String ROOT_ALIAS = "root";
    private static final String PARENT_ALIAS = "parent";
    private Marshaller marshaller;
    private SubSelectCriterion criterion;

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
        criterion = new SubSelectCriterion(PROPERTY, ROOT_ALIAS, SUBSELECT);
        criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
        marshaller = new JacksonMarshaller(null, null);
    }

    /**
     * alias is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoAliasSupplied() {
        new TextSearchCriterion(PROPERTY, null, null, SUBSELECT);
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
        new TextSearchCriterion(null, null, ROOT_ALIAS, SUBSELECT);
    }

    /**
     * Tests restriction is correct
     */
    @Test
    public void testNotRestriction() {
        criterion.setBooleanNot(true);
        assertEquals("Incorrect restriction",
            NameUtils.concat(PARENT_ALIAS, PROPERTY) + " != (" + SUBSELECT + ")",
            criterion.getRestriction());
    }

    /**
     * Tests restriction is correct
     */
    @Test
    public void testRestriction() {
        assertEquals("Incorrect restriction",
            NameUtils.concat(PARENT_ALIAS, PROPERTY) + " = (" + SUBSELECT + ")",
            criterion.getRestriction());
    }

    /**
     * Tests property is nested two levels, should return final nested level as alias and
     * property
     */
    @Test
    public void testSearchPropertyNameNestedTwoLevels() {
        TextSearchCriterion localCriterion = new TextSearchCriterion("grandParent.parent." +
                PROPERTY, null, ROOT_ALIAS, SUBSELECT);

        localCriterion.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

        assertEquals("Should return just root", NameUtils.concat("parent", PROPERTY) + " like ?",
            localCriterion.getRestriction());
    }

    /**
     * Tests property is at root level
     */
    @Test
    public void testSearchPropertyNameRootLevel() {
        TextSearchCriterion localCriterion = new TextSearchCriterion(PROPERTY, null, ROOT_ALIAS,
                SUBSELECT);

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
                ROOT_ALIAS, SUBSELECT);

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
        assertEquals("Incorrect number of values", 0, values.size());
    }

    /**
     * unmarshalls values correctly
     */
    @Test
    public void unmarshalls() {
        SubSelectCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
                SubSelectCriterion.class);

        assertNotNull("Should have returned projection", unmarshalledCrit);
        assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
        assertEquals("Should have set text", SUBSELECT, unmarshalledCrit.getSubSelect());
        assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
    }
}
