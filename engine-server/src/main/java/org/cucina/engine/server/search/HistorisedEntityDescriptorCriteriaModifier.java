package org.cucina.engine.server.search;

import java.util.Map;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.utils.NameUtils;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.search.marshall.SearchCriterionMarshaller;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.criterion.JoinCriterion;
import org.cucina.search.query.criterion.SubSelectCriterion;
import org.cucina.search.query.modifier.AbstractCriteriaModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * CriteriaModifier for historised types which adds history specific criterion.
 *
 * @author hkelsey
 *
 */
public class HistorisedEntityDescriptorCriteriaModifier
    extends AbstractCriteriaModifier {
    private static final Logger LOG = LoggerFactory.getLogger(HistorisedEntityDescriptorCriteriaModifier.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param searchBean JAVADOC.
     * @param params JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    protected SearchBean doModify(SearchBean searchBean, Map<String, Object> params) {
        String subjectType = (String) params.get(PersistableEntity.APPLICATION_TYPE);

        if (EntityDescriptor.class.getSimpleName().equals(subjectType)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding history criterion to search");
            }

            String subjectAlias = searchBean.getAliasByType().get(subjectType);

            Assert.notNull(subjectAlias,
                "subjectAlias must be set up in SearchBean for type [" + subjectType + "]");

            //Join domain id with token domainObjectId
            searchBean.addCriterion(new JoinCriterion(SearchCriterionMarshaller.HISTORY_ALIAS,
                    "id", subjectAlias, "token.domainObjectId",
                    SearchCriterionMarshaller.HISTORY_ALIAS));
            //Join EntityDescriptor applicationType with token domainObjectType
            searchBean.addCriterion(new JoinCriterion(SearchCriterionMarshaller.HISTORY_ALIAS,
                    EntityDescriptor.APPLICATION_TYPE_PROP, subjectAlias, "token.domainObjectType",
                    SearchCriterionMarshaller.HISTORY_ALIAS));
            //max joining with EntityDescriptor
            searchBean.addCriterion(new SubSelectCriterion("id",
                    SearchCriterionMarshaller.HISTORY_ALIAS,
                    "select max(hr.id) from HistoryRecord hr " +
                    "where hr.token.domainObjectId = " + NameUtils.concat(subjectAlias, "id") +
                    " and hr.token.domainObjectType = " +
                    NameUtils.concat(subjectAlias, EntityDescriptor.APPLICATION_TYPE_PROP)));
        }

        return searchBean;
    }
}
