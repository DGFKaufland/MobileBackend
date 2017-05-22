package de.kaufland.ksilence.api;

import de.kaufland.ksilence.model.MobileNotification;
import de.kaufland.ksilence.service.NotificationService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value=Api.Path.NOTIFICATIONS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<MobileNotification>> readAll(){
        return new ResponseEntity<Iterable<MobileNotification>>(notificationService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.NOTIFICATIONS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MobileNotification> create(@RequestBody MobileNotification pNotification) throws IOException {
        return new ResponseEntity<MobileNotification>(notificationService.create(pNotification), HttpStatus.CREATED);
    }
        
    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.NOTIFICATIONS_FOR_SAMSUNG, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MobileNotification> createSamsungNotification(@RequestBody MobileNotification pNotification) throws IOException {
        return new ResponseEntity<MobileNotification>(notificationService.createSamsungNotification(pNotification), HttpStatus.CREATED);
    }
    
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.TEST, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MobileNotification> test(@RequestBody MobileNotification pNotification) throws IOException {
        return new ResponseEntity<MobileNotification>(notificationService.test(pNotification), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value=Api.Path.NOTIFICATION, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MobileNotification> read(@PathVariable("id") String pNotificationId){
        return new ResponseEntity<MobileNotification>(notificationService.read(pNotificationId), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.NOTIFICATION, method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable("id") String pNotificationId, @RequestBody MobileNotification pNotification) throws IOException {
        notificationService.update(pNotification);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
  

}
