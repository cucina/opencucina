package org.cucina.eggtimer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@RestController
public class Delay {
    private static final Logger LOG = LoggerFactory.getLogger(Delay.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param time JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping("/delay")
    boolean delay(@RequestParam
    long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOG.error("Oops", e);

            return false;
        }

        return true;
    }
}
