package co.dj.employee_data_management.controller;

import co.dj.employee_data_management.dto.CreateEmployeeDto;
import co.dj.employee_data_management.dto.EmployeeResponseDto;
import co.dj.employee_data_management.model.Employee;
import co.dj.employee_data_management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody CreateEmployeeDto createEmployeeDto) {
        Employee saved = employeeService.createEmployee(createEmployeeDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId()).toUri();

        return ResponseEntity.created(location).body(EmployeeResponseDto.fromEntity(saved));
    }
}
