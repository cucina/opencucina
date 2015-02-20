package org.cucina.cluster.repository.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.cluster.model.ClusterControl;
import org.cucina.core.InstanceFactory;
import org.cucina.testassist.utils.LoggingEnabler;
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
public class ClusterControlRepositoryImplTest {
    private ClusterControlRepositoryImpl repo;
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private EntityManager em;
    @Mock
    private InstanceFactory instanceFactory;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        LoggingEnabler.enableLog(ClusterControlRepositoryImpl.class);
        repo = new ClusterControlRepositoryImpl(instanceFactory);
        repo.setEntityManager(em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testComplete() {
        CriteriaUpdate<ClusterControl> cq = mock(CriteriaUpdate.class);

        when(cb.createCriteriaUpdate(ClusterControl.class)).thenReturn(cq);

        Root<ClusterControl> root = mock(Root.class);

        when(cq.from(ClusterControl.class)).thenReturn(root);

        Path<Object> pathc = mock(Path.class);

        when(root.get("complete")).thenReturn(pathc);

        Predicate cp = mock(Predicate.class);

        when(cb.equal(pathc, false)).thenReturn(cp);

        Path<Object> pathe = mock(Path.class);

        when(root.get("event")).thenReturn(pathe);

        Predicate ep = mock(Predicate.class);

        when(cb.equal(pathe, "eventName")).thenReturn(ep);

        Path<Object> pathan = mock(Path.class);

        when(root.get("activeNodeId")).thenReturn(pathan);

        Predicate np = mock(Predicate.class);

        when(cb.equal(pathan, "nodeId")).thenReturn(np);

        Predicate and = mock(Predicate.class);

        when(cb.and(ep, cp, np)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);
        when(cq.set(pathc, true)).thenReturn(cq);

        Query tq = mock(Query.class);

        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.executeUpdate()).thenReturn(1).thenReturn(0);
        assertTrue(repo.complete("eventName", "nodeId"));
        assertFalse(repo.complete("eventName", "nodeId"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testCreate() {
        ClusterControl cc = new ClusterControl();

        when(instanceFactory.getBean(ClusterControl.class.getSimpleName())).thenReturn(cc);

        Map<Object, Object> properties = new HashMap<Object, Object>();

        properties.put("1", 2L);

        assertTrue(repo.create("eventName", "nodeId", properties));
        assertEquals("eventName", cc.getEvent());
        assertEquals("nodeId", cc.getActiveNodeId());
        assertEquals(properties, cc.getProperties());

        assertEquals(false, cc.isComplete());
        verify(instanceFactory).getBean(ClusterControl.class.getSimpleName());
        verify(em).persist(cc);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteByEventName() {
        CriteriaDelete<ClusterControl> cq = mock(CriteriaDelete.class);

        when(cb.createCriteriaDelete(ClusterControl.class)).thenReturn(cq);

        Root<ClusterControl> root = mock(Root.class);

        when(cq.from(ClusterControl.class)).thenReturn(root);

        Path<Object> pathc = mock(Path.class);

        when(root.get("complete")).thenReturn(pathc);

        Predicate cp = mock(Predicate.class);

        when(cb.equal(pathc, false)).thenReturn(cp);

        Path<Object> pathe = mock(Path.class);

        when(root.get("event")).thenReturn(pathe);

        Predicate ep = mock(Predicate.class);

        when(cb.equal(pathe, "eventName")).thenReturn(ep);

        Predicate and = mock(Predicate.class);

        when(cb.and(ep, cp)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        Query tq = mock(Query.class);

        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(repo.deleteByEventName("eventName"));
        assertFalse(repo.deleteByEventName("eventName"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFind() {
        repo.find(100L);
        verify(em).find(ClusterControl.class, 100L);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindAllCurrentNotThisNode() {
        CriteriaQuery<ClusterControl> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(ClusterControl.class)).thenReturn(cq);

        Root<ClusterControl> root = mock(Root.class);

        when(cq.from(ClusterControl.class)).thenReturn(root);

        Path<Object> pathdi = mock(Path.class);

        when(root.get("complete")).thenReturn(pathdi);

        Predicate predi = mock(Predicate.class);

        when(cb.equal(pathdi, false)).thenReturn(predi);

        Path<Object> pathan = mock(Path.class);

        when(root.get("activeNodeId")).thenReturn(pathan);

        Predicate preda = mock(Predicate.class);

        when(cb.notEqual(pathan, "thisNode")).thenReturn(preda);

        Predicate and = mock(Predicate.class);

        when(cb.and(predi, preda)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        TypedQuery<ClusterControl> tq = mock(TypedQuery.class);

        when(em.createQuery(cq)).thenReturn(tq);
        repo.findCurrentByNotThisNode("thisNode");
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByEventNameAndCurrent() {
        CriteriaQuery<ClusterControl> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(ClusterControl.class)).thenReturn(cq);

        Root<ClusterControl> root = mock(Root.class);

        when(cq.from(ClusterControl.class)).thenReturn(root);

        Path<Object> pathc = mock(Path.class);

        when(root.get("complete")).thenReturn(pathc);

        Predicate cp = mock(Predicate.class);

        when(cb.equal(pathc, false)).thenReturn(cp);

        Path<Object> pathe = mock(Path.class);

        when(root.get("event")).thenReturn(pathe);

        Predicate ep = mock(Predicate.class);

        when(cb.equal(pathe, "eventName")).thenReturn(ep);

        Predicate and = mock(Predicate.class);

        when(cb.and(ep, cp)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        TypedQuery<ClusterControl> tq = mock(TypedQuery.class);

        when(em.createQuery(cq)).thenReturn(tq);
        repo.findByEventNameAndCurrent("eventName");
        verify(tq).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByEventNameAndNodeIdAndCurrent() {
        CriteriaQuery<ClusterControl> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(ClusterControl.class)).thenReturn(cq);

        Root<ClusterControl> root = mock(Root.class);

        when(cq.from(ClusterControl.class)).thenReturn(root);

        Path<Object> pathc = mock(Path.class);

        when(root.get("complete")).thenReturn(pathc);

        Predicate cp = mock(Predicate.class);

        when(cb.equal(pathc, false)).thenReturn(cp);

        Path<Object> pathn = mock(Path.class);

        when(root.get("activeNodeId")).thenReturn(pathn);

        Predicate np = mock(Predicate.class);

        when(cb.equal(pathn, "nodeId")).thenReturn(np);

        Path<Object> pathe = mock(Path.class);

        when(root.get("event")).thenReturn(pathe);

        Predicate ep = mock(Predicate.class);

        when(cb.equal(pathe, "eventName")).thenReturn(ep);

        Predicate and = mock(Predicate.class);

        when(cb.and(ep, cp, np)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        TypedQuery<ClusterControl> tq = mock(TypedQuery.class);

        when(em.createQuery(cq)).thenReturn(tq);
        repo.findByEventNameAndNodeIdAndCurrent("eventName", "nodeId");
        verify(tq).getSingleResult();
    }
}
