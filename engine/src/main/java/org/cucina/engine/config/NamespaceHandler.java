package org.cucina.engine.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NamespaceHandler
		extends NamespaceHandlerSupport {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Override
	public void init() {
		registerBeanDefinitionParser("environment", new EnvironmentBeanDefinitionParser());
		registerBeanDefinitionParser("scheduler", new SchedulerDefinitionParser());
		registerBeanDefinitionParser("bulkTransitionListener",
				new BulkTransitionEventListenerDefinitionParser());
		registerBeanDefinitionParser("transitionListener",
				new TransitionEventListenerDefinitionParser());
		registerBeanDefinitionParser("bulkWorkflowService",
				new BulkWorkflowServiceDefinitionParser());
		registerBeanDefinitionParser("definitionService", new DefinitionServiceDefinitionParser());
		registerBeanDefinitionParser("tokenFactory", new TokenFactoryDefinitionParser());
		registerBeanDefinitionParser("tokenRepository", new TokenRepositoryDefinitionParser());
		registerBeanDefinitionParser("workflowRepository", new WorkflowRepositoryDefinitionParser());
		registerBeanDefinitionParser(SaveOperationDefinitionParser.DEFAULT_ID,
				new SaveOperationDefinitionParser());
		registerBeanDefinitionParser(DeleteOperationDefinitionParser.DEFAULT_ID,
				new DeleteOperationDefinitionParser());
		registerBeanDefinitionParser(DomainExpressionCheckDefinitionParser.DEFAULT_ID,
				new DomainExpressionCheckDefinitionParser());
		registerBeanDefinitionParser(TransitionOperationDefinitionParser.DEFAULT_ID,
				new TransitionOperationDefinitionParser());
		registerBeanDefinitionParser(ExpressionCheckDefinitionParser.DEFAULT_ID,
				new ExpressionCheckDefinitionParser());
		registerBeanDefinitionParser(ExpressionOperationDefinitionParser.DEFAULT_ID,
				new ExpressionOperationDefinitionParser());
		registerBeanDefinitionParser(EmailOperationDefinitionParser.DEFAULT_ID,
				new EmailOperationDefinitionParser());
		registerBeanDefinitionParser(ParameterValueExtractorOperationDefinitionParser.DEFAULT_ID,
				new ParameterValueExtractorOperationDefinitionParser());
	}
}
