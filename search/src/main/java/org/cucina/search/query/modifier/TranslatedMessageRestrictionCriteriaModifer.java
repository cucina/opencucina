package org.cucina.search.query.modifier;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.i18n.api.LocaleService;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.cucina.search.query.projection.Projection;
import org.cucina.search.query.projection.TranslatedMessageWithJoinProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Interrogates SearchBean to establish if it has any TranslatedMessageWithJoinProjection, if so it adds criterion
 * restricting according to the User's preferred locale.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TranslatedMessageRestrictionCriteriaModifer
    extends AbstractCriteriaModifier {
    private static final Logger LOG = LoggerFactory.getLogger(TranslatedMessageRestrictionCriteriaModifer.class);
    private LocaleService localeService;

    /**
     * Creates a new TranslatedMessageRestrictionCriteriaModifer object.
     *
     * @param i18nService JAVADOC.
     */
    public TranslatedMessageRestrictionCriteriaModifer(LocaleService localeService) {
        super();
        Assert.notNull(localeService, "localeService must be provided as an argument");
        this.localeService = localeService;
    }

    /**
    * Interrogates SearchBean to establish if it has any TranslatedMessageWithJoinProjection, if so it adds criterion
    * restricting according to the User's preferred locale.
     *
     * @param searchBean SearchBean.
     * @param params Map<String,Object>.
     *
     * @return JAVADOC.
     */
    @Override
    protected SearchBean doModify(SearchBean searchBean, Map<String, Object> params) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "We have projections, checking them to see if any are TranslatedMessageWithJoinProjection, in which case restrict by User's locale");
        }

        if (CollectionUtils.isNotEmpty(searchBean.getProjections())) {
            Locale locale = localeService.currentUserLocale();

            for (Projection projection : searchBean.getProjections()) {
                if (projection instanceof TranslatedMessageWithJoinProjection) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("TranslatedMessageWithJoinProjection found restricting [" +
                            projection.getName() + "] by user locale [" + locale.toString() + "]");
                    }

                    TextSearchCriterion localeRestriction = new TextSearchCriterion("localeCd",
                            projection.getAlias(), projection.getRootAlias(), locale.toString());

                    localeRestriction.setExact(true);

                    searchBean.addCriterion(localeRestriction);

                    Collection<SearchCriterion> searchCriteria = new HashSet<SearchCriterion>();

                    searchCriteria.add(localeRestriction);

                    ((TranslatedMessageWithJoinProjection) projection).setSearchCriteria(searchCriteria);
                }
            }
        }

        return searchBean;
    }
}
