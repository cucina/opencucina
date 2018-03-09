package org.cucina.engine.definition;

import org.cucina.engine.ExecutionContext;


/**
 * <code>Action</code>s can be configured to be invoked when entering and
 * leaving {@link State Places} in a {@link ProcessDefinition}.
 * <code>Action</code>s are intended to be singleton, thread-safe objects
 * that be reused for each invocation. <p/> When entering or leaving a
 * {@link State} the {@link #execute execute} method of an <code>Action</code>
 * is invoked as appropriate.
 *
 * @author Rob Harrop
 * @see Check
 */
public interface Operation
		extends ProcessElement {
	/**
	 * Invokes the <code>Action</code>. <code>Action</code>s have full
	 * access to the {@link ExecutionContext} which contains not only
	 * user supplied data but also the {@link Token} representing the state of
	 * the current workflow.
	 */
	void execute(ExecutionContext executionContext);
}
