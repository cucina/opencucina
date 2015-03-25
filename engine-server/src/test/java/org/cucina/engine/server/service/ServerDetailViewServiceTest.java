package org.cucina.engine.server.service;

import java.util.HashMap;
import java.util.Map;

import org.cucina.engine.server.converter.DetailViewConfig;
import org.cucina.engine.server.event.workflow.ValueEvent;
import org.cucina.engine.server.repository.EntityDescriptorRepository;
import org.cucina.engine.server.testassist.Foo;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Tests that ServerDetailViewService functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ServerDetailViewServiceTest {
    private static final String SERVER_NAME = "serve";
    private static final String APP_NAME = "mine";
    @Mock
    private EntityDescriptorRepository entityDescriptorRepository;
    private ServerDetailViewService service;

    /**
     * Sends request for data.
     */
    @Test
    public void getData() {
        Map<String, Object> result = new HashMap<String, Object>();
        ValueEvent event = new ValueEvent(APP_NAME);

        event.setValue(result);

        //  assertEquals(result, service.getData(2L, Foo.TYPE, APP_NAME));
    }

    /**
     * Requires applicationName as arg
     */
    @Test(expected = IllegalArgumentException.class)
    public void getDataRequiresApplicationName() {
        service.getData(2L, Foo.TYPE, null);
    }

    /**
     * Requires id as arg
     */
    @Test(expected = IllegalArgumentException.class)
    public void getDataRequiresId() {
        service.getData(null, Foo.TYPE, APP_NAME);
    }

    /**
     * Requires type as arg
     */
    @Test(expected = IllegalArgumentException.class)
    public void getDataRequiresType() {
        service.getData(2L, null, APP_NAME);
    }

    /**
     * Sends request for data but incorrect class returned.
     */
    @Test
    public void getDataResultNotMap() {
        assertNull("Should return null as did not return map",
            service.getData(2L, Foo.TYPE, APP_NAME));
    }

    /**
     * Sends request for data view config.
     */
    @Test
    public void getDetailViewConfig() {
        DetailViewConfig result = new DetailViewConfig();
        ValueEvent event = new ValueEvent(APP_NAME);

        event.setValue(result);

        //        assertEquals(result, service.getDetailViewConfig(Foo.TYPE, APP_NAME));
    }

    /**
     * Requires applicationName as arg
     */
    @Test(expected = IllegalArgumentException.class)
    public void getDetailViewConfigRequiresApplicationName() {
        service.getDetailViewConfig(Foo.TYPE, null);
    }

    /**
     * Requires type as arg
     */
    @Test(expected = IllegalArgumentException.class)
    public void getDetailViewConfigRequiresType() {
        service.getDetailViewConfig(null, APP_NAME);
    }

    /**
     * Sets up for test.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new ServerDetailViewService(entityDescriptorRepository);
        service.setServerApplicationName(SERVER_NAME);
    }
}
