package org.cucina.engine.config;

import org.cucina.engine.schedule.TaskSchedulerScheduleManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


/**
 * Test SchedulerDefinitionParser functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SchedulerDefinitionParserTest {
	private SchedulerDefinitionParser parser;

	/**
	 * Test parses for correct class
	 */
	@Test
	public void checkBeanClass() {
		assertEquals("Should be TaskSchedulerScheduleManager.class", TaskSchedulerScheduleManager.class,
				parser.getBeanClass(null));
	}

	/**
	 * Check parse works
	 */
	@Test
	public void parses() {
		Element element = mock(Element.class);

		when(element.getAttribute("scheduler")).thenReturn("scheduler");
		when(element.getAttribute("slots")).thenReturn("slots");

		BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

		when(builder.addConstructorArgReference("scheduler")).thenReturn(builder);
		when(builder.addPropertyValue(eq("slots"), any(String.class))).thenReturn(builder);

		parser.doParse(element, builder);

		verify(builder).addConstructorArgReference("scheduler");
		verify(builder).addPropertyValue(eq("slots"), eq("slots"));
	}

	/**
	 * Check parse works when slots empty and doesn't add corresponding property value
	 */
	@Test
	public void parsesEmptySlots() {
		Element element = mock(Element.class);

		when(element.getAttribute("scheduler")).thenReturn("scheduler");
		when(element.getAttribute("slots")).thenReturn("");

		BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

		when(builder.addConstructorArgReference("scheduler")).thenReturn(builder);

		parser.doParse(element, builder);

		verify(builder).addConstructorArgReference("scheduler");
		verify(builder, never()).addPropertyValue(eq("slots"), any(String.class));
	}

	/**
	 * Set up for test
	 */
	@Before
	public void setup() {
		parser = new SchedulerDefinitionParser();
	}
}
