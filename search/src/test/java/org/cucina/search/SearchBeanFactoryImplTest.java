package org.cucina.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.groups.Default;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.lang3.StringUtils;

import org.cucina.core.InstanceFactory;
import org.cucina.core.utils.NameUtils;

import org.cucina.search.marshall.SearchCriterionMarshallManager;
import org.cucina.search.marshall.SearchCriterionMarshaller;
import org.cucina.search.model.ProjectionSeed;
import org.cucina.search.query.OrderBy;
import org.cucina.search.query.ProjectionFactory;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.cucina.search.query.projection.CountProjection;
import org.cucina.search.query.projection.Projection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.cucina.search.query.projection.TranslatedPropertyProjection;
import org.cucina.search.testassist.Foo;

import org.cucina.security.model.Preference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchBeanFactoryImplTest {
    private static final String NAME_PROPERTY = "name";
    private static final String VALUE_PROPERTY = "value";
    private static final String ID_PROPERTY = "id";
    private static final String VALUE_ALIAS = "theValue";
    private static final String NAME_ALIAS = NAME_PROPERTY;
    private static final String DATE_PROPERTY = "modifyDate";
    private static final String DATE_ALIAS = DATE_PROPERTY;
    private static final String ID_ALIAS = "id";
    private static final String VALUE = "bla bla";
    private static final String ID = "25";
    private static final String NAME = "frank";
    private static final String FOREIGN_PROPERTY = "bar";
    private static final String FOREIGN_ALIAS = "foreign";
    private static final String RESTRICT_ALIAS = "restrictToJobs";
    private Map<String, String> aliasPropertyMap;
    private SearchBeanFactoryImpl factory;

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        aliasPropertyMap = new HashMap<String, String>();
        aliasPropertyMap.put(ID_ALIAS, ID_PROPERTY);
        aliasPropertyMap.put(VALUE_ALIAS, VALUE_PROPERTY);
        aliasPropertyMap.put(NAME_ALIAS, NAME_PROPERTY);

        factory = new SearchBeanFactoryImpl();

        factory.setDefaultProjectionGroup(Default.class);

        Collection<Class<?>> classList = new ArrayList<Class<?>>();

        classList.add(Foo.class);
        factory.setClassList(classList);
        factory.setCriteriaOverrides(Collections.EMPTY_MAP);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAddCountProjections() {
        SearchBean search = new SearchBean();

        LinkedHashMap<String, ProjectionSeed> aliases = new LinkedHashMap<String, ProjectionSeed>();

        ProjectionSeed nameSeed = new ProjectionSeed("", "surname", null);

        nameSeed.setEntityAlias("Name");

        aliases.put("name", nameSeed);

        Map<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>> aliasByProjGroup = new HashMap<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>>();
        Map<String, LinkedHashMap<String, ProjectionSeed>> aliasedProperties = new HashMap<String, LinkedHashMap<String, ProjectionSeed>>();

        aliasedProperties.put("Foo", aliases);

        aliasByProjGroup.put(Default.class, aliasedProperties);

        factory.aliasedProperties = aliasByProjGroup;
        factory.addCountProjection("Foo", search, "thecount");

        Collection<Projection> pros = search.getProjections();

        assertNotNull("Projections is null", pros);
        assertEquals(1, pros.size());

        CountProjection projection = (CountProjection) pros.iterator().next();

        assertEquals("id", projection.getName());
        assertEquals("thecount", projection.getAlias());
        assertEquals("Name", projection.getRootAlias());
    }

    /**
     * Test that only the requested projections are added and are in the order
     * requested.
     */
    @Test
    public void testAddNamedProjections() {
        SearchBean search = new SearchBean();

        LinkedHashMap<String, ProjectionSeed> aliases = new LinkedHashMap<String, ProjectionSeed>();

        ProjectionSeed nameSeed = new ProjectionSeed("Foo", "Name", "", "surname", null);

        ProjectionSeed addressSeed = new ProjectionSeed("Foo", "Address", "", "firstline", null);

        ProjectionSeed firstNameSeed = new ProjectionSeed("Foo", "firstName", "", "firstName", null);

        aliases.put("name", nameSeed);
        aliases.put("address", addressSeed);
        aliases.put("firstName", firstNameSeed);

        Map<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>> aliasByProjGroup = new HashMap<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>>();
        Map<String, LinkedHashMap<String, ProjectionSeed>> aliasedProperties = new HashMap<String, LinkedHashMap<String, ProjectionSeed>>();

        aliasedProperties.put("Foo", aliases);

        aliasByProjGroup.put(Default.class, aliasedProperties);

        factory.aliasedProperties = aliasByProjGroup;

        Projection firstNameProjection = new SimplePropertyProjection("firstName", "firstName",
                "firstName");
        Projection surnameProjection = new SimplePropertyProjection("surname", "name", "Name");
        Projection translatedProjection = new TranslatedPropertyProjection("firstline", "address",
                "Address");

        ProjectionFactory profactory = mock(ProjectionFactory.class);

        when(profactory.create("Foo", "firstName", "firstName", "firstName", null))
            .thenReturn(firstNameProjection);
        when(profactory.create("Foo", "surname", "name", "Name", null)).thenReturn(surnameProjection);
        when(profactory.create("Foo", "firstline", "address", "Address", null))
            .thenReturn(translatedProjection);
        factory.setProjectionFactory(profactory);

        factory.addProjections("Foo", search, Arrays.asList("address", "name"));

        Collection<Projection> pros = search.getProjections();

        assertNotNull("Projections is null", pros);
        assertEquals(2, pros.size());

        Iterator<Projection> pIter = pros.iterator();

        assertEquals(pIter.next().getAlias(), "address");
        assertEquals(pIter.next().getAlias(), "name");

        Iterator<Projection> proIt = pros.iterator();

        TranslatedPropertyProjection tpp = (TranslatedPropertyProjection) proIt.next();

        assertEquals(translatedProjection, tpp);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAddProjections() {
        SearchBean search = new SearchBean();

        LinkedHashMap<String, ProjectionSeed> aliases = new LinkedHashMap<String, ProjectionSeed>();

        ProjectionSeed nameSeed = new ProjectionSeed("Foo", "Name", "", "surname", null);

        ProjectionSeed addressSeed = new ProjectionSeed("Foo", "Address", "", "firstline", null);

        addressSeed.setEntityAlias("Address");

        aliases.put("name", nameSeed);
        aliases.put("address", addressSeed);

        Map<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>> aliasByProjGroup = new HashMap<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>>();
        Map<String, LinkedHashMap<String, ProjectionSeed>> aliasedProperties = new HashMap<String, LinkedHashMap<String, ProjectionSeed>>();

        aliasedProperties.put("Foo", aliases);

        aliasByProjGroup.put(Default.class, aliasedProperties);

        factory.aliasedProperties = aliasByProjGroup;

        Projection surnameProjection = new SimplePropertyProjection("surname", "name", "Name");
        Projection translatedProjection = new TranslatedPropertyProjection("firstline", "address",
                "Address");

        ProjectionFactory profactory = mock(ProjectionFactory.class);

        when(profactory.create("Foo", "surname", "name", "Name", null)).thenReturn(surnameProjection);
        when(profactory.create("Foo", "firstline", "address", "Address", null))
            .thenReturn(translatedProjection);
        factory.setProjectionFactory(profactory);

        factory.addProjections("Foo", search);

        Collection<Projection> pros = search.getProjections();

        assertNotNull("Projections is null", pros);
        assertEquals(2, pros.size());

        Iterator<Projection> proIt = pros.iterator();

        SimplePropertyProjection projection = (SimplePropertyProjection) proIt.next();

        assertEquals(surnameProjection, projection);

        TranslatedPropertyProjection tpp = (TranslatedPropertyProjection) proIt.next();

        assertEquals(translatedProjection, tpp);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Test
    public void testAfterPropertiesSet()
        throws Exception {
        factory.init();

        LinkedHashMap<String, ProjectionSeed> props = factory.aliasedProperties.get(factory.defaultProjectionGroup)
                                                                               .get(Foo.TYPE);

        assertEquals("wrong number of aliases", 3, props.size());
        assertTrue("No id", props.containsKey("id"));
        assertTrue("No name", props.containsKey("name"));
        assertTrue("No value", props.containsKey("theValue"));

        for (Object alias : props.keySet()) {
            ProjectionSeed seed = props.get(alias);

            if (alias.equals(ID_PROPERTY) || alias.equals(NAME_PROPERTY) ||
                    alias.equals(VALUE_PROPERTY)) {
                assertNotNull(seed);
                assertEquals("Wrong root for the alias", "foo", seed.getEntityAlias());
                assertEquals("Wrong property for the alias", aliasPropertyMap.get(alias),
                    seed.getProperty());
            }
        }
    }

    /**
     * Test that we get a SearchBean generated for the given type with values
     * for each provided restriction. Ignoring those that the helper doesn't
     * know about ie not in the properties list.
     */
    @Test
    public void testBuildSearchBean() {
        Map<String, String> additionalAliases = new HashMap<String, String>();

        additionalAliases.put(Preference.class.getSimpleName(), "preference");

        Map<String, Map<String, String>> additionalAliasesByType = new HashMap<String, Map<String, String>>();

        additionalAliasesByType.put(Foo.TYPE, additionalAliases);

        factory.additionalAliasesByType = additionalAliasesByType;

        HashMap<String, Object> criteria = new HashMap<String, Object>();

        criteria.put(NAME_ALIAS, NAME);
        criteria.put(ID_ALIAS, ID);
        criteria.put(VALUE_ALIAS, VALUE);
        criteria.put(FOREIGN_ALIAS, ID);
        criteria.put(RESTRICT_ALIAS, "on");

        ProjectionSeed idSeed = new ProjectionSeed("", ID_PROPERTY, null);

        idSeed.setEntityAlias("foo");

        ProjectionSeed nameSeed = new ProjectionSeed("", NAME_ALIAS, null);

        nameSeed.setEntityAlias("foo");

        ProjectionSeed valueSeed = new ProjectionSeed("", VALUE_PROPERTY, null);

        valueSeed.setEntityAlias("foo");

        Map<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>> aliasByProjGroup = new HashMap<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>>();
        Map<String, LinkedHashMap<String, ProjectionSeed>> aliasedProperties = new HashMap<String, LinkedHashMap<String, ProjectionSeed>>();

        aliasedProperties.put(Foo.TYPE, new LinkedHashMap<String, ProjectionSeed>());
        aliasedProperties.get(Foo.TYPE).put(ID_ALIAS, idSeed);
        aliasedProperties.get(Foo.TYPE).put(NAME_ALIAS, nameSeed);
        aliasedProperties.get(Foo.TYPE).put(VALUE_ALIAS, valueSeed);

        aliasByProjGroup.put(Default.class, aliasedProperties);

        factory.aliasedProperties = aliasByProjGroup;

        InstanceFactory instanceFactory = mock(InstanceFactory.class);

        when(instanceFactory.getPropertyType(Foo.TYPE, ID_PROPERTY)).thenReturn("Long");
        when(instanceFactory.getPropertyType(Foo.TYPE, NAME_ALIAS)).thenReturn("String");
        when(instanceFactory.getPropertyType(Foo.TYPE, VALUE_PROPERTY)).thenReturn("String");

        factory.setInstanceFactory(instanceFactory);

        SearchCriterionMarshallManager marshaller = mock(SearchCriterionMarshallManager.class);

        when(marshaller.unmarshall("Long", ID_PROPERTY, ID_ALIAS, Foo.TYPE, "foo", criteria))
            .thenReturn(new NumberSearchCriterion(ID_PROPERTY, null, "foo", Long.valueOf(ID), null));
        when(marshaller.unmarshall("String", NAME_ALIAS, NAME_ALIAS, Foo.TYPE, "foo", criteria))
            .thenReturn(new TextSearchCriterion(NAME_ALIAS, null, "foo", NAME));
        when(marshaller.unmarshall("String", VALUE_PROPERTY, VALUE_ALIAS, Foo.TYPE, "foo", criteria))
            .thenReturn(new TextSearchCriterion(VALUE_PROPERTY, null, "foo", VALUE));
        when(marshaller.unmarshall(NAME_ALIAS, Foo.TYPE, "foo", criteria)).thenReturn(null);
        when(marshaller.unmarshall(ID_ALIAS, Foo.TYPE, "foo", criteria)).thenReturn(null);
        when(marshaller.unmarshall(VALUE_ALIAS, Foo.TYPE, "foo", criteria)).thenReturn(null);
        when(marshaller.unmarshall(FOREIGN_ALIAS, Foo.TYPE, "foo", criteria)).thenReturn(null);
        when(marshaller.unmarshall(RESTRICT_ALIAS, Foo.TYPE, "foo", criteria)).thenReturn(null);
        factory.setSearchCriterionMarshallManager(marshaller);

        SearchBean bean = factory.buildSearchBean(Foo.TYPE, criteria, SearchType.TASKLIST);
        Collection<SearchCriterion> searchCriteria = bean.getCriteria();

        assertEquals(3, searchCriteria.size());

        // Expect three criterion from the passed in parameters
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new BeanPropertyValueEqualsPredicate("rootAlias", "foo"),
                    new AndPredicate(new BeanPropertyValueEqualsPredicate(NAME_ALIAS, ID_PROPERTY),
                        new BeanPropertyValueEqualsPredicate(
                            SearchCriterionMarshaller.FROM_PROPERTY, Long.valueOf(ID))))));
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new BeanPropertyValueEqualsPredicate(NAME_ALIAS, NAME_ALIAS),
                    new BeanPropertyValueEqualsPredicate(VALUE_PROPERTY, NAME))));
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new BeanPropertyValueEqualsPredicate(NAME_ALIAS, VALUE_PROPERTY),
                    new BeanPropertyValueEqualsPredicate(VALUE_PROPERTY, VALUE))));

        assertEquals("Should have added preference alias", "preference",
            bean.getAliasByType().get(Preference.class.getSimpleName()));
    }

    /**
     * Test that the utility method is working as expected.
     */
    @Test
    public void testGetPropertyFromAlias() {
        HashMap<String, LinkedHashMap<String, ProjectionSeed>> aliasedProperties = new HashMap<String, LinkedHashMap<String, ProjectionSeed>>();

        ProjectionSeed idSeed = new ProjectionSeed("", ID_PROPERTY, null);

        idSeed.setEntityAlias("foo");

        ProjectionSeed nameSeed = new ProjectionSeed("", NAME_ALIAS, null);

        nameSeed.setEntityAlias("foo");

        ProjectionSeed valueSeed = new ProjectionSeed("", VALUE_PROPERTY, null);

        valueSeed.setEntityAlias("foo");

        ProjectionSeed dateSeed = new ProjectionSeed("", DATE_PROPERTY, null);

        dateSeed.setEntityAlias("foo");

        ProjectionSeed foreignSeed = new ProjectionSeed("", FOREIGN_PROPERTY, null);

        foreignSeed.setEntityAlias("foo");

        ProjectionSeed restrictSeed = new ProjectionSeed("", RESTRICT_ALIAS, null);

        restrictSeed.setEntityAlias("foo");

        aliasedProperties.put(Foo.TYPE, new LinkedHashMap<String, ProjectionSeed>());
        aliasedProperties.get(Foo.TYPE).put(ID_ALIAS, idSeed);
        aliasedProperties.get(Foo.TYPE).put(NAME_ALIAS, nameSeed);
        aliasedProperties.get(Foo.TYPE).put(VALUE_ALIAS, valueSeed);
        aliasedProperties.get(Foo.TYPE).put(DATE_ALIAS, dateSeed);
        aliasedProperties.get(Foo.TYPE).put(FOREIGN_ALIAS, foreignSeed);
        aliasedProperties.get(Foo.TYPE).put(RESTRICT_ALIAS, restrictSeed);

        Map<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>> aliasByProjGroup = new HashMap<Class<?>, Map<String, LinkedHashMap<String, ProjectionSeed>>>();

        aliasByProjGroup.put(Default.class, aliasedProperties);

        factory.aliasedProperties = aliasByProjGroup;

        assertEquals(NameUtils.concat("foo", ID_PROPERTY),
            factory.getPropertyFromAlias(Foo.TYPE, ID_ALIAS));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testMarshall()
        throws Exception {
        factory.init();

        SearchBean bean = new SearchBean();
        TextSearchCriterion tc = new TextSearchCriterion("name", null, "foo", "value");

        bean.addCriterion(tc);

        SearchCriterionMarshallManager marshaller = mock(SearchCriterionMarshallManager.class);

        factory.setSearchCriterionMarshallManager(marshaller);
        assertNotNull("Should return marshalledCriteria", factory.marshallCriteria(Foo.TYPE, bean));

        verify(marshaller).marshall("name", "name", tc, new HashMap<String, Object>());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testMarshallWithAlias()
        throws Exception {
        factory.init();

        SearchBean bean = new SearchBean();
        TextSearchCriterion tc = new TextSearchCriterion("name", "aTextSearchCriterion", "foo",
                "value");

        bean.addCriterion(tc);

        SearchCriterionMarshallManager marshaller = mock(SearchCriterionMarshallManager.class);

        factory.setSearchCriterionMarshallManager(marshaller);
        assertNotNull("Should return marshalledCriteria", factory.marshallCriteria(Foo.TYPE, bean));

        verify(marshaller)
            .marshall("aTextSearchCriterion", "name", tc, new HashMap<String, Object>());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testOrderBy() {
        SearchBean search = new SearchBean();

        factory.addOrder("orderProperty", true, search);

        List<OrderBy> order = search.getOrder();

        assertNotNull("should not be null", order);
        assertEquals("should contain an OrderBy", 1, order.size());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testUnmarshallCriterionInBuildSearchbean()
        throws Exception {
        //testing default behaviour as fix to bug ACR245020
        factory.init();
        factory.buildSearchBean("test", Collections.<String, Object>emptyMap());
    }
}
