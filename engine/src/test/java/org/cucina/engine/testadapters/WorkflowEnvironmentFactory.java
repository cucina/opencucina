
package org.cucina.engine.testadapters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;

import org.cucina.core.spring.ELExpressionExecutor;
import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.engine.DefaultProcessEnvironment;
import org.cucina.engine.DefaultProcessSessionFactory;
import org.cucina.engine.LocalProcessDriver;
import org.cucina.engine.ProcessDriverFactory;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.ProcessSessionFactory;
import org.cucina.engine.TokenFactory;
import org.cucina.engine.WorkflowListener;
import org.cucina.engine.definition.ProcessDefinitionHelper;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.service.ProcessService;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.expression.BeanResolver;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowEnvironmentFactory {
    /**
     * JAVADOC Method Level Comments
     *
     * @param resolverRegistry JAVADOC.
     *
     * @return JAVADOC.
     */
    public static ProcessEnvironment buildEnvironment(BeanResolver beanResolver,
        ProcessDefinitionRegistry definitionRegistry, ProcessDefinitionParser definitionParser,
        ProcessService workflowService, Collection<Resource> resources)
        throws Exception {
        DefaultProcessEnvironment defaultWorkflowEnvironment = new DefaultProcessEnvironment();
        ApplicationContext applicationContext = mock(ApplicationContext.class);

        when(applicationContext.containsBean("beanResolver")).thenReturn(true);

        if (beanResolver == null) {
            beanResolver = mock(BeanResolver.class);
        }

        when(applicationContext.getBean("beanResolver")).thenReturn(beanResolver);
        when(applicationContext.containsBean("workflowDefinitionRegistry")).thenReturn(true);

        if (definitionRegistry == null) {
            definitionRegistry = mock(ProcessDefinitionRegistry.class);
        }

        when(applicationContext.getBean("workflowDefinitionRegistry")).thenReturn(definitionRegistry);
        when(applicationContext.containsBean("workflowDefinitionParser")).thenReturn(true);

        if (definitionParser == null) {
            definitionParser = mock(ProcessDefinitionParser.class);
        }

        when(applicationContext.getBean("workflowDefinitionParser")).thenReturn(definitionParser);
        when(applicationContext.containsBean("workflowSessionFactory")).thenReturn(true);

        defaultWorkflowEnvironment.setTokenFactory(mock(TokenFactory.class));

        ProcessDriverFactory executorFactory = mock(ProcessDriverFactory.class);

        when(executorFactory.getExecutor()).thenReturn(new LocalProcessDriver());

        ProcessSessionFactory sessionFactory = new DefaultProcessSessionFactory(definitionRegistry,
                Collections.singletonList((WorkflowListener) new StandardOutputWorkflowListener()),
                executorFactory);

        when(applicationContext.getBean("workflowSessionFactory")).thenReturn(sessionFactory);
        when(applicationContext.containsBean("workflowService")).thenReturn(true);

        if (workflowService == null) {
            workflowService = mock(ProcessService.class);
        }

        when(applicationContext.getBean("workflowService")).thenReturn(workflowService);
        when(applicationContext.containsBean("workflowDefinitionHelper")).thenReturn(true);

        ProcessDefinitionHelper workflowDefinitionHelper = mock(ProcessDefinitionHelper.class);

        when(applicationContext.getBean("workflowDefinitionHelper"))
            .thenReturn(workflowDefinitionHelper);

        when(applicationContext.containsBean("expressionExecutor")).thenReturn(true);

        ExpressionExecutor exex = new ELExpressionExecutor();

        when(applicationContext.getBean("expressionExecutor")).thenReturn(exex);
        defaultWorkflowEnvironment.setApplicationContext(applicationContext);
        defaultWorkflowEnvironment.setDefinitionResources(resources);
        defaultWorkflowEnvironment.start();

        return defaultWorkflowEnvironment;
    }
}
