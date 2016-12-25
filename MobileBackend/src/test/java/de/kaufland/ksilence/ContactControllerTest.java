package de.kaufland.ksilence;

import de.kaufland.ksilence.api.Api;
import de.kaufland.ksilence.model.MobileContact;
import de.kaufland.ksilence.model.OperatingSystem;
import de.kaufland.ksilence.repository.ContactRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@TestPropertySource(locations = "classpath:test.properties")
@WebIntegrationTest("server.port:8080")
public class ContactControllerTest {
    // First Contact
    private static long CONTACT1_ID = 1;
    private static String CONTACT1_NAME = "Max Mustermann";
    private static String CONTACT1_REGISTRATIONTOKEN = "13370000420000";
    private static OperatingSystem CONTACT1_OS = OperatingSystem.ANDROID;
    private static boolean CONTACT1_AVAILABLE = true;
    // Second Contact
    private static long CONTACT2_ID = 2;
    private static String CONTACT2_NAME = "Maria Musterfrau";
    private static String CONTACT2_REGISTRATIONTOKEN = "13370000420001";
    private static OperatingSystem CONTACT2_OS = OperatingSystem.IOS;
    private static boolean CONTACT2_AVAILABLE = true;
    // Settings
    private static final String URL = "http://localhost:8080/" + Api.Path.CONTACTS;
    private static final RestTemplate REST = new TestRestTemplate();

    @Autowired
    ContactRepository contactRepository;

    @Before
    public void setUp() {
        MobileContact newContact = new MobileContact();
        newContact.setName(CONTACT1_NAME);
        newContact.setRegistrationToken(CONTACT1_REGISTRATIONTOKEN);
        newContact.setOs(CONTACT1_OS);
        newContact.setAvailable(CONTACT1_AVAILABLE);
        contactRepository.save(newContact);
    }

    @After
    public void tearDown() {
        contactRepository.deleteAll();
    }

    @Test
    public void testSetup(){
        long cntContacts = contactRepository.count();
        assertTrue("Expecting that at least one contact exists", cntContacts > 0);
    }

    @Test
    public void testCreate(){
        long oldContacts = contactRepository.count();

        MobileContact newContact = new MobileContact();
        newContact.setId(CONTACT2_ID);
        newContact.setName(CONTACT2_NAME);
        newContact.setRegistrationToken(CONTACT2_REGISTRATIONTOKEN);
        newContact.setOs(CONTACT2_OS);
        newContact.setAvailable(CONTACT2_AVAILABLE);
        ResponseEntity<MobileContact> response = REST.postForEntity(URL, newContact, MobileContact.class);

        assertEquals("Expecting that httpStatus CREATED returned", HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Expecting that one contact was added to repository", oldContacts + 1, contactRepository.count());
    }

    @Test
    public void testCreateWithEmptyName(){
        long oldContacts = contactRepository.count();
        MobileContact invalidContact = new MobileContact();

        // Name is null
        invalidContact.setId(CONTACT2_ID);
        invalidContact.setName(null);
        invalidContact.setRegistrationToken(CONTACT2_REGISTRATIONTOKEN);
        invalidContact.setOs(CONTACT2_OS);
        invalidContact.setAvailable(CONTACT2_AVAILABLE);
        ResponseEntity<MobileContact> response = REST.postForEntity(URL, invalidContact, MobileContact.class);

        assertEquals("Expecting that httpStatus BAD REQUEST returned", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no contact was added to repository", oldContacts, contactRepository.count());

        // Name is empty string
        invalidContact.setName("");
        response = REST.postForEntity(URL, invalidContact, MobileContact.class);

        assertEquals("Expecting that httpStatus BAD REQUEST returned", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no contact was added to repository", oldContacts, contactRepository.count());
    }

    @Test
    public void testCreateWithEmptyRegistrationToken(){
        long oldContacts = contactRepository.count();
        MobileContact invalidContact = new MobileContact();

        // Registration Token is null
        invalidContact.setId(CONTACT2_ID);
        invalidContact.setName(CONTACT2_NAME);
        invalidContact.setRegistrationToken(null);
        invalidContact.setOs(CONTACT2_OS);
        invalidContact.setAvailable(CONTACT2_AVAILABLE);
        ResponseEntity<MobileContact> response = REST.postForEntity(URL, invalidContact, MobileContact.class);

        assertEquals("Expecting that httpStatus BAD REQUEST returned", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no contact was added to repository", oldContacts, contactRepository.count());

        // Registration Token is empty string
        invalidContact.setRegistrationToken("");
        response = REST.postForEntity(URL, invalidContact, MobileContact.class);

        assertEquals("Expecting that httpStatus BAD REQUEST returned", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expecting that no contact was added to repository", oldContacts, contactRepository.count());
    }

    @Test
    public void testReadAll(){
        ResponseEntity<MobileContact[]> response = REST.getForEntity(URL, MobileContact[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contactRepository.count(), response.getBody().length);
    }

    @Test
    public void testUpdateWithExistingContact(){
        MobileContact existingContact = contactRepository.findById(CONTACT1_ID);
        long id = existingContact.getId();
        existingContact.setName(CONTACT2_NAME);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MobileContact> entity = new HttpEntity<MobileContact>(existingContact, headers);

        ResponseEntity<MobileContact> response = REST.exchange(URL + "/" + id, HttpMethod.PUT, entity, MobileContact.class, "");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CONTACT2_NAME, contactRepository.findById(id).getName());
    }

    @Test
    public void testUpdateWithNonExistingContact(){
        MobileContact existingContact = contactRepository.findById(CONTACT1_ID);
        long id = existingContact.getId();
        existingContact.setName(CONTACT2_NAME);
        existingContact.setId(192837981273l);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MobileContact> entity = new HttpEntity<MobileContact>(existingContact, headers);

        ResponseEntity<MobileContact> response = REST.exchange(URL + "/" + id, HttpMethod.PUT, entity, MobileContact.class, "");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(CONTACT1_NAME, contactRepository.findById(id).getName());
    }


}
