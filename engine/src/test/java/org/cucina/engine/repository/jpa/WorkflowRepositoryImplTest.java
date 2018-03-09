package org.cucina.engine.repository.jpa;

import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.model.Workflow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowRepositoryImplTest {
	@Mock
	private CriteriaBuilder cb;
	@Mock
	private EntityManager em;
	private WorkflowRepositoryImpl repo;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		repo = new WorkflowRepositoryImpl();
		repo.setEntityManager(em);
		when(em.getCriteriaBuilder()).thenReturn(cb);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testExists() {
		CriteriaQuery<Workflow> cq = mock(CriteriaQuery.class);

		when(cb.createQuery(Workflow.class)).thenReturn(cq);

		Root<Workflow> token = mock(Root.class);

		when(cq.from(Workflow.class)).thenReturn(token);

		Path<Object> pathdi = mock(Path.class);

		when(token.get("workflowId")).thenReturn(pathdi);

		Predicate predi = mock(Predicate.class);

		when(cb.equal(pathdi, "definitionId")).thenReturn(predi);

		TypedQuery<Workflow> tq = mock(TypedQuery.class);

		when(tq.getSingleResult()).thenReturn(mock(Workflow.class));
		when(em.createQuery(cq)).thenReturn(tq);
		assertTrue(repo.exists("definitionId"));
		verify(cq).where(predi);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testIsActive() {
		CriteriaQuery<Long> cq = mock(CriteriaQuery.class);

		when(cb.createQuery(Long.class)).thenReturn(cq);

		Root<ProcessToken> token = mock(Root.class);

		when(cq.from(ProcessToken.class)).thenReturn(token);

		Path<Object> pathdi = mock(Path.class);

		when(token.get("workflowDefinitionId")).thenReturn(pathdi);

		Predicate predi = mock(Predicate.class);

		when(cb.equal(pathdi, "definitionId")).thenReturn(predi);

		TypedQuery<Long> tq = mock(TypedQuery.class);

		when(tq.getSingleResult()).thenReturn(1L);
		when(em.createQuery(cq)).thenReturn(tq);
		assertTrue(repo.isActive("definitionId"));
		verify(cq).where(predi);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAll() {
		CriteriaQuery<Workflow> cq = mock(CriteriaQuery.class);

		when(cb.createQuery(Workflow.class)).thenReturn(cq);

		TypedQuery<Workflow> tq = mock(TypedQuery.class);

		when(em.createQuery(cq)).thenReturn(tq);
		repo.findAll();
		verify(tq).getResultList();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadByWorkflowId() {
		CriteriaQuery<Workflow> cq = mock(CriteriaQuery.class);

		when(cb.createQuery(Workflow.class)).thenReturn(cq);

		Root<Workflow> token = mock(Root.class);

		when(cq.from(Workflow.class)).thenReturn(token);

		Path<Object> pathdi = mock(Path.class);

		when(token.get("workflowId")).thenReturn(pathdi);

		Predicate predi = mock(Predicate.class);

		when(cb.equal(pathdi, "definitionId")).thenReturn(predi);

		TypedQuery<Workflow> tq = mock(TypedQuery.class);

		when(em.createQuery(cq)).thenReturn(tq);
		repo.findByWorkflowId("definitionId");
		verify(cq).where(predi);
		verify(tq).getSingleResult();
	}
}
