package de.kaufland.ksilence.api;

import de.kaufland.ksilence.model.MobileDepartment;
import de.kaufland.ksilence.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value=Api.Path.DEPARTMENTS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<MobileDepartment>> readAll() throws IOException {
        return new ResponseEntity<Iterable<MobileDepartment>>(departmentService.readAll(), HttpStatus.OK);
    }

}
