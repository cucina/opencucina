package org.cucina.search.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.cucina.search.query.SearchQuery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests HibernateSearchDao functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class JpaSearchDaoTest {
    private static final String QUERY = "select foo from Foo foo";
    private static final String NAMED_PARAMS_QUERY = "select foo from Foo foo where id in :ids and id in :others and id = :id";
    private static final Collection<Long> IDS_COLL = Collections.singleton(12L);
    private static final Long[] IDS_ARRAY = { 14L, 15L };
    private static final Long ID = 18L;
    private static final String[] NAMED_PARAMS = new String[] { "ids", "others", "id" };
    private static final List<Object> VALUES = new ArrayList<Object>();
    private static final String RESULT_KEY = "resultKey";
    private static final String RESULT_VALUE = "resultValue";
    private EntityManager em;
    private JpaSearchDao dao;
    private List<Object> results = new ArrayList<Object>();
    private Query query;

    /**
     * Test build update returns correct value
     */
    @Test
    public void bulkUpdate() {
        String updateString = "delete Permission";

        when(em.createQuery(updateString)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(2);
        assertEquals("Incorrect number updated", 2, dao.bulkUpdate(updateString, new Object[] {  }));
    }

    /**
     * Find by query and values
     */
    @Test
    public void findByNamedParams() {
        when(em.createQuery(NAMED_PARAMS_QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);
        assertEquals("Incorrect results", results,
            dao.find(
                new SearchQuery(NAMED_PARAMS_QUERY, Arrays.asList(NAMED_PARAMS),
                    Arrays.asList(new Object[] { IDS_COLL, IDS_ARRAY, ID }))));

        verify(query).setParameter(NAMED_PARAMS[0], IDS_COLL);
        verify(query).setParameter(NAMED_PARAMS[1], IDS_ARRAY);
        verify(query).setParameter(NAMED_PARAMS[2], ID);
    }

    /**
     * Find by namedQuery
     */
    @Test
    public void findByNamedQuery() {
        when(em.createNamedQuery("myQuery")).thenReturn(query);
        when(query.setParameter(1, 1)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results,
            dao.findByNamedQuery("myQuery", new Object[] { 1 }));
    }

    /**
     * Find by namedQuery
     */
    @Test
    public void findByNamedQueryMap() {
        Query query = mock(Query.class);

        when(em.createNamedQuery("myQuery")).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        List<Map<Object, Object>> retResults = dao.findByNamedQueryMap("myQuery", new Object[] { 1 });

        assertNotSame("Incorrect results", results, retResults);
        assertEquals("Should contain value", RESULT_VALUE,
            retResults.iterator().next().get(RESULT_KEY));

        verify(query).setParameter(1, 1);

        //        verify(query).setHint(QueryHints.RESULT_TYPE, ResultType.Map);
    }

    /**
     * Find by namedQuery
     */
    @Test
    public void findByNamedQueryNoParams() {
        when(em.createNamedQuery("myQuery")).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results, dao.findByNamedQuery("myQuery"));
    }

    /**
     * Find by namedQuery
     */
    @Test
    public void findByNamedQueryWithNamedParams() {
        when(em.createNamedQuery("myQuery")).thenReturn(query);
        when(query.setParameter(NAMED_PARAMS[0], IDS_COLL)).thenReturn(query);
        when(query.setParameter(NAMED_PARAMS[1], IDS_ARRAY)).thenReturn(query);
        when(query.setParameter(NAMED_PARAMS[2], ID)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results,
            dao.findByNamedQuery("myQuery", NAMED_PARAMS, new Object[] { IDS_COLL, IDS_ARRAY, ID }));
    }

    /**
     * Find by namedQuery
     */
    @Test
    public void findByNamedQueryWithNamedParamsMap() {
        Query query = mock(Query.class);

        when(em.createNamedQuery("myQuery")).thenReturn(query);
        when(query.setParameter(NAMED_PARAMS[0], IDS_COLL)).thenReturn(query);
        when(query.setParameter(NAMED_PARAMS[1], IDS_ARRAY)).thenReturn(query);
        when(query.setParameter(NAMED_PARAMS[2], ID)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results,
            dao.findByNamedQueryMap("myQuery", NAMED_PARAMS,
                new Object[] { IDS_COLL, IDS_ARRAY, ID }));

        verify(query).setParameter(NAMED_PARAMS[0], IDS_COLL);
        verify(query).setParameter(NAMED_PARAMS[1], IDS_ARRAY);
        verify(query).setParameter(NAMED_PARAMS[2], ID);

        //        verify(query).setHint(QueryHints.RESULT_TYPE, ResultType.Map);
    }

    /**
     * Find by Query
     */
    @Test
    public void findByNativeQuery() {
        when(em.createNativeQuery(QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results,
            dao.findByNativeQuery(new SearchQuery(QUERY, (List<Object>) null)));
    }

    /**
     * Find by namedQuery
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNativeQueryWithNamedParams() {
        List<Object> params = new ArrayList<Object>();

        params.add(IDS_COLL);

        assertEquals("Incorrect results", results,
            dao.findByNativeQuery(new SearchQuery(QUERY, Collections.singletonList("name"), params)));
    }

    /**
     * Find by namedQuery
     */
    @Test
    public void findByNativeQueryWithParams() {
        when(em.createNativeQuery(QUERY)).thenReturn(query);
        when(query.setParameter(1, IDS_COLL)).thenReturn(query);
        when(query.setParameter(2, IDS_ARRAY)).thenReturn(query);
        when(query.setParameter(3, ID)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        List<Object> params = new ArrayList<Object>();

        params.add(IDS_COLL);
        params.add(IDS_ARRAY);
        params.add(ID);

        assertEquals("Incorrect results", results,
            dao.findByNativeQuery(new SearchQuery(QUERY, params)));
    }

    /**
     * Find by Query
     */
    @Test
    public void findByQuery() {
        when(em.createQuery(QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results,
            dao.find(new SearchQuery(QUERY, (List<Object>) null)));
    }

    /**
     * Find by Query
     */
    @Test
    public void findByQueryMap() {
        Query query = mock(Query.class);

        when(em.createQuery(QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results, dao.findMap(new SearchQuery(QUERY, VALUES)));

        //        verify(query).setHint(QueryHints.RESULT_TYPE, ResultType.Map);
    }

    /**
     * Find by Query
     */
    @Test
    public void findByQueryMapRestrictResults() {
        Query query = mock(Query.class);

        when(em.createQuery(QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results,
            dao.findMap(new SearchQuery(QUERY, VALUES), 10, 20));

        //        verify(query).setHint(QueryHints.RESULT_TYPE, ResultType.Map);
        verify(query).setMaxResults(20);
        verify(query).setFirstResult(10);
    }

    /**
     * Find by Query
     */
    @Test
    public void findByQueryRestrictResults() {
        when(em.createQuery(QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results, dao.find(new SearchQuery(QUERY, VALUES), 10, 20));

        verify(query).setMaxResults(20);
        verify(query).setFirstResult(10);
    }

    /**
     * Set up test
     */
    @Before
    public void onSetup() {
        query = mock(Query.class);
        em = mock(EntityManager.class);

        dao = new JpaSearchDao();
        dao.setEntityManager(em);

        Map<String, Object> result = new HashMap<String, Object>();

        result.put(RESULT_KEY, RESULT_VALUE);

        results.add(result);
    }

    /**
     * find requires query
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresQuery() {
        dao.find(null);
    }

    /**
     * find requires query on query
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresQueryQuery() {
        dao.find(new SearchQuery(null, VALUES));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testReadOnlyHint() {
        when(em.createQuery(QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results,
            dao.find(new SearchQuery(QUERY, true, (Object[]) null)));

        //        verify(query).setHint(QueryHints.READ_ONLY, true);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testReadOnlyHintNot() {
        when(em.createQuery(QUERY)).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        assertEquals("Incorrect results", results, dao.find(new SearchQuery(QUERY, (Object[]) null)));

        //       verify(query, never()).setHint(QueryHints.READ_ONLY, HintValues.TRUE);
    }
}
