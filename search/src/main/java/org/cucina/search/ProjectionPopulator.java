
package org.cucina.search;

import java.util.Map;

import org.cucina.search.query.SearchBean;


/**
 * Generates projections for
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ProjectionPopulator {
    String PROJECTIONS = "projections";

    /**
     * Populates SearchBean with projections for type.
     *
     * @param type JAVADOC.
     * @param bean SearchBean.
     * @param params Map<String, Object>
     */
    void populate(String type, SearchBean bean, Map<String, Object> params,
        Class<?>... projectionGroup);
}
