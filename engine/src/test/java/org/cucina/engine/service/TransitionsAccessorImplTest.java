package org.cucina.engine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.testassist.Foo;

import org.cucina.security.access.AccessManager;
import org.cucina.security.access.AccessRegistry;
import org.cucina.security.model.Privilege;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TransitionsAccessorImplTest {
    @Mock
    private AccessManager<UserDetails> accessManager;
    @Mock
    private AccessRegistry accessRegistry;
    @Mock
    private ProcessDefinitionRegistry definitionRegistry;
    @Mock
    private ProcessEnvironment workflowEnvironment;
    private TransitionsAccessorImpl accessor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        accessor = new TransitionsAccessorImpl(accessRegistry, accessManager, workflowEnvironment);
    }

    /**
     * Test that if User has permission they see the secured transition
     */
    @Test
    public void testListPermittedTransitionsHasPermission() {
        Map<String, Object> propertyValues = new HashMap<String, Object>();

        when(accessManager.hasPermission("myPriv", "wflwDfn", propertyValues)).thenReturn(true);
        when(accessManager.hasPermission("yourPriv", "wflwDfn", propertyValues)).thenReturn(false);

        Privilege defaultPriv = new Privilege();

        defaultPriv.setName("DEF");
        when(accessRegistry.getDefaultPrivilege()).thenReturn(defaultPriv);

        final State place = mock(State.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("toEnd");
        transition.setPrivilegeName("yourPriv,myPriv");
        transes.add(transition);
        when(place.getAllTransitions()).thenReturn(transes);

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State findPlace(String placeId) {
                    return place;
                }
            };

        when(definitionRegistry.findWorkflowDefinition("wflwDfn")).thenReturn(definition);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        Collection<String> transitionIds = accessor.listPermittedTransitions("wflwDfn",
                "helloWorld", "wflwDfn", propertyValues);

        assertNotNull("Should have returned transitions", transitionIds);
        assertEquals("Incorrect number transitions", 1, transitionIds.size());
        assertTrue("Should contain toEnd", transitionIds.contains("toEnd"));
    }

    /**
     * Test that if User has permission they see the secured transition
     */
    @Test
    public void testListPermittedTransitionsHasPermissionNoConfiguredPrivilege() {
        Map<String, Object> propertyValues = new HashMap<String, Object>();

        when(accessManager.hasPermission("DEF", "wflwDfn", propertyValues)).thenReturn(true);

        Privilege defaultPriv = new Privilege();

        defaultPriv.setName("DEF");
        when(accessRegistry.getDefaultPrivilege()).thenReturn(defaultPriv);

        final State place = mock(State.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("toEnd");
        transes.add(transition);
        when(place.getAllTransitions()).thenReturn(transes);

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State findPlace(String placeId) {
                    return place;
                }
            };

        when(definitionRegistry.findWorkflowDefinition("wflwDfn")).thenReturn(definition);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        Collection<String> transitionIds = accessor.listPermittedTransitions("wflwDfn",
                "helloWorld", "wflwDfn", propertyValues);

        assertNotNull("Should have returned transitions", transitionIds);
        assertEquals("Incorrect number transitions", 1, transitionIds.size());
        assertTrue("Should contain toEnd", transitionIds.contains("toEnd"));
    }

    /**
     * Test that if User has permission they see the secured transition
     */
    @Test
    public void testListPermittedTransitionsMulipleTransitions() {
        Map<String, Object> propertyValues = new HashMap<String, Object>();

        when(accessManager.hasPermission("myPriv", "wflwDfn", propertyValues)).thenReturn(true);
        when(accessManager.hasPermission("yourPriv", "wflwDfn", propertyValues)).thenReturn(false);
        when(accessManager.hasPermission("DEF", "wflwDfn", propertyValues)).thenReturn(true);

        Privilege defaultPriv = new Privilege();

        defaultPriv.setName("DEF");
        when(accessRegistry.getDefaultPrivilege()).thenReturn(defaultPriv);

        final State place = mock(State.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("toEnd");
        transition.setPrivilegeName("yourPriv,myPriv");

        Transition transition2 = new Transition();

        transition2.setId("toBeginning");

        Transition transition3 = new Transition();

        transition3.setId("toSomewhere");
        transition3.setPrivilegeName("yourPriv");

        transes.add(transition);
        transes.add(transition2);
        transes.add(transition3);

        when(place.getAllTransitions()).thenReturn(transes);

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State findPlace(String placeId) {
                    return place;
                }
            };

        when(definitionRegistry.findWorkflowDefinition("wflwDfn")).thenReturn(definition);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        Collection<String> transitionIds = accessor.listPermittedTransitions("wflwDfn",
                "helloWorld", "wflwDfn", propertyValues);

        assertNotNull("Should have returned transitions", transitionIds);
        assertEquals("Incorrect number transitions", 2, transitionIds.size());
        assertTrue("Should contain toEnd", transitionIds.contains("toEnd"));
        assertTrue("Should contain toBeginning", transitionIds.contains("toBeginning"));
    }

    /**
     * Test that if User has permission they see the secured transition
     */
    @Test
    public void testListPermittedTransitionsNoObjectCheckHasPermission() {
        when(accessManager.hasPrivilege("myPriv")).thenReturn(true);
        when(accessManager.hasPrivilege("yourPriv")).thenReturn(false);

        final State place = mock(State.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("toEnd");
        transition.setPrivilegeName("yourPriv,myPriv");

        Transition transition2 = new Transition();

        transition2.setId("toB3g");
        transes.add(transition);
        transes.add(transition2);
        when(place.getAllTransitions()).thenReturn(transes);

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State findPlace(String placeId) {
                    return place;
                }
            };

        when(definitionRegistry.findWorkflowDefinition("wflwDfn")).thenReturn(definition);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        Collection<String> transitionIds = accessor.listPermittedTransitionsNoObjectCheck("wflwDfn",
                "helloWorld");

        assertNotNull("Should have returned transitions", transitionIds);
        assertEquals("Incorrect number transitions", 2, transitionIds.size());
        assertTrue("Should contain toEnd", transitionIds.contains("toEnd"));
        assertTrue("Should contain toEnd", transitionIds.contains("toB3g"));
    }

    /**
     * Test that if User doesn't have permission they don't see the secured
     * transition
     */
    @Test
    public void testListPermittedTransitionsNoObjectCheckNoPermission() {
        when(accessManager.hasPrivilege("myPriv")).thenReturn(false);
        when(accessManager.hasPrivilege("yourPriv")).thenReturn(false);

        final State place = mock(State.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("toEnd");
        transition.setPrivilegeName("yourPriv,myPriv");
        transes.add(transition);
        when(place.getAllTransitions()).thenReturn(transes);

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State findPlace(String placeId) {
                    return place;
                }
            };

        when(definitionRegistry.findWorkflowDefinition("wflwDfn")).thenReturn(definition);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        Collection<String> transitionIds = accessor.listPermittedTransitionsNoObjectCheck("wflwDfn",
                "helloWorld");

        assertNotNull("Should have returned transitions", transitionIds);
        assertEquals("Incorrect number transitions", 0, transitionIds.size());
    }

    /**
     * Test that if User doesn't have permission they don't see the secured
     * transition
     */
    @Test
    public void testListPermittedTransitionsNoPermission() {
        Map<String, Object> propertyValues = new HashMap<String, Object>();

        when(accessManager.hasPermission("myPriv", "wflwDfn", propertyValues)).thenReturn(false);
        when(accessManager.hasPermission("yourPriv", "wflwDfn", propertyValues)).thenReturn(false);

        Privilege defaultPriv = new Privilege();

        defaultPriv.setName("DEF");
        when(accessRegistry.getDefaultPrivilege()).thenReturn(defaultPriv);

        final State place = mock(State.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("toEnd");
        transition.setPrivilegeName("yourPriv,myPriv");
        transes.add(transition);
        when(place.getAllTransitions()).thenReturn(transes);

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State findPlace(String placeId) {
                    return place;
                }
            };

        when(definitionRegistry.findWorkflowDefinition("wflwDfn")).thenReturn(definition);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        Collection<String> transitionIds = accessor.listPermittedTransitions("wflwDfn",
                "helloWorld", "wflwDfn", propertyValues);

        assertNotNull("Should have returned transitions", transitionIds);
        assertEquals("Incorrect number transitions", 0, transitionIds.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testListPermittedTransitionsObject() {
        Foo domainObject = new Foo();

        when(accessManager.hasPermission(eq("myPriv"), eq("Foo"),
                anyMapOf(String.class, Object.class))).thenReturn(true);
        when(accessManager.hasPermission(eq("yourPriv"), eq("Foo"),
                anyMapOf(String.class, Object.class))).thenReturn(false);
        when(accessManager.hasPermission(eq("DEF"), eq("Foo"), anyMapOf(String.class, Object.class)))
            .thenReturn(true);

        Privilege defaultPriv = new Privilege();

        defaultPriv.setName("DEF");
        when(accessRegistry.getDefaultPrivilege()).thenReturn(defaultPriv);

        final State place = mock(State.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("toEnd");
        transition.setPrivilegeName("yourPriv,myPriv");

        Transition transition2 = new Transition();

        transition2.setId("toBeginning");

        Transition transition3 = new Transition();

        transition3.setId("toSomewhere");
        transition3.setPrivilegeName("yourPriv");

        transes.add(transition);
        transes.add(transition2);
        transes.add(transition3);

        when(place.getAllTransitions()).thenReturn(transes);

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State findPlace(String placeId) {
                    return place;
                }
            };

        when(definitionRegistry.findWorkflowDefinition("wflwDfn")).thenReturn(definition);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        Collection<String> transitionIds = accessor.listPermittedTransitions("wflwDfn",
                "helloWorld", domainObject);

        assertNotNull("Should have returned transitions", transitionIds);
        assertEquals("Incorrect number transitions", 2, transitionIds.size());
        assertTrue("Should contain toEnd", transitionIds.contains("toEnd"));
        assertTrue("Should contain toBeginning", transitionIds.contains("toBeginning"));
    }
}
