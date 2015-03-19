package org.cucina.i18n.validation;

import java.util.Collection;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.ListNodeRepository;
import org.cucina.i18n.service.I18nService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniqueListNodeValidator
    implements ConstraintValidator<UniqueListNode, ListNode> {
    private static final Logger LOG = LoggerFactory.getLogger(UniqueListNodeValidator.class);
    @Autowired
    private I18nService i18nService;
    @Autowired
    private ListNodeRepository listNodeRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNode JAVADOC.
     * @param arg1 JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isValid(ListNode listNode, ConstraintValidatorContext arg1) {
        Locale locale = findI18nService().getLocale();
        Message ref = listNode.getLabel();
        String refText = ref.getBestMessage(locale);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Reference text for locale '" + locale + "' is  '" + refText + "'");
        }

        if (refText == null) {
            return true;
        }

        Collection<ListNode> results = findListNodeRepository().findByType(listNode.getType());

        for (ListNode ln : results) {
            Message message = ln.getLabel();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Label: " + message);
            }

            if (message == null) {
                continue;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(message.getBestMessage(locale));
            }

            if (refText.equals(message.getBestMessage(locale))) {
                return false;
            }
        }

        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param uniqueMessageCode JAVADOC.
     */
    @Override
    public void initialize(UniqueListNode uniqueListNode) {
    }

    private I18nService findI18nService() {
        if (null == i18nService) {
            LOG.debug("Failed to autowire, attempting to hotwire byName");
            i18nService = (I18nService) SingletonBeanFactory.getInstance()
                                                            .getBean(I18nService.I18N_SERVICE_ID);

            if (i18nService == null) {
                // this may not bring back the desired result
                LOG.debug("Failed to autowire, attempting to hotwire by class");

                i18nService = SingletonBeanFactory.getInstance().getBean(I18nService.class);
            }

            Assert.notNull(i18nService, "i18nService is null");
        }

        return i18nService;
    }

    private ListNodeRepository findListNodeRepository() {
        if (null == listNodeRepository) {
            LOG.debug("Failed to autowire, attempting to hotwire byName");
            listNodeRepository = (ListNodeRepository) SingletonBeanFactory.getInstance()
                                                                          .getBean(ListNodeRepository.LISTNODE_REPOSITORY_ID);

            if (listNodeRepository == null) {
                // this may not bring back the desired result
                LOG.debug("Failed to autowire, attempting to hotwire by class");

                listNodeRepository = SingletonBeanFactory.getInstance()
                                                         .getBean(ListNodeRepository.class);
            }

            Assert.notNull(listNodeRepository, "listNodeRepository is null");
        }

        return listNodeRepository;
    }
}
