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
 * Test MaxProjection functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MaxProjectionTest {
    private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.projection.MaxProjection\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"myName\"}";
    private static final String PROPERTY = "name";
    private static final String ALIAS = "myName";
    private static final String ROOT_ALIAS = "root";
    private static final String PARENT_ALIAS = "parent";
    private Marshaller marshaller;
    private MaxProjection projection;

    /**
     * Marshalls correctly
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
        projection = new MaxProjection(PROPERTY, ALIAS, ROOT_ALIAS);
        projection.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
        marshaller = new JacksonMarshaller(null, null);
    }

    /**
     * Test requires property
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresProperty() {
        new MaxProjection(null, ALIAS, ROOT_ALIAS);
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
            "max(" + NameUtils.concat(PARENT_ALIAS, PROPERTY) + ") as " + ALIAS,
            projection.getProjection());
    }

    /**
     * Unmarshalls correctly
     */
    @Test
    public void unmarshalls() {
        MaxProjection unmarshalledProj = marshaller.unmarshall(MARSHALLED_PROJECTION,
                MaxProjection.class);

        assertNotNull("Should have returned projection", unmarshalledProj);
        assertEquals("Should have set name", PROPERTY, unmarshalledProj.getName());
        assertEquals("Should have set name", ALIAS, unmarshalledProj.getAlias());
        assertEquals("Should have set name", ROOT_ALIAS, unmarshalledProj.getRootAlias());
    }
}
