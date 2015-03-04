package org.cucina.eggtimer;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ScheduleClient
    implements Handler {
    @Autowired
    private ApplicationContext applicationContext;
    private CountDownLatch latch;
    @Autowired
    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ResponseHandler responseHandler;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        responseHandler.setHandler(this);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param response
     *            JAVADOC.
     */
    @Override
    public void handleResponse(String response) {
        System.err.println(response);
        latch.countDown();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testRepeats()
        throws Exception {
        latch = new CountDownLatch(5);

        Request request = new Request();

        request.setDelay(1);
        request.setPeriod(1L);
        request.setDestination("scheduled.call");
        request.setMessage("wake up");
        request.setName("name");

        send(request);
        latch.await();
        // cancel
        request.setDelay(0);
        send(request);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSingle()
        throws Exception {
        latch = new CountDownLatch(1);

        Request request = new Request();

        request.setDelay(5);
        request.setDestination("scheduled.call");
        request.setMessage("wake up");
        request.setName("name");

        send(request);
        latch.await();
    }

    private void send(Object obj)
        throws Exception {
        final String value = objectMapper.writeValueAsString(obj);

        System.err.println("Sending:" + value);
        jmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session)
                    throws JMSException {
                    Message mess = session.createTextMessage(value);

                    return mess;
                }
            });
    }

    public class Request {
        private Long period;
        private String destination;
        private String message;
        private String name;
        private long delay;

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public long getDelay() {
            return delay;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getDestination() {
            return destination;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPeriod(Long period) {
            this.period = period;
        }

        public Long getPeriod() {
            return period;
        }
    }
}
