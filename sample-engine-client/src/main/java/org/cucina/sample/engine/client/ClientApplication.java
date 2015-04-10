package org.cucina.sample.engine.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.EnableJms;

import org.cucina.core.spring.ContextPrinter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
 */
@SpringBootApplication
@EnableJms
public class ClientApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ClientApplication.class);

    /**
     *
     *
     * @param args
     *            .
     *
     * @throws Exception .
     */
    public static void main(String[] args)
        throws Exception {
        ApplicationContext context = SpringApplication.run(ClientApplication.class, args);

        if (LOG.isTraceEnabled()) {
            ContextPrinter.traceBeanNames(context, LOG);
        }
    }
}
