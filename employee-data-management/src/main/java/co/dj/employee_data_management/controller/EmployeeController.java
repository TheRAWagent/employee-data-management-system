package co.dj.employee_data_management.controller;

import co.dj.employee_data_management.dto.CreateEmployeeDto;
import co.dj.employee_data_management.dto.EmployeeResponseDto;
import co.dj.employee_data_management.dto.PatchEmployeeDto;
import co.dj.employee_data_management.dto.UpdateEmployeeDto;
import co.dj.employee_data_management.model.Employee;
import co.dj.employee_data_management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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

    @GetMapping
    public Page<EmployeeResponseDto> getEmployees(
            @RequestParam(value = "employeeId", required = false) UUID employeeId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return employeeService.getEmployees(employeeId ,search, page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateEmployeeDto dto) {

        EmployeeResponseDto updated = employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> patchEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody PatchEmployeeDto dto) {

        EmployeeResponseDto updated = employeeService.patchEmployee(id, dto);
        return ResponseEntity.ok(updated);
    }
}
