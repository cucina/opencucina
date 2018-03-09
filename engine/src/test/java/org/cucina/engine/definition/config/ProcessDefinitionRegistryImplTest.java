package org.cucina.engine.definition.config;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
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
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProcessDefinitionRegistryImplTest {
	@Mock
	private InstanceFactory instanceFactory;
	private ProcessDefinition wd;
	@Mock
	private ProcessDefinitionParser parser;
	private ProcessDefinitionRegistryImpl registry;
	@Mock
	private WorkflowRepository workflowRepository;
	private byte[] data = "XXX".getBytes();

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
		when(workflowRepository.findByWorkflowId("test")).thenReturn(wf);

		assertEquals("Hello", registry.getWorkflowSource("test"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void listWorkflowDefinitionIds() {
		Collection<String> list = new ArrayList<String>();

		list.add("a");
		when(workflowRepository.findAllIds()).thenReturn(list);

		assertTrue(registry.listWorkflowDefinitionIds().contains("a"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		registry = new ProcessDefinitionRegistryImpl(instanceFactory, workflowRepository, parser);
		wd = new ProcessDefinition();

		wd.setId("test");

		when(parser.parse(any(ByteArrayResource.class))).thenReturn(wd);
	}

	/**
	 * register definition
	 */
	@Test
	public void testSaveProcess()
			throws BindException {
		when(instanceFactory.getBean(WorkflowHistory.class.getSimpleName()))
				.thenReturn(new WorkflowHistory());

		Workflow workflow = new Workflow();

		when(instanceFactory.getBean(Workflow.class.getSimpleName())).thenReturn(workflow);

		Attachment attachment = new Attachment();

		attachment.setFilename("filename");
		attachment.setData(data);

		when(parser.parse(any(ByteArrayResource.class))).thenReturn(wd);
		registry.saveProcess(attachment);

		assertEquals("Should have set definition", wd, registry.findWorkflowDefinition("test"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testFindWorkflowDefinition()
			throws Exception {
		List<Resource> resources = new ArrayList<Resource>();
		byte[] ab = "a".getBytes();
		Resource ar = new ByteArrayResource(ab);

		resources.add(ar);

		ProcessDefinition wd = new ProcessDefinition();

		wd.setId("test");

		when(parser.parse(ar)).thenReturn(wd);
		when(workflowRepository.exists("test")).thenReturn(false);
		when(workflowRepository.findByWorkflowId("nottest")).thenReturn(null);
		when(instanceFactory.getBean(WorkflowHistory.class.getSimpleName()))
				.thenReturn(new WorkflowHistory());

		Workflow workflow = new Workflow();

		when(instanceFactory.getBean(Workflow.class.getSimpleName())).thenReturn(workflow);
		when(instanceFactory.getBean(Attachment.class.getSimpleName())).thenReturn(new Attachment());

		registry.readWorkflowDefinitions(resources);
		assertNotNull(registry.findWorkflowDefinition("test"));
		assertNull(registry.findWorkflowDefinition("nottest"));

		try {
			registry.saveProcess(null);
			fail("Should not allow for null entries");
		} catch (RuntimeException ex) {
			// success
		}

		verify(workflowRepository).save(workflow);
	}
}
