
package org.cucina.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompositeInstanceFactoryTest {
    private CompositeInstanceFactory instanceFactory;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void before()
        throws Exception {
        LoggingEnabler.enableLog(CompositeInstanceFactory.class);
        instanceFactory = new CompositeInstanceFactory();

        Collection<String> i18nProperties = new HashSet<String>();

        i18nProperties.add("Foo.name");

        InstanceFactory if1 = mock(InstanceFactory.class);

        when(if1.isI18nProperty("Foo", "name")).thenReturn(true);
        when(if1.getPropertyType("Foo", "bla")).thenReturn("Bla");

        Collection<String> blaProperties = new HashSet<String>();

        blaProperties.add("Bla.name");

        InstanceFactory if2 = mock(InstanceFactory.class);

        when(if1.getPropertyType("Bla", "name")).thenReturn("BlaNameType");

        List<InstanceFactory> instanceFactories = new ArrayList<InstanceFactory>();

        instanceFactories.add(mock(InstanceFactory.class));
        instanceFactories.add(if1);
        instanceFactories.add(if2);
        instanceFactory.setInstanceFactories(instanceFactories);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void classType() {
        assertNull("Made up type", instanceFactory.getClassType("MadeUpType"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBean() {
        assertNull(instanceFactory.getBean("User"));
        assertNull(instanceFactory.getBean("String"));
    }

    /**
     * Test nested properties still return the correct type from different instance factories.
     */
    @Test
    public void testGetNestedProperty() {
        assertEquals("BlaNameType", instanceFactory.getPropertyType("Foo", "bla.name"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetPropertyTypeString() {
        assertNull(instanceFactory.getPropertyType("User", "password"));
        assertNull(instanceFactory.getPropertyType("Foo", "xxx"));
    }

    /**
     * Checks all instanceFactories that property is i18n
     */
    @Test
    public void testInternationalised() {
        assertTrue("Should be internationalised", instanceFactory.isI18nProperty("Foo", "name"));
    }

    /**
     * Checks all instanceFactories that property is not i18n
     */
    @Test
    public void testNotInternationalised() {
        assertFalse("Should not be internationalised", instanceFactory.isI18nProperty("Foo", "fred"));
    }
}
