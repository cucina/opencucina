package org.cucina.search.query.jpa;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.*;
import org.cucina.search.query.projection.Projection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;


/**
 * SearchQueryGenerator for jpa which produces SpriteQuery object containing jpql and objects in
 * the order of substitutions in jpql.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JpaSearchQueryGenerator
		implements SearchQueryGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(JpaSearchQueryGenerator.class);
	@PersistenceContext
	private EntityManager entityManager;
	private InstanceFactory instanceFactory;
	private boolean processProjections = true;

	/**
	 * Set sessionFactory
	 *
	 * @param entityManager String
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Set instanceFactory
	 *
	 * @param instanceFactory InstanceFactory.
	 */
	@Required
	public void setInstanceFactory(InstanceFactory instanceFactory) {
		this.instanceFactory = instanceFactory;
	}

	/**
	 * Set processProjections, if false projections will not be added to the jpql,
	 * By default this is true.
	 *
	 * @param processProjections boolean
	 */
	public void setProcessProjections(boolean processProjections) {
		this.processProjections = processProjections;
	}

	/**
	 * Generate the query from specified SearchBean
	 *
	 * @param searchBean SearchBean.
	 * @return SpriteQuery. If projections were supplied it will have selectMap = true
	 */
	@Override
	public SearchQuery generateQuery(SearchBean searchBean) {
		Assert.notNull(searchBean, "searchBean must be provided as an argument");
		Assert.isTrue(MapUtils.isNotEmpty(searchBean.getAliasByType()),
				"aliasByType cannot be null and must have one value");

		if (LOG.isDebugEnabled()) {
			LOG.debug("Create search for types and aliases [" + searchBean.getAliasByType() + "]");
		}

		//Initialise jpql parts
		StringBuffer jpqlProjectString = new StringBuffer();
		StringBuffer jpqlJoinString = new StringBuffer(" from ");
		StringBuffer jpqlRestrictString = new StringBuffer();
		Collection<String> aliases = new ArrayList<String>();

		//Keep track of all joins
		Collection<String> existingJoins = new HashSet<String>();

		//Keep track of order of all values to add to SpriteQuery
		List<Object> values = new ArrayList<Object>();

		Map<String, String> typeByAlias = new HashMap<String, String>();

		//Create mapping of typeByAlias
		for (String type : searchBean.getAliasByType().keySet()) {
			typeByAlias.put(searchBean.getAliasByType().get(type), type);
		}

		for (Iterator<Map.Entry<String, String>> aliasByTypeIterator = searchBean.getAliasByType()
				.entrySet()
				.iterator();
			 aliasByTypeIterator.hasNext(); ) {
			Map.Entry<String, String> aliasByType = aliasByTypeIterator.next();

			String type = aliasByType.getKey();
			String alias = aliasByType.getValue();

			Assert.isTrue(!aliases.contains(alias),
					"Invalid setup, cannot have clashing aliases, there must be projection alias clashing with table name [" +
							alias + "]");

			aliases.add(alias);
			jpqlJoinString.append(type);
			jpqlJoinString.append(" ");
			jpqlJoinString.append(alias);
			jpqlJoinString.append(" ");

			if (processProjections) {
				Collection<Projection> projections = searchBean.getProjections(alias);

				jpqlProjectString = addProjections(projections, jpqlProjectString, jpqlJoinString,
						existingJoins, typeByAlias, aliases);
			}

			Collection<SearchCriterion> criteria = searchBean.getCriteria(alias);

			if (CollectionUtils.isNotEmpty(criteria)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Adding restrictions ");
				}

				//If this is second time through add an and
				if (jpqlRestrictString.length() > 0) {
					jpqlRestrictString.append(" and ");
				}

				for (Iterator<SearchCriterion> iterator = criteria.iterator(); iterator.hasNext(); ) {
					SearchCriterion crit = iterator.next();

					Map<String, String> parentAliases = crit.getParentAliases();

					//If it doesn't already have alias then it needs join added, so add it
					//Do prior to getting restriction as we need parentAlias to be set
					if (MapUtils.isEmpty(parentAliases)) {
						addJoin(jpqlJoinString, existingJoins, typeByAlias, crit);
					}

					String restrictionValue = crit.getRestriction();

					if (LOG.isDebugEnabled()) {
						LOG.debug("Adding restriction [" + restrictionValue + "]");
					}

					jpqlRestrictString.append(restrictionValue);
					values.addAll(crit.getValues());

					if (iterator.hasNext()) {
						jpqlRestrictString.append(" and ");
					}
				}
			}

			if (aliasByTypeIterator.hasNext()) {
				jpqlJoinString.append(", ");
			}
		}

		//Iterate over positional parameters and convert to JPA positional parameters, i.e. convert from "?, ?" to "?1, ?2".
		if (jpqlRestrictString.indexOf("?") > 0) {
			int prevIndex = 0;

			for (int position = 0; 0 < jpqlRestrictString.indexOf("?", prevIndex + 1);
				 position++) {
				prevIndex = jpqlRestrictString.indexOf("?", prevIndex + 1);
				jpqlRestrictString.insert(prevIndex + 1, position + 1);
			}
		}

		//Make the final jpql String depending upon what's been added
		StringBuffer jpql = new StringBuffer("select ");

		if (processProjections && (jpqlProjectString.length() > 0)) {
			jpql.append(jpqlProjectString);
		} else {
			// use case in ForeignKeySearchCriterionSystemTest
			jpql.append(StringUtils.join(aliases, ','));
		}

		jpql.append(jpqlJoinString);

		if (jpqlRestrictString.length() > 0) {
			jpql.append("where ");
			jpql.append(jpqlRestrictString);
		}

		if (processProjections && (searchBean.aggregateProjectionCount() > 0) &&
				(searchBean.aggregateProjectionCount() < searchBean.getProjections().size())) {
			jpql.append(" group by ");

			for (Projection projection : searchBean.getProjections()) {
				if (projection.isGroupable()) {
					jpql.append(projection.getSearchPropertyName());
					jpql.append(" ,");
				}
			}

			//remove last ','
			jpql.deleteCharAt(jpql.length() - 1);
		}

		if (CollectionUtils.isNotEmpty(searchBean.getOrder())) {
			jpql.append(" order by ");

			for (int i = 0; i < searchBean.getOrder().size(); i++) {
				if (i > 0) {
					jpql.append(",");
				}

				OrderBy orderBy = searchBean.getOrder().get(i);

				jpql.append(orderBy.getRestriction());
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Generated jpql is [" + jpql.toString() + "]");
		}

		SearchQuery query = new SearchQuery(jpql.toString(), values);

		query.setSelectMap(processProjections && (jpqlProjectString.length() > 0));

		return query;
	}

	/**
	 * Adds joins and updates component with the parentAliases generated keyed by parent property name or
	 * rootAlias if only one level deep
	 *
	 * @param jpqlJoinString StringBuffer.
	 * @param existingJoins  Collection<String>.
	 * @param typeByAlias    Map<String,String> of types keyed by alias in order to aid property validation
	 * @param component      SearchComponent which needs joins created for.
	 */
	protected void addJoin(StringBuffer jpqlJoinString, Collection<String> existingJoins,
						   Map<String, String> typeByAlias, SearchComponent component) {
		Map<String, List<Join>> distinctJoins = component.getJoins();

		Map<String, String> parentAliases = new HashMap<String, String>();

		if (MapUtils.isNotEmpty(distinctJoins)) {
			StringBuffer qualifiedJoin = new StringBuffer(component.getRootAlias());

			for (Map.Entry<String, List<Join>> entries : distinctJoins.entrySet()) {
				String rootAlias = entries.getKey();
				List<Join> joins = entries.getValue();
				String parentType = typeByAlias.get(rootAlias);

				Assert.notNull(parentType, "parentType not found for root alias [" + rootAlias +
						"]");

				//First parent will always be rootAlias, and there may be no joins in which case we still need to set parentAlias on component
				String parentAlias = rootAlias;

				if (CollectionUtils.isNotEmpty(joins)) {
					String property = null;

					for (Join join : joins) {
						property = join.getProperty();

						//If this isn't a persistent class then end
						String childType = instanceFactory.getPropertyType(parentType, property);

						Assert.notNull(childType,
								"Must map to a type in system [" + parentType + "], [" + property +
										"]");

						Class<?> childClass = instanceFactory.getClassType(childType);

						//If no type for this then it must be the property to search on, mustn't it?
						if ((childClass == null) ||
								(entityManager.getMetamodel().managedType(childClass) == null)) {
							throw new IllegalArgumentException("Error as this type [" + childType +
									"] does not have a mapping.");
						}

						//Make alias name of property, if we need to be unique adjust next
						String alias = property;

						if (join.isUnique()) {
							int incr = 0;
							boolean exists = true;

							//If unique always concat with incr so we won't confuse with standard alias
							alias = property + incr++;

							//while loop here to increment an int in order to append to alias to make unique..... that's the idea anyway
							while (exists) {
								for (String existingJoin : existingJoins) {
									if (existingJoin.contains(alias)) {
										alias = property + incr++;

										break;
									}
								}

								exists = false;
							}
						}

						qualifiedJoin = qualifiedJoin.append(".");
						qualifiedJoin = qualifiedJoin.append(alias);

						if (!existingJoins.contains(qualifiedJoin.toString())) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Adding Join [" +
										NameUtils.concat(parentAlias, property) + "]");
							}

							//Might want to pull the join into the projection/criterion at some point, for now all left
							jpqlJoinString.append(join.getJoin());
							jpqlJoinString.append(NameUtils.concat(parentAlias, property));
							jpqlJoinString.append(" ");
							jpqlJoinString.append(alias);
							jpqlJoinString.append(" ");

							existingJoins.add(qualifiedJoin.toString());
						}

						//Now reset parent to current join
						parentAlias = alias;
						parentType = childType;
					}

					//Key the parent property to it's alias for use in searchComponent
					parentAliases.put(property, parentAlias);
				} else {
					//if no joins add root alias keyed by root alias
					parentAliases.put(rootAlias, rootAlias);
				}
			}
		}

		//if nothing added add root alias keyed by root alias
		if (parentAliases.size() == 0) {
			parentAliases.put(component.getRootAlias(), component.getRootAlias());
		}

		//And finally set alias on component
		component.setParentAliases(parentAliases);
	}

	private StringBuffer addProjections(Collection<Projection> projections,
										StringBuffer jpqlProjectString, StringBuffer jpqlJoinString,
										Collection<String> existingJoins, Map<String, String> typeByAlias,
										Collection<String> aliases) {
		if (CollectionUtils.isNotEmpty(projections)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding projections ");
			}

			//If this is second time through add a ,
			if (jpqlProjectString.length() > 0) {
				jpqlProjectString.append(", ");
			}

			for (Iterator<Projection> iterator = projections.iterator(); iterator.hasNext(); ) {
				Projection projection = iterator.next();

				Assert.isTrue(!aliases.contains(projection.getAlias()),
						"Invalid setup, cannot have clashing aliases, could be that this alias is table name alias [" +
								projection.getAlias() + "]");

				//Need to add join first as parentAlias needs to be set first
				addJoin(jpqlJoinString, existingJoins, typeByAlias, projection);

				String projectionValue = projection.getProjection();

				if (LOG.isDebugEnabled()) {
					LOG.debug("Adding projection [" + projectionValue + "]");
				}

				jpqlProjectString.append(projectionValue);

				if (iterator.hasNext()) {
					jpqlProjectString.append(", ");
				}

				//Add alias to catch reuse
				aliases.add(projection.getAlias());
			}
		}

		return jpqlProjectString;
	}
}
