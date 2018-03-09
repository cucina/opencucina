package org.cucina.engine;

import org.cucina.engine.definition.ProcessDefinitionHelper;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.service.ProcessService;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.BeanResolver;


public interface ProcessEnvironment
		extends ApplicationContextAware {
	BeanResolver getBeanResolver();

	ProcessDefinitionParser getDefinitionParser();

	ProcessDefinitionRegistry getDefinitionRegistry();

	ProcessDefinitionHelper getProcessDefinitionHelper();

	ProcessDriverFactory getProcessDriverFactory();

	ProcessService getService();

	TokenFactory getTokenFactory();
}
