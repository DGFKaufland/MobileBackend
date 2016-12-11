package de.kaufland.ksilence;

import de.kaufland.ksilence.api.Api;
import de.kaufland.ksilence.model.Message;
import de.kaufland.ksilence.repository.MessageRepository;
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
public class MessageControllerTest {
    // Settings
    private static final String URL = "http://localhost:8080/" + Api.Path.MESSAGES;
    private static final RestTemplate REST = new TestRestTemplate();

    @Autowired
    MessageRepository messageRepository;

    @Test
    public void testReadAll(){
        ResponseEntity<Message[]> response = REST.getForEntity(URL, Message[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messageRepository.count(), response.getBody().length);
    }

}
