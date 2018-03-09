package org.cucina.cluster;

import org.apache.commons.lang3.StringUtils;
import org.cucina.cluster.event.*;
import org.cucina.cluster.model.ClusterControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;

import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 * @version $Revision: $
 */
public class ClusterServiceImpl
		implements ClusterService, ApplicationContextAware, ApplicationListener<ClusterProcessEvent> {
	/**
	 * heartBeatEvent
	 */
	public static final String HEARTBEAT = "heartBeatEvent";
	/**
	 * localWakeUpEvent
	 */
	public static final String LOCALWAKEUP = "localWakeUpEvent";
	private static final Logger LOG = LoggerFactory.getLogger(ClusterServiceImpl.class);
	private ActiveClusterNodeService activeClusterNodeService;
	private ApplicationContext applicationContext;

	// TODO registry of which event to publish depending on the eventname
	private ApplicationEvent completeEvent;

	// TODO registry of which event to publish depending on the eventname
	private ApplicationEvent startEvent;
	private ClusterNode clusterNode;
	private HeartbeatService passiveHeartbeatService;
	private NodeRegister nodeRegister;

	/**
	 * Creates a new ClusterServiceImpl object.
	 *
	 * @param clusterControlDao        JAVADOC.
	 * @param activeClusterNodeService JAVADOC.
	 */
	public ClusterServiceImpl(NodeRegister nodeRegister,
							  ActiveClusterNodeService activeClusterNodeService,
							  HeartbeatService passiveHeartbeatService,
							  ClusterNode clusterNode) {
		Assert.notNull(nodeRegister, "nodeRegister is null");
		this.nodeRegister = nodeRegister;
		Assert.notNull(activeClusterNodeService, "activeClusterNodeService is null");
		this.activeClusterNodeService = activeClusterNodeService;
		Assert.notNull(passiveHeartbeatService, "passiveHeartbeatService is null");
		this.passiveHeartbeatService = passiveHeartbeatService;
		Assert.notNull(clusterNode, "clusterNode is null");
		this.clusterNode = clusterNode;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param ac JAVADOC.
	 * @throws BeansException JAVADOC.
	 */
	@Override
	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {
		this.applicationContext = ac;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param completeEvent JAVADOC.
	 */
	public void setCompleteEvent(ApplicationEvent completeEvent) {
		this.completeEvent = completeEvent;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param nodeId JAVADOC.
	 * @return JAVADOC.
	 */
	public boolean isMyNode(String nodeId) {
		return (StringUtils.isNotEmpty(nodeId) && clusterNode.myNodeId().equals(nodeId));
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param startEvent JAVADOC.
	 */
	public void setStartEvent(ApplicationEvent startEvent) {
		this.startEvent = startEvent;
	}

	/**
	 * Check whether there are any events not supported by heartbeats and switch
	 * processing for these events to the active form. Should be called on local
	 * wakeup.
	 */
	@Override
	public synchronized void checkOutstanding() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Checking for outstanding tasks");
		}

		for (ClusterControl control : nodeRegister.outstandingEvents(myNodeId())) {
			// TODO while one event is being processed, the next one will be out
			// of date. need to refresh the list after each event processing
			String eventName = control.getEvent();

			if (LOG.isDebugEnabled()) {
				LOG.debug("Found task that should be taken over " + eventName +
						" [passive->active]");
			}

			passiveHeartbeatService.stop(eventName);

			activeClusterNodeService.executeActive(eventName, myNodeId(), control.getProperties());
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 */
	@Override
	public void complete(String eventName) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Complete called for " + eventName);
		}

		passiveHeartbeatService.stop(eventName);
		nodeRegister.remove(eventName);

		// an event to be emitted to indicate the completion of an event
		// processing by the active node
		// TODO should add some discriminator to distinguish by eventName,
		// perhaps eventRegistry?
		if (completeEvent != null) {
			applicationContext.publishEvent(completeEvent);
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	@Override
	public void handle(ClusterProcessEvent event) {
		if (startEvent != null) {
			applicationContext.publishEvent(startEvent);
		}

		String eventName = (String) event.getSource();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Received control event for '" + eventName + "'");
		}

		if (nodeRegister.switchActivePassive(eventName, myNodeId(), event.getProperties())) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Started processing [" + eventName + "]. In active mode. [active:" +
						myNodeId() + ":" + eventName + "]");
			}

			// in active mode
			// stop local heartbeats just in case
			passiveHeartbeatService.stop(eventName);

			activeClusterNodeService.executeActive(eventName, myNodeId(), event.getProperties());
		} else {
			// in passive mode
			if (LOG.isDebugEnabled()) {
				LOG.debug("In passive monitoring mode [passive:" + eventName + "]");
			}

			passiveHeartbeatService.start(eventName, myNodeId());
		}

		applicationContext.publishEvent(new ClusterAttemptCompleteEvent(eventName));
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String myNodeId() {
		return clusterNode.myNodeId();
	}

	/**
	 * Start of an event processing either in active or passive mode.
	 *
	 * @param event Should be an instance of @see {@link ClusterProcessEvent}.
	 */
	@Override
	public void onApplicationEvent(ClusterProcessEvent event) {
		handle(event);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	@Override
	public void refresh() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Stopping all passive monitoring, and calling refresh");
		}

		passiveHeartbeatService.stopAll();

		Collection<String> events = nodeRegister.refresh(myNodeId());

		for (String event : events) {
			passiveHeartbeatService.start(event, myNodeId());

			if (LOG.isDebugEnabled()) {
				LOG.debug("Monitoring " + event);
			}
		}
	}

	/**
	 * resets the wait for the named heartbeat
	 */
	@Override
	public void resetByHeartbeat(String eventName) {
		nodeRegister.reset(eventName);
		passiveHeartbeatService.start(eventName, myNodeId());
	}

	public static class ClusterHeartbeatEventListener
			implements ApplicationListener<ClusterHeartbeatEvent> {
		private ClusterService clusterService;

		public ClusterHeartbeatEventListener(ClusterService clusterService) {
			this.clusterService = clusterService;
		}

		@Override
		public void onApplicationEvent(ClusterHeartbeatEvent event) {
			String eventName = (String) event.getSource();

			if (clusterService.isMyNode(event.getNodeId())) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Got cluster heartbeat from this node, the active node [" +
							event.getNodeId() + "] [active:" + eventName + "]. Not monitoring");
				}

				return;
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("Got cluster heartbeat from active node nodeId [" + event.getNodeId() +
						"], which is not this one .This node has nodeId [" + clusterService.myNodeId() +
						"] [passive:" + eventName + "]");
			}

			clusterService.resetByHeartbeat(eventName);
		}
	}

	public static class ClusterNotificationEventListener
			implements ApplicationListener<ClusterNotificationEvent> {
		private ClusterService clusterService;

		public ClusterNotificationEventListener(ClusterService clusterService) {
			this.clusterService = clusterService;
			Assert.notNull(clusterService, "clusterService is null");
		}

		/**
		 * Set applicationContext
		 *
		 * @param applicationContext ApplicationContext.
		 */
		@Override
		public void onApplicationEvent(ClusterNotificationEvent event) {
			String eventName = (String) event.getSource();

			if (clusterService.isMyNode(event.getNodeId())) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Got notification of task completion for '" + eventName +
							"' from my node. [active:" + eventName + "]");
				}

				return;
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("Got notification of task completion for '" + eventName +
						"' from nodeId [" + event.getNodeId() + "]. Not this node. This node is [" +
						clusterService.myNodeId() + "] [passive:" + eventName + "]");
			}

			clusterService.complete(eventName);
		}
	}

	public static class ClusterRefreshEventListener
			implements ApplicationListener<ClusterRefreshEvent> {
		private ClusterService clusterService;

		public ClusterRefreshEventListener(ClusterService clusterService) {
			this.clusterService = clusterService;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param arg0 JAVADOC.
		 */
		@Override
		public void onApplicationEvent(ClusterRefreshEvent event) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Received refresh event");
			}

			clusterService.refresh();
		}
	}

	public static class WakeupEvent
			extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public WakeupEvent(Object source) {
			super(source);
		}
	}

	public static class WakeupEventListener
			implements ApplicationListener<WakeupEvent> {
		private ClusterService clusterService;

		public WakeupEventListener(ClusterService clusterService) {
			this.clusterService = clusterService;
		}

		@Override
		public void onApplicationEvent(WakeupEvent event) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Got wakeup event [passive]");
			}

			clusterService.checkOutstanding();
		}
	}
}
