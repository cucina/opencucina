package org.cucina.engine.repository.jpa;

import org.cucina.core.InstanceFactory;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TokenRepositoryImplTest {
	@Mock
	private CriteriaBuilder cb;
	@Mock
	private EntityManager em;
	@Mock
	private InstanceFactory instF;
	private TokenRepositoryImpl trepo;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		trepo = new TokenRepositoryImpl(instF);
		trepo.setEntityManager(em);
		when(em.getCriteriaBuilder()).thenReturn(cb);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testCreate() {
		ProcessToken wf = new ProcessToken();
		Foo foo = new Foo();

		wf.setDomainObject(foo);
		doAnswer(new Answer<Object>() {
			public Void answer(InvocationOnMock invocation)
					throws Throwable {
				Foo ft = (Foo) invocation.getArguments()[0];

				ft.setId(111L);

				return null;
			}
		}).when(em).persist(foo);
		trepo.save(wf);
		verify(em).persist(wf);
		assertEquals("Foo", wf.getDomainObjectType());
		assertEquals(111L, wf.getDomainObjectId());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testCreateExist() {
		ProcessToken wf = new ProcessToken();
		Foo foo = new Foo(111L);

		wf.setDomainObject(foo);
		trepo.save(wf);
		verify(em).merge(foo);
		verify(em).persist(wf);
		assertEquals("Foo", wf.getDomainObjectType());
		assertEquals(111L, wf.getDomainObjectId());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadDomainIds() {
		CriteriaQuery<Long> cq = mock(CriteriaQuery.class);

		when(cb.createQuery(Long.class)).thenReturn(cq);

		Root<ProcessToken> token = mock(Root.class);

		when(cq.from(ProcessToken.class)).thenReturn(token);

		Path<Object> pathdi = mock(Path.class);

		when(token.get("workflowDefinitionId")).thenReturn(pathdi);

		Path<Object> pathpi = mock(Path.class);

		when(token.get("placeId")).thenReturn(pathpi);

		Path<Object> pathdo = mock(Path.class);

		when(token.get("domainObjectType")).thenReturn(pathdo);

		Predicate predi = mock(Predicate.class);

		when(cb.equal(pathdi, "workflowId")).thenReturn(predi);

		Predicate prepi = mock(Predicate.class);

		when(cb.equal(pathpi, "placeId")).thenReturn(prepi);

		Predicate predo = mock(Predicate.class);

		when(cb.equal(pathdo, "applicationType")).thenReturn(predo);

		Predicate preand = mock(Predicate.class);

		when(cb.and(predi, prepi, predo)).thenReturn(preand);

		TypedQuery<Long> tq = mock(TypedQuery.class);

		when(cq.where(preand)).thenReturn(cq);
		when(em.createQuery(cq)).thenReturn(tq);
		trepo.findDomainIdsByWorkflowIdPlaceIdApplicationType("workflowId", "placeId",
				"applicationType");

		verify(cq).select(token.<Long>get("domainObjectId"));
		verify(tq).getResultList();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadToken() {
		Foo foo = new Foo(1L);

		CriteriaQuery<ProcessToken> cq = mock(CriteriaQuery.class);

		when(cb.createQuery(ProcessToken.class)).thenReturn(cq);

		Root<ProcessToken> token = mock(Root.class);

		when(cq.from(ProcessToken.class)).thenReturn(token);

		Path<Object> pathdo = mock(Path.class);

		when(token.get("domainObjectType")).thenReturn(pathdo);

		Path<Object> pathpi = mock(Path.class);

		when(token.get("domainObjectId")).thenReturn(pathpi);

		Predicate predo = mock(Predicate.class);

		when(cb.equal(pathdo, "Foo")).thenReturn(predo);

		Predicate predi = mock(Predicate.class);

		when(cb.equal(pathpi, 1L)).thenReturn(predi);

		Predicate preand = mock(Predicate.class);

		when(cb.and(predo, predi)).thenReturn(preand);

		TypedQuery<ProcessToken> tq = mock(TypedQuery.class);

		when(em.createQuery(cq)).thenReturn(tq);

		ProcessToken tok = new ProcessToken();

		when(tq.getSingleResult()).thenReturn(tok);

		assertEquals(tok, trepo.findByDomain(foo));
		assertEquals(foo, tok.getDomainObject());
		verify(cq).where(preand);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testLoadTokens() {
		Serializable[] ids = new Serializable[]{1L};

		CriteriaQuery<Tuple> tcq = mock(CriteriaQuery.class);

		when(cb.createTupleQuery()).thenReturn(tcq);

		Root<ProcessToken> rt = mock(Root.class);

		when(tcq.from(ProcessToken.class)).thenReturn(rt);

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

		when(instF.<Foo>getClassType("applicationType")).thenReturn(Foo.class);

		Root<Foo> rc = mock(Root.class);

		when(tcq.from(Foo.class)).thenReturn(rc);

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
		ProcessToken token = new ProcessToken();

		when(tuple.get(0)).thenReturn(token);

		Foo foo = new Foo();

		when(tuple.get(1)).thenReturn(foo);
		list.add(tuple);
		when(tq.getResultList()).thenReturn(list);
		trepo.findByApplicationTypeAndIds("applicationType", ids);
		assertEquals(foo, token.getDomainObject());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testUpdate() {
		ProcessToken wf = new ProcessToken();

		wf.setId(222L);

		Foo foo = new Foo(111L);

		wf.setDomainObject(foo);
		wf.setDomainObjectId(foo.getId());
		wf.setDomainObjectType(foo.getApplicationType());
		trepo.save(wf);
		verify(em).merge(foo);
		verify(em).merge(wf);
	}
}
