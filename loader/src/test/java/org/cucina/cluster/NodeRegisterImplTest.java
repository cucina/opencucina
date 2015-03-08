package org.cucina.cluster;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cucina.cluster.model.ClusterControl;
import org.cucina.cluster.repository.ClusterControlRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class NodeRegisterImplTest {
    @Mock
    private ClusterControlRepository clusterControlRepository;
    private NodeRegisterImpl nodeRegister;

    /**
    * JAVADOC Method Level Comments
    */
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        nodeRegister = new NodeRegisterImpl(clusterControlRepository);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testOutstandingEvents() {
        ClusterControl clusterControl = new ClusterControl();

        clusterControl.setId(1L);
        clusterControl.setEvent("event");
        when(clusterControlRepository.findByEventNameAndCurrent("event")).thenReturn(clusterControl)
            .thenReturn(null);
        when(clusterControlRepository.deleteByEventName("event")).thenReturn(true);
        when(clusterControlRepository.create("event", "nodeId", null)).thenReturn(true);
        when(clusterControlRepository.find(clusterControl.getId())).thenReturn(clusterControl);

        Collection<ClusterControl> controls = nodeRegister.outstandingEvents("nodeId");

        assertNotNull("Controls are null", controls);
        assertFalse(controls.contains(clusterControl));
        nodeRegister.reset("event");
        // to get it to breach the threshold
        controls = nodeRegister.outstandingEvents("nodeId");
        controls = nodeRegister.outstandingEvents("nodeId");
        assertTrue(controls.contains(clusterControl));
        nodeRegister.remove("event");
        controls = nodeRegister.outstandingEvents("nodeId");
        assertFalse(controls.contains(clusterControl));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testRefresh() {
        String myNodeId = "nodeId";
        ClusterControl control = new ClusterControl();
        String event1 = "event1";

        control.setEvent(event1);

        ClusterControl control2 = new ClusterControl();
        String event2 = "event2";

        control2.setEvent(event2);
        when(clusterControlRepository.findCurrentByNotThisNode("nodeId"))
            .thenReturn(Arrays.asList(new ClusterControl[] { control, control2, null }));

        Collection<String> eventCollection = nodeRegister.refresh(myNodeId);

        assertTrue(eventCollection.contains(event1));
        assertTrue(eventCollection.contains(event2));
        verify(clusterControlRepository).findByEventNameAndCurrent(event1);
        verify(clusterControlRepository).findByEventNameAndCurrent(event2);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSwitchActivePassive() {
        ClusterControl control = new ClusterControl();

        control.setEvent("event");
        // should only call it once before the reset called
        when(clusterControlRepository.findByEventNameAndCurrent("event")).thenReturn(null)
            .thenReturn(control).thenReturn(null).thenReturn(control);

        Map<Object, Object> properties = new HashMap<Object, Object>();

        when(clusterControlRepository.create("event", "nodeId", properties)).thenReturn(false)
            .thenReturn(true);

        ClusterControl cuc = new ClusterControl();

        cuc.setEvent("event");
        assertFalse("Should be in passive mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));
        assertTrue("Should be in active mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));

        verify(clusterControlRepository, times(2)).create("event", "nodeId", properties);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSwitchActivePassiveGoesActiveButStopsAsTaskHadCompleted() {
        Map<Object, Object> properties = Collections.singletonMap(new Object(), new Object());

        ClusterControl control1 = new ClusterControl();

        control1.setId(1L);
        control1.setEvent("event");
        control1.setProperties(properties);

        ClusterControl control2 = new ClusterControl();

        control2.setId(1L);
        control2.setEvent("event");
        control2.setComplete(true);
        control2.setProperties(properties);

        when(clusterControlRepository.findByEventNameAndCurrent("event")).thenReturn(control1)
            .thenReturn(control1).thenReturn(control1).thenReturn(control1).thenReturn(null);

        when(clusterControlRepository.find(control1.getId())).thenReturn(control2);
        when(clusterControlRepository.create("event", "nodeId", properties)).thenReturn(true);

        nodeRegister.setToleranceThreshold(1);

        ClusterControl cuc = new ClusterControl();

        cuc.setId(3L);
        cuc.setEvent("event");
        assertFalse("Should be in passive mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));

        Collection<ClusterControl> events = nodeRegister.outstandingEvents("nodeId");

        assertTrue(events.isEmpty());

        assertFalse("Should be in passive mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));
        events = nodeRegister.outstandingEvents("nodeId");
        assertTrue(events.isEmpty());
        assertFalse("Should be in passive mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));
        events = nodeRegister.outstandingEvents("nodeId");
        assertTrue(events.isEmpty());

        verify(clusterControlRepository, never()).deleteByEventName("event");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSwitchActivePassiveGoesActiveForcesDelete() {
        Map<Object, Object> properties = Collections.singletonMap(new Object(), new Object());

        ClusterControl control1 = new ClusterControl();

        control1.setId(1L);
        control1.setEvent("event");
        control1.setProperties(properties);

        when(clusterControlRepository.findByEventNameAndCurrent("event")).thenReturn(control1)
            .thenReturn(control1).thenReturn(control1).thenReturn(control1).thenReturn(null);

        when(clusterControlRepository.find(control1.getId())).thenReturn(control1);
        when(clusterControlRepository.create("event", "nodeId", properties)).thenReturn(true);

        nodeRegister.setToleranceThreshold(1);

        ClusterControl cuc = new ClusterControl();

        cuc.setId(3L);
        cuc.setEvent("event");
        assertFalse("Should be in passive mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));

        Collection<ClusterControl> events = nodeRegister.outstandingEvents("nodeId");

        assertTrue(events.isEmpty());

        assertFalse("Should be in passive mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));
        events = nodeRegister.outstandingEvents("nodeId");
        assertTrue(events.isEmpty());
        assertFalse("Should be in passive mode",
            nodeRegister.switchActivePassive("event", "nodeId", properties));
        events = nodeRegister.outstandingEvents("nodeId");
        assertFalse(events.isEmpty());

        verify(clusterControlRepository).deleteByEventName("event");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSwitchActivePassiveReturnsFalse() {
        when(clusterControlRepository.findByEventNameAndCurrent("event"))
            .thenReturn(new ClusterControl());

        assertFalse(nodeRegister.switchActivePassive("event", "nodeId", null));
        verify(clusterControlRepository, times(2)).findByEventNameAndCurrent("event");
    }
}
