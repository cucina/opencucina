package org.cucina.engine.event;

import java.util.HashMap;
import java.util.Map;

import org.cucina.engine.CheckNotMetException;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.SignalFailedException;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.DomainRepository;
import org.cucina.engine.repository.TokenRepository;
import org.cucina.engine.service.ProcessService;
import org.cucina.engine.testassist.Foo;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TransitionEventListenerTest {
    private static final String TRANSITION_ID = "transition";
    @Mock
    private DomainRepository domainRepository;
    private Foo domain;
    private Map<String, Object> params;
    @Mock
    private ProcessEnvironment workflowEnvironment;
    @Mock
    private ProcessService workflowService;
    private ProcessToken token;
    @Mock
    private TokenRepository tokenRepository;
    private TransitionEventListener listener;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(workflowEnvironment.getService()).thenReturn(workflowService);
        listener = new TransitionEventListener(workflowEnvironment, tokenRepository,
                domainRepository);
        domain = new Foo(1L);
        token = new ProcessToken();
        params = new HashMap<String, Object>();
        doReturn(domain).when(domainRepository).load("Foo", domain.getId());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testOnApplicationEvent() {
        when(tokenRepository.findByDomain(domain)).thenReturn(token);
        when(workflowService.executeTransition(token, TRANSITION_ID, params)).thenReturn(token);

        listener.onApplicationEvent(new TransitionEvent(TRANSITION_ID, "notNeededButMaybeLater",
                "Foo", domain.getId(), params));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = CheckNotMetException.class)
    public void testOnApplicationEventConditionNotMet() {
        when(tokenRepository.findByDomain(domain)).thenReturn(token);
        when(workflowService.executeTransition(token, TRANSITION_ID, params))
            .thenThrow(new CheckNotMetException("ovd"));

        listener.onApplicationEvent(new TransitionEvent(TRANSITION_ID, "notNeededButMaybeLater",
                "Foo", domain.getId(), params));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnApplicationEventNoDomain() {
        try {
            listener.onApplicationEvent(new TransitionEvent(TRANSITION_ID,
                    "notNeededButMaybeLater", "Foo", domain.getId(), params));
        } finally {
        }
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnApplicationEventNoToken() {
        when(tokenRepository.findByDomain(domain)).thenReturn(null);

        listener.onApplicationEvent(new TransitionEvent(TRANSITION_ID, "notNeededButMaybeLater",
                "Foo", domain.getId(), params));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = SignalFailedException.class)
    public void testOnApplicationEventSignalFailedException() {
        when(tokenRepository.findByDomain(domain)).thenReturn(token);
        when(workflowService.executeTransition(token, TRANSITION_ID, params))
            .thenThrow(new SignalFailedException("ovd"));

        listener.onApplicationEvent(new TransitionEvent(TRANSITION_ID, "notNeededButMaybeLater",
                "Foo", domain.getId(), params));
    }
}
