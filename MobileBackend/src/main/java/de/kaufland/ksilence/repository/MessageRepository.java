package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Iterable<Message> findAll();

}
