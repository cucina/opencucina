package org.cucina.search.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.cucina.search.SearchDao;
import org.cucina.search.query.NamedQuery;
import org.cucina.search.query.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;


/**
 * Implements search functionality including ability to cache searches.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JpaSearchDao
    implements SearchDao {
    private static final Logger LOG = LoggerFactory.getLogger(JpaSearchDao.class);
    @PersistenceContext
    private EntityManager entityManager;
    private boolean useCache = false;

    /**
     * JAVADOC Method Level Comments
     *
     * @param entityManager
     *            JAVADOC.
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Should cacheable template be used
     *
     * @param useCache
     *            boolean
     */
    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public boolean isUseCache() {
        return useCache;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param updateString
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public int bulkUpdate(String updateString, Object... values) {
        List<Object> parameters = new ArrayList<Object>();

        if (ArrayUtils.isNotEmpty(values)) {
            parameters.addAll(Arrays.asList(values));
        }

        Query query = entityManager.createQuery(updateString);

        addParameters(query, null, parameters);

        return query.executeUpdate();
    }

    /**
     * Find by SpriteQuery query
     *
     * @param query
     *            SpriteQuery.
     *
     * @return List<?>.
     */
    @Override
    public <T> List<T> find(SearchQuery query) {
        Assert.notNull(query, "query cannot be null");
        Assert.notNull(query.getQuery(), "cannot provide a query with nothing to run");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Find by query [" + query + "]");
        }

        Query jpaQuery = prepareQuery(query, null, null);

        return doFind(jpaQuery);
    }

    @Override
    public <T> List<T> find(SearchQuery query, int firstResult, int maxResult) {
        Query jpaQuery = prepareQuery(query, firstResult, maxResult);

        return doFind(jpaQuery);
    }

    @Override
    public <T> List<T> findByNamedQuery(String namedQuery, Object... values)
        throws DataAccessException {
        return find(new NamedQuery(namedQuery, values));
    }

    /**
     * Search by named query
     *
     * @param namedQuery
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws DataAccessException
     *             JAVADOC.
     */
    @Override
    public <T> List<T> findByNamedQuery(String namedQuery, String[] paramNames, Object[] values)
        throws DataAccessException {
        return find(new NamedQuery(namedQuery, Arrays.asList(paramNames), Arrays.asList(values)));
    }

    @Override
    public <T> List<T> findByNamedQuery(NamedQuery namedQuery)
        throws DataAccessException {
        return find(namedQuery);
    }

    @Override
    public <T, V> List<Map<T, V>> findByNamedQueryMap(String namedQuery, Object... values)
        throws DataAccessException {
        return findMap(new NamedQuery(namedQuery, values));
    }

    @Override
    public <T, V> List<Map<T, V>> findByNamedQueryMap(String namedQuery, String[] paramNames,
        Object[] values)
        throws DataAccessException {
        return findMap(new NamedQuery(namedQuery, Arrays.asList(paramNames), Arrays.asList(values)));
    }

    @Override
    public <T, V> List<Map<T, V>> findByNamedQueryMap(NamedQuery query)
        throws DataAccessException {
        return findMap(query);
    }

    @Override
    public <T> List<T> findByNativeQuery(SearchQuery query) {
        Assert.notNull(query, "query cannot be null");
        Assert.notNull(query.getQuery(), "cannot provide a query with nothing to run");
        Assert.isTrue(CollectionUtils.isEmpty(query.getParamNames()),
            "does not support named parameters");

        Query jpaQuery = entityManager.createNativeQuery(query.getQuery());

        addParameters(jpaQuery, null, query.getValues());

        return doFind(jpaQuery);
    }

    @Override
    public <T, V> List<Map<T, V>> findMap(SearchQuery query, int firstResult, int maxResult) {
        query.setSelectMap(true);

        Query jpaQuery = prepareQuery(query, firstResult, maxResult);

        List<Map<String, Object>> results = doFind(jpaQuery);

        return convertRowToMap(results);
    }

    @Override
    public <T, V> List<Map<T, V>> findMap(SearchQuery query) {
        query.setSelectMap(true);

        Query jpaQuery = prepareQuery(query, null, null);

        List<Map<String, Object>> results = doFind(jpaQuery);

        return convertRowToMap(results);
    }

    private void setReadOnlyHint(Query query, boolean isReadOnly) {
        // TODO portable across JPA       query.setHint(QueryHints.READ_ONLY, isReadOnly);
    }

    private void setReturnMapHint(Query query) {
        // TODO ditto       query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
    }

    private void addParameters(Query jpaQuery, List<String> paramNames, List<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            if (CollectionUtils.isEmpty(paramNames)) {
                for (int i = 0; i < values.size(); i++) {
                    jpaQuery.setParameter(i + 1, values.get(i));
                }
            } else {
                for (int i = 0; i < values.size(); i++) {
                    jpaQuery.setParameter(paramNames.get(i), values.get(i));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> convertRowToMap(List<Map<String, Object>> results) {
        List<Map<String, Object>> convertedResults = new ArrayList<Map<String, Object>>();

        if (CollectionUtils.isNotEmpty(results)) {
            for (Map<String, Object> result : results) {
                convertedResults.add(new HashMap<String, Object>(result));
            }
        }

        return (List<T>) convertedResults;
    }

    /*
    * Return the result for the provided query. Implementations should provide
    * support for caching the query and returning the results as
    * List<Map<String,Object>> as provided by the jpa implementation used.
    *
    * @param jpaQuery
    *
    * @return
    */
    @SuppressWarnings("unchecked")
    private <T> List<T> doFind(Query query) {
        try {
            return query.getResultList();
        } catch (RuntimeException e) {
            LOG.warn("Oops", e);
            throw e;
        }
    }

    private Query prepareQuery(SearchQuery query, Integer firstResult, Integer maxResult) {
        Assert.notNull(query, "query cannot be null");
        Assert.notNull(query.getQuery(), "cannot provide a query with nothing to run");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Find by query [" + query + "]");
        }

        Query jpaQuery = null;

        if (query instanceof NamedQuery) {
            jpaQuery = entityManager.createNamedQuery(query.getQuery());
        } else {
            jpaQuery = entityManager.createQuery(query.getQuery());
        }

        addParameters(jpaQuery, query.getParamNames(), query.getValues());

        // If we are going to restrict result size
        if (firstResult != null) {
            jpaQuery.setFirstResult(firstResult);
            jpaQuery.setMaxResults(maxResult);
        }

        setReadOnlyHint(jpaQuery, query.isReadOnly());

        if (query.isSelectMap()) {
            setReturnMapHint(jpaQuery);
        }

        return jpaQuery;
    }
}
