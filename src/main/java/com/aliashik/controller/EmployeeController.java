package com.aliashik.controller;

import com.aliashik.exception.EmployeeNotFoundException;
import com.aliashik.model.Employee;
import com.aliashik.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @GetMapping("/employees")
    public ResponseEntity<?> getEmployees() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee newEmployee) {
        return new ResponseEntity<Employee>(repository.save(newEmployee), HttpStatus.CREATED);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {

        return new ResponseEntity<Employee>(repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found")), HttpStatus.OK);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return new ResponseEntity<Employee>(repository.save(employee), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return new ResponseEntity<Employee>(repository.save(newEmployee), HttpStatus.OK);
                });
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return new ResponseEntity<Employee>(HttpStatus.NO_CONTENT);
    }
}