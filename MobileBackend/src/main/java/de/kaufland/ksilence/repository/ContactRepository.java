package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.MobileContact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<MobileContact, Long>{

    Iterable<MobileContact> findAll();

    MobileContact findById(long id);

}
