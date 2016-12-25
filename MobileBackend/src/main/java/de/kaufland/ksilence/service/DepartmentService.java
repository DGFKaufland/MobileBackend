package de.kaufland.ksilence.service;

import de.kaufland.ksilence.model.MobileDepartment;
import de.kaufland.ksilence.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    DepartmentRepository departmentRepository;

    public Iterable<MobileDepartment> readAll() {
        log.debug("Read all DEPARTMENTS");
        return departmentRepository.findAll();
    }
}
