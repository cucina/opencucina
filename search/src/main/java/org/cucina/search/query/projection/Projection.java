
package org.cucina.search.query.projection;

import org.cucina.search.query.SearchComponent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Implementations of projections, i.e. items in select statement.
 *
 * @author $Author: $
 * @version $Revision: $
  */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface Projection
    extends SearchComponent {
    /**
     * If this is an aggregate function, i.e. max, min, etc.
     *
     * @return
     */
    @JsonIgnore
    boolean isAggregate();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    String getAlias();

    /**
     * If a group by is required whether this projection should be listed
     *
     * @return
     */
    @JsonIgnore
    boolean isGroupable();

    /**
     * Gets the projection for insertion into the overall query.
     *
     * @return projection String.
     */
    @JsonIgnore
    String getProjection();
}
