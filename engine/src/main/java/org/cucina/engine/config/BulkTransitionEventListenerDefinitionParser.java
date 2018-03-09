package org.cucina.engine.config;

import org.cucina.engine.event.PluralTransitionEventListener;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BulkTransitionEventListenerDefinitionParser
		extends AbstractSingleBeanDefinitionParser {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return PluralTransitionEventListener.class;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @param builder JAVADOC.
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		builder.addConstructorArgReference(element.getAttribute("bulkWorkflowService"));
	}
}
