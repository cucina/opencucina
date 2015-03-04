package org.cucina.eggtimer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@EnableAutoConfiguration
@ComponentScan
@EnableJms
@ImportResource(value = "classpath:/eggtimer.xml")
public class EggTimer {
    private static final Logger LOG = LoggerFactory.getLogger(EggTimer.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param args
     *            JAVADOC.
     *
     * @throws Exception
     *             JAVADOC.
     */
    public static void main(String[] args)
        throws Exception {
        ApplicationContext ac = SpringApplication.run(EggTimer.class, args);

        if (LOG.isDebugEnabled()) {
            String[] names = ac.getBeanDefinitionNames();

            for (int i = 0; i < names.length; i++) {
            	LOG.debug(names[i]);
            }
        }
    }
}
