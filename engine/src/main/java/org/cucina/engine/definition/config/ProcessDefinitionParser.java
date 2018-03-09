package org.cucina.engine.definition.config;

import org.cucina.engine.definition.ProcessDefinition;
import org.springframework.core.io.Resource;


/**
 * JAVADOC.
 *
 * @author vlevine
 * @version $Revision: 1.1 $
 */
public interface ProcessDefinitionParser {
	/**
	 * The main public interface for this class.  Reads
	 * xml from the resource and turns it into a workflow
	 * definition
	 *
	 * @param resource
	 * @return a workflow definition or null if the xml cannot be parsed
	 */
	public abstract ProcessDefinition parse(Resource resource);
}
