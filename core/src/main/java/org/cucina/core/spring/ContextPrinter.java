package org.cucina.core.spring;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ContextPrinter {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param context JAVADOC.
	 * @param log     JAVADOC.
	 */
	public static void traceBeanNames(ApplicationContext context, Logger log) {
		String[] names = context.getBeanDefinitionNames();
		List<String> list = Arrays.asList(names);

		Collections.sort(list);

		for (String string : list) {
			log.trace(string + " of type:" + context.getBean(string).getClass().getName());
		}
	}
}
