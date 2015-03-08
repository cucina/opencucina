
package org.cucina.search.query;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Defines contract for SearchCriterion.
 *
 * @author $Author: $
 * @version $Revision: $
  */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface SearchCriterion
    extends SearchComponent {
    /**
     * Gets restriction for the implementation
     *
     * @return String.
     */
    @JsonIgnore
    String getRestriction();

    /**
     * Gets the values for the restriction
     *
     * @return List<Object>.
     */
    @JsonIgnore
    List<Object> getValues();
}
