package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long>{

    Iterable<Notification> findAll();

    Notification findById(long id);
}
