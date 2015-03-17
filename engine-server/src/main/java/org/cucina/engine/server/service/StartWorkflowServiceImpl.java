package org.cucina.engine.server.service;

import java.util.Map;

import org.cucina.engine.definition.Token;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.service.WorkflowSupportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class StartWorkflowServiceImpl
    implements StartWorkflowService {
    private static final Logger LOG = LoggerFactory.getLogger(StartWorkflowServiceImpl.class);
    private WorkflowSupportService workflowSupportService;

    /**
     * Creates a new StartWorkflowServiceImpl object.
     *
     * @param workflowSupportService JAVADOC.
     */
    public StartWorkflowServiceImpl(WorkflowSupportService workflowSupportService) {
        this.workflowSupportService = workflowSupportService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param id JAVADOC.
     * @param applicationName JAVADOC.
     * @param parameters JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Token startWorkflow(String applicationType, Long id, String applicationName,
        Map<String, Object> parameters) {
        Token token = workflowSupportService.startWorkflow(new EntityDescriptor(applicationType,
                    id, applicationName), parameters);

        Assert.notNull(token,
            "Token is null, failed to start the workflow for " + applicationType + ":" + id +
            " of " + applicationName);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Started with object:" + token.getDomainObject());
        }

        return token;
    }
}
