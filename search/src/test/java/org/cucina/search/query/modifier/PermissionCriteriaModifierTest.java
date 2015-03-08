package org.cucina.search.query.modifier;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.search.SearchType;
import org.cucina.search.query.SearchBean;
import org.cucina.security.ContextUserAccessor;
import org.cucina.security.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PermissionCriteriaModifierTest {
    private LinkedHashMap<String, String> aliasByType;
    @Mock
    private PermissionCriteriaBuilder pcb;
    private PermissionCriteriaModifier modifier;
    @Mock
    private SearchBean searchBean;
    private User user;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);

        Map<String, PermissionCriteriaBuilder> criteriaBuilders = new HashMap<String, PermissionCriteriaBuilder>();

        criteriaBuilders.put(SearchType.DEFAULT.toString(), pcb);
        modifier = new PermissionCriteriaModifier(criteriaBuilders);
        aliasByType = new LinkedHashMap<String, String>();
        aliasByType.put("Foo", "foo");
        when(searchBean.getAliasByType()).thenReturn(aliasByType);
        when(searchBean.getSearchType()).thenReturn(SearchType.DEFAULT);
        user = loginUser();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDefaultModify() {
        when(pcb.buildCriteria(searchBean, user, "Foo", "foo",
                PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL)).thenReturn(searchBean);

        SearchBean sb = modifier.doModify(searchBean, null);

        assertNotNull("result is null", sb);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoModify() {
        modifier.setPermissionsInUse(false);

        SearchBean sb = modifier.doModify(searchBean, null);

        assertNotNull("result is null", sb);
    }

    private User loginUser() {
        User user = new User();

        user.setUsername("username");

        SecurityContext context;

        if (null == SecurityContextHolder.getContext()) {
            context = new SecurityContextImpl();

            SecurityContextHolder.setContext(context);
        }

        context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null, null);

        context.setAuthentication(authToken);

        UserDetailsService userAccesor = mock(UserDetailsService.class);

        when(userAccesor.loadUserByUsername("username")).thenReturn(user);

        BeanFactory beanFactory = mock(BeanFactory.class);

        when(beanFactory.getBean(ContextUserAccessor.USER_ACCESSOR_ID)).thenReturn(userAccesor);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);

        return user;
    }
}
