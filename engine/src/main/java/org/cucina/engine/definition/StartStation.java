package org.cucina.engine.definition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


/**
 * The {@link State} that signifies the start of a {@link ProcessDefinition}.
 *
 * @author Rob Harrop
 */
public class StartStation
    extends AbstractState {
    /**
     * JAVADOC Method Level Comments
     *
     * @param transitionId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Transition getTransition(String transitionId) {
        Transition transition;

        if (StringUtils.isNotEmpty(transitionId)) {
            transition = super.getTransition(transitionId);
        } else {
            transition = getDefaultTransition();
        }

        Assert.notNull(transition, "Should at least have a default transition");

        return transition;
    }
}
