package org.cucina.search.query.projection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.utils.NameUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Test that <code>CountProjection</code> functions as expected.
 *
 */
public class CountProjectionTest {
    private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.projection.CountProjection\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"the_count\"}";
    private static final String PROPERTY = "name";
    private static final String ROOT_ALIAS = "root";
    private static final String ALIAS = "the_count";
    private CountProjection projection;
    private Marshaller marshaller;

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void marshalls() {
        assertEquals("Should have marshalled correctly", MARSHALLED_PROJECTION,
            marshaller.marshall(projection));
    }

    /**
     * Set up as expected.
     */
    @Before
    public void onSetup() {
        projection = new CountProjection(PROPERTY, ALIAS, ROOT_ALIAS);
        projection.setParentAliases(Collections.singletonMap(ROOT_ALIAS, "fred"));
        marshaller = new JacksonMarshaller(null, null);
    }

    /**
     * Test requires property
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresProperty() {
        new CountProjection(null, ALIAS, ROOT_ALIAS);
    }

    /**
     * Tests that the projection is an aggregate
     */
    @Test
    public void testIsAggregate() {
        assertTrue("Should be aggregate", projection.isAggregate());
    }

    /**
     * Tests that the projection shouldn't be listed in group by
     */
    @Test
    public void testIsGroupable() {
        assertFalse("Should not be groupable", projection.isGroupable());
    }

    /**
     * Test projection
     */
    @Test
    public void testProjection() {
        assertEquals("Should return just root",
            CountProjection.COUNT + "(" + NameUtils.concat("fred", PROPERTY) + ") as " + ALIAS,
            projection.getProjection());
    }

    /**
     * Tests property is nested two levels, should return final nested level as
     * alias and property
     */
    @Test
    public void testSearchPropertyNameNestedTwoLevels() {
        CountProjection LocalProjection = new CountProjection("grandParent.parent." + PROPERTY,
                ALIAS, ROOT_ALIAS);

        LocalProjection.setParentAliases(Collections.singletonMap("parent", "fred"));

        assertEquals("Should return just root",
            CountProjection.COUNT + "(" + NameUtils.concat("fred", PROPERTY) + ") as " + ALIAS,
            LocalProjection.getProjection());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void unmarshalls() {
        CountProjection unmarshalledProj = marshaller.unmarshall(MARSHALLED_PROJECTION,
                CountProjection.class);

        assertNotNull("Should have returned projection", unmarshalledProj);
        assertEquals("Should have set name", PROPERTY, unmarshalledProj.getName());
        assertEquals("Should have set alias", ALIAS, unmarshalledProj.getAlias());
        assertEquals("Should have set name", ROOT_ALIAS, unmarshalledProj.getRootAlias());
    }
}
