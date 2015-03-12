package org.cucina.engine.definition.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.cucina.engine.definition.ProcessDefinition;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MapBasedProcessDefinitionRegistryTest {
    /**
     * Test method for 'org.cucina.opvantage.workflow.definition.config.MapBasedWorkflowDefinitionRegistry.findWorkflowDefinition(String)'
     */
    @Test
    public void testFindWorkflowDefinition() {
        List<Resource> resources = new ArrayList<Resource>();
        byte[] ab = "a".getBytes();
        Resource ar = new ByteArrayResource(ab);

        resources.add(ar);

        ProcessDefinition wd = new ProcessDefinition();

        wd.setId("test");

        ProcessDefinitionParser parser = mock(ProcessDefinitionParser.class);

        when(parser.parse(ar)).thenReturn(wd);

        MapBasedProcessDefinitionRegistry reg = new MapBasedProcessDefinitionRegistry(parser);

        reg.readWorkflowDefinitions(resources);
        assertNotNull(reg.findWorkflowDefinition("test"));
        assertNull(reg.findWorkflowDefinition("nottest"));

        try {
            reg.registerWorkflowDefinition(null);
            fail("Should not allow for null entries");
        } catch (RuntimeException ex) {
            // success
        }
    }

    /**
     * register definition
     */
    @Test
    public void updateDefinition() {
        ProcessDefinition wd = new ProcessDefinition();

        wd.setId("test");

        MapBasedProcessDefinitionRegistry reg = new MapBasedProcessDefinitionRegistry(mock(ProcessDefinitionParser.class));

        reg.registerWorkflowDefinition(wd);

        assertEquals("Should have set definition", wd, reg.findWorkflowDefinition("test"));
    }

    /**
     * whens id to be set correctly on definition
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateDefinitionNoId() {
        ProcessDefinition wd = new ProcessDefinition();

        MapBasedProcessDefinitionRegistry reg = new MapBasedProcessDefinitionRegistry(mock(ProcessDefinitionParser.class));

        reg.registerWorkflowDefinition(wd);

        reg.findWorkflowDefinition("test");
    }
}
