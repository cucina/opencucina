package org.cucina.engine.operations;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.TokenRepository;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;


/**
 * Transitions domains evaluated to by <code>domainExpression</code> using transition evaluated to
 * by <code>transitionIdExpression</code>. Loads and persists tokens as they are updated.
 *
 * @param <T> Class type for which workflow transition will be run.
 * @author $Author: $
 * @version $Revision: $
 */
public class TransitionOperation
		extends AbstractOperation {
	private ProcessEnvironment workflowEnvironment;
	private String domainExpression;
	private String transitionIdExpression;
	private TokenRepository tokenRepository;

	/**
	 * Creates a new WorkflowTransitionAction object.
	 *
	 * @param tokenRepository     JAVADOC.
	 * @param workflowEnvironment JAVADOC.
	 */
	public TransitionOperation(TokenRepository tokenRepository,
							   ProcessEnvironment workflowEnvironment) {
		super();
		Assert.notNull(tokenRepository, "tokenRepository cannot be null");
		this.tokenRepository = tokenRepository;
		Assert.notNull(workflowEnvironment, "workflowEnvironment cannot be null");
		this.workflowEnvironment = workflowEnvironment;
	}

	/**
	 * Set domainExpression, the expression that returns a Collection of or single T domain object
	 * for transition evaluated to by <code>transitionIdExpression</code>. This will be from the context ExecutionContext passed
	 * into the execute method.
	 *
	 * @param domainExpression String
	 */
	public void setDomainExpression(String domainExpression) {
		this.domainExpression = domainExpression;
	}

	/**
	 * Set transitionIdExpression that evaluates to the transition to be run against domain/s
	 * evaluated to by <code>domainExpression</code>. This will be from the context ExecutionContext passed
	 * into the execute method.
	 *
	 * @param transitionIdExpression String
	 */
	public void setTransitionIdExpression(String transitionIdExpression) {
		this.transitionIdExpression = transitionIdExpression;
	}

	/**
	 * Execute workflow transition for domains.
	 *
	 * @param executionContext ExecutionContext.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ExecutionContext executionContext) {
		Assert.notNull(transitionIdExpression, "transitionIdExpression not set");
		Assert.notNull(domainExpression, "domainExpression not set");

		Object domain = executionContext.getExpressionExecutor()
				.evaluate(executionContext, domainExpression);
		String transitionId = executionContext.getExpressionExecutor()
				.evaluate(executionContext, transitionIdExpression);

		if (domain != null) {
			if (domain instanceof Collection) {
				Collection<Persistable<Long>> domains = (Collection<Persistable<Long>>) domain;

				for (Persistable<Long> object : domains) {
					transition(object, transitionId, executionContext);
				}
			} else {
				transition((Persistable<Long>) domain, transitionId, executionContext);
			}
		}
	}

	private void transition(Persistable<Long> domain, String transitionId,
							ExecutionContext executionContext) {
		Assert.notNull(transitionId,
				"transitionIdExpression has not evaluated to a valid transitionId");

		ProcessToken token = tokenRepository.findByDomain(domain);

		Assert.isTrue(domain == token.getDomainObject(),
				"Loaded token should have same instance of domain as returned in expression 'domainExpression'.");

		//Create a new parameters map to prevent contamination and initialise from executionContext parameters.
		HashMap<String, Object> parameters = new HashMap<String, Object>(executionContext.getParameters());

		workflowEnvironment.getService().executeTransition(token, transitionId, parameters);

		tokenRepository.save(token);
	}
}
