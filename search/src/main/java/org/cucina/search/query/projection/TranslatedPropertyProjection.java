package org.cucina.search.query.projection;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import org.springframework.util.Assert;

import org.cucina.i18n.api.LocaleService;

/**
 * Implementation of a translated property
 * 
 * @deprecated since only viable for the local data model, whereas messages are
 *             part of i18n service now and may live in a different db.
 */
public class TranslatedPropertyProjection extends AbstractProjection {
	/** messages.messages */
	public static final String BASENAME = "messages.messages";
	private LocaleService localeService;
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
	public TranslatedPropertyProjection(String name, String alias,
			String rootAlias, LocaleService localeService) {
		super(name, alias, rootAlias);
		Assert.notNull(localeService, "localeService is null");
		this.localeService = localeService;
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
	public TranslatedPropertyProjection(String name, String alias,
			String rootAlias, String basename, LocaleService localeService) {
		this(name, alias, rootAlias, localeService);
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

		selectClause.append(bestMsgJpql(basename, getSearchPropertyName(),
				false));
		selectClause.append("),");
		selectClause.append(getSearchPropertyName());
		selectClause.append("))");
		selectClause.append(" as ");
		selectClause.append(getAlias());

		return selectClause.toString();
	}

	private String bestMsgJpql(String basename, String code, boolean includeCd) {
		Locale locale = localeService.currentUserLocale();
		Locale defaultLocale = Locale.getDefault();

		StringBuilder selectClause = new StringBuilder("SELECT\n");
		StringBuilder fromClause = new StringBuilder("FROM Message AS msg\n");

		fromClause.append("JOIN msg.internationalisedMessages AS d\n");

		if (includeCd) {
			selectClause.append("msg.messageCd,");
		}

		if (defaultLocale.equals(locale)) {
			selectClause.append("d.messageTx\n");
		} else {
			selectClause.append("CASE\n");
			fromClause
					.append("LEFT JOIN msg.internationalisedMessages AS lang\n");
			fromClause.append("ON lang.localeCd = '");
			fromClause.append(locale.getLanguage());
			fromClause.append("'\n");

			if (locale.getCountry().length() > 0) {
				selectClause.append("WHEN country.messageTx IS NULL\n");
				selectClause.append("THEN\n");
				selectClause.append("CASE\n");
				fromClause
						.append("LEFT JOIN msg.internationalisedMessages AS country\n");
				fromClause.append("ON country.localeCd = '");
				fromClause.append(locale.getLanguage());
				fromClause
						.append(((locale.getCountry().length()) > 0) ? ("_" + locale
								.getCountry()) : "");
				fromClause.append("'\n");
			}

			selectClause.append("WHEN lang.messageTx IS NULL\n");
			selectClause.append("THEN d.messageTx\n");
			selectClause.append("ELSE lang.messageTx\n");

			if (locale.getCountry().length() > 0) {
				selectClause.append("END\n");
				selectClause.append("ELSE country.messageTx\n");
			}

			selectClause.append("END AS messageTx\n");
		}

		fromClause.append("WHERE d.localeCd     = '");
		fromClause.append(defaultLocale.getLanguage());
		fromClause
				.append(((defaultLocale.getCountry().length()) > 0) ? ("_" + defaultLocale
						.getCountry()) : "");
		fromClause.append("'\n");
		fromClause.append("AND msg.baseName     = '");
		fromClause.append(basename);
		fromClause.append("'");

		if (StringUtils.isNotBlank(code)) {
			fromClause.append("\n");
			fromClause.append("AND msg.messageCd = ");
			fromClause.append(code);
		}

		selectClause.append(fromClause);

		return selectClause.toString();
	}
}
