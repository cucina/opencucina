package org.cucina.sample.engine.client.app;

import org.apache.commons.lang3.StringUtils;
import org.cucina.engine.client.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * Action that has getter and setter expressions. Makes use of the configured
 * {@link ExpressionExecutor}. Copies value obtained <code>expressionFrom</code>
 * applying it using <code>expressionTo</code>. Both of these expressions are
 * applied on the executionContext.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component("elOperation")
public class ExpressionOperation
		implements Operation {
	private static final Logger LOG = LoggerFactory.getLogger(ExpressionOperation.class);
	@Autowired
	private ExpressionExecutor expressionExecutor;
	private String expressionFrom;
	private String expressionTo;

	/**
	 * Set expressionFrom, the expression to get the property to set to
	 * expressionTo.
	 *
	 * @param expressionFrom String.
	 */
	public void setExpressionFrom(String expressionFrom) {
		this.expressionFrom = expressionFrom;
	}

	/**
	 * Set expressionTo, the expression to set the property to returned from
	 * expressionFrom evaluation.
	 *
	 * @param expressionTo String.
	 */
	public void setExpressionTo(String expressionTo) {
		this.expressionTo = expressionTo;
	}

	/**
	 * Uses {@link ExpressionExecutor} to get and set properties.
	 *
	 * @param executionContext ExecutionContext.
	 */
	@Override
	public void execute(Object domain, Map<String, Object> parameters) {
		Assert.notNull(domain, "domain cannot be null");

		if (LOG.isDebugEnabled()) {
			LOG.debug("expressionFrom [" + expressionFrom + "]");
		}

		Object value = expressionExecutor.evaluate(domain, expressionFrom);

		if (LOG.isDebugEnabled()) {
			LOG.debug("value [" + value + "]");
		}

		if (StringUtils.isNotEmpty(expressionTo)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("expressionTo [" + expressionTo + "]");
			}

			expressionExecutor.set(domain, expressionTo, value);
		}
	}
}
