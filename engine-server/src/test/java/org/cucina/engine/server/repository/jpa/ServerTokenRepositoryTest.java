package org.cucina.engine.server.repository.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.engine.model.WorkflowToken;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.server.testassist.Foo;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ServerTokenRepositoryTest {
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private EntityManager em;
    private ServerTokenRepository repo;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        repo = new ServerTokenRepository();
        repo.setEntityManager(em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testLoadTokens() {
        Long[] ids = new Long[] { 1L };

        CriteriaQuery<Tuple> tcq = mock(CriteriaQuery.class);

        when(cb.createTupleQuery()).thenReturn(tcq);

        Root<WorkflowToken> rt = mock(Root.class);

        when(tcq.from(WorkflowToken.class)).thenReturn(rt);

        Path<Object> pathdo = mock(Path.class);

        when(rt.get("domainObjectType")).thenReturn(pathdo);

        Path<Object> pathpi = mock(Path.class);

        when(rt.get("domainObjectId")).thenReturn(pathpi);

        Predicate predo = mock(Predicate.class);

        when(cb.equal(pathdo, "applicationType")).thenReturn(predo);

        Predicate predi = mock(Predicate.class);

        when(pathpi.in((Object[]) ids)).thenReturn(predi);

        Predicate tokp = mock(Predicate.class);

        when(cb.and(predo, predi)).thenReturn(tokp);

        Root<EntityDescriptor> rc = mock(Root.class);

        when(tcq.from(EntityDescriptor.class)).thenReturn(rc);

        Path<Object> rid = mock(Path.class);

        when(rc.get("id")).thenReturn(rid);

        Predicate clap = mock(Predicate.class);

        when(cb.equal(pathpi, rid)).thenReturn(clap);

        Predicate pans = mock(Predicate.class);

        when(cb.and(clap, tokp)).thenReturn(pans);
        when(tcq.multiselect(rt, rc)).thenReturn(tcq);
        when(tcq.where(pans)).thenReturn(tcq);

        TypedQuery<Tuple> tq = mock(TypedQuery.class);

        when(em.createQuery(tcq)).thenReturn(tq);

        List<Tuple> list = new ArrayList<Tuple>();
        Tuple tuple = mock(Tuple.class);
        WorkflowToken token = new WorkflowToken();

        when(tuple.get(0)).thenReturn(token);

        Foo foo = new Foo();

        when(tuple.get(1)).thenReturn(foo);
        list.add(tuple);
        when(tq.getResultList()).thenReturn(list);
        repo.loadTokens("applicationType", ids);
        assertEquals(foo, token.getDomainObject());
    }
}
