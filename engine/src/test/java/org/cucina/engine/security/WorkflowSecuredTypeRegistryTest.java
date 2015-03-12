package org.cucina.engine.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.cucina.core.InstanceFactory;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.testassist.Bar;
import org.cucina.engine.testassist.Baz;
import org.cucina.engine.testassist.Foo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowSecuredTypeRegistryTest {
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private ProcessEnvironment workflowEnvironment;

    /**
    * Sets up for test.
    */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testListSecureProperties() {
        WorkflowSecuredTypeRegistry securedTypeRegistry = new WorkflowSecuredTypeRegistry();

        when(workflowEnvironment.listWorkflows()).thenReturn(Collections.singletonList(Bar.TYPE));
        when(instanceFactory.<Bar>getClassType(Bar.TYPE)).thenReturn(Bar.class);
        when(instanceFactory.getPropertyType(Bar.TYPE, "baz")).thenReturn(Baz.TYPE);
        when(instanceFactory.isForeignKey(Bar.TYPE, "baz")).thenReturn(true);
        when(instanceFactory.getPropertyType(Bar.TYPE, "foo")).thenReturn(Foo.TYPE);
        when(instanceFactory.isForeignKey(Bar.TYPE, "foo")).thenReturn(true);

        securedTypeRegistry.setInstanceFactory(instanceFactory);
        securedTypeRegistry.setWorkflowEnvironment(workflowEnvironment);

        Collection<String> securableProps = securedTypeRegistry.listSecurableProperties();

        assertEquals(2, securableProps.size());
        assertTrue(securableProps.containsAll(Arrays.asList("foo.Foo", "baz.Baz")));
        verify(workflowEnvironment).listWorkflows();
        verify(instanceFactory).getClassType(Bar.TYPE);
        verify(instanceFactory).getPropertyType(Bar.TYPE, "baz");
        verify(instanceFactory).isForeignKey("Bar", "baz");
        verify(instanceFactory).getPropertyType(Bar.TYPE, "foo");
        verify(instanceFactory).isForeignKey("Bar", "foo");
    }

    /**
     * Test that we only return properties for the types specified by
     * securableTypes
     */
    @Test
    public void testSecurableProvided() {
        WorkflowSecuredTypeRegistry securedTypeRegistry = new WorkflowSecuredTypeRegistry();

        securedTypeRegistry.setSecurableTypes(Collections.singletonList(Baz.TYPE));

        when(workflowEnvironment.listWorkflows()).thenReturn(Collections.singletonList(Bar.TYPE));
        when(instanceFactory.<Bar>getClassType(Bar.TYPE)).thenReturn(Bar.class);
        when(instanceFactory.getPropertyType(Bar.TYPE, "baz")).thenReturn(Baz.TYPE);
        when(instanceFactory.isForeignKey(Bar.TYPE, "baz")).thenReturn(true);
        when(instanceFactory.getPropertyType(Bar.TYPE, "foo")).thenReturn(Foo.TYPE);

        securedTypeRegistry.setInstanceFactory(instanceFactory);
        securedTypeRegistry.setWorkflowEnvironment(workflowEnvironment);

        Collection<String> securableProps = securedTypeRegistry.listSecurableProperties();

        assertEquals(1, securableProps.size());
        assertTrue(securableProps.containsAll(Arrays.asList("baz.Baz")));
        verify(workflowEnvironment).listWorkflows();
        verify(instanceFactory).getClassType(Bar.TYPE);
        verify(instanceFactory).getPropertyType(Bar.TYPE, "baz");
        verify(instanceFactory).isForeignKey("Bar", "baz");
        verify(instanceFactory).getPropertyType(Bar.TYPE, "foo");
    }
}
