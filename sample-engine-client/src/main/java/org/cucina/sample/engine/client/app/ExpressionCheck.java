package org.cucina.sample.engine.client.app;

import org.cucina.engine.client.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * @author vlevine
 */
@Component("elCheck")
public class ExpressionCheck
		implements Check {
	private static final Logger LOG = LoggerFactory.getLogger(ExpressionCheck.class);
	@Autowired
	private ExpressionExecutor expressionExecutor;
	private String expression;

	/**
	 * @param expression .
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * @param domain     .
	 * @param parameters .
	 * @return .
	 */
	@Override
	public boolean test(Object domain, Map<String, Object> parameters) {
		Assert.notNull(expression, "expression cannot be null");

		Object result = expressionExecutor.evaluate(domain, expression);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Result returned for expression [" + expression + "] is [" + result + "]");
		}

		return (result instanceof Boolean) ? ((Boolean) result).booleanValue() : false;
	}
}
