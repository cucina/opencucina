package org.cucina.engine.definition.config.xml;

import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.expression.BeanResolver;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.checks.AbstractCheck;
import org.cucina.engine.definition.AbstractState;
import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.operations.AbstractOperation;
import org.cucina.engine.testadapters.WorkflowEnvironmentFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 */
public class DigesterModuleWorkflowDefinitionParserTest {
    private static final String ANOTHER_WF = "<?xml version=\"1.0\"?>" +
        "<process-definition name=\"another-workflow\">" +
        "	<description>Splitted edit-review process</description>" + "	<start-state id=\"Start\">" +
        "		<description>The start state</description>" +
        "		<transition to=\"notEdit\" id=\"toEdit\">" +
        "			<description>The start state</description></transition>" + "	</start-state>" + //
        "	<state id=\"notEdit\">" + //
        "		<enter-action path=\"class:TestWorkflowAction\">" +
        "			<description>The enter action</description>" +
        "			<property name=\"property\" value=\"myProperty\"/>" +
        "			<map-property name=\"mapProperty\" >" +
        "				<entry key=\"mikesKey\">mikesValue</entry>" + "			</map-property>" +
        "		</enter-action>" + "		<transition to=\"approve\" id=\"toApprove\"/></state>" +
        "	<state id=\"approve\">" + "		<transition to=\"isApproved\" id=\"decider\"/></state>" +
        "	<decision id=\"isApproved\">" + //
        "		<transition to=\"edit\" id=\"toEdit2\">" +
        "			<condition path=\"class:TestDefaultCheck\">" +
        "				<description>The condition</description>" + //
        "			</condition>" + "			<condition path=\"bean:authCondition\"/>" + //
        "		</transition>" + //
        "		<transition to=\"end\" id=\"toEnd\"/>" +
        "	</decision><end-state id=\"end\"/></process-definition>";
    private static final String WORKFLOW = "<?xml version=\"1.0\"?>" +
        "<process-definition name=\"test-workflow\">" +
        "	<description>Simple edit-review process</description>" + "	<start-state id=\"Start\">" +
        "		<transition to=\"edit\" id=\"toEdit1\"/></start-state>" + "	<state id=\"edit\">" +
        "		<transition to=\"review\" id=\"toReview\">" +
        "			<description>Go to review stage</description></transition>" + "	</state>" + //
        "	<state id=\"review\">" + "		<transition to=\"Start\" id=\"toEdit2\">" +
        "			<condition path=\"class:TestDefaultCheck\">" + "			</condition>" + //
        "			<condition path=\"bean:authCondition\"/>" + "		</transition>" + //
        "		<transition to=\"end\" id=\"toEnd\"/>" +
        "	</state><end-state id=\"end\"/></process-definition>";
    private static final String TRIVIAL = "<?xml version=\"1.0\"?>" +
        "<process-definition name=\"trivial-workflow\">" +
        "<description>Trivial process</description>" + "<start-state id=\"Start\">" + //
        "	<transition to=\"end\" id=\"noEnd\">" +
        "		<condition path=\"bean:mockExecutionCondition\">" + "	    </condition></transition>" +
        "	<transition to=\"end\" id=\"toEnd\"/></start-state>" +
        "<end-state id=\"end\"/></process-definition>";
    private static final String RULES_LOCATION = "org/cucina/engine/definition/config/xml/workflow-rules-definitions.xml";
    @Mock
    private BeanResolver beanResolver;
    private DigesterModuleProcessDefinitionParser parser;

    /**
     * JAVADOC.
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        parser = new DigesterModuleProcessDefinitionParser(new UrlResource(
                    ClassLoader.getSystemResource(RULES_LOCATION)));

        when(beanResolver.resolve(null, "bean:authCondition")).thenReturn(new TestTrueCheck());
        when(beanResolver.resolve(null, "class:TestDefaultCheck")).thenReturn(new TestDefaultCheck());
        when(beanResolver.resolve(null, "class:TestWorkflowAction"))
            .thenReturn(new TestWorkflowOperation());
        WorkflowEnvironmentFactory.buildEnvironment(beanResolver, null, parser, null, null);
    }

    /**
     * Checks that the Properties are set on {@link AbstractState} for multiple
     * Places.
     */
    @Test
    public void testPlacePropertiesSet() {
        ProcessDefinition wfd = parser.parse(new InputStreamResource(
                    new ByteArrayInputStream(ANOTHER_WF.getBytes())));

        assertNotNull(wfd);

        State state = wfd.getStartState();

        Transition toEditTransition = state.getTransition("toEdit");
        State editState = toEditTransition.getOutput();

        assertNotNull("editState is null", editState);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testReadJoinsDefinitions() {
        ProcessDefinition wfd = parser.parse(new InputStreamResource(
                    new ByteArrayInputStream(ANOTHER_WF.getBytes())));

        assertNotNull(wfd);
        assertEquals("another-workflow", wfd.getId());

        State state = wfd.getStartState();

        assertNotNull(state);
        assertEquals(state.getId(), "Start");

        Transition tr = state.getTransition("toEdit");

        assertNotNull(tr);
        assertEquals(state, tr.getInput());
        state = tr.getOutput();
        assertNotNull(state);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testReadWorkflowDefinitions() {
        ProcessDefinition wfd = parser.parse(new InputStreamResource(
                    new ByteArrayInputStream(WORKFLOW.getBytes())));

        assertNotNull(wfd);
        assertEquals("test-workflow", wfd.getId());
        assertEquals("Simple edit-review process", wfd.getDescription());

        State state = wfd.getStartState();

        assertNotNull(state);

        Transition tr = state.getTransition("toEdit1");

        assertNotNull(tr);
        assertEquals(state, tr.getInput());
        assertEquals(wfd, tr.getWorkflowDefinition());
        state = tr.getOutput();
        assertNotNull(state);

        tr = state.getTransition("toReview");
        assertNotNull(tr);
        assertEquals(state, tr.getInput());
        assertEquals(wfd, tr.getWorkflowDefinition());
        assertEquals("Go to review stage", tr.getDescription());
        state = tr.getOutput();
        assertNotNull(state);
        tr = state.getTransition("toEdit2");
        assertNotNull(tr);
        assertEquals(state, tr.getInput());
        assertEquals(wfd, tr.getWorkflowDefinition());
        assertEquals(tr.getOutput(), wfd.getStartState());

        List<Check> conds = tr.getConditions();

        assertTrue(conds.size() > 0);
        assertEquals(TestDefaultCheck.class, conds.get(0).getClass());
    }

    /**
     * TODO May not be a valid case
     */
    public void testUnmodifiableConditions() {
        ProcessDefinition wfd = parser.parse(new InputStreamResource(
                    new ByteArrayInputStream(TRIVIAL.getBytes())));

        assertNotNull(wfd);
        assertEquals("trivial-workflow", wfd.getId());

        State state = wfd.getStartState();

        assertNotNull(state);

        Transition tr = state.getTransition("noEnd");

        assertNotNull(tr);

        List<Check> conds = tr.getConditions();

        assertTrue(conds.size() > 0);

        try {
            conds.add(new TestDefaultCheck());
            fail("Should not be allowed to modify conditions");
        } catch (RuntimeException e) {
            // Good
        }
    }

    public static class TestDefaultCheck
        extends AbstractCheck {
        @Override
        public boolean test(ExecutionContext executionContext) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    public static class TestTrueCheck
        extends AbstractCheck {
        @Override
        public boolean test(ExecutionContext executionContext) {
            return true;
        }
    }

    public static class TestWorkflowOperation
        extends AbstractOperation {
        private Map<String, String> mapProperty;
        private String property;

        /**
         * Set mapProperty
         *
         * @param mapProperty
         *            Map<String, String>.
         */
        public void setMapProperty(Map<String, String> mapProperty) {
            this.mapProperty = mapProperty;
        }

        /**
         * Get mapProperty
         *
         * @return mapProperty Map<String, String>.
         */
        public Map<String, String> getMapProperty() {
            return mapProperty;
        }

        /**
         * Set property
         *
         * @param property
         *            String.
         */
        public void setProperty(String property) {
            this.property = property;
        }

        /**
         * Get property
         *
         * @return property String.
         */
        public String getProperty() {
            return property;
        }

        /**
         * Execute does nothing
         *
         * @param executionContext
         *            ExecutionContext.
         */
        @Override
        public void execute(ExecutionContext executionContext) {
        }
    }
}
