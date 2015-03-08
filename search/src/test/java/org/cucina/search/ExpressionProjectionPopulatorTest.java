package org.cucina.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.search.query.ProjectionFactory;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.projection.MaxProjection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.cucina.search.testassist.Bar;
import org.cucina.search.testassist.Baz;
import org.cucina.search.testassist.Foo;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests ProjectionsCriteriaModifier functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ExpressionProjectionPopulatorTest {
    private static final String PROJECTIONS_LIST = "Bar.foo.id,max(Bar.foo.value),Foo.name,Foo.value,Bar.madeup";
    private static final String BAR_ALIAS = "Bar";
    private static final String HISTORY_ALIAS = "history";
    private ExpressionProjectionPopulator populator;
    private SearchBean bean;

    /**
     * Barfs if there is no alias for type in SearchBean.aliasByType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void barfsNoAlias() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(ProjectionPopulator.PROJECTIONS, "MadeUp.assignedTo");

        populator.populate(Bar.TYPE, bean, params);
    }

    /**
     * Barfs if no type is provided for param, i.e, just 'param' rather than 'Type.param'.
     */
    @Test(expected = IllegalArgumentException.class)
    public void barfsNoRootType() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(ProjectionPopulator.PROJECTIONS, "assignedTo");

        populator.populate(Bar.TYPE, bean, params);
    }

    /**
     * Projections are created as expected.
     */
    @Test
    public void createsProjections() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(ProjectionPopulator.PROJECTIONS, PROJECTIONS_LIST);

        ProjectionFactory factory = mock(ProjectionFactory.class);

        when(factory.create(Bar.TYPE, "foo.id", null, BAR_ALIAS, null))
            .thenReturn(new SimplePropertyProjection("foo.id", null, Bar.TYPE));
        when(factory.create(Bar.TYPE, "foo.value", null, BAR_ALIAS, "max"))
            .thenReturn(new MaxProjection("foo.value", null, Bar.TYPE));
        when(factory.create(Foo.class.getSimpleName(), "name", null, HISTORY_ALIAS, null))
            .thenReturn(new SimplePropertyProjection("baz.foo", null, Foo.class.getSimpleName()));
        when(factory.create(Foo.class.getSimpleName(), "value", null, HISTORY_ALIAS, null))
            .thenReturn(new SimplePropertyProjection("value", null, Baz.class.getSimpleName()));

        populator = new ExpressionProjectionPopulator(new PackageBasedInstanceFactory(
                    ClassUtils.getPackageName(Bar.class)), factory, mock(SearchBeanFactory.class));
        populator.populate(Bar.TYPE, bean, params);

        assertEquals("Incorrect number of projections", 4, bean.getProjections().size());

        assertNotNull("Should have created foo.id projection", bean.getProjection("foo.id"));
        assertNotNull("Should have created foo.value projection", bean.getProjection("foo.value"));
        assertNotNull("Should have created baz.foo projection", bean.getProjection("baz.foo"));
        assertNotNull("Should have created value projection", bean.getProjection("value"));
    }

    /**
     * No projectionslist provided so delegates to SearchBeanFactory. Test fixed 11/12/12 by AJ
     */
    @Test
    public void noProjectionsList() {
        Map<String, Object> params = new HashMap<String, Object>();

        SearchBeanFactory factory = mock(SearchBeanFactory.class);

        populator = new ExpressionProjectionPopulator(new PackageBasedInstanceFactory(
                    ClassUtils.getPackageName(Bar.class)), mock(ProjectionFactory.class), factory);
        populator.populate(Bar.TYPE, bean, params);
        verify(factory).addProjections(Bar.TYPE, bean);
    }

    /**
     * Sets up for test.
     */
    @Before
    public void setup() {
        populator = new ExpressionProjectionPopulator(new PackageBasedInstanceFactory(
                    ClassUtils.getPackageName(Bar.class)), mock(ProjectionFactory.class),
                mock(SearchBeanFactory.class));

        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put(Bar.TYPE, BAR_ALIAS);
        aliasByType.put(Foo.class.getSimpleName(), HISTORY_ALIAS);

        bean = new SearchBean();
        bean.setAliasByType(aliasByType);
    }
}
