package org.cucina.core.validation;

import java.util.Locale;
import java.util.Map;

import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;


/**
 * This interpolator allows to bind actual bean's properties values to the
 * validation failure messages. It expects for the constraint to have property
 * <code>properties</code> which contains array of property names. Values of the
 * bean's properties will be added to the contraintDescriptor's attributes
 * against the index of the array.
 *
 * So for message
 *
 * <pre>Hello {0} and your {1}
 *
 * <pre/>
 *
 * and constraint's 'properties' set to
 *
 * <pre>
 * properties= {&quot;name&quot;, &quot;sibling&quot;}
 * </pre>
 *
 * with a bean having properties accordingly set as <code>
 *  String name="Jack";
 *  String sibling="sister";
 *  </code>
 *
 * it'll produce <code>Hello Jack and your sister</code>
 *
 * If the constraint does not have field <code>properties</code> this
 * interpolator will bind the actual validated value to the key <code>0</code>
 *
 * It does not work if the implementation of javax.validation is hibernate.
 *
 * @author $Author: vlevine
 * @version $Revision: $
 */
public class BeanMessageInterpolator
    implements MessageInterpolator {
    private MessageInterpolator delegate;

    /**
     * Creates a new BeanMessageInterpolator object. The nested delegator is
     * pulled from the bootstrap of the validation provider
     *
     */
    public BeanMessageInterpolator() {
        @SuppressWarnings("rawtypes")
        Configuration configuration = Validation.byDefaultProvider().configure();

        delegate = configuration.getDefaultMessageInterpolator();
    }

    /**
     * Creates a new BeanMessageInterpolator object with provided delegator.
     *
     * @param delegate
     *            JAVADOC.
     */
    public BeanMessageInterpolator(MessageInterpolator delegate) {
        Assert.notNull(delegate, "delegate is null");
        this.delegate = delegate;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message
     *            JAVADOC.
     * @param context
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String interpolate(String message, Context context) {
        processAttributes(context);

        return delegate.interpolate(message, context);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message
     *            JAVADOC.
     * @param context
     *            JAVADOC.
     * @param locale
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String interpolate(String message, Context context, Locale locale) {
        processAttributes(context);

        return delegate.interpolate(message, context, locale);
    }

    private void processAttributes(Context context) {
        Map<String, Object> attributes = context.getConstraintDescriptor().getAttributes();

        // TODO determine whether attributes are modifiable
        if (attributes.containsKey("properties")) {
            Object bean = context.getValidatedValue();
            BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

            String[] props = (String[]) attributes.get("properties");

            for (int i = 0; i < props.length; i++) {
                if (beanWrapper.isReadableProperty(props[i])) {
                    Object property = beanWrapper.getPropertyValue(props[i]);

                    attributes.put(String.valueOf(i),
                        (property == null) ? null : property.toString());
                }
            }
        } else {
            attributes.put("0", context.getValidatedValue());
        }
    }
}
