package de.kaufland.ksilence.api;

import de.kaufland.ksilence.model.MobileMessage;
import de.kaufland.ksilence.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value=Api.Path.MESSAGES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<MobileMessage>> readAll() throws IOException {
        return new ResponseEntity<Iterable<MobileMessage>>(messageService.readAll(), HttpStatus.OK);
    }

}
