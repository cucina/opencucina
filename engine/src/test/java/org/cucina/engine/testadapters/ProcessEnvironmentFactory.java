package org.cucina.engine.testadapters;

import org.cucina.core.spring.ELExpressionExecutor;
import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.engine.*;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinitionHelper;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.service.ProcessService;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.expression.BeanResolver;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessEnvironmentFactory {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param resolverRegistry JAVADOC.
	 * @return JAVADOC.
	 */
	public static ProcessEnvironment buildEnvironment(BeanResolver beanResolver,
													  ProcessDefinitionRegistry definitionRegistry, ProcessDefinitionParser definitionParser,
													  ProcessService processService, Collection<Resource> resources)
			throws Exception {
		DefaultProcessEnvironment defaultProcessEnvironment = new DefaultProcessEnvironment();
		ApplicationContext applicationContext = mock(ApplicationContext.class);

		when(applicationContext.containsBean("beanResolver")).thenReturn(true);

		if (beanResolver == null) {
			beanResolver = mock(BeanResolver.class);
		}

		when(applicationContext.getBean("beanResolver")).thenReturn(beanResolver);
		when(applicationContext.containsBean("processDefinitionRegistry")).thenReturn(true);

		if (definitionRegistry == null) {
			definitionRegistry = mock(ProcessDefinitionRegistry.class);
		}

		when(applicationContext.getBean("processDefinitionRegistry")).thenReturn(definitionRegistry);
		when(applicationContext.containsBean("processDefinitionParser")).thenReturn(true);

		if (definitionParser == null) {
			definitionParser = mock(ProcessDefinitionParser.class);
		}

		when(applicationContext.getBean("processDefinitionParser")).thenReturn(definitionParser);
		when(applicationContext.containsBean("processSessionFactory")).thenReturn(true);

		defaultProcessEnvironment.setTokenFactory(mock(TokenFactory.class));

		ProcessDriverFactory executorFactory = mock(ProcessDriverFactory.class);

		when(executorFactory.getExecutor()).thenReturn(new LocalProcessDriver());

		ProcessSessionFactory sessionFactory = new DefaultProcessSessionFactory(definitionRegistry,
				Collections.singletonList((WorkflowListener) new StandardOutputWorkflowListener()),
				executorFactory);

		when(applicationContext.getBean("processSessionFactory")).thenReturn(sessionFactory);
		when(applicationContext.containsBean("processService")).thenReturn(true);

		if (processService == null) {
			processService = mock(ProcessService.class);
		}

		when(applicationContext.getBean("processService")).thenReturn(processService);
		when(applicationContext.containsBean("processDefinitionHelper")).thenReturn(true);

		ProcessDefinitionHelper processDefinitionHelper = mock(ProcessDefinitionHelper.class);

		when(applicationContext.getBean("processDefinitionHelper"))
				.thenReturn(processDefinitionHelper);

		when(applicationContext.containsBean("expressionExecutor")).thenReturn(true);

		ExpressionExecutor exex = new ELExpressionExecutor();

		when(applicationContext.getBean("expressionExecutor")).thenReturn(exex);
		defaultProcessEnvironment.setApplicationContext(applicationContext);
		defaultProcessEnvironment.setDefinitionResources(resources);
		defaultProcessEnvironment.setDefinitionRegistry(definitionRegistry);
		defaultProcessEnvironment.start();

		return defaultProcessEnvironment;
	}
}
