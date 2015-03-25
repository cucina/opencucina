package org.cucina.security.repository.jpa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.test.util.ReflectionTestUtils;

import org.cucina.security.model.Role;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
public class RoleRepositoryImplTest {
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private CriteriaQuery<Role> cq;
    @Mock
    private EntityManager em;
    private RoleRepositoryImpl repo;
    @Mock
    private TypedQuery<Role> tq;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        repo = new RoleRepositoryImpl();
        ReflectionTestUtils.setField(repo, "entityManager", em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Role.class)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDelete() {
        Role role = new Role();

        repo.delete(role);
        verify(em).remove(role);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testExists() {
        Root<Role> root = mock(Root.class);

        when(cq.from(Role.class)).thenReturn(root);

        Path<Object> path = mock(Path.class);

        when(root.get("name")).thenReturn(path);

        Predicate pre = mock(Predicate.class);

        when(cb.equal(path, "name")).thenReturn(pre);
        when(cq.where(pre)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(null);
        assertFalse(repo.exists("name"));
        when(tq.getSingleResult()).thenReturn(new Role());
        assertTrue(repo.exists("name"));
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
    public void testFindByName() {
        Root<Role> root = mock(Root.class);

        when(cq.from(Role.class)).thenReturn(root);

        Path<Object> path = mock(Path.class);

        when(root.get("name")).thenReturn(path);

        Predicate pre = mock(Predicate.class);

        when(cb.equal(path, "name")).thenReturn(pre);
        when(cq.where(pre)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
        repo.findByName("name");
        verify(tq).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSave() {
        Role role = new Role();

        repo.save(role);
        verify(em).persist(role);
        role.setId(1L);
        repo.save(role);
        verify(em).merge(role);
    }
}
