package de.kaufland.ksilence;

import de.kaufland.ksilence.api.Api;
import de.kaufland.ksilence.model.Contact;
import de.kaufland.ksilence.model.Notification;
import de.kaufland.ksilence.model.OperatingSystem;
import de.kaufland.ksilence.model.Status;
import de.kaufland.ksilence.repository.ContactRepository;
import de.kaufland.ksilence.repository.NotificationRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@TestPropertySource(locations = "classpath:test.properties")
@WebIntegrationTest("server.port:8080")
public class NotificationControllerTest {
    // NOTIFICATION
    private static final long NOT_ID = 1;
    private static final int NOT_TO = 1;
    private static final int NOT_FROM = 2;
    private static final String NOT_BODY = "Test Notification";
    private static final Status NOT_STATE = Status.OPEN;
    // Settings
    private static final String URL = "http://localhost:8080/" + Api.Path.NOTIFICATIONS;
    private static final RestTemplate REST = new TestRestTemplate();

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ContactRepository contactRepository;

    @After
    public void tearDown() {
        notificationRepository.deleteAll();
    }

    @Test
    public void testReadAll(){
        ResponseEntity<Notification[]> response = REST.getForEntity(URL, Notification[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notificationRepository.count(), response.getBody().length);
    }

    @Test
    public void CreateWithEmptyTo(){
        long cntNotifications = notificationRepository.count();

        // To is 0
        Notification newNotification = new Notification();
        newNotification.setId(NOT_ID);
        newNotification.setFromContactId(NOT_FROM);
        newNotification.setToContactId(0);
        newNotification.setBody(NOT_BODY);
        newNotification.setState(NOT_STATE);
        ResponseEntity<Notification> response = REST.postForEntity(URL, newNotification, Notification.class);

        assertEquals("Expecting httpStatus BAD REQUEST", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no Notification added to repository", cntNotifications, notificationRepository.count());
    }

    @Test
    public void CreateWithEmptyFrom(){
        long cntNotifications = notificationRepository.count();

        // To is 0
        Notification newNotification = new Notification();
        newNotification.setId(NOT_ID);
        newNotification.setFromContactId(0);
        newNotification.setToContactId(NOT_TO);
        newNotification.setBody(NOT_BODY);
        newNotification.setState(NOT_STATE);
        ResponseEntity<Notification> response = REST.postForEntity(URL, newNotification, Notification.class);

        assertEquals("Expecting httpStatus BAD REQUEST", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no Notification added to repository", cntNotifications, notificationRepository.count());
    }

    @Test
    public void CreateWithEmptyBody(){
        long cntNotifications = notificationRepository.count();
        Notification newNotification = new Notification();

        // Title is null
        newNotification.setId(NOT_ID);
        newNotification.setBody(null);
        ResponseEntity<Notification> response = REST.postForEntity(URL, newNotification, Notification.class);

        assertEquals("Expecting httpStatus BAD REQUEST", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no Notification added to repository", cntNotifications, notificationRepository.count());

        // Title is empty string
        newNotification.setId(NOT_ID);
        newNotification.setBody("");
        response = REST.postForEntity(URL, newNotification, Notification.class);

        assertEquals("Expecting httpStatus BAD REQUEST", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no Notification added to repository", cntNotifications, notificationRepository.count());
    }

}
