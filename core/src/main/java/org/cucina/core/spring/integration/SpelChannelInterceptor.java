package org.cucina.core.spring.integration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.expression.ExpressionUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;


/**
 * @author vlevine
 */
public class SpelChannelInterceptor
		extends ChannelInterceptorAdapter
		implements InitializingBean, BeanFactoryAware {
	private static final ExpressionParser expressionParser = new SpelExpressionParser(new SpelParserConfiguration(
			true, true));
	private BeanFactory beanFactory;
	private Expression expression;
	private StandardEvaluationContext evaluationContext;

	/**
	 * @param beanFactory .
	 * @throws BeansException .
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory)
			throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * @param expression .
	 */
	public void setExpression(String expression) {
		this.expression = expressionParser.parseExpression(expression);
	}

	/**
	 * @throws Exception .
	 */
	@Override
	public void afterPropertiesSet()
			throws Exception {
		evaluationContext = ExpressionUtils.createStandardEvaluationContext(this.beanFactory);
	}

	/**
	 * @param message .
	 * @param channel .
	 * @return .
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		expression.getValue(evaluationContext, message, null);

		return super.preSend(message, channel);
	}
}
