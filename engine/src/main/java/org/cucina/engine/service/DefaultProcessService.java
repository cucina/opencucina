package org.cucina.engine.service;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessSession;
import org.cucina.engine.ProcessSessionFactory;
import org.cucina.engine.SignalFailedException;
import org.cucina.engine.definition.Token;
import org.cucina.engine.definition.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultProcessService
		implements ProcessService {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessService.class);
	private ProcessSessionFactory processSessionFactory;

	/**
	 * Creates a new DefaultProcessService object.
	 */
	public DefaultProcessService(ProcessSessionFactory processSessionFactory) {
		Assert.notNull(processSessionFactory, "processSessionFactory is null");
		this.processSessionFactory = processSessionFactory;
	}

	@Override
	public Token executeTransition(Token token, String transitionId, Map<String, Object> parameters) {
		if (token.hasChildren()) {
			LOG.debug("Cannot transition token until it has children");
			throw new SignalFailedException("Cannot transition token until it has children");
		}

		String wfid = token.getProcessDefinitionId();

		Assert.notNull(wfid, "workflowDefinitionId is null");

		ProcessSession session = processSessionFactory.openSession(wfid);

		// call on with the named transition
		session.signal(session.createExecutionContext(token, parameters), transitionId);

		return token;
	}

	/**
	 * List transitions which can be applied to this token.
	 */
	@Override
	public Collection<String> listTransitions(Token token, Map<String, Object> parameters) {
		ProcessSession session = processSessionFactory.openSession(token.getProcessDefinitionId());
		ExecutionContext context = session.createExecutionContext(token, parameters);

		return session.getAvailableTransitions(context).stream().filter(Objects::nonNull).map(Transition::getId).collect(Collectors.toList());
	}

	@Override
	public Token startProcess(Object object, String processId, String transitionId,
							  Map<String, Object> parameters) {
		ProcessSession session = processSessionFactory.openSession(processId);

		if (session == null) {
			LOG.error("Failed to create a new process session for :" + processId);

			return null;
		}

		return session.startProcessInstance(object, transitionId, parameters);
	}
}
