package org.cucina.loader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvRowLoaderProcessorTest {
	@Mock
	private CsvRowLoader csvRowLoader;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testProcess()
			throws Exception {
		CsvRowLoaderProcessor processor = new CsvRowLoaderProcessor(csvRowLoader);
		FileLoaderContainer object = new FileLoaderContainer("Foo", new String[]{"h1", "h2"},
				new String[]{"d1", "d2"}, 123);

		processor.process(object);
		verify(csvRowLoader)
				.processRow("Foo", new String[]{"h1", "h2"}, new String[]{"d1", "d2"}, 123);
	}
}
