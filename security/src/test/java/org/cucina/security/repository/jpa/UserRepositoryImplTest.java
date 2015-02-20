package org.cucina.security.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;

import org.cucina.testassist.utils.LoggingEnabler;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
public class UserRepositoryImplTest {
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private CriteriaQuery<User> cq;
    @Mock
    private EntityManager em;
    @Mock
    private TypedQuery<User> tq;
    private UserRepositoryImpl repo;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        LoggingEnabler.enableLog(UserRepositoryImpl.class);
        repo = new UserRepositoryImpl();
        repo.setEntityManager(em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(User.class)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDelete() {
        User user = new User();

        repo.delete(user);
        verify(em).remove(user);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFind() {
        repo.find(111L);
        verify(em).find(User.class, 111L);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFindAll() {
        repo.findAll();
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByPermission() {
        Permission permission = new Permission();

        Root<User> root = mock(Root.class);

        when(cq.from(User.class)).thenReturn(root);

        Path<Collection<Permission>> colpath = mock(Path.class);

        when(root.<Collection<Permission>>get("permissions")).thenReturn(colpath);

        Predicate pre = mock(Predicate.class);

        when(cb.isMember(permission, colpath)).thenReturn(pre);
        when(cq.where(pre)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
        repo.findByPermission(permission);
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByRole() {
        Role role = new Role();
        Root<User> root = mock(Root.class);

        when(cq.from(User.class)).thenReturn(root);

        Path<Collection<Permission>> colpath = mock(Path.class);

        when(root.<Collection<Permission>>get("permissions")).thenReturn(colpath);

        Path<Object> path = mock(Path.class);

        when(colpath.<Object>get("role")).thenReturn(path);

        In<Object> iin = mock(In.class);

        when(cb.in(path)).thenReturn(iin);

        In<Object> in = mock(In.class);

        when(iin.value(role)).thenReturn(in);
        when(cq.where(in)).thenReturn(cq);
        repo.findByRole(role);
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindBySystemFalse() {
        Root<User> root = mock(Root.class);

        when(cq.from(User.class)).thenReturn(root);

        Path<Object> path = mock(Path.class);

        when(root.get("system")).thenReturn(path);

        Predicate pred = mock(Predicate.class);

        when(cb.equal(path, false)).thenReturn(pred);

        when(cq.where(pred)).thenReturn(cq);
        repo.findBySystem(false);
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindBySystemTrue() {
        Root<User> root = mock(Root.class);

        when(cq.from(User.class)).thenReturn(root);

        Path<Object> paths = mock(Path.class);

        when(root.get("system")).thenReturn(paths);

        Predicate sp = mock(Predicate.class);

        when(cb.equal(paths, true)).thenReturn(sp);

        Path<Object> patha = mock(Path.class);

        when(root.get("active")).thenReturn(patha);

        Predicate ap = mock(Predicate.class);

        when(cb.equal(patha, true)).thenReturn(ap);

        Path<Object> pathe = mock(Path.class);

        when(root.get("enabled")).thenReturn(pathe);

        Predicate ep = mock(Predicate.class);

        when(cb.equal(pathe, true)).thenReturn(ep);

        Predicate pred = mock(Predicate.class);

        when(cb.and(sp, ap, ep)).thenReturn(pred);

        when(cq.where(pred)).thenReturn(cq);
        repo.findBySystem(true);
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByUsernameForUpdate() {
        Root<User> root = mock(Root.class);

        when(cq.from(User.class)).thenReturn(root);

        Path<Object> path = mock(Path.class);

        when(root.get("username")).thenReturn(path);

        Predicate pre = mock(Predicate.class);

        when(cb.equal(path, "username")).thenReturn(pre);
        when(cq.where(pre)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);

        User user = new User();

        when(tq.getSingleResult()).thenReturn(user);
        assertEquals(user, repo.findByUsernameForUpdate("username"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadUserByUsername() {
        Root<User> root = mock(Root.class);

        when(cq.from(User.class)).thenReturn(root);

        Path<Object> path = mock(Path.class);

        when(root.get("username")).thenReturn(path);

        Predicate pre = mock(Predicate.class);

        when(cb.equal(path, "username")).thenReturn(pre);
        when(cq.where(pre)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);

        User user = new User();

        when(tq.getSingleResult()).thenReturn(user);
        assertEquals(user, repo.findByUsernameForUpdate("username"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSave() {
        User user = new User();

        repo.save(user);
        verify(em).persist(user);
        user.setId(1L);
        repo.save(user);
        verify(em).merge(user);
    }
}
