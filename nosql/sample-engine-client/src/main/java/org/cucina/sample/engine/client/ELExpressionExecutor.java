
package org.cucina.sample.engine.client;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Spring EL implementation of ExpressionExecutor.
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Component
public class ELExpressionExecutor
    implements ExpressionExecutor, BeanFactoryAware {
    private static final Logger LOG = LoggerFactory.getLogger(ELExpressionExecutor.class);
    private BeanFactory beanFactory;

    //Is thread safe.. so it says
    private ExpressionParser parser = new SpelExpressionParser();

    /**
     *Set beanFactory
     *
     * @param beanFactory BeanFactory.
     */
    @Override
    @Required
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Evaluate expression on context using Spring EL expression
     *
     * @param context Object.
     * @param expression String.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object context, String expression) {
        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext(context);

            if (beanFactory != null) {
                evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("expressionFrom [" + expression + "]");
            }

            return (T) parser.parseExpression(expression).getValue(evaluationContext);
        } catch (EvaluationException e) {
            LOG.warn("EvaluationException for expression [" + expression + "]", e);
            throw e;
        } catch (ParseException e) {
            LOG.warn("ParseException for expression [" + expression + "]", e);
            throw e;
        }
    }

    /**
     * Set expression on context using Spring EL expression
     *
     * @param context Object.
     * @param expression String.
     * @param value Object.
     */
    @Override
    public void set(Object context, String expression, Object value) {
        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext(context);

            if (beanFactory != null) {
                evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("expressionFrom [" + expression + "]");
            }

            parser.parseExpression(expression).setValue(evaluationContext, value);
        } catch (EvaluationException e) {
            LOG.warn("EvaluationException for expression [" + expression + "] for value [" + value +
                "]", e);
            throw e;
        } catch (ParseException e) {
            LOG.warn("ParseException for expression [" + expression + "] for value [" + value +
                "]", e);
            throw e;
        }
    }
}
