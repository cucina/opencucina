package org.cucina.search.query;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains query and values for a query. values order should match order of
 * substitutions in query. Ummutable.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchQuery {
	private static final Logger LOG = LoggerFactory
			.getLogger(SearchQuery.class);
	private List<String> paramNames;
	private List<Object> values;
	private String query;
	private boolean readOnly = false;
	private boolean selectMap = false;

	/**
	 * Creates a new SpriteQuery object.
	 *
	 * @param query  String.
	 * @param values List<Object>.
	 */
	public SearchQuery(String query, List<Object> values) {
		super();
		this.query = query;
		this.values = values;
	}

	/**
	 * Creates a new SearchQuery object.
	 *
	 * @param query    JAVADOC.
	 * @param values   JAVADOC.
	 * @param readOnly JAVADOC.
	 */
	public SearchQuery(String query, List<Object> values, boolean readOnly) {
		super();
		this.values = values;
		this.query = query;
		this.readOnly = readOnly;
	}

	/**
	 * Creates a new SpriteQuery object.
	 *
	 * @param query  String.
	 * @param values List<Object>.
	 */
	public SearchQuery(String query, List<String> paramNames,
					   List<Object> values) {
		super();
		this.query = query;
		this.values = values;
		this.paramNames = paramNames;
	}

	/**
	 * Creates a new SearchQuery object.
	 *
	 * @param query    JAVADOC.
	 * @param values   JAVADOC.
	 * @param readOnly JAVADOC.
	 */
	public SearchQuery(String query, List<String> paramNames,
					   List<Object> values, boolean readOnly) {
		super();
		this.values = values;
		this.query = query;
		this.readOnly = readOnly;
		this.paramNames = paramNames;
	}

	/**
	 * Creates a new SearchQuery object.
	 *
	 * @param query
	 * @param objects
	 */
	public SearchQuery(String query, Object... objects) {
		super();
		this.query = query;
		this.values = (objects == null) ? Collections.emptyList() : Arrays
				.asList(objects);
	}

	/**
	 * Creates a new SearchQuery object.
	 *
	 * @param query
	 * @param readOnly
	 * @param objects
	 */
	public SearchQuery(String query, boolean readOnly, Object... objects) {
		this(query, objects);
		this.readOnly = readOnly;
	}

	/**
	 * Create a template string to use as an 'in'
	 *
	 * @param length
	 * @return
	 */
	public static String createIn(int length) {
		StringBuffer sb = new StringBuffer(" in(");

		for (int i = 0; i < length; i++) {
			if (i > 0) {
				sb.append(",");
			}

			sb.append("?");
		}

		sb.append(")");

		if (LOG.isDebugEnabled()) {
			LOG.debug("Constructed query fragment :'" + sb + "'");
		}

		return sb.toString();
	}

	/**
	 * @return parameterNames
	 */
	public List<String> getParamNames() {
		return paramNames;
	}

	/**
	 * Get query
	 *
	 * @return query String.
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @return readOnly flag
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Returns whether or not we expect results to be returned as
	 * <code>Map</code>s.
	 *
	 * @return
	 */
	public boolean isSelectMap() {
		return selectMap;
	}

	/**
	 * Sets whether or not we want results to be returned as <code>Map</code>
	 *
	 * @param selectMap
	 */
	public void setSelectMap(boolean selectMap) {
		this.selectMap = selectMap;
	}

	/**
	 * Get values
	 *
	 * @return values List<Object>.
	 */
	public List<Object> getValues() {
		return values;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param obj JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof SearchQuery)) {
			return false;
		}

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		SearchQuery q = (SearchQuery) obj;

		equalsBuilder.append(query, q.query).append(values, q.values)
				.append(readOnly, q.readOnly).append(paramNames, q.paramNames);

		return equalsBuilder.isEquals();
	}

	/**
	 * hashCode override
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Override
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
