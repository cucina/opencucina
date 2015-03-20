package org.cucina.engine.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.testadapters.MockWorkflowDefinitionBuilder;
import org.junit.Before;
import org.junit.Test;


/**
 * Test WorkflowDefinitionHelperImpl functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowDefinitionHelperImplTest {
    private ProcessDefinitionHelperImpl helper;

    /**
     * Sets up for test
     */
    @Before
    public void onsetup() {
        helper = new ProcessDefinitionHelperImpl(mock(ProcessDefinitionRegistry.class));
    }

    /**
     * Requires transitionId
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresTransitionId() {
        helper.findPlaceId(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID, null);
    }

    /**
     * Requires workflowId
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresWorkflowId() {
        helper.findPlaceId(null, "rabbitch");
    }

    /**
     * Test transitionId non existent so fails to find place.
     */
    @Test(expected = TransitionNotFoundException.class)
    public void testFailsToFindId()
        throws Exception {
        ProcessDefinition definition = MockWorkflowDefinitionBuilder.buildHelloWorldDefinition();

        ProcessDefinitionRegistry registry = mock(ProcessDefinitionRegistry.class);

        when(registry.findWorkflowDefinition(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID))
            .thenReturn(definition);

        helper = new ProcessDefinitionHelperImpl(registry);
        helper.findPlaceId(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID, "rabbitch");
    }

    /**
     * Test finds placeId
     */
    @Test
    public void testFindsId()
        throws Exception {
        ProcessDefinition definition = MockWorkflowDefinitionBuilder.buildHelloWorldDefinition();

        ProcessDefinitionRegistry registry = mock(ProcessDefinitionRegistry.class);

        when(registry.findWorkflowDefinition(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID))
            .thenReturn(definition);

        helper = new ProcessDefinitionHelperImpl(registry);

        String placeId = helper.findPlaceId(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID, "toEnd");

        assertEquals("placeId is not as expected", "helloWorld", placeId);
    }

    /**
     * Test fails with IllegalArgumentException if workflow doesn't exist
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWorkflowId()
        throws Exception {
        ProcessDefinitionRegistry registry = mock(ProcessDefinitionRegistry.class);

        when(registry.findWorkflowDefinition(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID))
            .thenReturn(null);

        helper = new ProcessDefinitionHelperImpl(registry);
        helper.findPlaceId(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID, "toEnd");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void workflowEnded() {
        ProcessDefinition definition = MockWorkflowDefinitionBuilder.buildHelloWorldDefinition();

        ProcessDefinitionRegistry registry = mock(ProcessDefinitionRegistry.class);

        when(registry.findWorkflowDefinition(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID))
            .thenReturn(definition);

        helper = new ProcessDefinitionHelperImpl(registry);

        Token token = mock(Token.class);

        when(token.getProcessDefinitionId())
            .thenReturn(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID);
        when(token.getPlaceId()).thenReturn("end");

        assertTrue("workflow is not ended", helper.isEnded(token));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void workflowNotEnded() {
        ProcessDefinition definition = MockWorkflowDefinitionBuilder.buildHelloWorldDefinition();

        ProcessDefinitionRegistry registry = mock(ProcessDefinitionRegistry.class);

        when(registry.findWorkflowDefinition(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID))
            .thenReturn(definition);

        helper = new ProcessDefinitionHelperImpl(registry);

        Token token = mock(Token.class);

        when(token.getProcessDefinitionId())
            .thenReturn(MockWorkflowDefinitionBuilder.HELLO_WORLD_ID);
        when(token.getPlaceId()).thenReturn("helloWorld");

        assertFalse("workflow is ended", helper.isEnded(token));
    }
}
