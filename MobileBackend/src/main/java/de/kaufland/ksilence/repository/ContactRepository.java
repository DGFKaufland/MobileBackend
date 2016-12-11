package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Long>{

    Iterable<Contact> findAll();

    Contact findById(long id);

}
