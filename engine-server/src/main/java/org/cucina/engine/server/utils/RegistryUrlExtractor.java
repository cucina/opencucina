package org.cucina.engine.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.cucina.engine.server.communication.ClientRegistry;
import org.cucina.engine.server.communication.ClientRegistry.DestinationDescriptor;
import org.cucina.engine.server.event.CallbackEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component("registryUrlExtractor")
public class RegistryUrlExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryUrlExtractor.class);
    private ClientRegistry clientRegistry;

    /**
     * Creates a new RegistryUrlExtractor object.
     *
     * @param clientRegistry JAVADOC.
     */
    @Autowired
    public RegistryUrlExtractor(ClientRegistry clientRegistry) {
        Assert.notNull(clientRegistry, "clientRegistry is null");
        this.clientRegistry = clientRegistry;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param payload JAVADOC.
     *
     * @return JAVADOC.
     */
    public String findDestinationName(CallbackEvent payload) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("event " + payload);
        }

        DestinationDescriptor dd = clientRegistry.getDestination(payload.getApplicationName());

        return dd.getDestinationName();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param payload JAVADOC.
     *
     * @return JAVADOC.
     */
    public String findProtocol(CallbackEvent payload) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("event " + payload);
        }

        DestinationDescriptor dd = clientRegistry.getDestination(payload.getApplicationName());

        return dd.getProtocol();
    }
}
