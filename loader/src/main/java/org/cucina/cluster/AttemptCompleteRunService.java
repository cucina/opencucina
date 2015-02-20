package org.cucina.cluster;

import org.cucina.cluster.event.ClusterAttemptCompleteEvent;
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
public class AttemptCompleteRunService extends LocalRunService
    implements 
        ApplicationListener<ClusterAttemptCompleteEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AttemptCompleteRunService.class);
    private AgentRunner executorRunner;

    /**
     * Creates a new AttemptCompleteRunService object.
     *
     * @param executorRunner JAVADOC.
     */
    public AttemptCompleteRunService(AgentRunner executorRunner) {
        Assert.notNull(executorRunner, "executorRunner is null");
        this.executorRunner = executorRunner;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     */
    @Override
    public void onApplicationEvent(ClusterAttemptCompleteEvent event) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Received notification that attempt completed event for '" + event.getSource() +
                "' with properties [" + event.getProperties() + "]");
        }	
    	
       onEvent(event);
    }

	@Override
	public AgentRunner getExecutorRunner() {
		return executorRunner;
	}
}
