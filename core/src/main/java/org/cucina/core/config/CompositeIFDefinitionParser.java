package org.cucina.core.config;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.CompositeInstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompositeIFDefinitionParser
		extends AbstractDefaultIdBeanDefinitionParser {
	private static final String INSTANCE_FACTORIES = "instanceFactories";
	private static final String REF = "ref";
	private static final String PACKAGE = "package";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return CompositeInstanceFactory.class;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	protected String getDefaultId() {
		return SingletonBeanFactory.INSTANCE_FACTORY_ID;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param element JAVADOC.
	 * @param builder JAVADOC.
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		List<Element> resources = DomUtils.getChildElementsByTagName(element,
				SingletonBeanFactory.INSTANCE_FACTORY_ID);

		List<Object> resnames = new ManagedList<Object>();

		for (Element relement : resources) {
			String pack = relement.getAttribute(PACKAGE);

			if (StringUtils.isNotEmpty(pack)) {
				resnames.add(new PackageBasedInstanceFactory(pack));
			} else {
				resnames.add(new RuntimeBeanReference(relement.getAttribute(REF)));
			}
		}

		builder.addPropertyValue(INSTANCE_FACTORIES, resnames);
	}
}
