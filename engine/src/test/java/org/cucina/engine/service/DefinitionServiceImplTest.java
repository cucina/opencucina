package org.cucina.engine.service;

import org.springframework.validation.BindException;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
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
        service = new DefinitionServiceImpl(registry, instanceFactory);
    }

    /**
     * JAVADOC Method Level Comments
     * @throws BindException
     */
    @Test
    public void testCreate()
        throws BindException {
        WorkflowHistory wh = new WorkflowHistory();

        when(instanceFactory.getBean(WorkflowHistory.class.getSimpleName())).thenReturn(wh);

        Workflow w = new Workflow();

        when(instanceFactory.getBean(Workflow.class.getSimpleName())).thenReturn(w);

        Attachment attachment = new Attachment();

        when(instanceFactory.getBean(Attachment.class.getSimpleName())).thenReturn(attachment);

        byte[] content = "hello".getBytes();

        service.create("hello", "text/xml", content);
    }

    /**
     * JAVADOC Method Level Comments
     * @throws BindException
     */
    @Test
    public void testDelete()
        throws BindException {
        service.delete("100L");
        verify(registry).delete("100L");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws BindException JAVADOC.
     */

    /*@Test(expected = BindException.class)
    public void testDeleteFailedValidation()
        throws BindException {
        Workflow workflow = new Workflow();

        when(workflowRepository.findByWorkflowId("100L")).thenReturn(workflow);

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
    }*/

    /**
     * JAVADOC Method Level Comments
     * @throws BindException
     */
    @Test
    public void testUpdate()
        throws BindException {
        Attachment attachment = new Attachment();

        when(instanceFactory.getBean(Attachment.class.getSimpleName())).thenReturn(attachment);

        byte[] content = "hello".getBytes();

        service.update("hello", "text/xml", content);
        verify(registry).updateProcess(attachment);
    }
}
