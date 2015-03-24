package org.cucina.engine.server.utils;

import org.cucina.engine.server.communication.ClientRegistry;
import org.cucina.engine.server.communication.ClientRegistry.DestinationDescriptor;
import org.cucina.engine.server.event.CallbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component("registryUrlExtractor")
public class RegistryUrlExtractor {
    private ClientRegistry clientRegistry;

    /**
     * Creates a new RegistryUrlExtractor object.
     *
     * @param clientRegistry JAVADOC.
     */
    @Autowired
    public RegistryUrlExtractor(ClientRegistry clientRegistry) {
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
        DestinationDescriptor dd = clientRegistry.getDestination(payload.getApplicationName());

        return dd.getProtocol();
    }
}
