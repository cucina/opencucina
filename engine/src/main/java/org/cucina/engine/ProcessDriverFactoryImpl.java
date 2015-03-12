package org.cucina.engine;

import org.springframework.util.Assert;

import org.cucina.core.spring.ExpressionExecutor;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ProcessDriverFactoryImpl
    implements ProcessDriverFactory {
    private ExpressionExecutor expressionExecutor;
    private ProcessDriver executor;
    private TokenFactory tokenFactory;

    /**
     * Creates a new ExecutorFactoryImpl object.
     */
    public ProcessDriverFactoryImpl(TokenFactory tokenFactory,
        ExpressionExecutor expressionExecutor, ProcessDriver executor) {
        Assert.notNull(tokenFactory, "tokenFactory is null");
        this.tokenFactory = tokenFactory;
        Assert.notNull(expressionExecutor, "expressionExecutor is null");
        this.expressionExecutor = expressionExecutor;
        Assert.notNull(executor, "executor is null");
        this.executor = executor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public ProcessDriver getExecutor() {
        return executor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public ExpressionExecutor getExpressionExecutor() {
        return expressionExecutor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public TokenFactory getTokenFactory() {
        return tokenFactory;
    }
}
