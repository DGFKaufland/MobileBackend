package de.kaufland.ksilence.api;

import de.kaufland.ksilence.model.Contact;
import de.kaufland.ksilence.service.ContactService;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

    @Autowired
    ContactService contactService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value=Api.Path.CONTACTS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Contact>> readAll(){
        return new ResponseEntity<Iterable<Contact>>(contactService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.CONTACTS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> create(@RequestBody Contact pContact){
        return new ResponseEntity<Contact>(contactService.create(pContact), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = Api.Path.CONTACT, method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable("id") String pContactId, @RequestBody Contact contact) {
        contactService.update(contact);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }

}
