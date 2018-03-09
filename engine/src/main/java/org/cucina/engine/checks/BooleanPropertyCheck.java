package org.cucina.engine.checks;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.Check;


/**
 * Condition that just returns the value of the boolean property. The property
 * has a setter and a getter, so that it can be manipulated dynamically (e.g. in
 * a web page).
 *
 * @author dsyer
 */
public class BooleanPropertyCheck
		extends AbstractCheck {
	private boolean value = false;

	/**
	 * True if this condition will pass, false otherwise. Defaults to false if
	 * the property is not set.
	 *
	 * @return
	 */
	public boolean isTrue() {
		return value;
	}

	/**
	 * Set the value of the true property.
	 *
	 * @param value
	 */
	public void setTrue(boolean value) {
		this.value = value;
	}

	/**
	 * Simply returns isTrue().
	 *
	 * @param executionContext JAVADOC.
	 * @return JAVADOC.
	 * @see Check#test(ExecutionContext)
	 */
	public boolean test(ExecutionContext executionContext) {
		return isTrue();
	}
}
