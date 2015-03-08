package org.cucina.search.query.modifier;

import java.util.ArrayList;
import java.util.Collection;

import org.cucina.core.InstanceFactory;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.ForeignKeySearchCriterion;

import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PermissionCriteriaBuilderHelperImplTest {
    @Mock
    private InstanceFactory instanceFactory;
    private PermissionCriteriaBuilderHelperImpl helper;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        helper = new PermissionCriteriaBuilderHelperImpl(instanceFactory);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testBuildClause() {
        Collection<Permission> permissions = new ArrayList<Permission>();
        Permission permission = new Permission();
        Collection<Dimension> dimensions = new ArrayList<Dimension>();
        Dimension dimension = new Dimension();

        dimension.setPropertyName("bars");
        dimension.setDomainObjectId(20L);
        dimensions.add(dimension);
        permission.setDimensions(dimensions);

        permissions.add(permission);

        SearchCriterion sc = helper.buildClause("Foo", "foo", permissions);

        assertNull("result is not null", sc);

        when(instanceFactory.getPropertyType("Foo", "bars")).thenReturn("whoaaah");
        sc = helper.buildClause("Foo", "foo", permissions);
        System.err.println(sc);
        assertNotNull("search criteria is null", sc);
        assertTrue("Not a ForeignKeySearchCriterion", sc instanceof ForeignKeySearchCriterion);

        ForeignKeySearchCriterion fsc = (ForeignKeySearchCriterion) sc;

        assertTrue(fsc.getValue().contains(20L));
        assertEquals("bars", fsc.getName());
        assertEquals("foo", fsc.getRootAlias());
        assertEquals("bars", fsc.getAlias());
    }
}
