package org.cucina.search.query;

import org.cucina.core.InstanceFactory;
import org.cucina.core.utils.ClassDescriptor;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchQueryGeneratorFactory {
	private InstanceFactory instanceFactory;
	private SearchQueryGenerator postProcessSearchQueryGenerator;
	private SearchQueryGenerator searchQueryGenerator;

	/**
	 * Creates a new SearchQueryGeneratorFactory object.
	 *
	 * @param instanceFactory                 JAVADOC.
	 * @param searchQueryGenerator            JAVADOC.
	 * @param postProcessSearchQueryGenerator JAVADOC.
	 */
	public SearchQueryGeneratorFactory(InstanceFactory instanceFactory,
									   SearchQueryGenerator searchQueryGenerator,
									   SearchQueryGenerator postProcessSearchQueryGenerator) {
		Assert.notNull(instanceFactory);
		Assert.notNull(searchQueryGenerator);
		Assert.notNull(postProcessSearchQueryGenerator);
		this.instanceFactory = instanceFactory;
		this.searchQueryGenerator = searchQueryGenerator;
		this.postProcessSearchQueryGenerator = postProcessSearchQueryGenerator;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param searchBean JAVADOC.
	 * @return JAVADOC.
	 */
	public SearchQueryGenerator getSearchQueryGenerator(SearchBean searchBean) {
		SearchQueryGenerator ret = searchQueryGenerator;

		LinkedHashMap<String, String> map = searchBean.getAliasByType();

		//get first applicationtype
		if ((map != null) && !map.isEmpty()) {
			String applicationType = map.keySet().iterator().next();

			Class<Object> clazz = instanceFactory.getClassType(applicationType);

			if ((clazz != null) && ClassDescriptor.isPostProcessProjections(clazz)) {
				ret = postProcessSearchQueryGenerator;
			}
		}

		return ret;
	}
}
