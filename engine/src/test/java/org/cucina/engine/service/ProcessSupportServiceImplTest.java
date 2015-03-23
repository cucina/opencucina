package org.cucina.engine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.cucina.core.model.Attachment;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.Decision;
import org.cucina.engine.definition.EndStation;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.ProcessDefinitionHelper;
import org.cucina.engine.definition.StartStation;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Station;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.HistoryRecordRepository;
import org.cucina.engine.repository.TokenRepository;
import org.cucina.engine.testassist.Foo;

import org.cucina.search.SearchBeanFactory;
import org.cucina.search.SearchService;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchResults;

import org.cucina.security.api.AccessFacade;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentMatcher;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessSupportServiceImplTest {
    private static final String WORKFLOW_ID = "workflowId";
    @Mock
    private AccessFacade accessFacade;
    @Mock
    private DefinitionService definitionService;
    @Mock
    private HistoryRecordRepository historyRecordRepository;
    @Mock
    private ProcessDefinitionHelper helper;
    @Mock
    private ProcessDefinitionRegistry definitionRegistry;
    @Mock
    private ProcessEnvironment processEnvironment;
    @Mock
    private ProcessService processService;
    private ProcessSupportServiceImpl service;
    @Mock
    private SearchBeanFactory searchBeanFactory;
    @Mock
    private SearchService searchService;
    @Mock
    private TokenRepository tokenRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new ProcessSupportServiceImpl();
        service.setHistoryRecordRepository(historyRecordRepository);
        service.setProcessEnvironment(processEnvironment);
        service.setTokenRepository(tokenRepository);
        service.setDefinitionService(definitionService);
        service.setSearchBeanFactory(searchBeanFactory);
        service.setSearchService(searchService);
        service.setAccessRegistry(accessFacade);
    }

    /**
     * JAVADOC Method Level Comments
     */
    public void listWorkflowProperties() {
        Collection<Long> ids = new HashSet<Long>();

        ids.add(12L);

        Map<String, Object> params = new HashMap<String, Object>();

        params.put(ProcessToken.DOMAIN_OBJ_ID_NAME, ids);

        SearchBean searchBean = new SearchBean();

        when(searchBeanFactory.buildSearchBean(Foo.TYPE, params)).thenReturn(searchBean);

        SearchResults searchResults = mock(SearchResults.class);

        when(searchService.search(eq(searchBean), eq(params))).thenReturn(searchResults);

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

        when(searchResults.<String, Object>searchMap(0, Integer.MAX_VALUE)).thenReturn(results);

        Collection<Map<String, Object>> actualResults = service.listWorkflowProperties(ids, Foo.TYPE);

        assertSame("Should be same map", results, actualResults);

        verify(searchBeanFactory).addProjections(Foo.TYPE, searchBean);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadStatesByTransition() {
        ProcessDefinition definition = new ProcessDefinition();

        // create the start state
        StartStation startState = new StartStation();

        startState.setId("start");
        definition.setStartState(startState);

        // create the Coke state
        Station coke = new Station();

        coke.setId("coke");

        // create the end state
        EndStation end = new EndStation();

        end.setId("end");

        Transition toChoose = new Transition();

        toChoose.setId("toCoke");
        toChoose.setDefault(true);
        startState.addTransition(toChoose);
        toChoose.setOutput(coke);

        // create the transition to coke
        Transition toEnd = new Transition();

        toEnd.setId("toEnd");
        coke.addTransition(toEnd);
        toEnd.setOutput(end);

        when(definitionService.loadDefinition(WORKFLOW_ID)).thenReturn(definition);

        Collection<Map<String, String>> results = service.loadTransitionInfo(WORKFLOW_ID);

        assertNotNull("Should have returned results", results);

        boolean foundToCoke = false;
        boolean foundToEnd = false;

        for (Map<String, String> result : results) {
            if ("toCoke".equals(result.get("transition"))) {
                assertEquals("Incorrect stateFrom", "start", result.get("stateFrom"));
                assertEquals("Incorrect stateTo", "coke", result.get("stateTo"));
                foundToCoke = true;
            } else if ("toEnd".equals(result.get("transition"))) {
                assertEquals("Incorrect stateFrom", "coke", result.get("stateFrom"));
                assertEquals("Incorrect stateTo", "end", result.get("stateTo"));
                foundToEnd = true;
            } else {
                fail("Should not have any other transition [" + result.get("transition"));
            }
        }

        assertTrue("Should have found foundToCoke", foundToCoke);
        assertTrue("Should have found foundToEnd", foundToEnd);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void startWorkflow() {
        Foo foo = new Foo();
        Map<String, Object> params = new HashMap<String, Object>();
        ProcessToken token = new ProcessToken();

        token.setDomainObject(foo);

        when(processService.startProcess(foo, "Foo", null, params)).thenReturn(token);
        when(processEnvironment.getService()).thenReturn(processService);

        assertEquals("Should have returned token", token, service.startWorkflow(foo, params));

        verify(tokenRepository).save(token);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void startWorkflowNoneExists() {
        Foo foo = new Foo();
        Map<String, Object> params = new HashMap<String, Object>();

        when(processService.startProcess(foo, "Foo", null, params)).thenReturn(null);
        when(processEnvironment.getService()).thenReturn(processService);

        assertNull("Should have returned null value", service.startWorkflow(foo, params));
    }

    /**
     * Tests that when the workflow is ended it is deleted
     */
    @Test
    public void testEndedDelete() {
        final ProcessToken token = new ProcessToken();

        token.setDomainObjectId(100L);

        when(helper.isEnded(token)).thenReturn(true);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("comments", "comment");
        parameters.put("attachment", null);
        parameters.put("approvedBy", null);
        parameters.put("assignedTo", null);
        parameters.put("extraParams", null);
        when(processService.executeTransition(token, "transitionId", parameters)).thenReturn(token);
        when(processEnvironment.getProcessDefinitionHelper()).thenReturn(helper);
        when(processEnvironment.getService()).thenReturn(processService);

        Map<Long, Integer> map = new HashMap<Long, Integer>();

        map.put(100L, 0);

        List<ProcessToken> result = new ArrayList<ProcessToken>();

        token.setProcessDefinitionId("workflowDefinitionId");
        result.add(token);
        when(tokenRepository.findByApplicationTypeAndIds(any(String.class), any(Long[].class)))
            .thenReturn(result);
        service.setTokenRepository(tokenRepository);
        service.makeTransition(map, "Foo", "transitionId", "comment", null, null, null, null, null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testListActionableTransitions() {
        String systemPrivilege = "SYSTEM_ONLY";
        String privilegeName = "yourPriv";
        String definitionId = "wflwDfn";

        final Station place = mock(Station.class);
        Collection<Transition> transes = new ArrayList<Transition>();
        Transition transition = new Transition();

        transition.setId("approve");
        transition.setPrivilegeName(privilegeName);
        transition.setInput(place);
        transes.add(transition);

        Transition transition2 = new Transition();

        transition2.setId("reject");
        transition2.setInput(place);
        transes.add(transition2);

        Transition transition3 = new Transition();

        transition3.setId("systemTrans");
        transition3.setInput(place);
        transition3.setPrivilegeName(systemPrivilege);
        transes.add(transition3);

        when(place.getAllTransitions()).thenReturn(transes);

        final Station place2 = mock(Station.class);
        Collection<Transition> transes2 = new ArrayList<Transition>();

        Transition transition4 = new Transition();

        transition4.setId("approve2");
        transition4.setPrivilegeName(privilegeName);
        transition4.setInput(place2);
        transes2.add(transition4);

        when(place2.getAllTransitions()).thenReturn(transes2);

        final Decision dec = mock(Decision.class);

        verify(dec, times(0)).getAllTransitions();

        @SuppressWarnings("serial")
        ProcessDefinition definition = new ProcessDefinition() {
                @Override
                public State[] getAllPlaces() {
                    return new State[] { place, place2, dec };
                }
            };

        when(definitionRegistry.findWorkflowDefinition(definitionId)).thenReturn(definition);
        when(processEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);
        when(accessFacade.getSystemPrivilege()).thenReturn("SYSTEM_ONLY");

        Collection<Transition> ret = service.listActionableTransitions(definitionId);

        assertNotNull(ret);
        assertEquals(3, ret.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testListActionableTransitionsNoWorkflow() {
        when(processEnvironment.getDefinitionRegistry()).thenReturn(definitionRegistry);

        when(definitionRegistry.findWorkflowDefinition("xxx")).thenReturn(null);

        Collection<Transition> ret = service.listActionableTransitions("xxx");

        assertNotNull(ret);
        assertEquals(0, ret.size());
    }

    /**
     * Tests that gets transitions for all tokens
     */
    @Test
    public void testListAllTransitions() {
        Collection<String> transitions1 = new ArrayList<String>();

        transitions1.add("hello");

        ProcessToken token1 = new ProcessToken();

        token1.setDomainObjectId(12L);

        when(processService.listTransitions(token1, null)).thenReturn(transitions1);

        Collection<String> transitions2 = new ArrayList<String>();

        transitions2.add("tom");
        transitions2.add("matt");

        ProcessToken token2 = new ProcessToken();

        token2.setDomainObjectId(13L);

        when(processService.listTransitions(token2, null)).thenReturn(transitions2);

        when(processEnvironment.getService()).thenReturn(processService);

        List<ProcessToken> tokens = new ArrayList<ProcessToken>();

        tokens.add(token1);
        tokens.add(token2);
        when(tokenRepository.findByApplicationTypeAndIds(eq(Foo.TYPE), any(Long[].class)))
            .thenReturn(tokens);

        Map<Long, Collection<String>> results = service.listAllTransitions(Collections.<Long>singleton(
                    100L), Foo.TYPE);

        assertEquals("Incorrect number results", 2, results.size());

        Collection<String> transIds = results.get(12L);

        assertNotNull("Should have transitions", transIds);
        assertEquals("Incorrect number transitions", 1, transIds.size());
        assertTrue("Should contain value", transIds.contains("hello"));
        transIds = results.get(13L);
        assertNotNull("Should have transitions", transIds);
        assertEquals("Incorrect number transitions", 2, transIds.size());
        assertTrue("Should contain value", transIds.contains("tom"));
        assertTrue("Should contain value", transIds.contains("matt"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testListTransitions() {
        Collection<String> transes = new ArrayList<String>();

        transes.add("hello");

        ProcessToken token = new ProcessToken();

        token.setPlaceId("haha");
        when(processService.listTransitions(token, null)).thenReturn(transes);
        when(processEnvironment.getService()).thenReturn(processService);

        List<ProcessToken> result = new ArrayList<ProcessToken>();

        token.setProcessDefinitionId("workflowDefinitionId");
        result.add(token);
        when(tokenRepository.findByApplicationTypeAndIds(any(String.class), any(Long[].class)))
            .thenReturn(result);
        assertTrue("Not containing hello",
            service.listTransitions(Collections.<Long>singleton(100L), "Foo").contains("hello"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMakeSingleTransition() {
        Foo foo = new Foo();

        foo.setId(100L);

        final ProcessToken token = new ProcessToken();

        token.setDomainObject(foo);
        token.setVersion(0);
        token.setId(1L);

        when(helper.isEnded(token)).thenReturn(false);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("comments", "comment");
        parameters.put("attachment", null);
        parameters.put("approvedBy", null);
        parameters.put("assignedTo", null);
        parameters.put("extraParams", null);
        when(processService.executeTransition(token, "transitionId", parameters)).thenReturn(token);

        when(processEnvironment.getProcessDefinitionHelper()).thenReturn(helper);
        when(processEnvironment.getService()).thenReturn(processService);

        tokenRepository.save(token);
        token.setProcessDefinitionId("workflowDefinitionId");
        when(tokenRepository.findByApplicationTypeAndIds("Foo", 100L))
            .thenReturn(Collections.singleton(token));
        service.makeTransition(100L, "Foo", "transitionId", "comment", null, null, null, null);
        // this one should call for delete caused by helper.isEnded returning true
        when(helper.isEnded(token)).thenReturn(true);
        service.makeTransition(100L, "Foo", "transitionId", "comment", null, null, null, null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoToken() {
        Map<Long, Integer> map = new HashMap<Long, Integer>();

        when(tokenRepository.findByApplicationTypeAndIds(any(String.class), any(Long[].class)))
            .thenReturn(null);
        service.makeTransition(map, "Foo", "transitionId", null, null, null, null, null, null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testObtainHistory() {
        List<HistoryRecord> result = new ArrayList<HistoryRecord>();

        when(tokenRepository.findHistoryRecordsByDomainObjectIdAndDomainObjectType(1L, "Foo"))
            .thenReturn(result);
        assertEquals(result, service.obtainHistory(1L, "Foo"));
    }

    /**
     * Simple test that we utilize searchDao to obtain the history summary and that we
     * use the Message.getBestMessage to return the reason value.
     */
    @Test
    public void testObtainHistorySummary() {
        List<HistoryRecord> results = new ArrayList<HistoryRecord>();
        HistoryRecord historyRec1 = new HistoryRecord();

        historyRec1.setReason("A reason");

        HistoryRecord historyRec2 = new HistoryRecord();

        results.add(historyRec1);
        results.add(historyRec2);
        when(historyRecordRepository.findByIdAndApplicationType(1L, "Foo")).thenReturn(results);

        List<Map<Object, Object>> summary = service.obtainHistorySummary(1L, Foo.TYPE);

        assertEquals(2, summary.size());
        assertEquals("A reason", summary.get(0).get("reason"));
        assertNull(summary.get(1).get("reason"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSunnyDay() {
        final ProcessToken token = new ProcessToken();

        token.setDomainObjectId(100L);
        token.setVersion(0);
        token.setId(1L);

        when(helper.isEnded(token)).thenReturn(false);

        final Attachment attachment = new Attachment();

        attachment.setFilename("filename");

        final String comments = "comment";

        when(processService.executeTransition(eq(token), eq("transitionId"),
                argThat(new ArgumentMatcher<Map<String, Object>>() {
                public boolean matches(Object params) {
                    Map<String, Object> mParams = ((Map<String, Object>) params);

                    // Check the comments and attachment but make sure that we've cloned the Attachment
                    Attachment attachmentParam = (Attachment) mParams.get("attachment");

                    return mParams.get("comments").equals("comment") &&
                            (attachmentParam != attachment) &&
                            (attachmentParam.getFilename() == attachment.getFilename());
                }
            }))).thenReturn(token);
        when(processEnvironment.getProcessDefinitionHelper()).thenReturn(helper);
        when(processEnvironment.getService()).thenReturn(processService);

        Map<Long, Integer> map = new HashMap<Long, Integer>();

        map.put(100L, 0);

        List<ProcessToken> result = new ArrayList<ProcessToken>();

        token.setProcessDefinitionId("workflowDefinitionId");
        result.add(token);

        when(tokenRepository.findByApplicationTypeAndIds(any(String.class), any(Long[].class)))
            .thenReturn(result);
        tokenRepository.save(token);
        service.makeTransition(map, "Foo", "transitionId", comments, null, null, null, null,
            attachment);
    }
}
