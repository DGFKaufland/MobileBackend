package de.kaufland.ksilence.api;

import de.kaufland.ksilence.model.Notification;
import de.kaufland.ksilence.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value=Api.Path.NOTIFICATIONS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Notification>> readAll(){
        return new ResponseEntity<Iterable<Notification>>(notificationService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.NOTIFICATIONS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> create(@RequestBody Notification pNotification) throws IOException {
        return new ResponseEntity<Notification>(notificationService.create(pNotification), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value=Api.Path.NOTIFICATION, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> read(@PathVariable("id") String pNotificationId){
        return new ResponseEntity<Notification>(notificationService.read(pNotificationId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.NOTIFICATION, method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable("id") String pNotificationId, @RequestBody Notification pNotification) throws IOException {
        notificationService.update(pNotification);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
