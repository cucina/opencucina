
package org.cucina.cluster;

import org.cucina.cluster.event.ClusterBroadcastEvent;
import org.cucina.loader.agent.AgentRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class BroadcastRunService
    extends LocalRunService
    implements ApplicationListener<ClusterBroadcastEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(BroadcastRunService.class);
    private AgentRunner executorRunner;

    /**
     * Creates a new BroadcastRunService object.
     *
     * @param executorRunner JAVADOC.
     */
    public BroadcastRunService(AgentRunner executorRunner) {
        Assert.notNull(executorRunner, "executorRunner is null");
        this.executorRunner = executorRunner;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     */
    @Override
    public void onApplicationEvent(ClusterBroadcastEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received cluster broadcast event for '" + event.getSource() +
                "' with properties [" + event.getProperties() + "]");
        }
    	
        onEvent(event);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public AgentRunner getExecutorRunner() {
        return executorRunner;
    }
}
