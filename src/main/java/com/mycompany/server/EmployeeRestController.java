package com.mycompany.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("employees")
public class EmployeeRestController {
    private final EmployeeRepository repository;
    private final Logger logger = LoggerFactory.getLogger(EmployeeRestController.class);


    @Autowired
    EmployeeRestController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Employee>> all(@RequestParam(value = "text", required = false) String containingText) {
        final List<Employee> employees = repository.findAll();
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(employees);
    }

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<String> addEmployee(@RequestBody Employee newEmployee) {

        repository.save(newEmployee);
        logger.info("employee saved:" + newEmployee.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteEmployee(@RequestBody Employee employee) {
        repository.delete(employee);
        logger.info("employee deleted:" + employee.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
