package org.cucina.search.query.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang3.ClassUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.search.query.Join;
import org.cucina.search.query.LeftJoin;
import org.cucina.search.query.OrderBy;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.SearchQuery;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.JoinCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.cucina.search.query.projection.MaxProjection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.cucina.search.testassist.Bar;
import org.cucina.search.testassist.Baz;
import org.cucina.search.testassist.Foo;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests HibernateSearchQueryGenerator generates HQL queries correctly
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JpaSearchQueryGeneratorTest {
    private static final String FOO_ALIAS = "foo";
    private static final String P1_1_NAME = "P_1_1";
    private static final String C1_1_1_NAME = "C_1_1_1";
    private JpaSearchQueryGenerator queryGenerator;
    private LinkedHashMap<String, String> defaultAliasByType;
    private LinkedHashMap<String, String> defaultTypeByAlias;

    /**
     * Tests from method when no existingJoins have been specified, i.e. creates the join
     * hql syntax
     */
    @Test
    public void fromEmpty() {
        StringBuffer hqlJoinString = new StringBuffer();

        Collection<String> existingJoins = new HashSet<String>();

        SimplePropertyProjection projection = new SimplePropertyProjection("bazs.bars.name", null,
                FOO_ALIAS);

        queryGenerator.addJoin(hqlJoinString, existingJoins, defaultTypeByAlias, projection);

        assertEquals("Incorrect hql fragment", "left join foo.bazs bazs left join bazs.bars bars ",
            hqlJoinString.toString());
        assertEquals("Incorrect size of existingjoins", 2, existingJoins.size());
        assertTrue("Should contain join", existingJoins.contains("foo.bazs"));
        assertTrue("Should contain join", existingJoins.contains("foo.bazs.bars"));
    }

    /**
     * Tests that when an existing join has been specified, that we don't rewrite it
     */
    @Test
    public void fromExistingJoin() {
        StringBuffer hqlJoinString = new StringBuffer();

        Collection<String> existingJoins = new HashSet<String>();

        existingJoins.add("foo.bazs");

        SimplePropertyProjection projection = new SimplePropertyProjection("bazs.bars.name", null,
                FOO_ALIAS);

        queryGenerator.addJoin(hqlJoinString, existingJoins, defaultTypeByAlias, projection);

        assertEquals("Incorrect hql fragment", "left join bazs.bars bars ", hqlJoinString.toString());
        assertEquals("Incorrect size of existingjoins", 2, existingJoins.size());
        assertTrue("Should contain join", existingJoins.contains("foo.bazs"));
        assertTrue("Should contain join", existingJoins.contains("foo.bazs.bars"));
    }

    /**
     * Test happy day generation with projections and restrictions
     */
    @Test
    public void generateQuery() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.name", "bazsName", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.id", "barsId", FOO_ALIAS));

        Collection<Bar> bars = new HashSet<Bar>();

        for (int i = 0; i < 112; i++) {
            bars.add(new Bar(Integer.valueOf(i).longValue()));
        }

        searchBean.addCriterion(new TextSearchCriterion("bazs.bars.name", null, FOO_ALIAS,
                C1_1_1_NAME));
        searchBean.addCriterion(new TextSearchCriterion("bazs.name", null, FOO_ALIAS, P1_1_NAME));
        searchBean.addCriterion(new InSearchCriterion("bazs", null, FOO_ALIAS, bars));
        searchBean.addCriterion(new TextSearchCriterion("name", null, FOO_ALIAS, P1_1_NAME));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo.name as name, bazs.name as bazsName, bars.id as barsId from Foo foo left join foo.bazs bazs left join bazs.bars bars where " +
            "bars.name like ?1 and bazs.name like ?2 and foo.bazs in (?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25," +
            "?26,?27,?28,?29,?30,?31,?32,?33,?34,?35,?36,?37,?38,?39,?40,?41,?42,?43,?44,?45,?46,?47,?48,?49,?50,?51,?52,?53,?54,?55,?56,?57,?58,?59,?60,?61," +
            "?62,?63,?64,?65,?66,?67,?68,?69,?70,?71,?72,?73,?74,?75,?76,?77,?78,?79,?80,?81,?82,?83,?84,?85,?86,?87,?88,?89,?90,?91,?92,?93,?94,?95,?96,?97," +
            "?98,?99,?100,?101,?102,?103,?104,?105,?106,?107,?108,?109,?110,?111,?112,?113,?114) and foo.name like ?115",
            query.getQuery());

        assertEquals("invalid number values", 115, query.getValues().size());
        assertEquals("Invalid value", "%" + C1_1_1_NAME + "%", query.getValues().get(0));
        assertEquals("Invalid value", "%" + P1_1_NAME + "%", query.getValues().get(1));
    }

    /**
     * Should throw exception when aliases clash, in this instance when table name alias clash follows projection
     */
    @Test(expected = IllegalArgumentException.class)
    public void generateQueryAliasClashProjectionAlias() {
        defaultAliasByType.put(Bar.TYPE, "bar");

        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "bar", FOO_ALIAS));

        queryGenerator.generateQuery(searchBean);
    }

    /**
     * Should throw exception when aliases clash
     */
    @Test(expected = IllegalArgumentException.class)
    public void generateQueryAliasClashTableAlias() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", FOO_ALIAS, FOO_ALIAS));

        queryGenerator.generateQuery(searchBean);
    }

    /**
     * Test happy day generation with restrictions and ignore projections
     */
    @Test
    public void generateQueryDontProcessProjections() {
        queryGenerator.setProcessProjections(false);

        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.name", "bazsName", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.id", "barsId", FOO_ALIAS));

        searchBean.addCriterion(new TextSearchCriterion("bazs.bars.name", null, FOO_ALIAS,
                C1_1_1_NAME));
        searchBean.addCriterion(new TextSearchCriterion("bazs.name", null, FOO_ALIAS, P1_1_NAME));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo from Foo foo left join foo.bazs bazs left join bazs.bars bars where bars.name like ?1 and bazs.name like ?2",
            query.getQuery());

        assertEquals("invalid number values", 2, query.getValues().size());
        assertEquals("Invalid value", "%" + C1_1_1_NAME + "%", query.getValues().get(0));
        assertEquals("Invalid value", "%" + P1_1_NAME + "%", query.getValues().get(1));
    }

    /**
     * Exception is thrown when type of search is not specified
     */
    @Test(expected = IllegalArgumentException.class)
    public void generateQueryEmptyTypeAlias() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(new LinkedHashMap<String, String>());

        searchBean.addProjection(new SimplePropertyProjection("name", "name", "foo"));
        searchBean.addProjection(new SimplePropertyProjection("bazs.name", "bazs.name", "foo"));

        searchBean.addCriterion(new TextSearchCriterion("bazs.bars.name", null, "foo", C1_1_1_NAME));

        queryGenerator.generateQuery(searchBean);
    }

    /**
     * Test happy day generation with projections and restrictions
     */
    @Test
    public void generateQueryMultipleRootTypes() {
        defaultAliasByType.put(Bar.TYPE, "bar");

        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.name", "bazsName", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.id", "barsId", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("name", "barName", "bar"));

        searchBean.addCriterion(new TextSearchCriterion("bazs.bars.name", null, FOO_ALIAS,
                C1_1_1_NAME));
        searchBean.addCriterion(new TextSearchCriterion("bazs.name", null, FOO_ALIAS, P1_1_NAME));
        searchBean.addCriterion(new JoinCriterion("bar", "id", "bar", "id", FOO_ALIAS));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo.name as name, bazs.name as bazsName, bars.id as barsId, bar.name as barName from Foo foo left join foo.bazs bazs left join bazs.bars bars , Bar bar where bars.name like ?1 and bazs.name like ?2 and bar.id = foo.id",
            query.getQuery());

        assertEquals("invalid number values", 2, query.getValues().size());
        assertEquals("Invalid value", "%" + C1_1_1_NAME + "%", query.getValues().get(0));
        assertEquals("Invalid value", "%" + P1_1_NAME + "%", query.getValues().get(1));
    }

    /**
     * Test happy day generation with projections and no restrictions
     */
    @Test
    public void generateQueryNoRestrictions() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.name", "bazsName", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.id", "barsId", FOO_ALIAS));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo.name as name, bazs.name as bazsName, bars.id as barsId from Foo foo left join foo.bazs bazs left join bazs.bars bars ",
            query.getQuery());

        assertEquals("invalid number values", 0, query.getValues().size());
    }

    /**
     * Exception is thrown when type of search is not specified
     */
    @Test(expected = IllegalArgumentException.class)
    public void generateQueryNoTypeAlias() {
        SearchBean searchBean = new SearchBean();

        searchBean.addProjection(new SimplePropertyProjection("name", "name", "foo"));
        searchBean.addProjection(new SimplePropertyProjection("bazs.name", "bazs.name", "foo"));

        searchBean.addCriterion(new TextSearchCriterion("bazs.bars.name", null, "foo", C1_1_1_NAME));

        queryGenerator.generateQuery(searchBean);
    }

    /**
     * Test happy day generation with multi orderby
     */
    @Test
    public void generateQueryWithMultiSorting() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));

        List<OrderBy> order = new ArrayList<OrderBy>();

        order.add(new OrderBy("name", Boolean.TRUE));
        order.add(new OrderBy("id", Boolean.FALSE));

        searchBean.setOrder(order);

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo.name as name from Foo foo  order by name asc,id desc", query.getQuery());

        assertEquals("invalid number values", 0, query.getValues().size());
    }

    /**
     * Test happy day generation with orderby
     */
    @Test
    public void generateQueryWithOrderByAscAndGroupBy() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.id", "barsId", FOO_ALIAS));
        searchBean.addProjection(new MaxProjection("bazs.name", "bazsName", FOO_ALIAS));

        searchBean.setOrder(Arrays.<OrderBy>asList(new OrderBy("name", true)));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo.name as name, bars.id as barsId, max(bazs.name) as bazsName from Foo foo left join foo.bazs bazs left join bazs.bars bars  group by foo.name ,bars.id  order by name asc",
            query.getQuery());

        assertEquals("invalid number values", 0, query.getValues().size());
    }

    /**
     * Test happy day generation with orderby
     */
    @Test
    public void generateQueryWithOrderByDesc() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));

        searchBean.setOrder(Arrays.<OrderBy>asList(new OrderBy("name", false)));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query", "select foo.name as name from Foo foo  order by name desc",
            query.getQuery());

        assertEquals("invalid number values", 0, query.getValues().size());
    }

    /**
     * Test that unique joins can be made and than restriction can be "linked" via alias to the projections unique joins
     * in order to restrict search
     */
    @Test
    public void generateQueryWithUniqueJoins() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        TextSearchCriterion criterion = new TextSearchCriterion("bazs.bars.name", null, FOO_ALIAS,
                "en");

        searchBean.addCriterion(criterion);

        Collection<SearchCriterion> projCriterion = new HashSet<SearchCriterion>();

        projCriterion.add(criterion);

        SimplePropertyProjection projection1 = new SimplePropertyProjection("bazs.bars.name",
                "barName1", FOO_ALIAS) {
                @Override
                public Map<String, List<Join>> getJoins() {
                    List<Join> joins = new ArrayList<Join>();

                    joins.add(new LeftJoin("bazs", false));
                    joins.add(new LeftJoin("bars", true));

                    return Collections.singletonMap(FOO_ALIAS, joins);
                }
            };

        projection1.setSearchCriteria(projCriterion);

        SimplePropertyProjection projection2 = new SimplePropertyProjection("bazs.bars.name",
                "barName2", FOO_ALIAS) {
                @Override
                public Map<String, List<Join>> getJoins() {
                    List<Join> joins = new ArrayList<Join>();

                    joins.add(new LeftJoin("bazs", false));
                    joins.add(new LeftJoin("bars", true));

                    return Collections.singletonMap(FOO_ALIAS, joins);
                }
            };

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));
        searchBean.addProjection(projection1);
        searchBean.addProjection(projection2);
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.name", "barName3",
                FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.id", "barsId", FOO_ALIAS));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo.name as name, bars0.name as barName1, bars1.name as barName2, bars.name as barName3, bars.id as barsId from Foo foo left join foo.bazs bazs left join bazs.bars bars0 left join bazs.bars bars1 left join bazs.bars bars where bars0.name like ?1",
            query.getQuery());

        assertEquals("invalid number values", 1, query.getValues().size());
        assertEquals("Invalid value", "%en%", query.getValues().get(0));
    }

    /**
     * Invalid Join should be caught and thrown back
     */
    @Test(expected = IllegalArgumentException.class)
    public void invalidJoin() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("freds.name", "name", FOO_ALIAS));

        queryGenerator.generateQuery(searchBean);
    }

    /**
     * Test with no projections specified
     */
    @Test
    public void noProjections() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addCriterion(new TextSearchCriterion("bazs.bars.name", null, FOO_ALIAS,
                C1_1_1_NAME));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo from Foo foo left join foo.bazs bazs left join bazs.bars bars where bars.name like ?1",
            query.getQuery());
    }

    /**
     * Tests with no restriction specified
     */
    @Test
    public void noRestrictions() {
        SearchBean searchBean = new SearchBean();

        searchBean.setAliasByType(defaultAliasByType);

        searchBean.addProjection(new SimplePropertyProjection("name", "name", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.name", "bazsName", FOO_ALIAS));
        searchBean.addProjection(new SimplePropertyProjection("bazs.bars.id", "barsId", FOO_ALIAS));

        SearchQuery query = queryGenerator.generateQuery(searchBean);

        assertEquals("Invalid query",
            "select foo.name as name, bazs.name as bazsName, bars.id as barsId from Foo foo left join foo.bazs bazs left join bazs.bars bars ",
            query.getQuery());
    }

    /**
     * Sets up for test
     */
    @SuppressWarnings("unchecked")
    @Before
    public void onSetUp()
        throws Exception {
        queryGenerator = new JpaSearchQueryGenerator();

        EntityManager entityManager = mock(EntityManager.class);
        Metamodel mm = mock(Metamodel.class);
        ManagedType<Baz> bazm = mock(ManagedType.class);

        when(mm.managedType(Baz.class)).thenReturn(bazm);

        ManagedType<Bar> barm = mock(ManagedType.class);

        when(mm.managedType(Bar.class)).thenReturn(barm);

        ManagedType<Foo> foom = mock(ManagedType.class);

        when(mm.managedType(Foo.class)).thenReturn(foom);
        when(entityManager.getMetamodel()).thenReturn(mm);

        queryGenerator.setEntityManager(entityManager);

        InstanceFactory instanceFactory = new PackageBasedInstanceFactory(ClassUtils.getPackageName(
                    Foo.class) + ".");

        queryGenerator.setInstanceFactory(instanceFactory);

        defaultAliasByType = new LinkedHashMap<String, String>();
        defaultAliasByType.put(Foo.TYPE, FOO_ALIAS);

        defaultTypeByAlias = new LinkedHashMap<String, String>();
        defaultTypeByAlias.put(FOO_ALIAS, Foo.TYPE);
    }
}
