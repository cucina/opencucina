package org.cucina.engine.server.repository.jpa;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.server.testassist.JpaProvider;


/**
 * 
 *
 * @author vlevine
  */
public class ProcessTokenRepositoryImplTest
    extends JpaProvider {
    private ProcessTokenRepositoryImpl repo;

    /**
     *
     */
    @Test
    public void testListAggregated() {
        Collection<Object[]> coll = repo.countByGroupProcessDefinitionId();

        System.err.println(ToStringBuilder.reflectionToString(coll.iterator().next()));
    }

    /**
     *
     *
     * @throws Exception .
     */
    @Override
    protected void onSetUp()
        throws Exception {
        super.onSetUp();
        repo = new ProcessTokenRepositoryImpl();
        repo.setEntityManager(getEntityManager());

        EntityDescriptor ed = getInstanceFactory()
                                  .getBean(EntityDescriptor.class.getSimpleName());

        ed.setApplicationName("client");
        ed.setApplicationType("Token");
        ed.setRemoteId("111");
        getEntityManager().persist(ed);

        ProcessToken token = getInstanceFactory().getBean(ProcessToken.class.getSimpleName());

        token.setDomainObjectType(EntityDescriptor.class.getSimpleName());
        token.setDomainObjectId(ed.getId());
        token.setProcessDefinitionId("process!");
        getEntityManager().persist(token);
    }
}
