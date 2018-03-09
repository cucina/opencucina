package org.cucina.search.query;

import org.cucina.search.query.projection.Projection;


/**
 * Generates appropriate projections
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ProjectionFactory {
	/**
	 * Create projection according to arguments.
	 *
	 * @param rootType  String of type name is relative to.
	 * @param name      String property name, fully qualified if appropriate.
	 * @param alias     String.
	 * @param rootAlias String.
	 * @param type      String non mandatory marker for special function.
	 * @return Projection.
	 */
	Projection create(String rootType, String name, String alias, String rootAlias,
					  String specialFunction);
}
