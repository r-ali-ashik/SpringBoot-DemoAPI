package com.aliashik.controller;

import com.aliashik.dto.EmployeeDTO;
import com.aliashik.exception.EmployeeNotFoundException;
import com.aliashik.model.Employee;
import com.aliashik.repository.EmployeeRepository;
import io.swagger.annotations.*;
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
@Api(tags = "employee", value = "employee", description = "Employee Resource")
class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @ApiOperation(value = "View a list of available employees", response = Iterable.class, notes = "This api fetches all of the employees, no param is required")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = "/employees", produces = "application/json")
    public ResponseEntity<List<Employee>> getEmployees() {
        log.info("get all employees");
        return new ResponseEntity(repository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Add an Employee", response = Employee.class, notes = "This api creates an Employee, corresponding json is required")
    @PostMapping(value = "/employees", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee> createEmployee(
            @ApiParam(value = "employee json", required = true) @RequestBody EmployeeDTO employeeDTO) {

        log.info("add an employee");
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return new ResponseEntity(repository.save(employee), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Search an employee with an id", response = Employee.class, notes = "This api gets an Employee with employee id")
    @GetMapping(value = "/employees/{id}", produces = "application/json")
    public ResponseEntity<Employee> getEmployee(
            @ApiParam(value = "id", required = true) @PathVariable Long id) {

        log.info("get an employee with id");
        return new ResponseEntity(repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found")), HttpStatus.OK);
    }

    @ApiOperation(value = "Update an Employee", response = Employee.class, notes = "This api updates an Employee")
    @PutMapping(value = "/employees/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee> updateEmployee(
            @ApiParam(value = "employee json", required = true) @RequestBody EmployeeDTO employeeDTO,
            @ApiParam(value = "employee id", required = true) @PathVariable Long id) {

        log.info("update an employee");
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

    @ApiOperation(value = "Delete an Employee", notes = "This api deletes an Employee")
    @DeleteMapping(value = "/employees/{id}", produces = "application/json")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id) {

        log.info("delete an employee");
        repository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}