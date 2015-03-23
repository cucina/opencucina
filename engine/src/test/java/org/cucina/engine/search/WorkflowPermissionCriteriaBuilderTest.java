package org.cucina.engine.search;

import java.util.ArrayList;
import java.util.Collection;

import org.cucina.engine.definition.Station;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.service.ProcessSupportService;

import org.cucina.search.query.SearchBean;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.modifier.PermissionCriteriaBuilderHelper;
import org.cucina.search.query.modifier.PermissionCriteriaModifier;

import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.PermissionDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class WorkflowPermissionCriteriaBuilderTest {
    private static final String PRIVILEGE_NAME = "privilegeName";
    @Mock
    private AccessFacade accessFacade;
    @Mock
    private PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper;
    @Mock
    private ProcessSupportService workflowSupportService;
    @Mock
    private SearchBean searchBean;
    private WorkflowPermissionCriteriaBuilder builder;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        builder = new WorkflowPermissionCriteriaBuilder(accessFacade,
                permissionCriteriaBuilderHelper, workflowSupportService);

        Collection<Transition> trax = new ArrayList<Transition>();
        Transition dtr = new Transition();

        dtr.setId("dtr");
        trax.add(dtr);

        Transition etr = new Transition();

        etr.setId("etr");
        etr.setPrivilegeName(PRIVILEGE_NAME);

        Station input = mock(Station.class);

        when(input.getId()).thenReturn("input");
        etr.setInput(input);
        trax.add(etr);

        when(workflowSupportService.listActionableTransitions("Foo")).thenReturn(trax);

        Collection<PermissionDto> permissions = new ArrayList<PermissionDto>();
        PermissionDto permission = new PermissionDto();

        permissions.add(permission);
        when(accessFacade.permissionsByUserAndPrivilege("user", "privilege")).thenReturn(permissions);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void test() {
        SearchBean sb = builder.buildCriteria(searchBean, "user", "Foo", "foo",
                PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL);

        assertNotNull("result is null", sb);
        assertEquals(searchBean, sb);
        verify(searchBean).addCriterion(any(InSearchCriterion.class));
    }
}
