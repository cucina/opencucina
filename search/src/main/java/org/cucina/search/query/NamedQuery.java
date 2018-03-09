package org.cucina.search.query;

import java.util.List;


/**
 * Contains query name and values for a query. values order should match order
 * of the parameters defined in the query.
 */
public class NamedQuery
		extends SearchQuery {
	/**
	 * Creates a new <code>NamedQuery</code>.
	 *
	 * @param name
	 * @param readOnly
	 * @param objects
	 */
	public NamedQuery(String name, boolean readOnly, Object... objects) {
		super(name, readOnly, objects);
	}

	/**
	 * Creates a new <code>NamedQuery</code>
	 *
	 * @param name
	 * @param values
	 * @param readOnly
	 */
	public NamedQuery(String name, List<Object> values, boolean readOnly) {
		super(name, values, readOnly);
	}

	/**
	 * Creates a new <code>NamedQuery</code> which will be run in readOnly mode.
	 *
	 * @param name
	 * @param values
	 */
	public NamedQuery(String name, List<Object> values) {
		this(name, values, false);
	}

	/**
	 * Creates a new <code>NamedQuery</code>.
	 *
	 * @param name
	 * @param paramNames
	 * @param values
	 * @param readOnly
	 */
	public NamedQuery(String name, List<String> paramNames, List<Object> values, boolean readOnly) {
		super(name, paramNames, values, readOnly);
	}

	/**
	 * Creates a new <code>NamedQuery</code> which will be run in readOnly mode.
	 *
	 * @param name
	 * @param paramNames
	 * @param values
	 */
	public NamedQuery(String name, List<String> paramNames, List<Object> values) {
		this(name, paramNames, values, false);
	}

	/**
	 * Creates a new <code>NamedQuery</code> which will be run in readOnly mode.
	 *
	 * @param name
	 * @param objects
	 */
	public NamedQuery(String name, Object... objects) {
		super(name, false, objects);
	}
}
