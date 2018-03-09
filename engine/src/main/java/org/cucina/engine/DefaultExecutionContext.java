package org.cucina.engine;

import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.engine.definition.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Simple implementation of {@link ExecutionContext} that provides constructor
 * and JavaBeans access to the internal data.
 *
 * @author vlevine
 */
public class DefaultExecutionContext
		implements ExecutionContext {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultExecutionContext.class);
	private List<WorkflowListener> workflowListeners;
	private Map<String, Object> parameters;
	private ProcessDriverFactory processDriverFactory;
	private Token token;

	/**
	 * Creates a new DefaultExecutionContext object.
	 *
	 * @param token             JAVADOC.
	 * @param parameters        JAVADOC.
	 * @param workflowListeners JAVADOC.
	 * @param executor          JAVADOC.
	 */
	public DefaultExecutionContext(Token token, Map<String, Object> parameters,
								   List<WorkflowListener> workflowListeners, ProcessDriverFactory processDriverFactory) {
		this.token = token;
		this.parameters = parameters;
		this.workflowListeners = workflowListeners;
		this.processDriverFactory = processDriverFactory;
	}

	/**
	 * Creates a new DefaultExecutionContext object.
	 *
	 * @param token   JAVADOC.
	 * @param context JAVADOC.
	 */
	public DefaultExecutionContext(Token token, ExecutionContext context) {
		this.token = token;

		if (context != null) {
			this.parameters = context.getParameters();
			this.workflowListeners = context.getWorkflowListeners();
			this.processDriverFactory = context.getProcessDriverFactory();
		} else {
			LOG.debug("Initialized by copy with null input context");
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public ExpressionExecutor getExpressionExecutor() {
		return processDriverFactory.getExpressionExecutor();
	}

	/**
	 * JAVADOC
	 *
	 * @return JAVADOC
	 */
	public Map<String, Object> getParameters() {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		return parameters;
	}

	/**
	 * JAVADOC
	 *
	 * @param parameters JAVADOC
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public ProcessDriver getProcessDriver() {
		return processDriverFactory.getExecutor();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public ProcessDriverFactory getProcessDriverFactory() {
		return processDriverFactory;
	}

	/**
	 * JAVADOC
	 *
	 * @return JAVADOC
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * JAVADOC
	 *
	 * @param token JAVADOC
	 */
	public void setToken(Token token) {
		this.token = token;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public TokenFactory getTokenFactory() {
		return processDriverFactory.getTokenFactory();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public List<WorkflowListener> getWorkflowListeners() {
		return workflowListeners;
	}

	/**
	 * JAVADOC.
	 *
	 * @param key   JAVADOC.
	 * @param value JAVADOC.
	 */
	public void addParameter(Object key, Object value) {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		parameters.put(key.toString(), value);
	}
}
