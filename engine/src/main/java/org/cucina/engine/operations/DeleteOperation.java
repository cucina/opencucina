package org.cucina.engine.operations;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.repository.DomainRepository;
import org.springframework.util.Assert;


/**
 * Deletes and nullifies the current domainObject on the Token in context.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DeleteOperation
		extends AbstractOperation {
	private DomainRepository domainRepository;

	/**
	 * Creates a new DeleteAction object.
	 *
	 * @param domainRepository JAVADOC.
	 */
	public DeleteOperation(DomainRepository domainRepository) {
		Assert.notNull(domainRepository, "domainRepository is null");
		this.domainRepository = domainRepository;
	}

	/**
	 * Calls delete on domainRepository and nullifies domainObject on Token
	 *
	 * @param executionContext ExecutionContext.
	 */
	@Override
	public void execute(ExecutionContext executionContext) {
		domainRepository.delete(executionContext.getToken().getDomainObject());
		executionContext.getToken().setDomainObject(null);
	}
}
