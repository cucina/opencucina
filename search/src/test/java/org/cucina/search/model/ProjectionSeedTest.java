package org.cucina.search.model;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.search.model.projection.Search;

import javax.validation.groups.Default;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProjectionSeedTest {
	/**
	 * JAVADOC Method Level Comments
	 */
//    @Test
	public void testGetProjectionsA() {
		List<ProjectionSeed> result = ProjectionSeed.getProjections(Annoited.class);

		assertNotNull("result is null", result);

		ProjectionSeed bytes = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "bytes"));

		assertNotNull("seed cannot be null", bytes);
		assertEquals("name.bytes", bytes.getProperty());
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "number")));

		ProjectionSeed shortname = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "short name"));

		assertEquals("name", shortname.getProperty());
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "lastDate")));
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "nextDate")));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	//  @Test
	public void testGetProjectionsB() {
		//these should be the same as the ones above
		List<ProjectionSeed> result = ProjectionSeed.getProjections(Annoited.class, Default.class);

		assertNotNull("result is null", result);

		ProjectionSeed bytes = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "bytes"));

		assertNotNull("seed cannot be null", bytes);
		assertEquals("name.bytes", bytes.getProperty());
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "number")));

		ProjectionSeed shortname = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "short name"));

		assertEquals("name", shortname.getProperty());
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "lastDate")));
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "nextDate")));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
//    @Test
	public void testGetProjectionsC() {
		List<ProjectionSeed> result = ProjectionSeed.getProjections(Annoited.class, Search.class);

		assertNotNull("result is null", result);

		ProjectionSeed bytes = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "bytes"));

		assertNotNull("seed cannot be null", bytes);
		assertEquals("name.bytes", bytes.getProperty());
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "number")));

		ProjectionSeed shortname = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "short name"));

		assertEquals("name", shortname.getProperty());

		ProjectionSeed lastDate = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "lastDate"));

		assertNotNull("lastDate cannot be null", lastDate);
		assertEquals("lastDate", lastDate.getProperty());
		assertEquals("preference", lastDate.getEntityAlias());
		assertEquals("Preference", lastDate.getEntityClazz());
		assertNull("null as column has been marked with Export",
				CollectionUtils.find(result, new BeanPropertyValueEqualsPredicate("alias", "nextDate")));

		System.err.println(result);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
//    @Test
	public void testGetProjectionsD() {
		List<ProjectionSeed> result = ProjectionSeed.getProjections(Annoited.class, Default.class);

		assertNotNull("result is null", result);

		ProjectionSeed bytes = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "bytes"));

		assertNotNull("seed cannot be null", bytes);
		assertEquals("name.bytes", bytes.getProperty());

		ProjectionSeed number = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "number"));

		assertNotNull("number cannot be null", number);
		assertEquals("number", number.getProperty());

		ProjectionSeed shortname = (ProjectionSeed) CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "short name"));

		assertEquals("name", shortname.getProperty());
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "lastDate")));
		assertNull(CollectionUtils.find(result,
				new BeanPropertyValueEqualsPredicate("alias", "nextDate")));

		System.err.println(result);
	}

	/**
	 * JAVADOC for Class Level
	 *
	 * @author $Author: $
	 * @version $Revision: $
	 */
	public class Annoited {
		private Collection<String> strings;
		private String name;
		private double value;
		private int number;

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @return JAVADOC.
		 */
		@ProjectionColumn
		public String getName() {
			return name;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param name JAVADOC.
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @return JAVADOC.
		 */
		@ProjectionColumn
		public int getNumber() {
			return number;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param number JAVADOC.
		 */
		public void setNumber(int number) {
			this.number = number;
		}

		@ProjectionColumn
		public Collection<String> getStrings() {
			return strings;
		}

		public void setStrings(Collection<String> strings) {
			this.strings = strings;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @return JAVADOC.
		 */
		public double getValue() {
			return value;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param value JAVADOC.
		 */
		public void setValue(double value) {
			this.value = value;
		}
	}
}
