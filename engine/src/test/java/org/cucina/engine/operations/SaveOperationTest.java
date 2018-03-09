package org.cucina.engine.operations;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.definition.Token;
import org.cucina.engine.repository.DomainRepository;
import org.cucina.engine.testassist.Foo;
import org.junit.Test;

import static org.mockito.Mockito.*;


/**
 * @author vlevine
 */
public class SaveOperationTest {
	/**
	 * saves token
	 */
	@Test
	public void saves() {
		Foo foo = new Foo();
		Token token = mock(Token.class);

		doReturn(foo).when(token).getDomainObject();

		DomainRepository repo = mock(DomainRepository.class);

		SaveOperation action = new SaveOperation(repo);

		action.execute(new DefaultExecutionContext(token, null, null, null));
		verify(repo).save(foo);
	}
}
