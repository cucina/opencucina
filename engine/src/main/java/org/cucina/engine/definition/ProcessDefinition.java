package org.cucina.engine.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.validation.Create;
import org.cucina.core.validation.Update;
import org.cucina.engine.DefaultProcessEnvironment;
import org.cucina.engine.SignalFailedException;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.validation.WorkflowDefinitionUnique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Describes an entire workflow as network-representation based on Petri-nets.
 * Each state in the workflow is represented as a {@link State} and paths
 * between states are represented as {@link Transition Transitions}.
 *
 * @see State
 * @see Transition
 */
public class ProcessDefinition
		implements Serializable {
	private static final long serialVersionUID = -6601188179518248024L;
	private static final Logger LOG = LoggerFactory.getLogger(ProcessDefinition.class);

	/**
	 * Stores all the {@link State Places} that are part of this
	 * <code>WorkflowDefinition</code>.
	 */
	private transient Map<String, State> places = new HashMap<String, State>();

	/**
	 * Gets the {@link StartStation} for this <code>WorkflowDefinition</code>.
	 */
	private transient StartStation startState;
	private String description;
	@NotNull(groups = {
			Create.class, Update.class}
			, message = "org.cucina.engine.validation.null.id")
	@WorkflowDefinitionUnique(groups = {
			Create.class}
			, properties = {
			"id"}
	)
	private String id;

	/**
	 * Retrieves all the {@link State Places} that are part of this
	 * <code>WorkflowDefinition</code>.
	 */
	public State[] getAllPlaces() {
		Collection<State> places = getPlaces().values();

		return places.toArray(new State[places.size()]);
	}

	public void setAllPlaces(State[] placesArray) {
		for (int i = 0; i < placesArray.length; i++) {
			registerPlace(placesArray[i]);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the {@link StartStation} for this <code>WorkflowDefinition</code>.
	 */
	public StartStation getStartState() {
		return startState;
	}

	/**
	 * Sets the {@link StartStation} for this <code>WorkflowDefinition</code>.
	 * Updates the {@link StartStation} so that it is aware of its containing
	 * <code>WorkflowDefinition</code>.
	 */
	public void setStartState(StartStation startState) {
		this.startState = startState;
		this.startState.setProcessDefinition(this);
	}

	/**
	 * Finds a {@link State} that is part of this
	 * <code>WorkflowDefinition</code> by ID.
	 */
	public State findPlace(String placeId) {
		State place = getPlaces().get(placeId);

		if (place == null) {
			throw new SignalFailedException("Failed to find place named '" + placeId +
					"' in workflow '" + id + "'");
		}

		return place;
	}

	/**
	 * Returns String including all elements of the Workflow definition
	 *
	 * @return String
	 */
	public String toLongString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", getId())
				.append("description", getDescription()).toString();
	}

	/**
	 * Registers a {@link State} as part of the <code>WorkflowDefinition</code>.
	 */
	protected void registerPlace(State place) {
		getPlaces().put(place.getId(), place);
	}

	private Map<String, State> getPlaces() {
		if (places == null) {
			places = new HashMap<String, State>();
		}

		return places;
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		id = (String) in.readObject();

		ProcessDefinitionRegistry registry = DefaultProcessEnvironment.instance()
				.getDefinitionRegistry();

		Assert.notNull(registry, "Failed to find workflowDefinitionRegistry in application context");

		ProcessDefinition wfd = registry.findWorkflowDefinition(id);

		Assert.notNull(wfd, "Failed to find workflow with id:'" + id + "'");
		startState = wfd.getStartState();
		setAllPlaces(wfd.getAllPlaces());
	}

	private void writeObject(ObjectOutputStream os)
			throws IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Serializing...");
		}

		os.writeObject(id);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Finished.");
		}
	}
}
