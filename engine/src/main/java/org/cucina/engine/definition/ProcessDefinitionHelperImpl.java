package org.cucina.engine.definition;

import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.springframework.util.Assert;


/**
 * Implementation of Helper which interrogates {@link ProcessDefinition}.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessDefinitionHelperImpl
		implements ProcessDefinitionHelper {
	private ProcessDefinitionRegistry definitionRegistry;

	/**
	 * Constructor taking definitionRegistry as argument
	 *
	 * @param definitionRegistry WorkflowDefinitionRegistry
	 */
	public ProcessDefinitionHelperImpl(ProcessDefinitionRegistry definitionRegistry) {
		super();
		Assert.notNull(definitionRegistry, "definitionRegistry cannot be null");
		this.definitionRegistry = definitionRegistry;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param token JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean isEnded(Token token) {
		Assert.notNull(token, "token cannot be null");
		Assert.notNull(token.getProcessDefinitionId(), "token must have a workflowDefinitionId");
		Assert.notNull(token.getPlaceId(), "token must have a placeId");

		ProcessDefinition definition = definitionRegistry.findWorkflowDefinition(token.getProcessDefinitionId());

		Assert.notNull(definition,
				"No definition defined for [" + token.getProcessDefinitionId() + "]");

		State place = definition.findPlace(token.getPlaceId());

		Assert.notNull(place,
				"No place [" + token.getPlaceId() + "] defined for [" +
						token.getProcessDefinitionId() + "]");

		return place instanceof EndStation;
	}

	/**
	 * Interrogates workflow with <code>workflowId</code> and returns place id that contains
	 * <code>transitionId</code>.
	 *
	 * @param workflowId   String.
	 * @param transitionId String.
	 * @return placeId String.
	 */
	@Override
	public String findPlaceId(String workflowId, String transitionId) {
		Assert.notNull(workflowId, "Should provide workflowId as an argument");
		Assert.notNull(transitionId, "Should provide transitionId as an argument");

		ProcessDefinition workflowDefinition = definitionRegistry.findWorkflowDefinition(workflowId);

		Assert.notNull(workflowDefinition,
				"No workflow definition defined for id [" + workflowId + "]");

		State[] allPlaces = workflowDefinition.getAllPlaces();

		for (int i = 0; i < allPlaces.length; ++i) {
			State place = allPlaces[i];

			if (place.hasTransition(transitionId)) {
				return place.getId();
			}
		}

		throw new TransitionNotFoundException("Transition [" + transitionId + "] not found.");
	}
}
