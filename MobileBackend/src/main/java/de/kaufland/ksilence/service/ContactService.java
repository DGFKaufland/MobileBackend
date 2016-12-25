package de.kaufland.ksilence.service;

import de.kaufland.ksilence.exception.EmptyParameterException;
import de.kaufland.ksilence.exception.EntityExistsException;
import de.kaufland.ksilence.exception.EntityNotFoundException;
import de.kaufland.ksilence.model.MobileContact;
import de.kaufland.ksilence.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    ContactRepository contactRepository;

    public Iterable<MobileContact> readAll() {
        log.debug("Read all CONTACTS");
        return contactRepository.findAll();
    }

    public MobileContact create(MobileContact pContact) {
        log.debug("Create CONTACT: " + pContact);
        if(pContact.getName() == null || pContact.getName().isEmpty() || pContact.getName().equals("")){
            throw new EmptyParameterException("Name");
        } else if (pContact.getRegistrationToken() == null || pContact.getRegistrationToken().isEmpty() || pContact.getRegistrationToken().equals("")){
            throw new EmptyParameterException("Registration Token");
        } else if (contactRepository.findById(pContact.getId()) != null){
            throw new EntityExistsException();
        }
        return contactRepository.save(pContact);
    }

    public void update(MobileContact contact) {
        log.debug("Update CONTACT: (" + contact.getId() + ")");
        MobileContact existingContact = contactRepository.findById(contact.getId());

        if (existingContact == null) {
            throw new EntityNotFoundException();
        } else if (contact.getName() == null || contact.getName().isEmpty()) {
            throw new EmptyParameterException("Name");
        } else if (contact.getRegistrationToken() == null || contact.getRegistrationToken().isEmpty()) {
            throw new EmptyParameterException("Registration Token");
        }

        log.debug("From: " + existingContact);
        MobileContact updatedContact = contactRepository.save(contact);
        log.debug("To:   " + updatedContact);
    }

}
