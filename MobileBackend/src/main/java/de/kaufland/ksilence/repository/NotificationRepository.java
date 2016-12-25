package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.MobileNotification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<MobileNotification, Long>{

    Iterable<MobileNotification> findAll();

    MobileNotification findById(long id);
}
