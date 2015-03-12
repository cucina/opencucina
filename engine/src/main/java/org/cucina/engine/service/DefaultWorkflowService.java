package org.cucina.engine.service;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessSession;
import org.cucina.engine.ProcessSessionFactory;
import org.cucina.engine.SignalFailedException;
import org.cucina.engine.definition.Token;
import org.cucina.engine.definition.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DefaultWorkflowService
    implements WorkflowService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWorkflowService.class);
    private ProcessSessionFactory workflowSessionFactory;

    /**
     * Creates a new DefaultWorkflowService object.
     *
     * @param workflowSessionFactory
     *            JAVADOC.
     */
    public DefaultWorkflowService(ProcessSessionFactory workflowSessionFactory) {
        Assert.notNull(workflowSessionFactory, "workflowSessionFactory is null");
        this.workflowSessionFactory = workflowSessionFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param token
     *            JAVADOC.
     * @param transitionId
     *            JAVADOC.
     * @param parameters
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Token executeTransition(Token token, String transitionId, Map<String, Object> parameters) {
        if (token.hasChildren()) {
            LOG.debug("Cannot transition token until it has children");
            throw new SignalFailedException("Cannot transition token until it has children");
        }

        String wfid = token.getWorkflowDefinitionId();

        Assert.notNull(wfid, "workflowDefinitionId is null");

        ProcessSession session = workflowSessionFactory.openSession(wfid);

        // call on with the named transition
        session.signal(session.createExecutionContext(token, parameters), transitionId);

        return token;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param token
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public Collection<String> listTransitions(Token token, Map<String, Object> parameters) {
        ProcessSession session = workflowSessionFactory.openSession(token.getWorkflowDefinitionId());
        ExecutionContext context = session.createExecutionContext(token, parameters);
        Collection<Transition> transes = session.getAvailableTransitions(context);

        return CollectionUtils.collect(transes,
            new Transformer() {
                @Override
                public Object transform(Object arg0) {
                    if (arg0 == null) {
                        return null;
                    }

                    return ((Transition) arg0).getId();
                }
            });
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param object
     *            JAVADOC.
     * @param workflowId
     *            JAVADOC.
     * @param transitionId
     *            JAVADOC.
     * @param parameters
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Token startWorkflow(Object object, String workflowId, String transitionId,
        Map<String, Object> parameters) {
        ProcessSession session = workflowSessionFactory.openSession(workflowId);

        if (session == null) {
            LOG.error("Failed to create a new workflow session for :" + workflowId);

            return null;
        }

        return session.startWorkflowInstance(object, transitionId, parameters);
    }
}
