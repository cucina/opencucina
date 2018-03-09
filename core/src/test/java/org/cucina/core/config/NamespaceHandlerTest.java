package org.cucina.core.config;

import org.junit.Test;


/**
 * Test NamespaceHandler inits correctly.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NamespaceHandlerTest {
	/**
	 * init
	 */
	@Test
	public void registers() {
		NamespaceHandler handler = new NamespaceHandler();

		handler.init();
	}
}
