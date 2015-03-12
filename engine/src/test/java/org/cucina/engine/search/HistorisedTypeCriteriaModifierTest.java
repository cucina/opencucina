package org.cucina.engine.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.InstanceofPredicate;

import org.cucina.core.model.PersistableEntity;

import org.cucina.engine.testassist.Bar;
import org.cucina.engine.testassist.Foo;

import org.cucina.search.marshall.SearchCriterionMarshaller;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.JoinCriterion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class HistorisedTypeCriteriaModifierTest {
    private static final String NAME_PROPERTY = "name";
    private static final String ID_PROPERTY = "id";
    private static final String NAME_ALIAS = NAME_PROPERTY;
    private static final String ALIAS = "foo";
    private HistorisedTypeCriteriaModifier modifier;
    private SearchBean searchbean;

    /**
     * Checks that historic criteria are added to search
     */
    @Test
    public void modifySearchBean() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(PersistableEntity.APPLICATION_TYPE, Foo.TYPE);

        SearchBean bean = modifier.doModify(searchbean, params);
        Collection<SearchCriterion> searchCriteria = bean.getCriteria();

        assertEquals(3, searchCriteria.size());

        // Expect three criterion added implicitly
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new InstanceofPredicate(InSearchCriterion.class),
                    new AndPredicate(new BeanPropertyValueEqualsPredicate("name",
                            "token.domainObjectType"),
                        new AndPredicate(new BeanPropertyValueEqualsPredicate("value",
                                Collections.singleton(Foo.TYPE)),
                            new BeanPropertyValueEqualsPredicate("rootAlias",
                                SearchCriterionMarshaller.HISTORY_ALIAS))))));
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new InstanceofPredicate(JoinCriterion.class),
                    new AndPredicate(new BeanPropertyValueEqualsPredicate("lhs", "id"),
                        new BeanPropertyValueEqualsPredicate("rhs", "token.domainObjectId")))));
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new BeanPropertyValueEqualsPredicate("rootAlias",
                        SearchCriterionMarshaller.HISTORY_ALIAS),
                    new AndPredicate(new BeanPropertyValueEqualsPredicate(NAME_ALIAS, ID_PROPERTY),
                        new BeanPropertyValueEqualsPredicate("subSelect",
                            "select max(hr.id) from HistoryRecord hr where hr.token.domainObjectId = foo.id and hr.token.domainObjectType = 'Foo'")))));
    }

    /**
     * Should not modify
     */
    @Test
    public void nonHistorisedType() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(PersistableEntity.APPLICATION_TYPE, Bar.TYPE);

        SearchBean bean = modifier.doModify(searchbean, params);
        Collection<SearchCriterion> searchCriteria = bean.getCriteria();

        assertEquals(0, searchCriteria.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup()
        throws Exception {
        modifier = new HistorisedTypeCriteriaModifier();
        modifier.setHistorisedTypes(Collections.singleton(Foo.TYPE));

        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put(Foo.TYPE, ALIAS);
        searchbean = new SearchBean();
        searchbean.setAliasByType(aliasByType);
    }
}
