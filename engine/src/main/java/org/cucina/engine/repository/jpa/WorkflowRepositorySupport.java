package org.cucina.engine.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Tuple;

import org.cucina.core.model.PersistableEntity;
import org.cucina.engine.model.WorkflowToken;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class WorkflowRepositorySupport {
    /**
     * JAVADOC Method Level Comments
     *
     * @param input JAVADOC.
     *
     * @return JAVADOC.
     */
    protected Collection<WorkflowToken> populate(Collection<Tuple> input) {
        Collection<WorkflowToken> result = new ArrayList<WorkflowToken>();

        for (Tuple tuple : input) {
            WorkflowToken wt = (WorkflowToken) tuple.get(0);
            PersistableEntity pe = (PersistableEntity) tuple.get(1);

            Assert.notNull(wt, "token should not be null");
            Assert.notNull(pe, "domain should not be null");
            wt.setDomainObject(pe);
            result.add(wt);
        }

        return result;
    }
}
