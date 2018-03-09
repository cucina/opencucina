package org.cucina.security.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class UsernameValidator
		implements ConstraintValidator<ValidUsername, String> {
	private static final Logger LOG = LoggerFactory.getLogger(UsernameValidator.class);
	@Autowired(required = false)
	private UsernameValidatingPlugin[] usernameValidatingPlugins;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 * @param context  JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		boolean result = false;

		context.disableDefaultConstraintViolation();

		if ((usernameValidatingPlugins != null) && (usernameValidatingPlugins.length > 0)) {
			for (int i = 0; i < usernameValidatingPlugins.length; i++) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Using plugin:" + usernameValidatingPlugins[i]);
				}

				UsernameValidatingPlugin uvp = usernameValidatingPlugins[i];

				result = uvp.isValid(username);

				if (!result) {
					LOG.warn("UserName plugin '" + uvp + " had failed for username '" + username +
							"' with message:" + uvp.message());
					context.buildConstraintViolationWithTemplate(uvp.message())
							.addPropertyNode(username).addConstraintViolation();

					return false;
				}
			}
		}

		return true;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 */
	@Override
	public void initialize(ValidUsername vu) {
	}
}
