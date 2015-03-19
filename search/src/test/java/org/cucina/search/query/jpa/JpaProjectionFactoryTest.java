package org.cucina.search.query.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.ClassUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.i18n.model.ListNode;
import org.cucina.search.query.projection.MaxProjection;
import org.cucina.search.query.projection.Projection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.cucina.search.query.projection.TranslatedPropertyProjection;
import org.cucina.search.testassist.Foo;
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
     * TranslatedMessageWithJoinProjection created.
     */
    @Test
    public void createsTranslatedProjection() {
        Projection projection = factory.create(Foo.TYPE, "name", PROJECTION_ALIAS, FOO_ALIAS, null);

        assertNotNull("must return projection", projection);
        assertTrue(projection instanceof TranslatedPropertyProjection);
        assertEquals("projection name incorrect", "name", projection.getName());
        assertEquals("projection alias incorrect", PROJECTION_ALIAS, projection.getAlias());
        assertEquals("projection alias incorrect", FOO_ALIAS, projection.getRootAlias());
    }

    /**
     * TranslatedMessageWithJoinProjection created.
     */
    @Test
    public void createsTranslatedProjectionForListNode() {
        InstanceFactory iFactory = mock(InstanceFactory.class);

        when(iFactory.getPropertyType(Foo.TYPE, "name")).thenReturn(ListNode.class.getSimpleName());

        JpaProjectionFactory pFactory = new JpaProjectionFactory(iFactory);
        Projection projection = pFactory.create(Foo.TYPE, "name", PROJECTION_ALIAS, FOO_ALIAS, null);

        assertNotNull("must return projection", projection);
        assertTrue(projection instanceof TranslatedPropertyProjection);
        assertEquals("projection name incorrect", "name.label.messageCd", projection.getName());
        assertEquals("projection alias incorrect", PROJECTION_ALIAS, projection.getAlias());
        assertEquals("projection alias incorrect", FOO_ALIAS, projection.getRootAlias());
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
