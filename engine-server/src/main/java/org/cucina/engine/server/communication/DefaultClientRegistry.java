package org.cucina.engine.server.communication;

import org.cucina.engine.server.event.RegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.HashMap;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $ TODO - make it persisting to share between nodes in a
 * cluster
 */
@ManagedResource
@Component("clientRegistry")
public class DefaultClientRegistry
		implements ClientRegistry, ApplicationListener<RegistrationEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultClientRegistry.class);
	private Map<String, DestinationDescriptor> appDestination;

	/**
	 * Creates a new DefaultClientRegistry object.
	 */
	public DefaultClientRegistry() {
		appDestination = new HashMap<String, DestinationDescriptor>();
	}

	/**
	 * Creates a new DefaultClientRegistry object.
	 *
	 * @param appDestination JAVADOC.
	 */
	public DefaultClientRegistry(Map<String, String> appDestination) {
		this();

		for (Map.Entry<String, String> in : appDestination.entrySet()) {
			this.appDestination.put(in.getKey(), new DestinationDescriptor(in.getValue()));
		}
	}

	private static String quote(String s) {
		return "\"" + s + "\"";
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationName JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public DestinationDescriptor getDestination(String applicationName) {
		// Assert.isTrue(appDestination.containsKey(applicationName),
		// "Unknown application '" + applicationName + "'");
		return appDestination.get(applicationName);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationName JAVADOC.
	 * @param destinationName JAVADOC.
	 */
	public void addRegistration(String applicationName, String destinationName) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Adding '" + applicationName + "' @ '" + destinationName + "'");
		}

		appDestination.put(applicationName, new DestinationDescriptor(destinationName));
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@ManagedOperation
	public ObjectName[] listRegistry() {
		ObjectName[] result = new ObjectName[appDestination.size()];
		int i = 0;

		for (Map.Entry<String, DestinationDescriptor> entry : appDestination.entrySet()) {
			try {
				result[i] = ObjectName.getInstance("souffle", quote(entry.getKey()),
						quote(entry.getValue().toString()));
			} catch (MalformedObjectNameException e) {
				// Should never happen
				LOG.error("Oops", e);
			}

			i++;
		}

		return result;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	@Override
	public void onApplicationEvent(RegistrationEvent event) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Registration request:" + event);
		}

		addRegistration(event.getApplicationName(), event.getDestinationName());
	}

	/**
	 * Cleans it up
	 */
	@ManagedOperation
	public void purge() {
		appDestination.clear();
	}

	/**
	 * removes named record.
	 *
	 * @param applicationName name of the record.
	 * @return whether it succeeded.
	 */
	@ManagedOperation
	public boolean remove(String applicationName) {
		return null != appDestination.remove(applicationName);
	}
}
