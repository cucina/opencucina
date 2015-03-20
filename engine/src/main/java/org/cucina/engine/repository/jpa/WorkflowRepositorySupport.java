package org.cucina.engine.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Tuple;

import org.cucina.core.model.PersistableEntity;
import org.cucina.engine.model.ProcessToken;
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
    protected Collection<ProcessToken> populate(Collection<Tuple> input) {
        Collection<ProcessToken> result = new ArrayList<ProcessToken>();

        for (Tuple tuple : input) {
            ProcessToken wt = (ProcessToken) tuple.get(0);
            PersistableEntity pe = (PersistableEntity) tuple.get(1);

            Assert.notNull(wt, "token should not be null");
            Assert.notNull(pe, "domain should not be null");
            wt.setDomainObject(pe);
            result.add(wt);
        }

        return result;
    }
}
