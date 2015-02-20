package org.cucina.tools.eclipselink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.cucina.audit.CucinaHistoryPolicy;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.eis.EISDescriptor;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.queries.ContainerPolicy;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.mappings.ForeignReferenceMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.RelationTableMechanism;
import org.eclipse.persistence.mappings.structures.ObjectRelationalDataTypeDescriptor;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.tools.schemaframework.DefaultTableGenerator;
import org.eclipse.persistence.tools.schemaframework.FieldDefinition;
import org.eclipse.persistence.tools.schemaframework.TableCreator;
import org.eclipse.persistence.tools.schemaframework.TableDefinition;

/**
 * Generates the tables required for historization.
 *
 */
public class HistoricTableGenerator extends DefaultTableGenerator {
	/** This is a field JAVADOC */
	public static final String CURRENT_TIME_VAR = "v_curr_time";
	private static final String END_DELIMITER = "\n/";
	private static final String DB2_INIT_SCRIPT = "begin atomic\n"
			+ "declare v_curr_time date default current_date;\n" + "{0}\n"
			+ "end";
	private static final String ORACLE_INIT_SCRIPT = "declare\n"
			+ "v_curr_time date := current_date;\n" + "begin\n" + "{0}\n"
			+ "end;";
	private Project project;
	private StringBuilder historyInitializerScript = new StringBuilder();

	/**
	 * Default constructor.
	 * 
	 * @param project
	 */
	public HistoricTableGenerator(Project project) {
		super(project);
		// Have to store a local version because the one in the parent class is
		// default access.
		this.project = project;
	}

	/**
	 * Ignores the generateFKConstraints as we've generating mirror tables.
	 * 
	 * @param project
	 * @param generateFKConstraints
	 */
	public HistoricTableGenerator(Project project, boolean generateFKConstraints) {
		super(project, false);
		// Have to store a local version because the one in the parent class
		// is default access.
		this.project = project;
	}

	/**
	 * Returns the required sql to initialise the history for the seed data.
	 *
	 * @return
	 */
	public String getHistoryInitializerScript() {
		return getPlatformInitScript().replace("{0}", historyInitializerScript)
				+ END_DELIMITER;
	}

	/**
	 * We're only interested in those items which have a HistoryPolicy
	 *
	 * @see org.eclipse.persistence.tools.schemaframework.DefaultTableGenerator#generateDefaultTableCreator()
	 *
	 * @return JAVADOC.
	 */
	@Override
	public TableCreator generateDefaultTableCreator() {
		TableCreator tblCreator = new TableCreator();

		// go through each descriptor and build the table/field definitions out
		// of mappings
		for (ClassDescriptor descriptor : this.project.getOrderedDescriptors()) {
			if (descriptor.getHistoryPolicy() == null) {
				continue;
			}

			if ((descriptor instanceof XMLDescriptor)
					|| (descriptor instanceof EISDescriptor)
					|| (descriptor instanceof ObjectRelationalDataTypeDescriptor)) {
				// default table generator does not support ox, eis and
				// object-relational descriptor
				AbstractSessionLog.getLog().log(SessionLog.WARNING,
						SessionLog.DDL, "relational_descriptor_support_only",
						(Object[]) null, true);

				return tblCreator;
			}

			// Aggregate descriptors do not contain table/field data and are
			// processed through their owning entities. Aggregate descriptors
			// can not exist on their own.
			// Table per tenant descriptors will not be initialized.
			if (!descriptor.isDescriptorTypeAggregate()
					&& !(descriptor.hasTablePerMultitenantPolicy() && !project
							.allowTablePerMultitenantDDLGeneration())) {
				initTableSchema(descriptor);
			}
		}

		// Post init the schema for relation table and direct collection/map
		// tables, and several special mapping handlings.
		for (ClassDescriptor descriptor : this.project.getOrderedDescriptors()) {
			if (descriptor.getHistoryPolicy() == null) {
				continue;
			}

			// Aggregate descriptors do not contain table/field data and are
			// processed through their owning entities. Aggregate descriptors
			// can not exist on their own.
			// Table per tenant descriptors will not be initialized.
			if (!descriptor.isAggregateDescriptor()
					&& !descriptor.isAggregateCollectionDescriptor()
					&& !(descriptor.hasTablePerMultitenantPolicy() && !project
							.allowTablePerMultitenantDDLGeneration())) {
				postInitTableSchema(descriptor);

				// If VPD descriptor we need to generate some DDL for its
				// default table.
				if (descriptor.hasMultitenantPolicy()) {
					descriptor.getMultitenantPolicy()
							.addToTableDefinition(
									getTableDefFromDBTable(descriptor
											.getDefaultTable()));
				}
			}
		}

		tblCreator.addTableDefinitions(tableMap.values());

		return tblCreator;
	}

	/**
	 * Always set <code>FieldDefinition#unique</code> to false as will have
	 * duplicate rows due to the table storing historical copies of each record.
	 */
	@Override
	protected FieldDefinition getFieldDefFromDBField(DatabaseField dbField) {
		FieldDefinition fieldDef = super.getFieldDefFromDBField(dbField);

		fieldDef.setUnique(false);

		return fieldDef;
	}

	/**
	 * We don't want to create any of the foreign key constraints
	 */
	@Override
	protected void addJoinColumnsFkConstraint(List<DatabaseField> fkFields,
			List<DatabaseField> targetFields, boolean cascadeOnDelete) {
	}

	/**
	 * We don't want any of the normal Unique key constraints because there will
	 * be duplicates as the generated table will hold a copy of each record at a
	 * given point in time.
	 */
	@Override
	protected void addUniqueKeyConstraints(TableDefinition sourceTableDef,
			Map<String, List<List<String>>> uniqueConstraintsMap) {
	}

	/**
	 * Only generate a historic table for the many-to-many join if it has a
	 * <code>HistoryPolicy</code>.
	 */
	@Override
	protected void buildRelationTableDefinition(
			ForeignReferenceMapping mapping,
			RelationTableMechanism relationTableMechanism,
			DatabaseField listOrderField, ContainerPolicy cp) {
		HistoryPolicy hp = ((ManyToManyMapping) mapping).getHistoryPolicy();

		if (hp != null) {
			super.buildRelationTableDefinition(mapping, relationTableMechanism,
					listOrderField, cp);
			historizeDefinition(this.tableMap.get(((ManyToManyMapping) mapping)
					.getRelationTableName()), hp);
		}
	}

	/**
	 * @see org.eclipse.persistence.tools.schemaframework.DefaultTableGenerator#
	 *      initTableSchema(org.eclipse.persistence.descriptors.ClassDescriptor)
	 *
	 * @param descriptor
	 *            JAVADOC.
	 */
	@Override
	protected void initTableSchema(ClassDescriptor descriptor) {
		super.initTableSchema(descriptor);
		historizeDefinition(this.tableMap.get(descriptor.getTableName()),
				descriptor.getHistoryPolicy());
	}

	/**
	 * Returns the database platform specific batch prefix.
	 * 
	 * @return
	 */
	private String getPlatformInitScript() {
		if (databasePlatform.isDB2()) {
			return DB2_INIT_SCRIPT;
		} else if (databasePlatform.isOracle()) {
			return ORACLE_INIT_SCRIPT;
		} else {
			throw new UnsupportedOperationException(databasePlatform.toString()
					+ " is not currently supported");
		}
	}

	/**
	 * Build an Insert statement which can be used to populate the historic
	 * table from the current verion.
	 * 
	 * @param tableName
	 * @param historyTableDefinition
	 */
	private void buildHistoryInitializationScript(
			TableDefinition tableDefinition, String historicName,
			FieldDefinition startField, FieldDefinition auditField) {
		Transformer optimusPrime = new Transformer() {
			@Override
			public Object transform(Object arg0) {
				return ((FieldDefinition) arg0).getName();
			}
		};

		String fields = StringUtils.join(CollectionUtils.collect(
				tableDefinition.getFields(), optimusPrime), ",");

		if (historyInitializerScript.length() > 0) {
			historyInitializerScript.append(System
					.getProperty("line.separator"));
		}

		historyInitializerScript.append("insert into ").append(historicName)
				.append("(").append(fields).append(",")
				.append(startField.getName());

		if (auditField != null) {
			historyInitializerScript.append(",").append(auditField.getName());
		}

		historyInitializerScript.append(") select ").append(fields).append(",")
				.append(CURRENT_TIME_VAR);

		if (auditField != null) {
			historyInitializerScript.append(",").append("'@AUDIT_USERNAME@'");
		}

		historyInitializerScript.append(" from ")
				.append(tableDefinition.getName()).append(";");
	}

	/**
	 * If the descriptor has a HistoryPolicy change the
	 * <code>TableDefinition#name</code> to the
	 * <code>HistoryPolicy#tableName</code> and add the
	 * <code>FieldDefinitions</code> for the start and end dates.
	 * 
	 * @param tableDefinition
	 * @param hp
	 */
	private void historizeDefinition(TableDefinition tableDefinition,
			HistoryPolicy hp) {
		if ((tableDefinition != null)
				&& (hp != null)
				&& !tableDefinition.getName().equals(
						hp.getHistoryTableNames().get(0))) {
			String historicName = hp.getHistoryTableNames().get(0);
			List<FieldDefinition> historicFields = new ArrayList<FieldDefinition>();

			DatabaseField start = hp.getStartFields().get(0);
			DatabaseField end = hp.getEndFields().get(0);

			FieldDefinition startField = new FieldDefinition(start.getName(),
					start.getType());

			startField.setIsPrimaryKey(true);

			FieldDefinition endField = new FieldDefinition(end.getName(),
					end.getType());

			historicFields.add(startField);
			historicFields.add(endField);

			FieldDefinition auditUserField = null;

			if (hp instanceof CucinaHistoryPolicy) {
				auditUserField = new FieldDefinition(((CucinaHistoryPolicy) hp)
						.getAuditUserField().getName(),
						((CucinaHistoryPolicy) hp).getAuditUserField().getType());
				auditUserField.setShouldAllowNull(false);
				historicFields.add(auditUserField);
			}

			buildHistoryInitializationScript(tableDefinition, historicName,
					startField, auditUserField);
			tableDefinition.setName(historicName);

			for (FieldDefinition fieldDefinition : historicFields) {
				tableDefinition.addField(fieldDefinition);
			}
		}
	}
}
