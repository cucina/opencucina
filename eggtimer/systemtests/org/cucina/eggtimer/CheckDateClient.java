package org.cucina.eggtimer;

import java.util.Calendar;
import java.util.Date;

import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertFalse;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CheckDateClient {
    private static final String URL = "http://localhost:8080/checkDate";

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void test() {
        RestTemplate restTemplate = new RestTemplate();
        Boolean result = restTemplate.postForObject(URL, new Date(), Boolean.class);

        assertFalse(result);
        
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -1);
        result = restTemplate.postForObject(URL, cal.getTime(), Boolean.class);

        assertFalse(result);
        
    }
}
