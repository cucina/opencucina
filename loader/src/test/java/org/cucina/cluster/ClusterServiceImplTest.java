
package org.cucina.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cucina.cluster.event.ClusterProcessEvent;
import org.cucina.cluster.model.ClusterControl;
import org.cucina.core.service.ContextService;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ClusterServiceImplTest {
    @Mock
    private ActiveClusterNodeService activeClusterNodeService;
    @Mock
    private ContextService contextService;
    @Mock
    private HeartbeatService heartbeatService;
    @Mock
    private NodeRegister nodeRegister;

    /**
    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void before() {
        LoggingEnabler.enableLog(ClusterServiceImpl.class);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testCheckOutstanding() {
        Collection<ClusterControl> events = new ArrayList<ClusterControl>();
        ClusterControl control = new ClusterControl();

        control.setEvent("event");

        events.add(control);

        Map<Object, Object> properties = new HashMap<Object, Object>();

        properties.put(1L, 2L);
        control.setProperties(properties);
        when(nodeRegister.outstandingEvents(any(String.class))).thenReturn(events);

        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService, new ClusterNode());

        service.checkOutstanding();
        verify(nodeRegister).outstandingEvents(any(String.class));
        verify(activeClusterNodeService)
            .executeActive(eq("event"), any(String.class), eq(properties));
        verify(heartbeatService).stop("event");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testComplete() {
        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService, new ClusterNode());
        ApplicationEvent completeEvent = mock(ApplicationEvent.class);

        service.setCompleteEvent(completeEvent);

        ApplicationContext ac = mock(ApplicationContext.class);

        service.setApplicationContext(ac);
        service.complete("eventName");
        verify(nodeRegister).remove("eventName");
        verify(ac).publishEvent(completeEvent);
        verify(heartbeatService).stop("eventName");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMyNodeId() {
        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService, new ClusterNode());

        String id = service.myNodeId();

        assertEquals(id, service.myNodeId());

        service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService, heartbeatService,
                  new ClusterNode());
        assertNotEquals(id, service.myNodeId());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnApplicationEvent() {
        doThrow(new NullPointerException()).when(contextService).putAll(null);

        when(nodeRegister.switchActivePassive(eq("active"), any(String.class), any(Map.class)))
            .thenReturn(true);
        when(nodeRegister.switchActivePassive(eq("passive"), any(String.class), any(Map.class)))
            .thenReturn(false);

        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService,  new ClusterNode());

        ClusterProcessEvent event = new ClusterProcessEvent("active");

        event.addProperty("1", 2L);

        ApplicationEvent startEvent = mock(ApplicationEvent.class);

        service.setStartEvent(startEvent);

        ApplicationContext ac = mock(ApplicationContext.class);

        service.setApplicationContext(ac);

        service.onApplicationEvent(event);

        ClusterProcessEvent event2 = new ClusterProcessEvent("passive");

        service.onApplicationEvent(event2);
        verify(nodeRegister, times(2))
            .switchActivePassive(any(String.class), any(String.class), any(Map.class));
        verify(ac, times(2)).publishEvent(startEvent);
        verify(activeClusterNodeService)
            .executeActive(eq("active"), any(String.class), eq(event.getProperties()));
        verify(heartbeatService).stop("active");
        verify(heartbeatService).start(eq("passive"), any(String.class));
        verify(contextService, never()).putAll(null);
    }
    
    
   
    
    @Test
    public void testRefresh() {
        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService,  new ClusterNode());
		when(nodeRegister.refresh(anyString())).thenReturn(Arrays.asList(new String[] {"x","y"}));
    	
		service.refresh();
    	
    	verify(heartbeatService, times(1)).stopAll();

    	verify(heartbeatService).start(eq("x"), anyString());
    	verify(heartbeatService).start(eq("y"), anyString());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testHandleProcessEvent() {
        doThrow(new NullPointerException()).when(contextService).putAll(null);

        when(nodeRegister.switchActivePassive(eq("active"), any(String.class), any(Map.class)))
            .thenReturn(true);
        when(nodeRegister.switchActivePassive(eq("passive"), any(String.class), any(Map.class)))
            .thenReturn(false);

        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService,  new ClusterNode());

        ClusterProcessEvent event = new ClusterProcessEvent("active");

        event.addProperty("1", 2L);

        ApplicationEvent startEvent = mock(ApplicationEvent.class);

        service.setStartEvent(startEvent);

        ApplicationContext ac = mock(ApplicationContext.class);

        service.setApplicationContext(ac);

        service.handle(event);

        ClusterProcessEvent event2 = new ClusterProcessEvent("passive");

        service.handle(event2);
        verify(nodeRegister, times(2))
            .switchActivePassive(any(String.class), any(String.class), any(Map.class));
        verify(ac, times(2)).publishEvent(startEvent);
        verify(activeClusterNodeService)
            .executeActive(eq("active"), any(String.class), eq(event.getProperties()));
        verify(heartbeatService).stop("active");
        verify(heartbeatService).start(eq("passive"), any(String.class));
        verify(contextService, never()).putAll(null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnApplicationEventWithProperties() {
        when(nodeRegister.switchActivePassive(eq("active"), any(String.class),
                (Map<Object, Object>) eq(null))).thenReturn(true);
        when(nodeRegister.switchActivePassive(eq("passive"), any(String.class),
                (Map<Object, Object>) eq(null))).thenReturn(false);

        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService,  new ClusterNode());

        ClusterProcessEvent event = new ClusterProcessEvent("active");

        event.addProperty("hi", "there");

        ApplicationEvent startEvent = mock(ApplicationEvent.class);

        service.setStartEvent(startEvent);

        ApplicationContext ac = mock(ApplicationContext.class);

        service.setApplicationContext(ac);

        service.onApplicationEvent(event);

        verify(nodeRegister)
            .switchActivePassive(eq("active"), any(String.class), eq(event.getProperties()));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testResetByHeartbeat() {
        ClusterServiceImpl service = new ClusterServiceImpl(nodeRegister, activeClusterNodeService,
                heartbeatService,  new ClusterNode());

        service.resetByHeartbeat("eventName");
        verify(nodeRegister).reset("eventName");
        verify(heartbeatService).start(eq("eventName"), any(String.class));
    }
}
