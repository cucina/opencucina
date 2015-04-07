package org.cucina.engine.operations;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.definition.Token;
import org.cucina.engine.repository.DomainRepository;
import org.cucina.engine.testassist.Foo;

import org.junit.Test;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Test that DeleteAction functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DeleteOperationTest {
    /**
     * delets token
     */
    @Test
    public void deletes() {
        Foo foo = new Foo();
        Token token = mock(Token.class);

        doReturn(foo).when(token).getDomainObject();

        DomainRepository repo = mock(DomainRepository.class);

        repo.delete(foo);

        DeleteOperation action = new DeleteOperation(repo);

        action.execute(new DefaultExecutionContext(token, null, null, null));
        verify(token).setDomainObject(null);
    }
}
