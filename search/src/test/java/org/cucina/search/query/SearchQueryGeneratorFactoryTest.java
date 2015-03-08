package org.cucina.search.query;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;

import org.apache.commons.lang3.ClassUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.search.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SearchQueryGeneratorFactoryTest {
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private SearchQueryGenerator postProcessSearchQueryGenerator;
    @Mock
    private SearchQueryGenerator searchQueryGenerator;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void test() {
        SearchBean searchBean = new SearchBean();
        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put("Type", "alias");
        searchBean.setAliasByType(aliasByType);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEmptyMap() {
        InstanceFactory instanceFactory = new PackageBasedInstanceFactory(ClassUtils.getPackageName(
                    Foo.class));

        SearchQueryGeneratorFactory factory = new SearchQueryGeneratorFactory(instanceFactory,
                searchQueryGenerator, postProcessSearchQueryGenerator);
        SearchBean sb = new SearchBean();

        sb.setAliasByType(new LinkedHashMap<String, String>());
        assertEquals(searchQueryGenerator, factory.getSearchQueryGenerator(sb));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNullMap() {
        SearchQueryGeneratorFactory factory = new SearchQueryGeneratorFactory(instanceFactory,
                searchQueryGenerator, postProcessSearchQueryGenerator);

        assertEquals(searchQueryGenerator, factory.getSearchQueryGenerator(new SearchBean()));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPostProcessInstanceFactoryReturnsNull() {
        SearchBean searchBean = new SearchBean();
        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put("Type", "alias");
        searchBean.setAliasByType(aliasByType);

        when(instanceFactory.getClassType("Type")).thenReturn(null);

        SearchQueryGeneratorFactory factory = new SearchQueryGeneratorFactory(instanceFactory,
                searchQueryGenerator, postProcessSearchQueryGenerator);

        assertEquals("should be normal search as instanceFactory returns null for clazz",
            searchQueryGenerator, factory.getSearchQueryGenerator(searchBean));
        verify(instanceFactory).getClassType("Type");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPostProcessNo() {
        SearchBean searchBean = new SearchBean();
        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put("Type", "alias");
        searchBean.setAliasByType(aliasByType);

        when(instanceFactory.getClassType("Type")).thenReturn(null);

        SearchQueryGeneratorFactory factory = new SearchQueryGeneratorFactory(instanceFactory,
                searchQueryGenerator, postProcessSearchQueryGenerator);

        assertEquals("should be normal search as class not annotated", searchQueryGenerator,
            factory.getSearchQueryGenerator(searchBean));
        verify(instanceFactory).getClassType("Type");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPostProcessReturnsDiffSQG() {
        SearchBean searchBean = new SearchBean();
        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put("Type", "alias");
        searchBean.setAliasByType(aliasByType);

        when(instanceFactory.<Annotated>getClassType("Type")).thenReturn(Annotated.class);

        SearchQueryGeneratorFactory factory = new SearchQueryGeneratorFactory(instanceFactory,
                searchQueryGenerator, postProcessSearchQueryGenerator);

        assertEquals("should be post process search as class annotated",
            postProcessSearchQueryGenerator, factory.getSearchQueryGenerator(searchBean));
        verify(instanceFactory).getClassType("Type");
    }

    @PostProcessProjections
    public class Annotated {
    }

    public class NotAnnotated {
    }
}
