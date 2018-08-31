package com.aliashik.controller;

import com.aliashik.dto.EmployeeDTO;
import com.aliashik.exception.EmployeeNotFoundException;
import com.aliashik.model.Employee;
import com.aliashik.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1")
class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        return new ResponseEntity(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return new ResponseEntity(repository.save(employee), HttpStatus.CREATED);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {

        return new ResponseEntity(repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found")), HttpStatus.OK);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long id) {


        return repository.findById(id)
                .map(employee -> {
                    employee.setName(employeeDTO.getName());
                    employee.setRole(employeeDTO.getRole());
                    return new ResponseEntity(repository.save(employee), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Employee employee = new Employee();
                    employee.setId(id);
                    BeanUtils.copyProperties(employeeDTO, employee);
                    return new ResponseEntity(repository.save(employee), HttpStatus.OK);
                });
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}