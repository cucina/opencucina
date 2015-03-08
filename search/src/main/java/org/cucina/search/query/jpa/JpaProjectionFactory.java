package org.cucina.search.query.jpa;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.utils.NameUtils;
import org.cucina.i18n.model.ListNode;
import org.cucina.search.query.ProjectionFactory;
import org.cucina.search.query.projection.CountProjection;
import org.cucina.search.query.projection.MaxProjection;
import org.cucina.search.query.projection.Projection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.cucina.search.query.projection.TranslatedPropertyProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Jpa implementation generating Projections
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class JpaProjectionFactory
    implements ProjectionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JpaProjectionFactory.class);
    private static final String MAX_AGG = "max";
    private static final String COUNT_AGG = "count";
    private InstanceFactory instanceFactory;

    /**
     * Constructor with instanceFactory argument.
     *
     * @param instanceFactory InstanceFactory
     */
    public JpaProjectionFactory(InstanceFactory instanceFactory) {
        super();
        Assert.notNull(instanceFactory, "instanceFactory cannot be null");
        this.instanceFactory = instanceFactory;
    }

    /**
     * Create projection according to arguments.
     *
     * @param rootType String of type name is relative to.
     * @param name String property name, fully qualified if appropriate.
     * @param alias String.
     * @param rootAlias String.
     * @param specialFunction String non mandatory as not all projections will have a special function.
     *
     * @return Projection.
     */
    @Override
    public Projection create(String rootType, String name, String alias, String rootAlias,
        String specialFunction) {
        Assert.notNull(rootType, "rootType cannot be null");
        Assert.notNull(name, "name cannot be null");
        Assert.notNull(rootAlias, "rootAlias cannot be null");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Attempt to create projection for search property '" + name + "', alias '" +
                alias + "' on type '" + rootType + "', rootAlias '" + rootAlias + "', aggregate '" +
                specialFunction + "'");
        }

        Projection projection = null;

        if (ListNode.class.getSimpleName().equals(instanceFactory.getPropertyType(rootType, name))) {
            projection = new TranslatedPropertyProjection(NameUtils.concat(name, "label.messageCd"),
                    alias, rootAlias, ListNode.BASENAME);
        } else if (instanceFactory.isTranslatedProperty(rootType, name)) {
            projection = new TranslatedPropertyProjection(name, alias, rootAlias);
        } else if (MAX_AGG.equals(specialFunction)) {
            projection = new MaxProjection(name, alias, rootAlias);
        } else if (COUNT_AGG.equals(specialFunction)) {
            projection = new CountProjection(name, alias, rootAlias);
        } else if (StringUtils.isBlank(specialFunction)) {
            projection = new SimplePropertyProjection(name, alias, rootAlias);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Projection generated [" + projection + "]");
        }

        Assert.notNull(projection, "No projection created for [" + name + "]");

        return projection;
    }
}
