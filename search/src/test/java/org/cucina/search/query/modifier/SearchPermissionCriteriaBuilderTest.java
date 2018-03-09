package org.cucina.search.query.modifier;

import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.FailingCriterion;
import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.PermissionDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchPermissionCriteriaBuilderTest {
	@Mock
	private AccessFacade accessFacade;
	@Mock
	private PermissionCriteriaBuilderHelper permissionCriteriaBuilderHelper;
	@Mock
	private SearchBean searchBean;
	@Mock
	private SearchCriterion searchCriterion;
	private SearchPermissionCriteriaBuilder builder;
	private String user = "User";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		builder = new SearchPermissionCriteriaBuilder(accessFacade, permissionCriteriaBuilderHelper);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testBuildCriteria() {
		Collection<PermissionDto> perms = new ArrayList<PermissionDto>();
		PermissionDto perm = new PermissionDto();

		perms.add(perm);
		when(accessFacade.permissionsByUserTypeAccessLevel(user, "Foo",
				PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL)).thenReturn(perms);
		when(permissionCriteriaBuilderHelper.buildClause("Foo", "foo", perms))
				.thenReturn(searchCriterion);

		SearchBean sb = builder.buildCriteria(searchBean, user, "Foo", "foo",
				PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL);

		assertNotNull("Result is null", sb);
		verify(searchBean).addCriterion(searchCriterion);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testBuildFail() {
		when(accessFacade.permissionsByUserTypeAccessLevel(user, "Foo",
				PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL)).thenReturn(null);

		SearchBean sb = builder.buildCriteria(searchBean, user, "Foo", "foo",
				PermissionCriteriaModifier.DEFAULT_ACCESS_LEVEL);

		assertNotNull("Result is null", sb);
		verify(searchBean).addCriterion(any(FailingCriterion.class));
	}
}
