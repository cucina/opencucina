package org.cucina.engine.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.cucina.core.spring.ExpressionExecutor;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.model.WorkflowToken;
import org.cucina.engine.repository.TokenRepository;
import org.cucina.engine.service.WorkflowService;
import org.cucina.engine.testassist.Foo;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * Test that WorkflowTransitionoperation functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowTransitionOperationTest {
    private static final String TRANSITION_ID = "tr1";
    private static final String TRANSITION_EXPRESSION = "transition";
    private static final String DOMAIN_EXPRESSION = "domainExpression";
    private Foo domain1;
    private Foo domain2;
    private Foo domain3;
    @Mock
    private TokenRepository tokenRepository;
    private WorkflowToken token1;
    private WorkflowToken token2;
    private WorkflowToken token3;

    /**
     * Check that the domain on the token has to be the same as the domain in context.
     */
    @Test(expected = IllegalArgumentException.class)
    public void domainSameInstance() {
        //Change domain on token to break
        token2.setDomainObject(new Foo());

        when(tokenRepository.loadToken(domain2)).thenReturn(token2);

        WorkflowService workflowService = mock(WorkflowService.class);

        when(workflowService.executeTransition(token2, TRANSITION_ID, new HashMap<String, Object>()))
            .thenReturn(token2);

        ProcessEnvironment environment = mock(ProcessEnvironment.class);

        when(environment.getService()).thenReturn(workflowService);

        TransitionOperation operation = new TransitionOperation(tokenRepository, environment);

        operation.setDomainExpression(DOMAIN_EXPRESSION);
        operation.setTransitionIdExpression(TRANSITION_EXPRESSION);

        ExpressionExecutor executor = mock(ExpressionExecutor.class);
        ExecutionContext context = mock(ExecutionContext.class);

        when(context.getExpressionExecutor()).thenReturn(executor);

        when(executor.evaluate(context, DOMAIN_EXPRESSION)).thenReturn(domain2);
        when(executor.evaluate(context, TRANSITION_EXPRESSION)).thenReturn(TRANSITION_ID);

        operation.execute(context);
        verify(tokenRepository).update(token2);
    }

    /**
     * Test that multiple domains can be transitioned.
     */
    @Test
    public void multipleDomain() {
        Collection<Object> domains = new ArrayList<Object>();

        domains.add(domain2);
        domains.add(domain3);

        when(tokenRepository.loadToken(domain2)).thenReturn(token2);
        when(tokenRepository.loadToken(domain3)).thenReturn(token3);

        WorkflowService workflowService = mock(WorkflowService.class);

        when(workflowService.executeTransition(token2, TRANSITION_ID, new HashMap<String, Object>()))
            .thenReturn(token2);
        when(workflowService.executeTransition(token3, TRANSITION_ID, new HashMap<String, Object>()))
            .thenReturn(token3);

        ProcessEnvironment environment = mock(ProcessEnvironment.class);

        when(environment.getService()).thenReturn(workflowService);

        TransitionOperation operation = new TransitionOperation(tokenRepository, environment);

        operation.setDomainExpression(DOMAIN_EXPRESSION);
        operation.setTransitionIdExpression(TRANSITION_EXPRESSION);

        ExpressionExecutor executor = mock(ExpressionExecutor.class);
        ExecutionContext context = mock(ExecutionContext.class);

        when(context.getExpressionExecutor()).thenReturn(executor);

        when(executor.evaluate(context, DOMAIN_EXPRESSION)).thenReturn(domains);
        when(executor.evaluate(context, TRANSITION_EXPRESSION)).thenReturn(TRANSITION_ID);

        operation.execute(context);
        verify(tokenRepository).update(token2);
        verify(tokenRepository).update(token3);
    }

    /**
     * Sets up for test.
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
        domain1 = new Foo();
        token1 = new WorkflowToken();
        token1.setDomainObject(domain1);

        domain2 = new Foo();
        token2 = new WorkflowToken();
        token2.setDomainObject(domain2);

        domain3 = new Foo();
        token3 = new WorkflowToken();
        token3.setDomainObject(domain3);
    }

    /**
     * Check can transition single domain.
     */
    @Test
    public void singleDomain() {
        when(tokenRepository.loadToken(domain2)).thenReturn(token2);

        WorkflowService workflowService = mock(WorkflowService.class);

        when(workflowService.executeTransition(token2, TRANSITION_ID, new HashMap<String, Object>()))
            .thenReturn(token2);

        ProcessEnvironment environment = mock(ProcessEnvironment.class);

        when(environment.getService()).thenReturn(workflowService);

        TransitionOperation operation = new TransitionOperation(tokenRepository, environment);

        operation.setDomainExpression(DOMAIN_EXPRESSION);
        operation.setTransitionIdExpression(TRANSITION_EXPRESSION);

        ExpressionExecutor executor = mock(ExpressionExecutor.class);
        ExecutionContext context = mock(ExecutionContext.class);

        when(context.getExpressionExecutor()).thenReturn(executor);
        when(executor.evaluate(context, DOMAIN_EXPRESSION)).thenReturn(domain2);
        when(executor.evaluate(context, TRANSITION_EXPRESSION)).thenReturn(TRANSITION_ID);

        operation.execute(context);
        verify(tokenRepository).update(token2);
    }
}
