package org.cucina.security.service;

import java.util.Collection;
import java.util.Collections;

import org.cucina.core.InstanceFactory;
import org.cucina.security.testassist.Foo;
import org.cucina.security.access.AbstractSecuredTypeRegistry;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * Silly name to fool default junit exclude filter
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class XAbstractSecuredTypeRegistryTest {
    @Mock
    private InstanceFactory instanceFactory;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testListSecurableProperties() {
        AbstractSecuredTypeRegistry registry = new AbstractSecuredTypeRegistry() {
                @Override
                public Collection<String> listSecuredTypes() {
                    return Collections.singleton("Foo");
                }
            };

        when(instanceFactory.<Foo>getClassType("Foo")).thenReturn(Foo.class);
        when(instanceFactory.getPropertyType("Foo", "message")).thenReturn("Message");
        when(instanceFactory.isForeignKey("Foo", "message")).thenReturn(true);

        registry.setInstanceFactory(instanceFactory);

        Collection<String> properties = registry.listSecurableProperties();

        assertTrue("Should contain message.Message", properties.contains("message.Message"));
    }
}
