package org.cucina.engine.definition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.cucina.engine.CheckNotMetException;
import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.ProcessDriverFactory;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC.
 *
 * @author $author$
 */
public class TransitionTest {
    @Mock
    private ProcessDriver executor;
    @Mock
    private ProcessDriverFactory executorFactory;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executorFactory.getExecutor()).thenReturn(executor);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = CheckNotMetException.class)
    public void testFailsCondition() {
        DefaultExecutionContext context = new DefaultExecutionContext(null, null, null,
                executorFactory);

        Transition transition = new Transition();

        Check condition = mock(Check.class);

        transition.addCondition(condition);
        when(executor.test(condition, context)).thenReturn(false);
        transition.checkConditions(context);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testSerialization()
        throws Exception {
        Transition transition = new Transition();

        transition.addCondition(mock(Check.class));
        transition.addLeaveAction(mock(Operation.class));
        transition.setDescription("description");
        transition.setId("id");
        transition.setInput(mock(State.class));
        transition.setOutput(mock(State.class));
        transition.setPrivilegeName("privilegeName");
        System.err.println("Transition before serialization: " + transition);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(transition);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Transition tr = (Transition) ois.readObject();

        System.err.println("Transition after:" + tr);
    }
}
