package org.cucina.core.spring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.ApplicationContext;

import org.slf4j.Logger;


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
     * @param log JAVADOC.
     */
    public static void traceBeanNames(ApplicationContext context, Logger log) {
        String[] names = context.getBeanDefinitionNames();
        List<String> list = Arrays.asList(names);

        Collections.sort(list);

        for (String string : list) {
            log.trace(string);
        }
    }
}
