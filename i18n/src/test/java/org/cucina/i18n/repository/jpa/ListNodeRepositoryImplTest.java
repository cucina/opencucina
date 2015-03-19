package org.cucina.i18n.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.i18n.model.ListNode;

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
public class ListNodeRepositoryImplTest {
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private EntityManager em;
    private ListNodeRepositoryImpl repo;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        repo = new ListNodeRepositoryImpl();
        repo.setEntityManager(em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFind() {
        repo.find(100L);
        verify(em).find(ListNode.class, 100L);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByType() {
        CriteriaQuery<ListNode> cq = mock(CriteriaQuery.class);

        when(cb.createQuery(ListNode.class)).thenReturn(cq);

        Root<ListNode> token = mock(Root.class);

        when(cq.from(ListNode.class)).thenReturn(token);

        Path<Object> pathdi = mock(Path.class);

        when(token.get("type")).thenReturn(pathdi);

        Predicate predi = mock(Predicate.class);

        when(cb.equal(pathdi, "type")).thenReturn(predi);

        TypedQuery<ListNode> tq = mock(TypedQuery.class);

        when(cq.where(predi)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
        repo.findByType("type");
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSaveCollectionOfListNode() {
        Collection<ListNode> nodes = new ArrayList<ListNode>();
        ListNode node = new ListNode();

        node.setId(100L);
        nodes.add(node);

        ListNode nnode = new ListNode();

        nodes.add(nnode);
        repo.save(nodes);
        verify(em).merge(node);
        verify(em).persist(nnode);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSaveListNode() {
        ListNode node = new ListNode();

        repo.save(node);
        verify(em).persist(node);
    }
}
