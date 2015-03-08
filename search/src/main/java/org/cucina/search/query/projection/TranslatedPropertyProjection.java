package org.cucina.search.query.projection;

import org.cucina.i18n.service.MessageHelper;
import org.springframework.util.Assert;


/**
 * Implementation of a translated property
 *
 */
public class TranslatedPropertyProjection
    extends AbstractProjection {
    /** messages.messages */
    public static final String BASENAME = "messages.messages";
    private String basename = BASENAME;

    /**
     * Creates a new TranslatedPropertyProjection object.
     *
     * @param name
     *            fully qualified field name, e.g. 'bazs.bars.name'.
     * @param alias
     *            projection alias.
     * @param rootAlias
     *            alias of 'root' Type.
     */
    public TranslatedPropertyProjection(String name, String alias, String rootAlias) {
        super(name, alias, rootAlias);
    }

    /**
     * Creates a new SimplePropertyProjection object.
     *
     * @param name
     *            fully qualified field name, e.g. 'bazs.bars.name'.
     * @param alias
     *            projection alias.
     * @param rootAlias
     *            alias of 'root' Type.
     * @param basename
     */
    public TranslatedPropertyProjection(String name, String alias, String rootAlias, String basename) {
        this(name, alias, rootAlias);
        Assert.hasText(basename, "basename is required!");
        this.basename = basename;
    }

    /**
     * Constructor is used by JSON for constructing new projections.
     */
    @SuppressWarnings("unused")
    private TranslatedPropertyProjection() {
        super();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param basename
     *            JAVADOC.
     */
    public void setBasename(String basename) {
        this.basename = basename;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getBasename() {
        return basename;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getProjection() {
        StringBuilder selectClause = new StringBuilder("(coalesce((");

        selectClause.append(MessageHelper.bestMsgJpql(basename, getSearchPropertyName(), false));
        selectClause.append("),");
        selectClause.append(getSearchPropertyName());
        selectClause.append("))");
        selectClause.append(" as ");
        selectClause.append(getAlias());

        return selectClause.toString();
    }
}
