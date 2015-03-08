package org.cucina.search;

import java.util.Map;


/**
 * Matches <code>Map</code> attributes properties.
 *
 */
public interface AttributeMatcher {
    /**
     * Returns true if the passed attributes are seen to match those of the
     * restrictions.
     *
     * @param attributes
     * @param restrictions
     * @return
     */
    boolean match(Map<String, Object> attributes, Map<String, Object> restrictions);
}
