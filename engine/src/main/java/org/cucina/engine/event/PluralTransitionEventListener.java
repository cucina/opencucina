
package org.cucina.engine.event;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.engine.service.BulkWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PluralTransitionEventListener
    implements ApplicationListener<PluralTransitionEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(PluralTransitionEventListener.class);
    private BulkWorkflowService bulkWorkflowService;

    /**
     * Creates a new TransitionEventListener object.
     *
     * @param workflowService
     *            JAVADOC.
     */
    public PluralTransitionEventListener(BulkWorkflowService bulkWorkflowService) {
        Assert.notNull(bulkWorkflowService, "bulkWorkflowService is null");
        this.bulkWorkflowService = bulkWorkflowService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     */
    @Override
    @Transactional
    public void onApplicationEvent(PluralTransitionEvent transitionEvent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received event:" + transitionEvent);
        }

        String transitionId = (String) transitionEvent.getSource();
        String workflowId = transitionEvent.getWorkflowId();
        String applicationType = transitionEvent.getApplicationType();
        Collection<Long> ids = transitionEvent.getIds();
        Map<String, Object> parameters = transitionEvent.getParameters();

        // if no ids provided, do a bulk transition for all objects having named
        // transition at their current state in the named workflow
        try {
            if (CollectionUtils.isEmpty(ids)) {
                bulkWorkflowService.executeTransitions(applicationType, workflowId, transitionId,
                    parameters);
            } else {
                // for ids and application type/workflowId
                bulkWorkflowService.executeTransitions(ids, applicationType, workflowId,
                    transitionId, parameters);
            }
        } catch (Exception e) {
            LOG.error("Unexpected exception", e);
        }
    }
}
