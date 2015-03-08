package org.cucina.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cucina.core.InstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.search.marshall.SearchCriterionMarshaller;
import org.cucina.search.marshall.SearchCriterionUnmarshaller;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.junit.Test;
import org.springframework.context.ApplicationContext;


/**
 * Check MapBasedSearchBeanMarshallManager functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MapBasedSearchBeanMarshallManagerTest {
    private static final String NAME = "name";
    private static final String ALIAS = "alias";
    private static final String TYPE = "type";
    private static final String ROOT_ALIAS = "rootAlias";
    private static final String ROOT_TYPE = "rootType";
    private static final String VALUE = "value";
    private MapBasedSearchBeanMarshallManager manager;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void initializes()
        throws Exception {
        ApplicationContext applicationContext = mock(ApplicationContext.class);

        when(applicationContext.containsBean(SingletonBeanFactory.INSTANCE_FACTORY_ID))
            .thenReturn(true);

        InstanceFactory instanceFactory = mock(InstanceFactory.class);

        when(applicationContext.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID))
            .thenReturn(instanceFactory);

        manager = new MapBasedSearchBeanMarshallManager();
        manager.setApplicationContext(applicationContext);
        manager.afterPropertiesSet();
    }

    /**
     * Marshalls using correct marshaller
     */
    @Test
    public void marshalls() {
        Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

        TextSearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        SearchCriterionMarshaller marshallerFalse = mock(SearchCriterionMarshaller.class);

        when(marshallerFalse.supports(TextSearchCriterion.class)).thenReturn(false);

        SearchCriterionMarshaller marshallerTrue = mock(SearchCriterionMarshaller.class);

        when(marshallerTrue.supports(TextSearchCriterion.class)).thenReturn(true);

        Set<SearchCriterionMarshaller> marshallers = new HashSet<SearchCriterionMarshaller>();

        marshallers.add(marshallerFalse);
        marshallers.add(marshallerTrue);
        manager = new MapBasedSearchBeanMarshallManager();
        manager.setMarshallers(marshallers);
        manager.marshall(ALIAS, null, criterion, marshalledCriterion);

        verify(marshallerTrue).marshall(ALIAS, null, criterion, marshalledCriterion);
    }

    /**
     * Unmarshalls using correct marshaller for alias.
     */
    @Test
    public void unmarshallsByAlias() {
        Map<String, Object> criteria = new HashMap<String, Object>();
        SearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        SearchCriterionUnmarshaller marshallerTrue = mock(SearchCriterionUnmarshaller.class);

        when(marshallerTrue.unmarshall(ALIAS, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria))
            .thenReturn(criterion);

        Map<String, SearchCriterionUnmarshaller> aliasKeyedUnmarshallers = new HashMap<String, SearchCriterionUnmarshaller>();

        aliasKeyedUnmarshallers.put(ALIAS, marshallerTrue);
        manager = new MapBasedSearchBeanMarshallManager();

        manager.setAliasKeyedUnmarshallers(aliasKeyedUnmarshallers);

        assertEquals("Returned criterion", criterion,
            manager.unmarshall(ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria));
    }

    /**
     * Unmarshalls and uses default SearchCriterionUnmarshaller if there is one
     */
    @Test
    public void unmarshallsByAliasDefault() {
        Map<String, Object> criteria = new HashMap<String, Object>();
        SearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        SearchCriterionUnmarshaller marshallerTrue = mock(SearchCriterionUnmarshaller.class);

        when(marshallerTrue.unmarshall(ALIAS, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria))
            .thenReturn(criterion);

        Map<String, SearchCriterionUnmarshaller> aliasKeyedUnmarshallers = new HashMap<String, SearchCriterionUnmarshaller>();

        aliasKeyedUnmarshallers.put("default", marshallerTrue);
        manager = new MapBasedSearchBeanMarshallManager();
        manager.setAliasKeyedUnmarshallers(aliasKeyedUnmarshallers);

        assertEquals("Returned criterion", criterion,
            manager.unmarshall(ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria));
    }

    /**
     * No Unmarshalling if none supplied.
     */
    @Test
    public void unmarshallsByAliasNone() {
        Map<String, Object> criteria = new HashMap<String, Object>();

        Map<String, SearchCriterionUnmarshaller> aliasKeyedUnmarshallers = new HashMap<String, SearchCriterionUnmarshaller>();

        manager = new MapBasedSearchBeanMarshallManager();
        manager.setAliasKeyedUnmarshallers(aliasKeyedUnmarshallers);

        assertNull("No criterion returned",
            manager.unmarshall(ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria));
    }

    /**
     * Unmarshalls using correct marshaller for type
     */
    @Test
    public void unmarshallsType() {
        Map<String, Object> criteria = new HashMap<String, Object>();
        SearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        SearchCriterionUnmarshaller marshallerTrue = mock(SearchCriterionUnmarshaller.class);

        when(marshallerTrue.unmarshall(NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria))
            .thenReturn(criterion);

        Map<String, SearchCriterionUnmarshaller> typeKeyedUnmarshallers = new HashMap<String, SearchCriterionUnmarshaller>();

        typeKeyedUnmarshallers.put(TYPE, marshallerTrue);
        manager = new MapBasedSearchBeanMarshallManager();
        manager.setTypeKeyedUnmarshallers(typeKeyedUnmarshallers);

        assertEquals("Returned criterion", criterion,
            manager.unmarshall(TYPE, NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria));
    }

    /**
     * Unmarshalls using default when marshaller not supplied.
     */
    @Test
    public void unmarshallsTypeDefault() {
        Map<String, Object> criteria = new HashMap<String, Object>();
        SearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

        SearchCriterionUnmarshaller marshallerTrue = mock(SearchCriterionUnmarshaller.class);

        when(marshallerTrue.unmarshall(NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria))
            .thenReturn(criterion);

        Map<String, SearchCriterionUnmarshaller> typeKeyedUnmarshallers = new HashMap<String, SearchCriterionUnmarshaller>();

        typeKeyedUnmarshallers.put("default", marshallerTrue);
        manager = new MapBasedSearchBeanMarshallManager();
        manager.setTypeKeyedUnmarshallers(typeKeyedUnmarshallers);
        assertEquals("Returned criterion", criterion,
            manager.unmarshall(TYPE, NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria));
    }

    /**
     * No unmarshalling if no marshaller supplied
     */
    @Test
    public void unmarshallsTypeNone() {
        Map<String, Object> criteria = new HashMap<String, Object>();

        Map<String, SearchCriterionUnmarshaller> typeKeyedUnmarshallers = new HashMap<String, SearchCriterionUnmarshaller>();

        manager = new MapBasedSearchBeanMarshallManager();
        manager.setTypeKeyedUnmarshallers(typeKeyedUnmarshallers);

        assertNull("Shouldn't return criterion no unmarshaller",
            manager.unmarshall(TYPE, NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria));
    }
}
