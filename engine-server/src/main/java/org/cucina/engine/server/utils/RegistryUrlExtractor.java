package org.cucina.engine.server.utils;

import org.cucina.engine.server.communication.ClientRegistry;
import org.cucina.engine.server.communication.ClientRegistry.DestinationDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
	 * @return JAVADOC.
	 */
	public String findDestinationName(String applicationName) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("applicationName " + applicationName);
		}

		DestinationDescriptor dd = clientRegistry.getDestination(applicationName);

		return dd.getDestinationName();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param payload JAVADOC.
	 * @return JAVADOC.
	 */
	public String findProtocol(String applicationName) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("applicationName " + applicationName);
		}

		DestinationDescriptor dd = clientRegistry.getDestination(applicationName);

		return dd.getProtocol();
	}
}
