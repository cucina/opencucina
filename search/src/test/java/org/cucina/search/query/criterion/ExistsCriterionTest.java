package org.cucina.search.query.criterion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests ExistsCriterion functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ExistsCriterionTest {
    private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.ExistsCriterion\"," +
        "\"name\":\"default\",\"rootAlias\":\"root\",\"alias\":\"default\",\"booleanNot\":false,\"exists\":\"select 1 from Foo where foo.id = 1 and outer.foo = foo.id\"}";
    private static final String EXISTS = "select 1 from Foo where foo.id = 1 and outer.foo = foo.id";
    private static final String ROOT_ALIAS = "root";
    private ExistsCriterion criterion;
    private Marshaller marshaller;

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
        criterion = new ExistsCriterion(ROOT_ALIAS, EXISTS);
        marshaller = new JacksonMarshaller(null, null);
    }

    /**
     * exists is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoExistsSupplied() {
        new ExistsCriterion(ROOT_ALIAS, null);
    }

    /**
     * alias is required
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoRootAliasSupplied() {
        new ExistsCriterion(null, EXISTS);
    }

    /**
     * Tests restriction is correct
     */
    @Test
    public void testRestriction() {
        assertEquals("Incorrect restriction", " exists (" + EXISTS + ")", criterion.getRestriction());
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
        ExistsCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
                ExistsCriterion.class);

        assertNotNull("Should have returned projection", unmarshalledCrit);
        assertEquals("Should have set text", EXISTS, unmarshalledCrit.getExists());
        assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
    }
}
