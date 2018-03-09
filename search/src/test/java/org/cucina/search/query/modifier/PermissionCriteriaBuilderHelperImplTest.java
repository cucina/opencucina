package org.cucina.search.query.modifier;

import org.cucina.core.InstanceFactory;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.ForeignKeySearchCriterion;
import org.cucina.security.api.DimensionDto;
import org.cucina.security.api.PermissionDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


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
		Collection<PermissionDto> permissions = new ArrayList<PermissionDto>();
		PermissionDto permission = new PermissionDto();
		Collection<DimensionDto> dimensions = new ArrayList<DimensionDto>();
		DimensionDto dimension = new DimensionDto();

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
