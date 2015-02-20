package org.cucina.loader.agent;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AgentCollectionTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testExecute() {
        LoggingEnabler.enableLog(AgentCollection.class);

        Agent executor = mock(Agent.class);

        AgentCollection collectionImpl = new AgentCollection(Collections.singletonList(
                    executor));

        collectionImpl.execute();
        verify(executor).execute();
    }
    
    @Test
    public void testRouterReturnsTrue() {
        Agent executor = mock(Agent.class);
        Router router = mock(Router.class);
        Agent executor2 = mock(Agent.class);
        Agent outputExecutor = mock(Agent.class);

        when(router.route()).thenReturn(true);

        AgentCollection collectionImpl = new AgentCollection(Arrays.asList(new Agent[] {executor, router, executor2}));
        collectionImpl.setOutput(outputExecutor);
        collectionImpl.execute();
        verify(executor).execute();
        verify(router).route();
        verify(executor2).execute();
        verify(outputExecutor).execute();

    }
    
    @Test
    public void testRouterReturnsFalse() {
        Agent executor = mock(Agent.class);
        Router router = mock(Router.class);
        Agent executor2 = mock(Agent.class);
        when(router.route()).thenReturn(false);
        
        Agent outputExecutor = mock(Agent.class);

        AgentCollection collectionImpl = new AgentCollection(Arrays.asList(new Agent[] {executor, router, executor2}));
        collectionImpl.setOutput(outputExecutor);

        collectionImpl.execute();
        verify(executor).execute();
        verify(router).route();
        verify(router).runAlternative();
        verify(executor2, never()).execute();
        verify(outputExecutor, never()).execute();
    }
    
    @Test(expected=RuntimeException.class)
    public void testExceptionsArePropagated() throws RuntimeException{
    	
    	 Agent executor = mock(Agent.class);
         Router router = mock(Router.class);
         Agent executor2 = mock(Agent.class);
         doThrow(new RuntimeException("blaah")).when(executor).execute();
         
         AgentCollection collectionImpl = new AgentCollection(Arrays.asList(new Agent[] {executor, router, executor2}));
         
         collectionImpl.execute();

    }
    
    @Test(expected=RuntimeException.class)
    public void testExceptionsArePropagatedAlternative() throws RuntimeException{
    	
    	 Agent executor = mock(Agent.class);
         Router router = mock(Router.class);
         Agent executor2 = mock(Agent.class);
         when(router.route()).thenReturn(false);
         doThrow(new RuntimeException("blaah")).when(router).runAlternative();

         AgentCollection collectionImpl = new AgentCollection(Arrays.asList(new Agent[] {executor, router, executor2}));
         collectionImpl.execute();
         
    }
    
    @Test(expected=RuntimeException.class)
    public void testExceptionsArePropagatedOutput() throws RuntimeException{
    	
    	 Agent executor = mock(Agent.class);
         Router router = mock(Router.class);
         Agent executor2 = mock(Agent.class);
         when(router.route()).thenReturn(true);
         doThrow(new RuntimeException("blaah")).when(executor2).execute();

         AgentCollection collectionImpl = new AgentCollection(Arrays.asList(new Agent[] {executor, router}));
         collectionImpl.setOutput(executor2);
         collectionImpl.execute();
    }
}
