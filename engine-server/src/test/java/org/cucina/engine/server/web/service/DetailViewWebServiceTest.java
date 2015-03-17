package org.cucina.engine.server.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cucina.engine.server.converter.DetailViewConfig;
import org.cucina.engine.server.converter.DetailViewPane;
import org.cucina.engine.server.service.DetailViewService;
import org.cucina.engine.server.testassist.Foo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * Test that DetailViewService functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DetailViewWebServiceTest {
    @Mock
    private DetailViewService detailViewService;
    private DetailViewWebService service;

    /**
     * Loads domain config as expected.
     */
    @Test
    public void loadDomainConfig() {
        List<String> properties1 = new ArrayList<String>();

        properties1.add("prop1");
        properties1.add("prop2");

        DetailViewPane pane1 = new DetailViewPane();

        pane1.setTitle("title1");
        pane1.setProperties(properties1);

        List<String> properties2 = new ArrayList<String>();

        properties2.add("prop3");

        DetailViewPane pane2 = new DetailViewPane();

        pane2.setTitle("title2");
        pane2.setProperties(properties2);

        List<DetailViewPane> panes = new ArrayList<DetailViewPane>();

        panes.add(pane1);
        panes.add(pane2);

        DetailViewConfig config = new DetailViewConfig(Foo.TYPE, panes);

        when(detailViewService.getDetailViewConfig(Foo.TYPE, "mikes")).thenReturn(config);

        List<Map<String, Object>> results = service.readDomainConfig(Foo.TYPE, "mikes");

        assertEquals("Incorrect number of panes", 2, results.size());

        Map<String, Object> pane = results.get(0);

        assertEquals("Incorrect number of name value pairs", 2, pane.size());
        assertEquals("Incorrect title", "title1", pane.get("title"));
        assertEquals("Incorrect properties", properties1, pane.get("properties"));
        pane = results.get(1);
        assertEquals("Incorrect number of name value pairs", 2, pane.size());
        assertEquals("Incorrect title", "title2", pane.get("title"));
        assertEquals("Incorrect properties", properties2, pane.get("properties"));
    }

    /**
     * Returns null if no DetailViewConfig is returned by DetailViewService.
     */
    @Test
    public void loadDomainConfigNull() {
        when(detailViewService.getDetailViewConfig(Foo.TYPE, "mikes")).thenReturn(null);

        assertNull("Should not have returned anything", service.readDomainConfig(Foo.TYPE, "mikes"));
    }

    /**
     * Tests that domain data is loaded as expected.
     */
    @Test
    public void loadDomainData() {
        //linked hashmap to preserve order for validation later
        Map<String, Object> results = new LinkedHashMap<String, Object>();

        results.put("name1", "value1");
        results.put("name2", "value2");

        when(detailViewService.getData(12L, Foo.TYPE, "mikes")).thenReturn(results);

        List<Map<String, Object>> serResults = service.readDomainData(12L, Foo.TYPE, "mikes");

        assertEquals("Incorrect number of results", 2, serResults.size());

        Map<String, Object> result = serResults.get(0);

        assertEquals("Incorrect name", "name1", result.get("name"));
        assertEquals("Incorrect value", "value1", result.get("value"));
        result = serResults.get(1);
        assertEquals("Incorrect name", "name2", result.get("name"));
        assertEquals("Incorrect value", "value2", result.get("value"));
    }

    /**
     * If empty domain data is returned by DetailViewService an empty list is returned.
     */
    @Test
    public void loadDomainDataEmpty() {
        when(detailViewService.getData(12L, Foo.TYPE, "mikes"))
            .thenReturn(new HashMap<String, Object>());

        List<Map<String, Object>> results = service.readDomainData(12L, Foo.TYPE, "mikes");

        assertEquals("Should return empty results ", 0, results.size());
    }

    /**
     * If no domain data is returned by DetailViewService an empty list is returned.
     */
    @Test
    public void loadDomainDataNull() {
        when(detailViewService.getData(12L, Foo.TYPE, "mikes")).thenReturn(null);

        List<Map<String, Object>> results = service.readDomainData(12L, Foo.TYPE, "mikes");

        assertEquals("Should return empty results ", 0, results.size());
    }

    /**
     * Sets up for test.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new DetailViewWebService(detailViewService);
    }
}
