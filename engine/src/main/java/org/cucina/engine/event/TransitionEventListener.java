package org.cucina.engine.event;

import java.util.Map;

import org.cucina.engine.CheckNotMetException;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.SignalFailedException;
import org.cucina.engine.definition.Token;
import org.cucina.engine.repository.DomainRepository;
import org.cucina.engine.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Persistable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TransitionEventListener
    implements ApplicationListener<TransitionEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(TransitionEventListener.class);
    private DomainRepository domainRepository;
    private TokenRepository tokenRepository;
    private ProcessEnvironment workflowEnvironment;

    /**
     * Creates a new TransitionEventListener object.
     *
     * @param workflowService
     *            JAVADOC.
     */
    public TransitionEventListener(ProcessEnvironment workflowEnvironment,
        TokenRepository tokenRepository, DomainRepository domainRepository) {
        Assert.notNull(workflowEnvironment, "workflowEnvironment is null");
        Assert.notNull(tokenRepository, "tokenRepository is null");
        Assert.notNull(domainRepository, "domainRepository is null");
        this.workflowEnvironment = workflowEnvironment;
        this.tokenRepository = tokenRepository;
        this.domainRepository = domainRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     */
    @Override
    @Transactional
    public void onApplicationEvent(TransitionEvent transitionEvent) {
        String transitionId = (String) transitionEvent.getSource();
        String applicationType = transitionEvent.getApplicationType();
        Long id = transitionEvent.getId();
        Map<String, Object> parameters = transitionEvent.getParameters();

        Persistable<Long> domain = domainRepository.load(applicationType, id);

        Assert.notNull(domain,
            "domain, type [" + applicationType + "], id [" + id + "] does not exist in db");

        Token token = tokenRepository.loadToken(domain);

        Assert.notNull(token,
            "token for domain, type [" + applicationType + "], id [" + id +
            "] does not exist in db");

        try {
            workflowEnvironment.getService().executeTransition(token, transitionId, parameters);
        } catch (SignalFailedException e) {
            LOG.error("Unexpected exception", e);
            throw e;
        } catch (CheckNotMetException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Transition failed condition [" + e.getLocalizedMessage() + "]");
            }

            throw e;
        }
    }
}
