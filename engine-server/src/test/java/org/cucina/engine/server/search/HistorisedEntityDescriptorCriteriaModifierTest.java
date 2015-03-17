
package org.cucina.engine.server.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.utils.NameUtils;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.search.marshall.SearchCriterionMarshaller;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.JoinCriterion;
import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class HistorisedEntityDescriptorCriteriaModifierTest {
    private static final String NAME_PROPERTY = "name";
    private static final String ID_PROPERTY = "id";
    private static final String NAME_ALIAS = NAME_PROPERTY;
    private static final String ALIAS = "foo";
    private HistorisedEntityDescriptorCriteriaModifier modifier;
    private SearchBean searchbean;

    /**
     * Checks that historic criteria are added to search
     */
    @Test
    public void modifySearchBean() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(PersistableEntity.APPLICATION_TYPE, EntityDescriptor.class.getSimpleName());

        SearchBean bean = modifier.doModify(searchbean, params);
        Collection<SearchCriterion> searchCriteria = bean.getCriteria();

        assertEquals(3, searchCriteria.size());

        // Expect three criterion added implicitly, subjectAlias
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new InstanceofPredicate(JoinCriterion.class),
                    new AndPredicate(new BeanPropertyValueEqualsPredicate("lhs",
                            EntityDescriptor.APPLICATION_TYPE_PROP),
                        new BeanPropertyValueEqualsPredicate("rhs", "token.domainObjectType")))));
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new InstanceofPredicate(JoinCriterion.class),
                    new AndPredicate(new BeanPropertyValueEqualsPredicate("lhs", "id"),
                        new BeanPropertyValueEqualsPredicate("rhs", "token.domainObjectId")))));
        assertNotNull(CollectionUtils.find(searchCriteria,
                new AndPredicate(new BeanPropertyValueEqualsPredicate("rootAlias",
                        SearchCriterionMarshaller.HISTORY_ALIAS),
                    new AndPredicate(new BeanPropertyValueEqualsPredicate(NAME_ALIAS, ID_PROPERTY),
                        new BeanPropertyValueEqualsPredicate("subSelect",
                            "select max(hr.id) from HistoryRecord hr where hr.token.domainObjectId = foo.id and hr.token.domainObjectType = " +
                            NameUtils.concat(ALIAS, EntityDescriptor.APPLICATION_TYPE_PROP))))));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup()
        throws Exception {
        modifier = new HistorisedEntityDescriptorCriteriaModifier();

        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put(EntityDescriptor.class.getSimpleName(), ALIAS);
        searchbean = new SearchBean();
        searchbean.setAliasByType(aliasByType);
    }
}
