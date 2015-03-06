package org.cucina.security.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.security.testassist.Bar;
import org.cucina.security.testassist.Baz;
import org.cucina.security.testassist.Foo;
import org.cucina.security.ContextUserAccessor;
import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Privilege;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class AccessManagerImplTest {
    @Mock
    private BeanFactory bf;
    @Mock
    private InstanceFactory instanceFactory;
    private Permission permission1;
    private Permission permission2;
    private AccessManagerImpl permissionManager;
    private Privilege privilege1;
    private Privilege privilege2;
    private Privilege privilege3;
    private Role role1;
    private User inactiveUser;
    private User user;
    @Mock
    private UserDetailsService uds;
    @Mock
    private UserRepository userRepository;

    /**
    * JAVADOC Method Level Comments
    */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(instanceFactory.getPropertyType(anyString(), anyString())).thenReturn("blaah");
        permissionManager = new AccessManagerImpl(instanceFactory, userRepository, "hoho");
        user = new User();

        user.setActive(true);

        inactiveUser = new User();
        inactiveUser.setActive(false);

        setSecurityContext(user);
        when(bf.getBean(ContextUserAccessor.USER_ACCESSOR_ID)).thenReturn(uds);
        when(uds.loadUserByUsername(anyString())).thenReturn(user);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetPropertyValue() {
        //default behaviour
        Map<String, Object> propertyValues = new HashMap<String, Object>();

        propertyValues.put("property", 1L);
        assertEquals(1L, permissionManager.getPropertyValue(Foo.TYPE, "property", propertyValues));

        //set up suffix for type Foo
        Map<String, String> suffixByType = new HashMap<String, String>();

        permissionManager.setSuffixByType(suffixByType);
        suffixByType.put(Foo.TYPE, "BLAAH");

        propertyValues.put("propertyBLAAH", 2L);
        assertEquals("should be 2L as we find a suffix for Foo", 2L,
            permissionManager.getPropertyValue(Foo.TYPE, "property", propertyValues));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetPropertyValueSuffixButNotUsed() {
        //default behaviour
        Map<String, Object> propertyValues = new HashMap<String, Object>();

        propertyValues.put("property", 1L);
        assertEquals(1L, permissionManager.getPropertyValue(Foo.TYPE, "property", propertyValues));

        //set up suffix for type Foo
        Map<String, String> suffixByType = new HashMap<String, String>();

        permissionManager.setSuffixByType(suffixByType);
        suffixByType.put(Foo.TYPE, "BLAAH");

        assertEquals("should be 1L as we revert to normal property name if one with suffix not found",
            1L, permissionManager.getPropertyValue(Foo.TYPE, "property", propertyValues));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testHasPermissionMatchingBar() {
        addDefaultJobs(user);

        Bar bar = new Bar();

        //this will match first permission
        Foo foo = new Foo();

        foo.setId(1L);

        Baz baz = new Baz();

        baz.setId(11L);
        bar.setFoo(foo);
        bar.setBaz(baz);

        assertTrue(permissionManager.hasPermission(privilege1.getName(), bar));
        assertTrue(permissionManager.hasPermission(privilege2.getName(), bar));
        assertFalse(permissionManager.hasPermission(privilege3.getName(), bar));

        verify(instanceFactory, times(3)).getPropertyType(Bar.TYPE, "foo");
        verify(instanceFactory, times(2)).getPropertyType(Bar.TYPE, "baz");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testHasPermissionNonMatchingFoo() {
        addDefaultJobs(user);

        Bar bar = new Bar();
        Foo foo = new Foo();

        foo.setId(4L);

        Baz baz = new Baz();

        baz.setId(11L);
        bar.setFoo(foo);
        bar.setBaz(baz);

        //match neither permission
        assertFalse(permissionManager.hasPermission(privilege1.getName(), bar));
        assertFalse(permissionManager.hasPermission(privilege2.getName(), bar));
        assertFalse(permissionManager.hasPermission(privilege3.getName(), bar));

        //match second permission
        foo.setId(2L);
        assertFalse(permissionManager.hasPermission(privilege1.getName(), bar));
        assertFalse(permissionManager.hasPermission(privilege2.getName(), bar));
        assertTrue(permissionManager.hasPermission(privilege3.getName(), bar));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHasPrivilegeRainyDay() {
        permissionManager.hasPrivilege(null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testHasPrivilegeSunnyDay() {
        addDefaultJobs(user);

        assertTrue("we have the privilege!", permissionManager.hasPrivilege(privilege1.getName()));
        assertTrue("we have the privilege!", permissionManager.hasPrivilege(privilege2.getName()));
        assertTrue("we have the privilege!", permissionManager.hasPrivilege(privilege3.getName()));
        assertFalse("we don't have the privilege!", permissionManager.hasPrivilege("BAD"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsAdmin() {
        assertFalse("Shouldn't be admin", permissionManager.isAdmin());
    }

    /**
     * Test that we get expected <code>User</code>s returned.
     */
    @Test
    public void testListActiveUsers() {
        addDefaultJobs(user);
        addDefaultJobs(inactiveUser);

        HashSet<User> loadedUsers = new HashSet<User>();

        loadedUsers.add(user);
        loadedUsers.add(inactiveUser);
        when(userRepository.findAll()).thenReturn(loadedUsers);

        Collection<User> users = permissionManager.listActiveUsers(privilege1.getName(), false);

        assertEquals("Should've returned 1 user", 1, users.size());
        assertEquals("Should've returned user", user, users.iterator().next());

        users = permissionManager.listActiveUsers(privilege1.getName(), null);
        assertEquals("Should've returned 1 user", 1, users.size());
        assertEquals("Should've returned user", user, users.iterator().next());

        users = permissionManager.listActiveUsers(privilege1.getName(), true);
        assertEquals("Shouldn't have returned any users because we want to exclude the current user",
            0, users.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMatches() {
        String typeName = Foo.TYPE;
        Map<String, Collection<Long>> dimensions = new HashMap<String, Collection<Long>>();

        dimensions.put("property", Arrays.asList(new Long[] { 1L, 2L }));

        Map<String, Object> propertyValues = new HashMap<String, Object>();

        propertyValues.put("property", 2L);
        assertTrue(permissionManager.matches(typeName, dimensions, propertyValues));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMatchesNullValue() {
        String typeName = Foo.TYPE;
        Map<String, Collection<Long>> dimensions = new HashMap<String, Collection<Long>>();

        dimensions.put("property", Arrays.asList(new Long[] { 1L, 2L }));

        Map<String, Object> propertyValues = new HashMap<String, Object>();

        propertyValues.put("property", null);
        assertFalse(permissionManager.matches(typeName, dimensions, propertyValues));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMatchesPE() {
        String typeName = Foo.TYPE;
        Map<String, Collection<Long>> dimensions = new HashMap<String, Collection<Long>>();

        dimensions.put("property", Arrays.asList(new Long[] { 1L, 2L }));

        Map<String, Object> propertyValues = new HashMap<String, Object>();
        Bar bar = new Bar();

        bar.setId(1L);
        propertyValues.put("property", bar);
        assertTrue(permissionManager.matches(typeName, dimensions, propertyValues));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMatchesStringValueFails() {
        String typeName = Foo.TYPE;
        Map<String, Collection<Long>> dimensions = new HashMap<String, Collection<Long>>();

        dimensions.put("property", Arrays.asList(new Long[] { 1L, 2L }));

        Map<String, Object> propertyValues = new HashMap<String, Object>();

        propertyValues.put("property", "blaah");
        assertFalse(permissionManager.matches(typeName, dimensions, propertyValues));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoDimensionsMatchesAsTrue() {
        String typeName = Foo.TYPE;
        Map<String, Collection<Long>> dimensions = new HashMap<String, Collection<Long>>();

        Map<String, Object> propertyValues = new HashMap<String, Object>();

        propertyValues.put("property", 1L);
        assertTrue(permissionManager.matches(typeName, dimensions, propertyValues));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPermissionMapCheck() {
        rolesPrivs();

        PersistableEntity entity = new Foo();
        Permission permission = new Permission();

        permission.setUsers(Collections.singleton(user));
        permission.setRole(role1);
        user.setPermissions(Collections.singleton(permission));
        //permission has no dimensions, so should return true
        assertTrue(permissionManager.hasPermission("GOOD", entity));
    }

    private void setSecurityContext(User user) {
        SecurityContext context = null;

        if (null == SecurityContextHolder.getContext()) {
            context = new SecurityContextImpl();

            SecurityContextHolder.setContext(context);
        }

        context = SecurityContextHolder.getContext();

        Collection<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();

        gas.add(new SimpleGrantedAuthority("role1"));
        gas.add(new SimpleGrantedAuthority("role2"));
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, gas));
    }

    private void addDefaultJobs(User user) {
        rolesPrivs();

        Dimension dim1 = new Dimension();

        dim1.setPropertyName("foo");
        dim1.setDomainObjectType(Foo.TYPE);
        dim1.setDomainObjectId(1L);

        Dimension dim2 = new Dimension();

        dim2.setPropertyName("baz");
        dim2.setDomainObjectType(Baz.TYPE);
        dim2.setDomainObjectId(11L);

        Collection<Dimension> dims1 = new HashSet<Dimension>();

        dims1.add(dim1);
        dims1.add(dim2);

        permission1 = new Permission();
        permission1.setRole(role1);
        permission1.setDimensions(dims1);

        Role role2 = new Role();

        role2.setId(51L);
        role2.setPrivileges(new HashSet<Privilege>());
        role2.getPrivileges().add(privilege3);

        Dimension dim3 = new Dimension();

        dim3.setPropertyName("foo");
        dim3.setDomainObjectType(Foo.TYPE);
        dim3.setDomainObjectId(2L);

        permission2 = new Permission();
        permission2.setRole(role2);
        permission2.setDimensions(Collections.singleton(dim3));

        Collection<Permission> permissions = new HashSet<Permission>();

        permissions.add(permission1);
        permissions.add(permission2);

        user.setPermissions(permissions);
    }

    private void rolesPrivs() {
        privilege1 = new Privilege();
        privilege1.setId(17L);
        privilege1.setName("GOOD");
        privilege2 = new Privilege();
        privilege2.setId(18L);
        privilege2.setName("FRANK");
        privilege3 = new Privilege();
        privilege3.setId(19L);
        privilege3.setName("SPANK");

        role1 = new Role();

        role1.setId(50L);
        role1.setPrivileges(new HashSet<Privilege>());

        role1.getPrivileges().add(privilege1);
        role1.getPrivileges().add(privilege2);
    }
}
