package org.cucina.search.model.projection;


/**
 * Used to mark projections which should not be returned in the std search group
 * but should still be available for searching.
 * TODO convert to annotation
 */
public interface ExtendedSearch
    extends Search {
}
