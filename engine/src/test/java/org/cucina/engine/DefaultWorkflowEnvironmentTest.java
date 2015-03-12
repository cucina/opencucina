package org.cucina.engine;

import java.io.ByteArrayInputStream;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DefaultWorkflowEnvironmentTest {
    private static final String RULES_DEFINITION_URL = "org/cucina/engine/definition/config/xml/workflow-rules-definitions.xml";
    private static final String TEST_WF = "<?xml version=\"1.0\"?>" +
        "<process-definition name=\"simple\">" + "<description>Simple processing</description>	" +
        "<start-state id=\"start\"><transition to=\"open\" id=\"toOpen\" default=\"true\"/></start-state>" +
        "<state id=\"open\"><transition to=\"open\" id=\"pigsEar\" privilegeName=\"COMMENT,APPROVAL\"/>" +
        "<transition to=\"end\" id=\"end\" privilegeName=\"APPROVAL\"/></state>" +
        "<end-state id=\"end\"/></process-definition>";
    private boolean stopped;

    /**
     * JAVADOC Method Level Comments
     */
    public void testClose() {
        DefaultProcessEnvironment environment = new DefaultProcessEnvironment();

        assertNull("Should be null, as hasn't been started", environment.isRunning());
        assertFalse("Shouldn't be stopped yet", stopped);

        environment.stop(new Runnable() {
                @Override
                public void run() {
                    stopped = true;
                }
            });

        assertTrue("Should have stopped", environment.isRunning());
        assertTrue("Should have stopped", stopped);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testInit()
        throws Exception {
        DefaultProcessEnvironment environment = new DefaultProcessEnvironment();
        final Resource defres = new ClassPathResource(RULES_DEFINITION_URL);
        ApplicationContext appc = mock(ApplicationContext.class);

        when(appc.getResource(any(String.class))).thenReturn(defres);

        environment.setApplicationContext(appc);

        List<Resource> resources = new ArrayList<Resource>();
        Resource resource = new InputStreamResource(new ByteArrayInputStream(TEST_WF.getBytes()));

        resources.add(resource);
        environment.setDefinitionResources(resources);
        environment.setTokenFactory(mock(TokenFactory.class));
        environment.start();
        assertNotNull("Parser is null", environment.getDefinitionParser());
        assertNotNull("DefinitionRegistry is null", environment.getDefinitionRegistry());
        assertNotNull("ResolverRegistry is null", environment.getBeanResolver());
        assertNotNull("Service is null", environment.getService());
    }
}
