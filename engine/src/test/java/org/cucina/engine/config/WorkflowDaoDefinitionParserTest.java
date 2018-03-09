package org.cucina.engine.config;

import org.cucina.engine.repository.jpa.WorkflowRepositoryImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowDaoDefinitionParserTest {
	private WorkflowRepositoryDefinitionParser parser = new WorkflowRepositoryDefinitionParser();

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanClassElement() {
		assertEquals(WorkflowRepositoryImpl.class, parser.getBeanClass(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testResolveId() {
		assertEquals("workflowRepository", parser.getDefaultId());
	}
}
