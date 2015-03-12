package org.cucina.eggtimer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.cucina.eggtimer.service.TemporalRepository;


/**
 * Service to check whether a date is before current date as it is known to the
 * db.
 *
 * @author $Author: vlevine$
 * @version $Revision: $
 */
@RestController
@RequestMapping(value = "/checkDate")
public class CheckDate {
    private TemporalRepository temporalRepository;

    /**
     * Creates a new TemporalControl object.
     *
     * @param temporalRepository
     *            JAVADOC.
     */
    @Autowired
    public CheckDate(TemporalRepository temporalRepository) {
        this.temporalRepository = temporalRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param date
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.POST)
    public boolean checkDate(Date date) {
        return temporalRepository.beforeCurrentDate(date);
    }
}
