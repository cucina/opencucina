package org.cucina.engine.client.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.BeanResolver;

import org.cucina.engine.client.Check;
import org.cucina.engine.client.Operation;
import org.cucina.engine.client.testassist.Foo;
import org.cucina.engine.server.definition.CheckDescriptorDto;
import org.cucina.engine.server.definition.OperationDescriptorDto;
import org.cucina.engine.server.event.ActionResultEvent;
import org.cucina.engine.server.event.BooleanEvent;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.EngineEvent;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ProcessEventHandlerTest {
    private static final String APPLICATION_NAME = "an";
    @Mock
    private BeanResolver beanResolver;
    @Mock
    private Check check;
    @Mock
    private DomainFindingService domainFindingService;
    private Foo foo;
    @Mock
    private Operation action;
    private ProcessEventHandler handler;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new ProcessEventHandler(beanResolver, domainFindingService);
        foo = new Foo();

        when(domainFindingService.find("Foo", 100L)).thenReturn(foo);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testHandleCheckEvent()
        throws Exception {
        CheckDescriptorDto element = new CheckDescriptorDto();

        element.setPath("path");
        element.setDomainId(100L);
        element.setDomainType("Foo");

        when(beanResolver.resolve(null, "path")).thenReturn(check);

        Map<String, Object> parameters = new HashMap<String, Object>();

        when(check.test(foo, parameters)).thenReturn(true);

        CallbackEvent event = new CallbackEvent(element, parameters, APPLICATION_NAME);
        EngineEvent re = handler.handleEvent(event);

        assertTrue(((BooleanEvent) re).isResult());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testHandleOperationEvent()
        throws Exception {
        OperationDescriptorDto element = new OperationDescriptorDto();

        element.setPath("path");
        element.setDomainId(100L);
        element.setDomainType("Foo");

        when(beanResolver.resolve(null, "path")).thenReturn(action);

        Map<String, Object> parameters = new HashMap<String, Object>();
        CallbackEvent event = new CallbackEvent(element, parameters, APPLICATION_NAME);
        EngineEvent re = handler.handleEvent(event);

        System.err.println(((ActionResultEvent) re).getSource());
    }
}
