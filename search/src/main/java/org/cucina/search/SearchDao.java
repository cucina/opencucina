
package org.cucina.search;

import java.util.List;
import java.util.Map;

import org.cucina.search.query.NamedQuery;
import org.cucina.search.query.SearchQuery;
import org.springframework.dao.DataAccessException;

/**
 * Search related functionality.
 * 
 * @author $Author: $
 * @version $Revision: $
 */
public interface SearchDao {
	/**
	 * Runs the search using the provided ad hoc query controlling offset and
	 * max number of results. Useful for paging through large result sets.
	 * 
	 * @param query
	 *            ad hoc query.
	 * @param firstResult
	 *            index of the first result.
	 * @param maxResult
	 *            limit of number of results.
	 * 
	 * @return results
	 */
	public <T> List<T> find(SearchQuery query, int firstResult, int maxResult);

	/**
	 * Runs the search using the provided ad hoc query
	 * 
	 * @param query
	 *            ad hoc query.
	 * 
	 * @return results
	 */
	public <T> List<T> find(SearchQuery query);

	/**
	 * Runs the search using the named query
	 * 
	 * @param namedQuery
	 *            Query name.
	 * @param paramNames
	 *            parameters names.
	 * @param values
	 *            parameters values.
	 * 
	 * @return List of results.
	 * 
	 * @throws DataAccessException
	 *             JAVADOC.
	 */
	public <T> List<T> findByNamedQuery(final String namedQuery,
			final String[] paramNames, final Object[] values)
			throws DataAccessException;

	/**
	 * issues bulk update on updateString with values.
	 * 
	 * @param updateString
	 * 
	 * @return int number of updated items
	 */
	int bulkUpdate(String updateString, Object... values);

	/**
	 * Search by a named query
	 * 
	 * @param namedQuery
	 * @param values - query parameters
	 * @return list of results
	 * @throws DataAccessException
	 */
	<T> List<T> findByNamedQuery(final String namedQuery,
			final Object... values) throws DataAccessException;

	/**
	 * Search by a named query 
	 * @param namedQuery
	 * @param paramNames - names of parameters
	 * @param values - parameters values
	 * @return list of results
	 * @throws DataAccessException
	 */
	<T, V> List<Map<T, V>> findByNamedQueryMap(final String namedQuery,
			final String[] paramNames, final Object[] values)
			throws DataAccessException;
	
	/**
	 * Search by a named query using the <code>NamedQuery</code> wrapper object
	 * to simplify setting of query properties like readOnly and any restriction values.
	 * @see org.cucina.meringue.query.NamedQuery. 
	 * @param namedQuery. 
	 */
	<T, V> List<Map<T, V>> findByNamedQueryMap(NamedQuery namedQuery) throws DataAccessException;
	
	/**
	 * Search by a named query using the <code>NamedQuery</code> wrapper object
	 * to simplify setting of query properties like readOnly and any restriction values.
	 * @see org.cucina.meringue.query.NamedQuery. 
	 * @param namedQuery. 
	 */
	<T> List<T> findByNamedQuery(NamedQuery namedQuery) throws DataAccessException;

	/**
	 * Search by named query 
	 * @param namedQuery
	 * @param values - parameter values
	 * @return List of maps of name-value pairs
	 * @throws DataAccessException
	 */
	<T, V> List<Map<T, V>> findByNamedQueryMap(final String namedQuery,
			final Object... values) throws DataAccessException;

	/**
	 * Search by the query paging results
	 * @param query
	 * @param firstResult offset from the first record
	 * @param maxResult size of the page
	 * @return List of maps of name-value pairs
	 */
	<T, V> List<Map<T, V>> findMap(SearchQuery query, int firstResult,
			int maxResult);

	/**
	 * Search by the query
	 * @param query
	 * @return List of maps of name-value pairs
	 */
	<T, V> List<Map<T, V>> findMap(SearchQuery query);

	/**
	 * Search by the native sql query
	 * @param query
	 * @return List of results
	 */
    <T> List<T> findByNativeQuery(SearchQuery query);
}
