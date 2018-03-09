package org.cucina.engine.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.event.TransitionEvent;
import org.cucina.engine.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * This class supposed to be used only from this package.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BulkWorkflowServiceImpl
		implements BulkWorkflowService, ApplicationContextAware {
	private static final Logger LOG = LoggerFactory.getLogger(BulkWorkflowServiceImpl.class);
	private ApplicationContext applicationContext;
	private TokenRepository tokenRepository;
	private ProcessEnvironment workflowEnvironment;

	/**
	 * Creates a new BulkWorkflowServiceImpl object.
	 *
	 * @param workflowEnvironment JAVADOC.
	 * @param tokenRepository     JAVADOC.
	 */
	public BulkWorkflowServiceImpl(ProcessEnvironment workflowEnvironment,
								   TokenRepository tokenRepository) {
		Assert.notNull(workflowEnvironment, "workflowEnvironment must be provided");
		this.workflowEnvironment = workflowEnvironment;
		Assert.notNull(tokenRepository, "tokenRepository must be provided");
		this.tokenRepository = tokenRepository;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationContext JAVADOC.
	 * @throws BeansException JAVADOC.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Execute transition
	 * <code>transitionId<code> for all tokens that are in the correct place in
	 * workflow <code>workflowId</code>
	 *
	 * @param workflowId   String.
	 * @param transitionId transitionId.
	 * @param parameters   Map<String, Object> to pass to workflow execution.
	 * @return JAVADOC.
	 */
	@Override
	public void executeTransitions(String applicationType, String workflowId, String transitionId,
								   Map<String, Object> parameters) {
		Assert.notNull(workflowId, "workflowId must be provided as a parameter");
		Assert.notNull(transitionId, "transitionId must be provided as a parameter");
		Assert.notNull(applicationType, "applicationType must be provided as a parameter");

		String placeId = workflowEnvironment.getProcessDefinitionHelper()
				.findPlaceId(workflowId, transitionId);

		Assert.notNull(placeId,
				"No place for worklowId [" + workflowId + "] and transition [" + transitionId + "]");

		Collection<Long> domainIds = tokenRepository.findDomainIdsByWorkflowIdPlaceIdApplicationType(workflowId, placeId,
				applicationType);

		if (CollectionUtils.isEmpty(domainIds)) {
			LOG.debug("Failed to find any tokens");

			return;
		}

		executeTransitions(domainIds, applicationType, workflowId, transitionId, parameters);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param ids             JAVADOC.
	 * @param applicationType JAVADOC.
	 * @param workflowId      JAVADOC.
	 * @param transitionId    JAVADOC.
	 * @param parameters      JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public void executeTransitions(Collection<Long> ids, String applicationType, String workflowId,
								   String transitionId, Map<String, Object> parameters) {
		Assert.isTrue(CollectionUtils.isNotEmpty(ids), "Ids collection is empty or null");
		Assert.notNull(workflowId, "workflowId must be provided as a parameter");
		Assert.notNull(transitionId, "transitionId must be provided as a parameter");
		Assert.notNull(applicationType, "applicationType must be provided as a parameter");

		// Ensure params are initialized now matter what
		Map<String, Object> params = new HashMap<String, Object>();

		if (MapUtils.isNotEmpty(parameters)) {
			params.putAll(parameters);
		}

		if (CollectionUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				// Create new params map containing all items in parameters
				applicationContext.publishEvent(new TransitionEvent(transitionId, workflowId,
						applicationType, id, params));
			}
		}
	}
}
