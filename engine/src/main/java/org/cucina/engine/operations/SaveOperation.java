package org.cucina.engine.operations;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.repository.DomainRepository;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;


/**
 * Creates or saves the current domainObject on the Token in context.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SaveOperation
		extends AbstractOperation {
	private DomainRepository domainRepository;

	/**
	 * Creates a new SaveAction object.
	 *
	 * @param domainRepository JAVADOC.
	 */
	public SaveOperation(DomainRepository domainRepository) {
		Assert.notNull(domainRepository, "domainRepository is null");
		this.domainRepository = domainRepository;
	}

	/**
	 * Calls save on domainRepository and nullifies domainObject on Token
	 *
	 * @param executionContext ExecutionContext.
	 */
	@Override
	public void execute(ExecutionContext executionContext) {
		Persistable<?> entity = executionContext.getToken().getDomainObject();

		domainRepository.save(entity);
		executionContext.getToken().setDomainObject(entity);
	}
}
