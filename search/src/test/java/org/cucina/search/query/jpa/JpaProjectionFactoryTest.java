package org.cucina.search.query.jpa;

import org.apache.commons.lang3.ClassUtils;

import org.cucina.core.PackageBasedInstanceFactory;

import org.cucina.search.query.projection.MaxProjection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.cucina.search.testassist.Foo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests JpaProjectionFactory functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JpaProjectionFactoryTest {
    private static final String PROJECTION_NAME = "value";
    private static final String PROJECTION_ALIAS = "alias";
    private static final String FOO_ALIAS = "foo";
    private JpaProjectionFactory factory;
    private PackageBasedInstanceFactory instanceFactory;

    /**
     * MaxProjection created.
     */
    @Test
    public void createsMaxProjection() {
        MaxProjection projection = (MaxProjection) factory.create(Foo.TYPE, PROJECTION_NAME,
                PROJECTION_ALIAS, FOO_ALIAS, "max");

        assertNotNull("must return projection", projection);
        assertEquals("projection name incorrect", PROJECTION_NAME, projection.getName());
        assertEquals("projection alias incorrect", PROJECTION_ALIAS, projection.getAlias());
        assertEquals("projection alias incorrect", FOO_ALIAS, projection.getRootAlias());
    }

    /**
     * SimplePropertyProjection created.
     */
    @Test
    public void createsSimpleProjection() {
        SimplePropertyProjection projection = (SimplePropertyProjection) factory.create(Foo.TYPE,
                PROJECTION_NAME, PROJECTION_ALIAS, FOO_ALIAS, null);

        assertNotNull("must return projection", projection);
        assertEquals("projection name incorrect", PROJECTION_NAME, projection.getName());
        assertEquals("projection alias incorrect", PROJECTION_ALIAS, projection.getAlias());
        assertEquals("projection name incorrect", PROJECTION_NAME, projection.getName());
    }

    /**
     * Sets up for test.
     */
    @Before
    public void setup() {
        instanceFactory = new PackageBasedInstanceFactory(ClassUtils.getPackageName(Foo.class));
        factory = new JpaProjectionFactory(instanceFactory);
    }
}
