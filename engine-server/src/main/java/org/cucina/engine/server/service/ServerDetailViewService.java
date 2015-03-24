package org.cucina.engine.server.service;

import java.util.Map;

import org.cucina.engine.server.converter.DetailViewConfig;
import org.cucina.engine.server.event.RequestDomainConfigEvent;
import org.cucina.engine.server.event.RequestDomainDataEvent;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.server.repository.EntityDescriptorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * Implementation of DataViewService which makes use of events to request data.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component
public class ServerDetailViewService
    implements DetailViewService {
    private static final Logger LOG = LoggerFactory.getLogger(ServerDetailViewService.class);
    private ConversionService conversionService;
    private EntityDescriptorRepository entityDescriptorRepository;
    @Value("${cucina.server.applicationName:cucinaEngine}")
    private String serverApplicationName;

    /**
     * Creates a new ServerDataViewService object.
     *
     * @param communication
     *            ClientCommunication.
     */
    @Autowired
    public ServerDetailViewService(
        @Qualifier(value = "myConversionService")
    ConversionService conversionService, EntityDescriptorRepository entityDescriptorRepository) {
        Assert.notNull(conversionService, "conversionService cannot be null");
        this.conversionService = conversionService;
        Assert.notNull(entityDescriptorRepository, "entityDescriptorRepository cannot be null");
        this.entityDescriptorRepository = entityDescriptorRepository;
    }

    /**
     * Get data using EntityDescriptor
     *
     * @param entityDescriptor
     *            EntityDescriptor.
     *
     * @return Map<String, Object> results of values keyed by property name.
     */
    @Override
    @Transactional
    public Map<String, Object> getData(EntityDescriptor entityDescriptor) {
        return getData(entityDescriptor.getId(), entityDescriptor.getApplicationType(),
            entityDescriptor.getApplicationName());
    }

    /**
     * Get data by id type from application represented by applicationName
     *
     * @param id
     *            Long.
     * @param type
     *            String.
     * @param applicationName
     *            String.
     *
     * @return Map<String, Object> results of values keyed by property name.
     */
    @Override
    @Transactional
    public Map<String, Object> getData(Long id, String type, String applicationName) {
        Assert.notNull(id, "id must be provided as arg");
        Assert.notNull(type, "type must be provided as arg");
        Assert.notNull(applicationName, "applicationName must be provided as arg");

        RequestDomainDataEvent event = new RequestDomainDataEvent(serverApplicationName,
                serverApplicationName);

        event.setId(id);
        event.setApplicationType(type);

        // TODO provide a channel of communication back to client
        return null;
    }

    /**
     * Get data view configuration for this type from application represented by
     * applicationName.
     *
     * @param type
     *            String.
     * @param applicationName
     *            String.
     *
     * @return List<Map<String, String>> data view config for type.
     */
    @Override
    @Transactional
    public DetailViewConfig getDetailViewConfig(String type, String applicationName) {
        Assert.notNull(type, "type must be provided as arg");
        Assert.notNull(applicationName, "applicationName must be provided as arg");

        RequestDomainConfigEvent event = new RequestDomainConfigEvent(serverApplicationName,
                serverApplicationName);

        event.setApplicationType(type);

        // TODO provide a channel of communication back to client
        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param serverApplicationName JAVADOC.
     */
    public void setServerApplicationName(String serverApplicationName) {
        this.serverApplicationName = serverApplicationName;
    }
}
