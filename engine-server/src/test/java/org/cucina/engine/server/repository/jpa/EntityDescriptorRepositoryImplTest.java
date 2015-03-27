package org.cucina.engine.server.repository.jpa;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.server.testassist.JpaProvider;
import org.junit.Test;


/**
 * 
 *
 * @author vlevine
  */
public class EntityDescriptorRepositoryImplTest
    extends JpaProvider {
    private EntityDescriptorRepositoryImpl repo;

    /**
     *
     */
    @Test
    public void testListAggregated() {
        Collection<Object[]> coll = repo.listAggregated();

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
        repo = new EntityDescriptorRepositoryImpl();
        repo.setEntityManager(getEntityManager());

        EntityDescriptor ed = getInstanceFactory()
                                  .getBean(EntityDescriptor.class.getSimpleName());

        ed.setApplicationName("client");
        ed.setApplicationType("Token");
        ed.setLocalId(111L);
        getEntityManager().persist(ed);

        ProcessToken token = getInstanceFactory().getBean(ProcessToken.class.getSimpleName());

        token.setDomainObjectType(EntityDescriptor.class.getSimpleName());
        token.setDomainObjectId(ed.getId());
        token.setProcessDefinitionId("process!");
        getEntityManager().persist(token);
    }
}
