package org.cucina.engine.testadapters;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.checks.AbstractCheck;
import org.cucina.engine.definition.Decision;
import org.cucina.engine.definition.EndStation;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.StartStation;
import org.cucina.engine.definition.Station;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.testassist.Foo;


/**
 * Builds mock {@link ProcessDefinition workflow definitions} for use in testing.
 *
 * @author Rob Harrop
 * @author Rick Evans
 */
public abstract class MockWorkflowDefinitionBuilder {
    /** This is a field JAVADOC */
    public static final String HELLO_WORLD_ID = "helloWorld";

    /** This is a field JAVADOC */
    public static final String CHOOSE_A_DRINK_ID = "chooseADrink";

    /**
     * JAVADOC
     *
     * @return JAVADOC
     */
    public static ProcessDefinition buildChooseADrinkDefinition() {
        ProcessDefinition definition = new ProcessDefinition();

        definition.setId(CHOOSE_A_DRINK_ID);

        // create the start state
        StartStation startState = new StartStation();

        startState.setId("start");
        definition.setStartState(startState);

        // create the decision
        Decision decision = new Decision();

        decision.setId("decision");

        // create the Coke state
        Station coke = new Station();

        coke.setId("coke");

        //create the Beer state
        Station beer = new Station();

        beer.setId("beer");

        // create the end state
        EndStation end = new EndStation();

        end.setId("end");

        // create the transition to decision
        Transition toChoose = new Transition();

        toChoose.setId("toChoose");
        toChoose.setDefault(true);
        startState.addTransition(toChoose);
        toChoose.setOutput(decision);

        // create the transition to coke
        Transition toCoke = new Transition();

        toCoke.setId("toCoke");
        decision.addTransition(toCoke);
        toCoke.setOutput(coke);
        toCoke.addCondition(new AbstractCheck() {
                public boolean test(ExecutionContext executionContext) {
                    Foo p = (Foo) executionContext.getToken().getDomainObject();

                    return (p.getValue() < 100);
                }
            });

        // create the transition to beer
        Transition toBeer = new Transition();

        toBeer.setId("toBeer");
        decision.addTransition(toBeer);
        toBeer.setOutput(beer);
        toBeer.addCondition(new AbstractCheck() {
                public boolean test(ExecutionContext executionContext) {
                    Foo p = (Foo) executionContext.getToken().getDomainObject();

                    return (p.getValue() >= 18);
                }
            });

        // transition from coke to end
        Transition toEndFromCoke = new Transition();

        toEndFromCoke.setId("toEndFromCoke");
        coke.addTransition(toEndFromCoke);
        toEndFromCoke.setOutput(end);

        // transition from beer to end
        Transition toEndFromBeer = new Transition();

        toEndFromBeer.setId("toEndFromBeer");
        beer.addTransition(toEndFromBeer);
        toEndFromBeer.setOutput(end);

        return definition;
    }

    /**
     * JAVADOC
     *
     * @return JAVADOC
     */
    public static ProcessDefinition buildHelloWorldDefinition() {
        ProcessDefinition definition = new ProcessDefinition();

        definition.setId(HELLO_WORLD_ID);

        // create the start state
        StartStation startState = new StartStation();

        startState.setId("start");
        definition.setStartState(startState);

        // create a toHelloWorld to the hello world state
        Transition toHelloWorld = new Transition();

        toHelloWorld.setId("toHelloWorld");
        toHelloWorld.setInput(startState);
        toHelloWorld.setDefault(true);
        startState.addTransition(toHelloWorld);

        // create the hello world state
        Station helloWorld = new Station();

        helloWorld.setId("helloWorld");
        toHelloWorld.setOutput(helloWorld);

        // create to the end state
        Transition toEnd = new Transition();

        toEnd.setId("toEnd");
        toEnd.setInput(helloWorld);
        toEnd.setPrivilegeName("myPriv,myPriv1");
        toEnd.setDefault(true);
        helloWorld.addTransition(toEnd);

        // create the end state
        EndStation endState = new EndStation();

        endState.setId("end");
        toEnd.setOutput(endState);

        return definition;
    }
}
