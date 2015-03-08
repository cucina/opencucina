
package org.cucina.search.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.core.model.projection.ExternalProjectionColumn;
import org.cucina.core.model.projection.ExternalProjectionColumns;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.model.projection.ProjectionColumns;
import org.cucina.core.utils.ClassDescriptor;
import org.springframework.util.Assert;


/**
 * Contains seed properties for a search projection
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ProjectionSeed {
    private String aggregate;
    private String alias;
    private String entityAlias;
    private String entityClazz;
    private String property;

    /**
    * Creates a new ProjectionSeed object.
    *
    * @param alias String.
    * @param property String.
    * @param aggregate String non mandatory obviously.
    */
    public ProjectionSeed(String alias, String property, String aggregate) {
        super();
        Assert.notNull(alias, "alias cannot be null");
        Assert.notNull(property, "property cannot be null");
        this.alias = alias;
        this.property = property;
        this.aggregate = aggregate;
    }

    /**
     * Creates a new ProjectionSeed object.
     * @param entityClazz
     * @param entityAlias
     * @param alias String.
     * @param property String.
     * @param aggregate String non mandatory obviously.
     */
    public ProjectionSeed(String entityClazz, String entityAlias, String alias, String property,
        String aggregate) {
        this(alias, property, aggregate);
        Assert.hasText(entityClazz, "entityClazz is required!");
        Assert.hasText(entityAlias, "entityAlias is required!");
        this.entityClazz = entityClazz;
        this.entityAlias = entityAlias;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param clazz JAVADOC.
     *
     * @return JAVADOC.
     */
    public static List<ProjectionSeed> getProjections(Class<?> clazz, Class<?>... requiredGroups) {
        List<ProjectionSeed> result = new ArrayList<ProjectionSeed>();

        ExternalProjectionColumns ann = clazz.getAnnotation(ExternalProjectionColumns.class);

        if ((ann != null) && ClassDescriptor.isInRequiredGroup(requiredGroups, ann.groups())) {
            ExternalProjectionColumn[] pcs = ann.value();

            if (StringUtils.isEmpty(ann.fieldName())) {
                throw new IllegalArgumentException(
                    "ProjectionColumns annotation on class level must have fieldName set");
            }

            for (int j = 0; j < pcs.length; j++) {
                ExternalProjectionColumn epc = pcs[j];

                if (ClassDescriptor.isInRequiredGroup(requiredGroups, epc.groups())) {
                    result.add(new ProjectionSeed(ann.clazz().getSimpleName(), ann.fieldName(),
                            epc.columnName(), epc.property(), epc.aggregate()));
                }
            }
        }

        // field by field
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(clazz);
        String entityClazz = clazz.getSimpleName();
        String entityAlias = StringUtils.uncapitalize(entityClazz);

        for (int i = 0; i < pds.length; i++) {
            Method m = pds[i].getReadMethod();
            ProjectionColumn project = m.getAnnotation(ProjectionColumn.class);

            if ((project != null) &&
                    ClassDescriptor.isInRequiredGroup(requiredGroups, project.groups())) {
                result.add(processAnnotation(entityClazz, entityAlias, pds[i].getName(), project));
            }

            ProjectionColumns projects = m.getAnnotation(ProjectionColumns.class);

            // ignore fieldName on annotation, instead assume the property name
            if ((projects != null) &&
                    ClassDescriptor.isInRequiredGroup(requiredGroups, projects.groups())) {
                ProjectionColumn[] pcs = projects.value();

                for (int j = 0; j < pcs.length; j++) {
                    result.add(processAnnotation(entityClazz, entityAlias, pds[i].getName(), pcs[j]));
                }
            }
        }

        return result;
    }

    /**
     * Get aggregate
     *
     * @return aggregate String.
     */
    public String getAggregate() {
        return aggregate;
    }

    /**
     * Get alias
     *
     * @return alias String.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Set entityAlias
     *
     * @param entityAlias String.
     */
    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    /**
     * Get entity's alias
     *
     * @return entityAlias String.
     */
    public String getEntityAlias() {
        return entityAlias;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getEntityClazz() {
        return entityClazz;
    }

    /**
     * Set property
     *
     * @param property Sring.
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Get property
     *
     * @return property String.
     */
    public String getProperty() {
        return property;
    }

    private static ProjectionSeed processAnnotation(String entityClazz, String entityAlias,
        String fieldname, ProjectionColumn project) {
        String columnName = project.columnName();

        if (StringUtils.isEmpty(columnName)) {
            columnName = fieldname;
        }

        String property = project.property();

        if (StringUtils.isNotEmpty(property)) {
            property = fieldname.concat(".").concat(property);
        } else {
            property = fieldname;
        }

        return new ProjectionSeed(entityClazz, entityAlias, columnName, property,
            project.aggregate());
    }
}
