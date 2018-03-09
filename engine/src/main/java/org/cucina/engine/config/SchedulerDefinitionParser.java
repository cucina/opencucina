package org.cucina.engine.config;

import org.apache.commons.lang3.StringUtils;
import org.cucina.engine.schedule.TaskSchedulerScheduleManager;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;


/**
 * SchedulerDefinitionParser which sets defines construction of {@link ScheduleEnvironmentImpl}.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SchedulerDefinitionParser
		extends AbstractSingleBeanDefinitionParser {
	/**
	 * Get beanClass that should be generated from this Parser.
	 *
	 * @param element Element.
	 * @return ScheduleEnvironmentImpl.class.
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return TaskSchedulerScheduleManager.class;
	}

	/**
	 * Set up properties required for {@link ScheduleEnvironmentImpl}.
	 *
	 * @param element Element.
	 * @param builder BeanDefinitionBuilder.
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		builder.addConstructorArgReference(element.getAttribute("scheduler"));

		String slots = element.getAttribute("slots");

		if (StringUtils.isNotEmpty(slots)) {
			builder.addPropertyValue("slots", slots);
		}
	}
}
