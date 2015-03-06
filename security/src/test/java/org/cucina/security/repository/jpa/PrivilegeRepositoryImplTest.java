package org.cucina.security.repository.jpa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.security.model.Privilege;
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
public class PrivilegeRepositoryImplTest {
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private CriteriaQuery<Privilege> cq;
    @Mock
    private EntityManager em;
    private PrivilegeRepositoryImpl repo;
    @Mock
    private TypedQuery<Privilege> tq;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        repo = new PrivilegeRepositoryImpl();
        repo.setEntityManager(em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Privilege.class)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDelete() {
        Privilege privilege = new Privilege();

        repo.delete(privilege);
        verify(em).remove(privilege);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testExists() {
        Root<Privilege> root = mock(Root.class);

        when(cq.from(Privilege.class)).thenReturn(root);

        Path<Object> pr = mock(Path.class);

        when(root.get("name")).thenReturn(pr);

        Predicate wi = mock(Predicate.class);

        when(cb.equal(pr, "name")).thenReturn(wi);
        when(cq.where(wi)).thenReturn(cq);
        when(tq.getSingleResult()).thenReturn(null);
        assertFalse(repo.exists("name"));
        when(tq.getSingleResult()).thenReturn(new Privilege());
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
        Root<Privilege> root = mock(Root.class);

        when(cq.from(Privilege.class)).thenReturn(root);

        Path<Object> pr = mock(Path.class);

        when(root.get("name")).thenReturn(pr);

        Predicate wi = mock(Predicate.class);

        when(cb.equal(pr, "name")).thenReturn(wi);
        when(cq.where(wi)).thenReturn(cq);

        repo.findByName("name");
        verify(tq).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByNameForUpdate() {
        Root<Privilege> root = mock(Root.class);

        when(cq.from(Privilege.class)).thenReturn(root);

        Path<Object> pr = mock(Path.class);

        when(root.get("name")).thenReturn(pr);

        Predicate wi = mock(Predicate.class);

        when(cb.equal(pr, "name")).thenReturn(wi);
        when(cq.where(wi)).thenReturn(cq);
        repo.findByNameForUpdate("name");
        verify(tq).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSave() {
        Privilege privilege = new Privilege();

        repo.save(privilege);
        verify(em).persist(privilege);
        privilege.setId(1L);
        repo.save(privilege);
        verify(em).merge(privilege);
    }
}
