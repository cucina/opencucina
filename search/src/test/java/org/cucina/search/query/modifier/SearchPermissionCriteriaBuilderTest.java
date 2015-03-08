package org.cucina.search.query.modifier;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.FailingCriterion;
import org.cucina.security.access.AccessRegistry;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Privilege;
import org.cucina.security.model.User;
import org.cucina.security.repository.PermissionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SearchPermissionCriteriaBuilderTest {
    @Mock
    private AccessRegistry accessRegistry;
    @Mock
    private PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper;
    @Mock
    private PermissionRepository permissionDao;
    @Mock
    private SearchBean searchBean;
    @Mock
    private SearchCriterion searchCriterion;
    private SearchPermissionCriteriaBuilder builder;
    private User user = new User();

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        builder = new SearchPermissionCriteriaBuilder(accessRegistry, permissionDao,
                permissionCriteriaBuilderHelper);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testBuildCriteria() {
        Privilege privilege = new Privilege();

        when(accessRegistry.lookup("Foo", PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL))
            .thenReturn(privilege);

        Collection<Permission> perms = new ArrayList<Permission>();
        Permission perm = new Permission();

        perms.add(perm);
        when(permissionDao.findByUserAndPrivilege(user, privilege)).thenReturn(perms);
        when(permissionCriteriaBuilderHelper.buildClause("Foo", "foo", perms))
            .thenReturn(searchCriterion);

        SearchBean sb = builder.buildCriteria(searchBean, user, "Foo", "foo",
                PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL);

        assertNotNull("Result is null", sb);
        verify(searchBean).addCriterion(searchCriterion);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testBuildFail() {
        Privilege privilege = new Privilege();

        when(accessRegistry.lookup("Foo", PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL))
            .thenReturn(privilege);

        when(permissionDao.findByUserAndPrivilege(user, privilege)).thenReturn(null);

        SearchBean sb = builder.buildCriteria(searchBean, user, "Foo", "foo",
                PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL);

        assertNotNull("Result is null", sb);
        verify(searchBean).addCriterion(any(FailingCriterion.class));
    }
}
