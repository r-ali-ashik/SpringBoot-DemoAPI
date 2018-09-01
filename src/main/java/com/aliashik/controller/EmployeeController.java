package com.aliashik.controller;

import com.aliashik.dto.EmployeeDTO;
import com.aliashik.exception.EmployeeNotFoundException;
import com.aliashik.model.Employee;
import com.aliashik.repository.EmployeeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "Employee", description = "Employee Resource", value = "employee")
class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @ApiOperation(value = "Get employees", notes = "This api fetches all of the employees, no param is required")
    @GetMapping(value = "/employees", produces = "application/json")
    public ResponseEntity<List<Employee>> getEmployees() {
        return new ResponseEntity(repository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create an Employee", notes = "This api creates an Employee, corresponding json is required")
    @PostMapping(value = "/employees", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee> createEmployee(
            @ApiParam(value = "employee json", required = true) @RequestBody EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return new ResponseEntity(repository.save(employee), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get Employee wit id", notes = "This api gets an Employee with employee id")
    @GetMapping(value = "/employees/{id}", produces = "application/json")
    public ResponseEntity<Employee> getEmployee(
            @ApiParam(value = "id", required = true) @PathVariable Long id) {

        return new ResponseEntity(repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found")), HttpStatus.OK);
    }

    @PutMapping(value = "/employees/{id}", consumes = "application/json", produces = "application/json")
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

    @DeleteMapping(value = "/employees/{id}", produces = "application/json")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}