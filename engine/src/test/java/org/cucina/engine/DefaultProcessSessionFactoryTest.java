
package org.cucina.engine;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.testadapters.MockProcessDefinitionBuilder;
import org.cucina.engine.testadapters.StandardOutputWorkflowListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * @author robh
 * @author vlevine
 */
public class DefaultProcessSessionFactoryTest {
    @Mock
    private ProcessDriverFactory executorFactory;
    @Mock
    private TokenFactory tokenFactory;
    @Mock
    private ProcessDriver executor;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executorFactory.getExecutor()).thenReturn(executor);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testOpenSessionWithDefinitionName() {
        ProcessDefinitionRegistry definitionRegistry = mock(ProcessDefinitionRegistry.class);

        DefaultProcessSessionFactory sessionFactory = new DefaultProcessSessionFactory(definitionRegistry,
                Collections.singletonList((WorkflowListener) new StandardOutputWorkflowListener()),
                executorFactory);

        ProcessDefinition workflowDefinition = MockProcessDefinitionBuilder.buildChooseADrinkDefinition();
        ProcessSession session = sessionFactory.openSession(workflowDefinition);

        assertNotNull(session);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testOpenSessionWithSuppliedDefinition() {
        ProcessDefinitionRegistry definitionRegistry = mock(ProcessDefinitionRegistry.class);
        ProcessDefinition workflowDefinition = MockProcessDefinitionBuilder.buildChooseADrinkDefinition();

        when(definitionRegistry.findWorkflowDefinition(workflowDefinition.getId()))
            .thenReturn(workflowDefinition);

        DefaultProcessSessionFactory sessionFactory = new DefaultProcessSessionFactory(definitionRegistry,
                Collections.singletonList((WorkflowListener) new StandardOutputWorkflowListener()),
                executorFactory);

        ProcessSession session = sessionFactory.openSession(workflowDefinition.getId());

        assertNotNull(session);
    }
}
