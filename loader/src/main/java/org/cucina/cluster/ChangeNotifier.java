package org.cucina.cluster;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.cucina.cluster.event.DataChangedEvent;
import org.cucina.core.service.ContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;


/**
 * After advice which identifies the object types that have been updated
 * to notify any interested processes once the transaction has completed.
 */
public class ChangeNotifier {
	/**
	 * This is a field JAVADOC
	 */
	public static final String DATA_MODIFIED_TYPES = "data.modified.types";
	/**
	 * This is a field JAVADOC
	 */
	public static final String DATA_EVENT_PROGRAMMATIC = "data.event.programmatic";
	private static final Logger LOG = LoggerFactory.getLogger(ChangeNotifier.class);
	private ClusterBroadcastService broadcastService;
	private ContextService contextService;
	private Set<String> ignoreTypes;

	/**
	 * Creates a new ChangeNotifier object.
	 */
	public ChangeNotifier(ContextService contextService, ClusterBroadcastService broadcastService,
						  Set<String> ignoreTypes) {
		super();
		Assert.notNull(contextService, "contextService cannot be null");
		Assert.notNull(broadcastService, "broadcastService cannot be null");
		Assert.notNull(ignoreTypes, "ignoreTypes cannot be null");
		this.contextService = contextService;
		this.broadcastService = broadcastService;
		this.ignoreTypes = ignoreTypes;
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	public void setProgrammatic() {
		contextService.put(DATA_EVENT_PROGRAMMATIC, Boolean.TRUE);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Boolean getProgrammatic() {
		return contextService.get(DATA_EVENT_PROGRAMMATIC);
	}

	/**
	 * Register changed types, on commit a <code>ClusterProcessEvent</code> will be
	 * published and any interested parties will be notified. If the list of <code>updatedTypes</code>
	 * contains any of the configured <code>ignoreTypes</code> we do nothing, this is necessary in the
	 * case of ClusterControl for example because it gets updated as part of the firing of the
	 * <code>ClusterProcessEvent</code> and would result in an infinite loop.
	 *
	 * @param updatedTypes
	 */
	public void registerChangedTypes(Collection<String> updatedTypes) {
		if (CollectionUtils.isEmpty(updatedTypes) ||
				CollectionUtils.containsAny(updatedTypes, ignoreTypes)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("skipping, updatedTypes contained one of the ignoreTypes " + ignoreTypes);
			}

			return;
		}

		HashSet<String> modifiedTypes = contextService.get(DATA_MODIFIED_TYPES);

		if (modifiedTypes == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("This is the first update we've captured");
			}

			modifiedTypes = new HashSet<String>();
			contextService.put(DATA_MODIFIED_TYPES, modifiedTypes);

			if (BooleanUtils.isNotTrue(getProgrammatic())) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(
							"Set up the TransactionSynchronization as we are not in programmatic mode");
				}

				//the default is to send the data changed event after each commit.
				//this is what this block of code is doing
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						sendEvent();
					}
				});
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("In programmatic mode");
				}
			}
		}

		modifiedTypes.addAll(updatedTypes);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	public void sendEvent() {
		HashSet<String> changedTypes = contextService.get(DATA_MODIFIED_TYPES);

		if (CollectionUtils.isEmpty(changedTypes)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("not broacasting changed types as set is empty or null");
			}

			return;
		}

		//Here we'll fire the event to inform interested consumers of the types that've been modified
		if (LOG.isDebugEnabled()) {
			LOG.debug("broadcasting dataChanged event to cluster with data " + changedTypes);
		}

		Map<String, Serializable> properties = new HashMap<String, Serializable>();

		//must be an array list when deserializing so we may as well make it clearer by converting
		//here
		properties.put(DataChangedEvent.MODIFIED_TYPES, new ArrayList<String>(changedTypes));
		broadcastService.broadcast("dataChanged", properties);
	}
}
