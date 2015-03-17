package org.cucina.engine;

import org.springframework.context.ApplicationContext;

import org.cucina.core.InstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.StartStation;
import org.cucina.engine.definition.Token;
import org.cucina.engine.model.WorkflowToken;
import org.cucina.engine.repository.TokenRepository;
import org.cucina.engine.testassist.Foo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TokenFactoryImplTest {
    @Mock
    private TokenRepository tokenRepository;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onstart() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testAfterPropertiesSet()
        throws Exception {
        TokenFactoryImpl factory = new TokenFactoryImpl(null, null);

        ApplicationContext applicationContext = mock(ApplicationContext.class);

        when(applicationContext.containsBean(SingletonBeanFactory.INSTANCE_FACTORY_ID))
            .thenReturn(true);

        InstanceFactory instanceFactory = mock(InstanceFactory.class);

        when(applicationContext.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID))
            .thenReturn(instanceFactory);
        when(applicationContext.containsBean("tokenRepository")).thenReturn(true);
        when(applicationContext.getBean("tokenRepository")).thenReturn(tokenRepository);
        factory.setApplicationContext(applicationContext);
        factory.afterPropertiesSet();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testCreateToken() {
        InstanceFactory instanceFactory = mock(InstanceFactory.class);

        when(instanceFactory.getBean("WorkflowToken")).thenReturn(new WorkflowToken());

        Foo foo = new Foo(123L);

        when(tokenRepository.findByDomain(foo)).thenReturn(null);

        TokenFactoryImpl factory = new TokenFactoryImpl(instanceFactory, tokenRepository);
        ProcessDefinition definition = new ProcessDefinition();

        definition.setId("id");

        StartStation startState = new StartStation();

        startState.setId("start");
        definition.setStartState(startState);

        Foo domainObject = new Foo();

        domainObject.setId(123L);

        factory.setTokenClass(WorkflowToken.class);

        Token token = factory.createToken(definition, domainObject);

        assertTrue("Should be WorkflowToken instance", token instanceof WorkflowToken);

        assertNotNull("Token is null", token);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFoundToken() {
        InstanceFactory instanceFactory = mock(InstanceFactory.class);

        WorkflowToken wtoken = new WorkflowToken();

        Foo foo = new Foo(123L);

        when(tokenRepository.findByDomain(foo)).thenReturn(wtoken);

        TokenFactoryImpl factory = new TokenFactoryImpl(instanceFactory, tokenRepository);

        factory.setTokenClass(WorkflowToken.class);

        ProcessDefinition definition = new ProcessDefinition();

        definition.setId("id");

        StartStation startState = new StartStation();

        startState.setId("start");
        definition.setStartState(startState);

        Foo domainObject = new Foo();

        domainObject.setId(123L);

        Token token = factory.createToken(definition, domainObject);

        assertNotNull("Token is null", token);
        assertTrue("Should be WorkflowToken instance", token instanceof WorkflowToken);
    }
}
