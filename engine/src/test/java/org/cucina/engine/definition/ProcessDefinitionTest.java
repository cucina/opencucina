package org.cucina.engine.definition;

import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.testadapters.MockProcessDefinitionBuilder;
import org.cucina.engine.testadapters.ProcessEnvironmentFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ProcessDefinitionTest {
	private ProcessDefinition wf;
	@Mock
	private ProcessDefinitionParser parser;
	@Mock
	private ProcessDefinitionRegistry definitionRegistry;

	/**
	 * JAVADOC.
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		wf = MockProcessDefinitionBuilder.buildHelloWorldDefinition();

		List<Resource> resources = new ArrayList<Resource>();
		byte[] ab = "a".getBytes();
		Resource ar = new ByteArrayResource(ab);

		resources.add(ar);

		when(parser.parse(ar)).thenReturn(wf);
		when(definitionRegistry.findWorkflowDefinition("helloWorld")).thenReturn(wf);
		ProcessEnvironmentFactory.buildEnvironment(null, definitionRegistry, parser, null, resources);
	}

	/**
	 * JAVADOC.
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testSerialization()
			throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(wf);

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		ProcessDefinition tr = (ProcessDefinition) ois.readObject();

		assertNotNull("failed to read", tr);

		//        System.err.println("After:" + tr);
	}
}
