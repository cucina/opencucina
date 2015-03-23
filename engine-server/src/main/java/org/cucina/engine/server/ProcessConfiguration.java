package org.cucina.engine.server;

import org.cucina.engine.service.ProcessSupportService;
import org.cucina.engine.service.ProcessSupportServiceImpl;
import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.remote.RemoteAccessFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@Configuration
@ComponentScan(basePackages="org.cucina.engine.service")
public class ProcessConfiguration {
    @Value(value = "cucina.access.url")
    private String accessUrl;

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeRepository JAVADOC.
     *
     * @return JAVADOC.
     */
    @Bean
    public AccessFacade accessFacade() {
        return new RemoteAccessFacade(accessUrl);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public ProcessSupportService processSupportService() {
        ProcessSupportServiceImpl service = new ProcessSupportServiceImpl();

        return service;
    }
}
