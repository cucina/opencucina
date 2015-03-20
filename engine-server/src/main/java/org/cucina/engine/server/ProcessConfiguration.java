package org.cucina.engine.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.cucina.engine.service.ProcessSupportService;
import org.cucina.engine.service.ProcessSupportServiceImpl;

import org.cucina.security.access.AccessRegistry;
import org.cucina.security.access.AccessRegistryImpl;
import org.cucina.security.repository.PrivilegeRepository;
import org.cucina.security.repository.jpa.PrivilegeRepositoryImpl;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@Configuration
public class ProcessConfiguration {
    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeRepository JAVADOC.
     *
     * @return JAVADOC.
     */
    @Bean
    public AccessRegistry accessRegistry(PrivilegeRepository privilegeRepository) {
        return new AccessRegistryImpl(privilegeRepository);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public PrivilegeRepository privilegeRepository() {
        return new PrivilegeRepositoryImpl();
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
