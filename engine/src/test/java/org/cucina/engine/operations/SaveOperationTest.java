package org.cucina.engine.operations;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.definition.Token;
import org.cucina.engine.repository.DomainRepository;
import org.cucina.engine.testassist.Foo;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Test that DeleteAction functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SaveOperationTest {
    /**
     * saves token
     */
    @Test
    public void saves() {
        Foo foo = new Foo();

        Token token = mock(Token.class);

        when(token.getDomainObject()).thenReturn(foo);

        DomainRepository repo = mock(DomainRepository.class);

        SaveOperation action = new SaveOperation(repo);

        action.execute(new DefaultExecutionContext(token, null, null, null));
        verify(repo).save(foo);
    }
}
