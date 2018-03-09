package org.cucina.engine.definition.config.xml;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.cucina.engine.definition.*;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Parser for xml workflow definitions. An instance of this class can be re-used
 * safely in a single thread, but it is not intended to be thread safe.
 * <p>
 * The core functionality is wrapped in various convenient accessors to parse
 * String, Resource, InputStream etc.
 *
 * @author vlevine
 */
public class DigesterModuleProcessDefinitionParser
		implements ProcessDefinitionParser {
	private static final Logger LOG = LoggerFactory.getLogger(DigesterModuleProcessDefinitionParser.class);
	private DigesterLoader loader;
	private EndStation endState;
	private ProcessDefinition processDefinition;
	private Set<State> states = new HashSet<State>();
	private Set<State> visitedPlaces = new HashSet<State>();
	private StartStation startState;
	private String description;

	/**
	 * Creates a new DigesterModuleWorkflowDefinitionParser object.
	 *
	 * @param rulesResource Resource containing parsing rules definition.
	 */
	public DigesterModuleProcessDefinitionParser(Resource rulesResource) {
		Assert.notNull(rulesResource, "ruleResource is null");
		loader = DigesterLoader.newLoader(new DigesterRulesModule(rulesResource));
		Assert.notNull(loader, "Failed to create loader for the resource:" + rulesResource);
	}

	/**
	 * Sets the description of the current process.
	 * Callback for digester
	 *
	 * @param description A description of the current process.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the end state for the current process as we are parsing.
	 * Callback for digester
	 *
	 * @param endState the end state.
	 */
	public void setEndState(EndStation endState) {
		this.endState = endState;
	}

	/**
	 * Callback for digester
	 *
	 * @param startState The startState to set for the current process.
	 */
	public void setStartState(StartStation startState) {
		this.startState = startState;
	}

	/**
	 * Callback for digester
	 *
	 * @param state JAVADOC
	 */
	public void addState(State state) {
		Assert.notNull(state, "State is null");
		states.add(state);
	}

	/**
	 * Callback for digester
	 */
	public void createWorkflowDefinition(String name) {
		processDefinition = new ProcessDefinition();
		processDefinition.setId(name);
	}

	/**
	 * JAVADOC.
	 *
	 * @param resource JAVADOC.
	 * @return JAVADOC.
	 * @see org.cucina.engine.definition.config.ProcessDefinitionParser#parse(org.springframework.core.io.Resource)
	 */
	public ProcessDefinition parse(Resource resource) {
		reset();

		return readDefinition(resource);
	}

	private void populate(AbstractState node) {
		visitedPlaces.add(node);
		node.setProcessDefinition(processDefinition);

		Collection<Transition> transitions = node.getAllTransitions();

		for (Transition transition : transitions) {
			transition.setProcessDefinition(processDefinition);

			if (transition instanceof WrapperTransition) {
				String to = ((WrapperTransition) transition).getTo();

				// TODO should be the same for all states
				if (endState.getId().equals(to)) {
					transition.setOutput(endState);
				} else if (startState.getId().equals(to)) {
					transition.setOutput(startState);
				} else {
					for (Iterator<State> it = states.iterator(); it.hasNext(); ) {
						AbstractState place = (AbstractState) it.next();

						if (place.getId().equals(to)) {
							if (!visitedPlaces.contains(place)) {
								populate(place);
							}

							transition.setOutput(place);
						}
					}
				}
			}
		}
	}

	private ProcessDefinition readDefinition(Resource definitionResource) {
		try {
			Digester digester = loader.newDigester();

			digester.push(this);
			digester.parse(definitionResource.getInputStream());
			Assert.notNull(processDefinition,
					"Failed to create the workflow definition as a result of parse");
			Assert.notNull(startState, "Failed to create the start state as a result of parse");
			Assert.notNull(endState, "Failed to create the end state as a result of parse");
			Assert.notNull(description, "Failed to create the description as a result of parse");
			processDefinition.setStartState(startState);
			processDefinition.setDescription(description);
			populate(processDefinition.getStartState());

			return processDefinition;
		} catch (IOException ex) {
			LOG.error("Could not read workflow definition: [" + definitionResource + "]", ex);
			throw new RuntimeException(ex);
		} catch (SAXException ex) {
			LOG.error("Parse error in workflow definition: [" + definitionResource + "]", ex);
			throw new RuntimeException(ex);
		}
	}

	private void reset() {
		states.clear();
		visitedPlaces.clear();
		endState = null;
		startState = null;
	}
}
