package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.MobileMessage;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MobileMessage, Long> {

    Iterable<MobileMessage> findAll();

}
