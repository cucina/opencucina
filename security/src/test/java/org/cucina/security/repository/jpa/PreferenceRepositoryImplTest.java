package org.cucina.security.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.test.util.ReflectionTestUtils;

import org.cucina.security.model.Preference;
import org.cucina.security.model.User;
import org.cucina.security.service.UserAccessor;
import org.cucina.security.testassist.SecurityHelper;
import static org.junit.Assert.assertEquals;

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
public class PreferenceRepositoryImplTest {
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private EntityManager em;
    private PreferenceRepositoryImpl repo;
    private User user;
    @Mock
    private UserAccessor userAccessor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        repo = new PreferenceRepositoryImpl();
        ReflectionTestUtils.setField(repo, "entityManager", em);
        ReflectionTestUtils.setField(repo, "userAccessor", userAccessor);

        when(em.getCriteriaBuilder()).thenReturn(cb);
        user = new User();
        user.setUsername("John");

        SecurityHelper.setupSecurity(user);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDelete() {
        CriteriaQuery<Preference> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(Preference.class)).thenReturn(cq);

        Root<Preference> root = mock(Root.class);

        when(cq.from(Preference.class)).thenReturn(root);

        Path<Object> pao = mock(Path.class);

        when(root.get("owner")).thenReturn(pao);

        Predicate po = cb.equal(pao, user);
        Path<Object> pan = mock(Path.class);

        when(root.get("name")).thenReturn(pan);

        Predicate pn = cb.equal(pan, "name");

        Predicate and = mock(Predicate.class);

        when(cb.and(po, pn)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        TypedQuery<Preference> tq = mock(TypedQuery.class);

        when(em.createQuery(cq)).thenReturn(tq);

        Preference pref = new Preference();

        pref.setId(100L);
        when(tq.getSingleResult()).thenReturn(pref);
        repo.delete("name");
        verify(em).remove(pref);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadAll() {
        CriteriaQuery<Preference> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(Preference.class)).thenReturn(cq);

        Root<Preference> root = mock(Root.class);

        when(cq.from(Preference.class)).thenReturn(root);

        Path<Object> pao = mock(Path.class);

        when(root.get("owner")).thenReturn(pao);

        Predicate po = cb.equal(pao, user);

        when(cq.where(po)).thenReturn(cq);

        TypedQuery<Preference> tq = mock(TypedQuery.class);

        when(em.createQuery(cq)).thenReturn(tq);

        Preference pref = new Preference();

        pref.setId(100L);
        when(tq.getResultList()).thenReturn(Collections.singletonList(pref));
        assertEquals(pref, repo.loadAll().iterator().next());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadString() {
        CriteriaQuery<Preference> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(Preference.class)).thenReturn(cq);

        Root<Preference> root = mock(Root.class);

        when(cq.from(Preference.class)).thenReturn(root);

        Path<Object> pao = mock(Path.class);

        when(root.get("owner")).thenReturn(pao);

        Predicate po = cb.equal(pao, user);
        Path<Object> pan = mock(Path.class);

        when(root.get("name")).thenReturn(pan);

        Predicate pn = cb.equal(pan, "name");

        Predicate and = mock(Predicate.class);

        when(cb.and(po, pn)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        TypedQuery<Preference> tq = mock(TypedQuery.class);

        when(em.createQuery(cq)).thenReturn(tq);

        Preference pref = new Preference();

        pref.setId(100L);
        when(tq.getSingleResult()).thenReturn(pref);
        assertEquals(pref, repo.load(user, "name"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLoadUserDetailsString() {
        User luser = new User();
        CriteriaQuery<Preference> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(Preference.class)).thenReturn(cq);

        Root<Preference> root = mock(Root.class);

        when(cq.from(Preference.class)).thenReturn(root);

        Path<Object> pao = mock(Path.class);

        when(root.get("owner")).thenReturn(pao);

        Predicate po = cb.equal(pao, luser);
        Path<Object> pan = mock(Path.class);

        when(root.get("name")).thenReturn(pan);

        Predicate pn = cb.equal(pan, "name");

        Predicate and = mock(Predicate.class);

        when(cb.and(po, pn)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        TypedQuery<Preference> tq = mock(TypedQuery.class);

        when(em.createQuery(cq)).thenReturn(tq);

        Preference pref = new Preference();

        pref.setId(100L);
        when(tq.getSingleResult()).thenReturn(pref);
        assertEquals(pref, repo.load(luser, "name"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSave() {
        Preference p = mock(Preference.class);

        when(p.getName()).thenReturn("name");
        when(p.isNew()).thenReturn(true);
        when(userAccessor.getCurrentUser()).thenReturn(user);
        repo.save(p);
        verify(em).persist(p);
        verify(p).setOwner(user);
        when(p.isNew()).thenReturn(false);
        repo.save(p);
        verify(em).merge(p);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSaveAll() {
        Collection<Preference> prefs = new ArrayList<Preference>();
        Preference p = mock(Preference.class);

        when(p.getName()).thenReturn("name");
        when(p.isNew()).thenReturn(true);
        prefs.add(p);

        Preference po = mock(Preference.class);

        when(po.getName()).thenReturn("name");
        when(po.isNew()).thenReturn(false);
        prefs.add(po);
        repo.saveAll(prefs);
        verify(em).persist(p);
        verify(em).merge(po);
    }
}
