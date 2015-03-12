package org.cucina.engine.service;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.core.validation.Delete;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.cucina.engine.repository.WorkflowRepository;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DefinitionServiceImplTest {
    private DefinitionServiceImpl service;
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private ProcessDefinitionRegistry registry;
    @Mock
    private ProcessEnvironment workflowEnvironment;
    @Mock
    private WorkflowRepository workflowRepository;

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void loadDefinition() {
        ProcessDefinition definition = new ProcessDefinition();

        definition.setId("definition");
        when(registry.findWorkflowDefinition("definition")).thenReturn(definition);

        assertEquals(definition, service.loadDefinition("definition"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(workflowEnvironment.getDefinitionRegistry()).thenReturn(registry);
        service = new DefinitionServiceImpl(workflowEnvironment, instanceFactory, workflowRepository);
    }

    /**
     * JAVADOC Method Level Comments
     * @throws BindException
     */
    @Test
    public void testCreate()
        throws BindException {
        ProcessDefinitionParser parser = mock(ProcessDefinitionParser.class);
        ProcessDefinition definition = new ProcessDefinition();

        definition.setId("definition");
        when(parser.parse(any(Resource.class))).thenReturn(definition);
        when(workflowEnvironment.getDefinitionParser()).thenReturn(parser);

        WorkflowHistory wh = new WorkflowHistory();

        when(instanceFactory.getBean(WorkflowHistory.class.getSimpleName())).thenReturn(wh);

        Workflow w = new Workflow();

        when(instanceFactory.getBean(Workflow.class.getSimpleName())).thenReturn(w);

        Attachment attachment = new Attachment();

        when(instanceFactory.getBean(Attachment.class.getSimpleName())).thenReturn(attachment);

        DefinitionServiceImpl service = new DefinitionServiceImpl(workflowEnvironment,
                instanceFactory, workflowRepository);

        byte[] content = "hello".getBytes();

        service.create("hello", "text/xml", content);
    }

    /**
     * JAVADOC Method Level Comments
     * @throws BindException
     */

    /*@Test
    public void testDelete()
        throws BindException {
        Workflow workflow = new Workflow();

        when(workflowRepository.loadByWorkflowId("100L")).thenReturn(workflow);

        DefinitionServiceImpl service = new DefinitionServiceImpl(workflowEnvironment,
                instanceFactory, workflowRepository);

        service.delete("100L");
        verify(workflowRepository).delete("100L");
    }*/

    /**
     * JAVADOC Method Level Comments
     *
     * @throws BindException JAVADOC.
     */
    @Test(expected = BindException.class)
    public void testDeleteFailedValidation()
        throws BindException {
        Workflow workflow = new Workflow();

        when(workflowRepository.loadByWorkflowId("100L")).thenReturn(workflow);

        Validator validator = mock(Validator.class);

        Set<ConstraintViolation<Workflow>> errors = new HashSet<ConstraintViolation<Workflow>>();
        @SuppressWarnings("unchecked")
        ConstraintViolation<Workflow> e = mock(ConstraintViolation.class);

        when(e.getMessageTemplate()).thenReturn("mess {0}");
        when(e.getLeafBean()).thenReturn(workflow);
        when(e.getMessage()).thenReturn("Mess");

        errors.add(e);

        when(validator.validate(workflow, Delete.class)).thenReturn(errors);
        service.setValidator(validator);
        service.delete("100L");
    }

    /**
     * JAVADOC Method Level Comments
     * @throws BindException
     */
    @Test
    public void testUpdate()
        throws BindException {
        ProcessDefinitionParser parser = mock(ProcessDefinitionParser.class);
        ProcessDefinition definition = new ProcessDefinition();

        definition.setId("definition");
        when(parser.parse(any(Resource.class))).thenReturn(definition);
        when(workflowEnvironment.getDefinitionParser()).thenReturn(parser);

        WorkflowHistory wh = new WorkflowHistory();

        when(instanceFactory.getBean(WorkflowHistory.class.getSimpleName())).thenReturn(wh);

        Attachment attachment = new Attachment();

        when(instanceFactory.getBean(Attachment.class.getSimpleName())).thenReturn(attachment);

        Workflow w = new Workflow();

        w.setWorkflowId("definition");

        when(workflowRepository.loadByWorkflowId("100L")).thenReturn(w);

        byte[] content = "hello".getBytes();

        service.update("100L", "hello", "text/xml", content);
        verify(workflowRepository).save(w);
    }
}
