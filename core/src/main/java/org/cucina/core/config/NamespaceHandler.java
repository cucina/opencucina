package org.cucina.core.config;

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
		registerBeanDefinitionParser("compositeInstanceFactory", new CompositeIFDefinitionParser());
		registerBeanDefinitionParser("packageInstanceFactory", new PackageIFDefinitionParser());
		registerBeanDefinitionParser("singletonBeanFactory",
				new SingletonBeanFactoryDefinitionParser());
		registerBeanDefinitionParser("contextService", new ContextServiceDefinitionParser());
	}
}
