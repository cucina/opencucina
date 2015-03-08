
package org.cucina.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class AbstractResultSetModifier
    implements ResultSetModifier {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractResultSetModifier.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param results JAVADOC.
     */
    public abstract void doModify(String applicationType, Collection<Map<String, Object>> results);

    /**
     * Modify the resultset if we're dealing with <code>Map</code>s.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void modify(String applicationType, @SuppressWarnings("rawtypes")
    Collection results) {
        if (CollectionUtils.isNotEmpty(results) && results.iterator().next() instanceof Map) {
            convertToHashMapRows(results);
            doModify(applicationType, results);
        } else {
            LOG.debug("Not modifying as results contains something other that <Map<String,Object>>");
        }
    }

    /**
     * Make sure that we've got <code>HashMap</code> rows so that we can modify them in the
     * modifier.
     * @param results
     */
    private void convertToHashMapRows(Collection<Map<String, Object>> results) {
        ArrayList<Map<String, Object>> hashMapResults = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> row : results) {
            if (row instanceof HashMap) {
                LOG.debug("It's already a Collection of HashMap skipping conversion");

                return;
            }

            HashMap<String, Object> hashMapRow = new HashMap<String, Object>();

            for (Map.Entry<String, Object> entry : row.entrySet()) {
                hashMapRow.put(entry.getKey(), entry.getValue());
            }

            hashMapResults.add(hashMapRow);
        }

        results.clear();
        results.addAll(hashMapResults);
    }
}
