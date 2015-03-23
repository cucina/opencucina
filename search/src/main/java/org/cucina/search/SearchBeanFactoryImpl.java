package org.cucina.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.groups.Default;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import org.cucina.core.InstanceFactory;
import org.cucina.core.utils.ClassDescriptor;
import org.cucina.core.utils.NameUtils;

import org.cucina.search.marshall.SearchCriterionMarshallManager;
import org.cucina.search.model.ProjectionSeed;
import org.cucina.search.query.OrderBy;
import org.cucina.search.query.ProjectionFactory;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.projection.CountProjection;
import org.cucina.search.query.projection.Projection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Helper class to generate <code>SearchBean</code>s for watchlists. Builds the
 * list of projections and search criteria from the provided config.
 *
 */
public class SearchBeanFactoryImpl
    implements SearchBeanFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SearchBeanFactoryImpl.class);
    Class<?> defaultProjectionGroup = Default.class;
    Map<String, Map<String, String>> additionalAliasesByType;
    /**
     * Holds the alias to property relation for each applicationType. The
     * underlying <code>Map</code> has alias as the key and property as the
     * value. Now keyed on Projection Group
     */
    Map<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>> aliasedProperties;
    private Collection<Class<?>> classList;

    /**
     * InstanceFactory which we'll use to work out the underlying type of the
     * search property.
     */
    private InstanceFactory instanceFactory;

    /**
     * <code>Map</code> of typeName.alias containing the actual property we'd
     * like to search on as this may differ from the display field.
     */
    private Map<String, String> criteriaOverrides = Collections.emptyMap();

    /**
     * Holds the <code>ProjectionProvider</code> to be applied to searches by
     * type and projection group.
     */
    private Map<String, Map<Class<?extends Default>, Collection<ProjectionProvider>>> projectionProviders =
        Collections.emptyMap();
    private ProjectionFactory projectionFactory;
    private SearchCriterionMarshallManager searchCriterionMarshaller;

    /**
     * JAVADOC Method Level Comments
     *
     * @param classList
     *            JAVADOC.
     */
    @Required
    public void setClassList(Collection<Class<?>> classList) {
        this.classList = classList;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param criteriaOverrides
     *            JAVADOC.
     */
    public void setCriteriaOverrides(Map<String, String> criteriaOverrides) {
        this.criteriaOverrides = criteriaOverrides;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param defaultProjectionGroup
     *            JAVADOC.
     */
    public void setDefaultProjectionGroup(Class<?> defaultProjectionGroup) {
        this.defaultProjectionGroup = defaultProjectionGroup;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param instanceFactory
     *            JAVADOC.
     */
    @Required
    public void setInstanceFactory(InstanceFactory instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    /**
     * Set projectionFactory
     *
     * @param projectionFactory
     *            ProjectionFactory
     */
    @Required
    public void setProjectionFactory(ProjectionFactory projectionFactory) {
        this.projectionFactory = projectionFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param projectionProviders
     *            JAVADOC.
     */
    public void setProjectionProviders(
        Map<String, Map<Class<?extends Default>, Collection<ProjectionProvider>>> projectionProviders) {
        this.projectionProviders = projectionProviders;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType
     *            JAVADOC.
     * @param alias
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see org.cucina.meringue.search.support.SearchBeanFactory#getPropertyFromAlias(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String getPropertyFromAlias(String applicationType, String alias) {
        Assert.hasText(applicationType, "applicationType is required!");
        Assert.hasText(alias, "alias is required!");

        Map<String, ProjectionSeed> aliases = getAliasBySubjectType(applicationType);

        Assert.isTrue(MapUtils.isNotEmpty(aliases),
            "No rootAlias found for subjectType:" + applicationType);

        ProjectionSeed seed = aliases.get(alias);

        Assert.notNull(seed, "Invalid alias " + alias + " for type " + applicationType);

        return NameUtils.concat(seed.getEntityAlias(), seed.getProperty());
    }

    /**
     * @param setSearchCriterionMarshallManager
     */
    @Required
    public void setSearchCriterionMarshallManager(
        SearchCriterionMarshallManager searchCriterionMarshaller) {
        this.searchCriterionMarshaller = searchCriterionMarshaller;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.cucina.meringue.search.support.SearchBeanFactory#addCountProjection
     * (java.lang.String, org.cucina.meringue.search.SearchBean)
     */

    /**
     * JAVADOC Method Level Comments
     *
     * @param subjectType
     *            JAVADOC.
     * @param search
     *            JAVADOC.
     * @param alias
     *            String, if not provided will depend upon CountProjection
     *            assigning.
     */
    @Override
    public void addCountProjection(String subjectType, SearchBean search, String alias) {
        Assert.notNull(search, "search is required!");
        Assert.hasText(subjectType, "subjectType is required!");

        Map<String, ProjectionSeed> aliases = getAliasBySubjectType(subjectType);

        Assert.isTrue(MapUtils.isNotEmpty(aliases),
            "No rootAlias found for subjectType:" + subjectType);

        ProjectionSeed seed = aliases.values().iterator().next();

        search.addProjection(new CountProjection("id", alias, seed.getEntityAlias()));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param subjectType
     *            JAVADOC.
     * @param property
     *            JAVADOC.
     * @param asc
     *            JAVADOC.
     * @param search
     *            JAVADOC. * @see
     *            org.cucina.meringue.search.support.SearchBeanFactory
     *            #addOrder(java .lang.String, java.lang.String, boolean,
     *            org.cucina.meringue.search.SearchBean)
     */
    @Override
    public void addOrder(String property, boolean asc, SearchBean search) {
        search.addOrder(new OrderBy(property, asc));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param subjectType
     *            JAVADOC.
     * @param search
     *            JAVADOC.
     * @param projections
     *            JAVADOC.
     * @see org.cucina.meringue.search.SearchBeanFactory#addProjections(java.lang
     *      .String, org.cucina.meringue.query.SearchBean, java.lang.String[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addProjections(String subjectType, SearchBean search,
        Collection<String> projections, Class<?>... projectionGroup) {
        Assert.notNull(search, "search is required!");
        Assert.hasText(subjectType, "subjectType is required!");
        Assert.notEmpty(projections, "projections is required!");

        addProjections(subjectType, search, projectionGroup);

        ArrayList<Projection> orderedProjections = new ArrayList<Projection>();

        for (String requestedProjection : projections) {
            for (Iterator<Projection> iter = search.getProjections().iterator(); iter.hasNext();) {
                Projection projection = iter.next();

                if (requestedProjection.equals(projection.getAlias())) {
                    orderedProjections.add(projection);
                    iter.remove();

                    break;
                }
            }
        }

        search.setProjections(orderedProjections);

        Collection<String> unresolved = CollectionUtils.subtract(projections,
                CollectionUtils.collect(search.getProjections(),
                    new BeanToPropertyValueTransformer("alias")));

        if (LOG.isDebugEnabled()) {
            LOG.debug("The following requested projections " + unresolved + " could not be found");
        }

        Assert.notEmpty(search.getProjections(), "No projections could be added!");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param subjectType
     *            JAVADOC.
     * @param search
     *            JAVADOC.
     * @see org.cucina.meringue.search.support.SearchBeanFactory#addProjections(java.lang.String,
     *      org.cucina.meringue.search.SearchBean)
     */
    @Override
    public void addProjections(String subjectType, SearchBean search, Class<?>... projectionGroup) {
        Assert.notNull(search, "search is required!");
        Assert.hasText(subjectType, "subjectType is required!");

        Map<String, ProjectionSeed> aliases = getAliasBySubjectType(subjectType, projectionGroup);

        for (Map.Entry<String, ProjectionSeed> entry : aliases.entrySet()) {
            ProjectionSeed seed = entry.getValue();

            if (LOG.isDebugEnabled()) {
                LOG.debug("For alias '" + entry.getKey() + "' loaded array [" +
                    seed.getEntityAlias() + "," + seed.getProperty() + "]");
            }

            Projection projection = projectionFactory.create(seed.getEntityClazz(),
                    seed.getProperty(), entry.getKey(), seed.getEntityAlias(), seed.getAggregate());

            if (projection != null) {
                search.addProjection(projection);
            }
        }

        Map<Class<?extends Default>, Collection<ProjectionProvider>> providersByGroup = projectionProviders.get(subjectType);

        if (MapUtils.isNotEmpty(providersByGroup)) {
            Collection<ProjectionProvider> providers = providersByGroup.get(ArrayUtils.isNotEmpty(
                        projectionGroup) ? projectionGroup[0] : defaultProjectionGroup);

            if (CollectionUtils.isNotEmpty(providers)) {
                for (ProjectionProvider projectionProvider : providers) {
                    projectionProvider.provide(subjectType, search);
                }
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param subjectType
     *            JAVADOC.
     * @param criteria
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see org.cucina.meringue.search.support.SearchBeanFactory#buildSearchBean(java.lang.String,
     *      java.util.Map)
     */
    @Override
    public SearchBean buildSearchBean(String subjectType, Map<String, Object> criteria,
        SearchType... searchType) {
        Assert.hasText(subjectType, "type is required!");

        SearchBean searchBean = new SearchBean();

        if ((searchType != null) && (searchType.length > 0)) {
            searchBean.setSearchType(searchType[0]);
        }

        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        searchBean.setAliasByType(aliasByType);

        String subjectAlias = StringUtils.uncapitalize(subjectType);

        aliasByType.put(subjectType, subjectAlias);

        if (MapUtils.isNotEmpty(additionalAliasesByType) &&
                MapUtils.isNotEmpty(additionalAliasesByType.get(subjectType))) {
            aliasByType.putAll(additionalAliasesByType.get(subjectType));
        }

        unmarshallCriteria(criteria, subjectType, searchBean);

        return searchBean;
    }

    /**
     * Transform the list of available columns into an aliased map.
     *
     * @throws Exception
     */
    public void init()
        throws Exception {
        aliasedProperties = new HashMap<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>>();
        initProjectionGroup(defaultProjectionGroup);
        additionalAliasesByType = new HashMap<String, Map<String, String>>();
        initAdditionalAliases();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param subjectType
     *            JAVADOC.
     * @param criteria
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see org.cucina.meringue.search.support.SearchBeanFactory#marshallCriteria(java.lang.String,
     *      org.cucina.meringue.search.SearchBean)
     */
    @Override
    public Map<String, Object> marshallCriteria(String subjectType, SearchBean criteria) {
        HashMap<String, Object> marshalledCriteria = new HashMap<String, Object>();

        for (SearchCriterion criterion : criteria.getCriteria()) {
            if (StringUtils.isBlank(criterion.getName())) {
                continue;
            }

            String propertyName = NameUtils.concat(criterion.getRootAlias(), criterion.getName());
            String alias = criterion.getAlias();

            if (StringUtils.isBlank(alias) || criterion.getName().equals(alias)) {
                alias = getPropertyAliasOverride(subjectType, propertyName, criterion.getName());
            }

            if (StringUtils.isBlank(alias) && StringUtils.isBlank(criterion.getName())) {
                continue;
            }

            searchCriterionMarshaller.marshall(alias, criterion.getName(), criterion,
                marshalledCriteria);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("MarshalledCriteria:" + marshalledCriteria);
        }

        return marshalledCriteria;
    }

    /**
     * Unmarshall the restrictions from the front-end into
     * <code>SearchCriterion</code> according to the underlying property type.
     *
     * @param criteria
     * @param aliases
     * @param searchBean
     * @param aliasByType
     */
    protected void unmarshallCriteria(Map<String, Object> criteria, String subjectType,
        SearchBean searchBean) {
        Map<String, ProjectionSeed> aliases = getAliasBySubjectType(subjectType);
        Map<String, String> typeByAlias = getTypeByAlias(searchBean.getAliasByType());

        for (Map.Entry<String, ProjectionSeed> entry : aliases.entrySet()) {
            ProjectionSeed seed = entry.getValue();
            String entityAlias = seed.getEntityAlias();
            String property = seed.getProperty();
            String aggregate = seed.getAggregate();
            String entityName = typeByAlias.get(entityAlias);
            String criteriaOverrideKey = NameUtils.concat(entityName, entry.getKey());

            // Now check if we have a criteriaOverride specified for
            // typeName.alias, if so use that as the property to search.
            if (criteriaOverrides.containsKey(criteriaOverrideKey)) {
                property = criteriaOverrides.get(criteriaOverrideKey);
            }

            if (StringUtils.isNotBlank(aggregate)) {
                // we don't allow criteria for aggregates
                continue;
            }

            String propertyType = instanceFactory.getPropertyType(entityName, property);

            Assert.hasText(propertyType,
                "Couldn't workout the propertyType for " +
                ToStringBuilder.reflectionToString(seed) + " entityName='" + entityName +
                "', property='" + property + "'");

            SearchCriterion criterion = searchCriterionMarshaller.unmarshall(propertyType,
                    property, entry.getKey(), entityName, entityAlias, criteria);

            if (criterion != null) {
                searchBean.addCriterion(criterion);
            }
        }

        String entityAlias = searchBean.getAliasByType().get(subjectType);

        // Iterate over criterion in case any are set up by propertyName
        for (String alias : criteria.keySet()) {
            SearchCriterion criterion = searchCriterionMarshaller.unmarshall(alias, subjectType,
                    entityAlias, criteria);

            if (criterion != null) {
                searchBean.addCriterion(criterion);
            }
        }
    }

    private Map<String, ProjectionSeed> getAliasBySubjectType(String subjectType,
        Class<?>... projectionGroup) {
        Class<?> projectionGrp = defaultProjectionGroup;

        if (!ArrayUtils.isEmpty(projectionGroup)) {
            projectionGrp = projectionGroup[0];
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting aliases for projection group " + projectionGrp +
                " and subjectType " + subjectType);
        }

        Map<String, LinkedHashMap<String, ProjectionSeed>> aliasedPropertiesByProjGroup = aliasedProperties.get(projectionGrp);

        if (aliasedPropertiesByProjGroup == null) {
            initProjectionGroup(projectionGrp);
        }

        aliasedPropertiesByProjGroup = aliasedProperties.get(projectionGrp);

        if (aliasedPropertiesByProjGroup == null) {
            LOG.warn("Attempting to init alias for projectionGroup [" +
                projectionGrp.getSimpleName().toString() +
                "] and it's null. Please check configuration");

            return Collections.emptyMap();
        }

        Map<String, ProjectionSeed> ret = aliasedPropertiesByProjGroup.get(subjectType);

        if (ret == null) {
            ret = Collections.emptyMap();
        }

        return ret;
    }

    /**
     * Return the alias for the given fully qualified property including the
     * rootAlias.
     *
     * @param property
     * @return
     */
    private String getPropertyAlias(String applicationType, String property) {
        Assert.hasText(applicationType, "applicationType must be provided!");
        Assert.hasText(property, "property must be provided!");

        Map<String, ProjectionSeed> aliasToProperty = getAliasBySubjectType(applicationType);

        for (Map.Entry<String, ProjectionSeed> entry : aliasToProperty.entrySet()) {
            if (property.equals(NameUtils.concat(entry.getValue().getEntityAlias(),
                            entry.getValue().getProperty()))) {
                return entry.getKey();
            }
        }

        return null;
    }

    private String getPropertyAliasOverride(String subjectType, String propertyName,
        String criterionName) {
        String alias = getPropertyAlias(subjectType, propertyName);

        for (String key : criteriaOverrides.keySet()) {
            String[] typeAlias = StringUtils.split(key, ".");

            Assert.isTrue(typeAlias.length == 2,
                "CriteriaOverride configuration issue, should be in the form type.alias");

            if (typeAlias[0].equals(subjectType) &&
                    criteriaOverrides.get(key).equals(criterionName)) {
                alias = typeAlias[1];
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("For subjectType='" + subjectType + "' propertyName='" + propertyName +
                "' criterionName='" + criterionName + "' found alias='" + alias + "'");
        }

        return alias;
    }

    /**
     * Returns a <code>Map<String,String></code> containing the entityName and
     * alias where alias is the key.
     *
     * @param entityAlias
     * @param aliasByType
     * @return
     */
    private Map<String, String> getTypeByAlias(LinkedHashMap<String, String> aliasByType) {
        HashMap<String, String> typeByAlias = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : aliasByType.entrySet()) {
            typeByAlias.put(entry.getValue(), entry.getKey());
        }

        return typeByAlias;
    }

    private void initAdditionalAliases() {
        for (Class<?> clazz : classList) {
            Map<String, String> additionalAliases = ClassDescriptor.getAdditionalAliases(clazz);

            if (MapUtils.isNotEmpty(additionalAliases) && MapUtils.isNotEmpty(additionalAliases)) {
                for (Map.Entry<String, String> additionalAliasByType : additionalAliases.entrySet()) {
                    // update existing entry
                    additionalAliases.put(additionalAliasByType.getKey(),
                        additionalAliasByType.getValue());
                }

                additionalAliasesByType.put(clazz.getSimpleName(), additionalAliases);
            }
        }
    }

    private Map<String, LinkedHashMap<String, ProjectionSeed>> initProjectionGroup(
        Class<?> projectionGroup) {
        HashMap<String, LinkedHashMap<String, ProjectionSeed>> typeToAlias = new HashMap<String, LinkedHashMap<String, ProjectionSeed>>();

        for (Class<?> clazz : classList) {
            List<ProjectionSeed> seeds = ProjectionSeed.getProjections(clazz, projectionGroup);
            String type = clazz.getSimpleName();

            Assert.notEmpty(seeds, "No properties provided for type " + type + "!");

            LinkedHashMap<String, ProjectionSeed> aliases = new LinkedHashMap<String, ProjectionSeed>();

            typeToAlias.put(type, aliases);

            for (ProjectionSeed seed : seeds) {
                aliases.put(seed.getAlias(), seed);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Alias map for projectionGroup'" + projectionGroup + "' and type '" +
                    type + "':" + aliases);
            }
        }

        aliasedProperties.put(projectionGroup, typeToAlias);

        return typeToAlias;
    }
}
