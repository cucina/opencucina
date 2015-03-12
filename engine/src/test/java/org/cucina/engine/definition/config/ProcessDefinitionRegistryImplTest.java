package org.cucina.engine.definition.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.cucina.engine.repository.WorkflowRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProcessDefinitionRegistryImplTest {
    @Mock
    private InstanceFactory instanceFactory;
    private ProcessDefinitionRegistryImpl registry;
    @Mock
    private ProcessEnvironment workflowEnvironment;
    @Mock
    private WorkflowRepository workflowRepository;

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void findWorkflowSource() {
        Workflow wf = new Workflow();
        WorkflowHistory history = new WorkflowHistory();
        Attachment attachment = new Attachment();

        attachment.setData("Hello".getBytes());
        history.setAttachment(attachment);
        wf.addHistory(history);
        wf.setWorkflowId("test");
        when(workflowRepository.loadByWorkflowId("test")).thenReturn(wf);

        assertEquals("Hello", registry.findWorkflowSource("test"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void listWorkflowDefinitionIds() {
        Collection<String> list = new ArrayList<String>();

        list.add("a");
        when(workflowRepository.listAll()).thenReturn(list);

        assertTrue(registry.listWorkflowDefinitionIds().contains("a"));
    }

    /**
     * register definition
     */
    @Test
    public void registerWorkflowDefinition() {
        ProcessDefinition wd = new ProcessDefinition();

        wd.setId("test");

        registry.registerWorkflowDefinition(wd);

        assertEquals("Should have set definition", wd, registry.findWorkflowDefinition("test"));
    }

    /**
     * whens id to be set correctly on definition
     */
    @Test(expected = IllegalArgumentException.class)
    public void registerWorkflowDefinitionNoId() {
        ProcessDefinition wd = new ProcessDefinition();

        registry.registerWorkflowDefinition(wd);
        registry.findWorkflowDefinition("test");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registry = new ProcessDefinitionRegistryImpl(workflowEnvironment, instanceFactory,
                workflowRepository);
    }

    /**
     * JAVADOC Method Level Comments
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

        when(workflowEnvironment.getDefinitionParser()).thenReturn(parser);
        when(workflowRepository.exists("test")).thenReturn(false);
        when(workflowRepository.loadByWorkflowId("nottest")).thenReturn(null);
        when(instanceFactory.getBean("WorkflowHistory")).thenReturn(new WorkflowHistory());

        Workflow workflow = new Workflow();

        when(instanceFactory.getBean("Workflow")).thenReturn(workflow);
        when(instanceFactory.getBean(Attachment.class.getSimpleName())).thenReturn(new Attachment());

        registry.readWorkflowDefinitions(resources);
        assertNotNull(registry.findWorkflowDefinition("test"));
        assertNull(registry.findWorkflowDefinition("nottest"));

        try {
            registry.registerWorkflowDefinition(null);
            fail("Should not allow for null entries");
        } catch (RuntimeException ex) {
            // success
        }

        verify(workflowRepository).save(workflow);
    }
}
