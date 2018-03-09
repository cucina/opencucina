package org.cucina.engine.config;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.engine.operations.EmailOperation;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EmailOperationDefinitionParser
		extends AbstractDefaultIdBeanDefinitionParser {
	public static final String DEFAULT_ID = "emailOperation";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return EmailOperation.class;
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

		if (StringUtils.isNotEmpty(element.getAttribute("userAccessor"))) {
			builder.addPropertyReference("userAccessor", element.getAttribute("userAccessor"));
		}
	}
}
