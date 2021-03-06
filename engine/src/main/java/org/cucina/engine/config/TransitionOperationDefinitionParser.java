package org.cucina.engine.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.engine.operations.TransitionOperation;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TransitionOperationDefinitionParser
		extends AbstractDefaultIdBeanDefinitionParser {
	/**
	 * This is a field JAVADOC
	 */
	public static final String DEFAULT_ID = "transitionOperation";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return TransitionOperation.class;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	protected String getDefaultId() {
		return DEFAULT_ID;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @param builder JAVADOC.
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		builder.addConstructorArgReference(element.getAttribute("tokenRepository"));
		builder.addConstructorArgReference(element.getAttribute("workflowEnvironment"));
	}
}
