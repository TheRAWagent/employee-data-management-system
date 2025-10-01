package co.dj.employee_data_management.service;

import co.dj.employee_data_management.dto.CreateEmployeeDto;
import co.dj.employee_data_management.dto.EmployeeResponseDto;
import co.dj.employee_data_management.dto.PatchEmployeeDto;
import co.dj.employee_data_management.dto.UpdateEmployeeDto;
import co.dj.employee_data_management.exception.EmailAlreadyExistsException;
import co.dj.employee_data_management.exception.EmployeeNotFoundException;
import co.dj.employee_data_management.model.Employee;
import co.dj.employee_data_management.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    @Transactional
    public Employee createEmployee(CreateEmployeeDto createEmployeeDto) {
        String normalizedEmail = normalize(createEmployeeDto.getEmail());

        if (employeeRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        Employee employee = new Employee();
        employee.setEmail(normalize(createEmployeeDto.getEmail()));
        employee.setName(createEmployeeDto.getName().trim());
        employee.setPosition(createEmployeeDto.getPosition().trim());
        return employeeRepository.save(employee);
    }

    public Page<EmployeeResponseDto> getEmployees(UUID employeeId, String search, Integer page, Integer size) {
        // If employeeId is provided, fetch only that employee
        if (employeeId != null) {
            return employeeRepository.findById(employeeId)
                    .map(emp -> new PageImpl(List.of(EmployeeResponseDto.fromEntity(emp))))
                    .orElseGet(() -> new PageImpl<>(List.of())); // empty page if not found
        }


        int pageNumber = page != null && page > 0 ? page-1 : 0;
        int pageSize = size != null && size > 0 ? size : 10;

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        String searchTerm = search != null ? search.trim() : "";

        return employeeRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCase(
                        searchTerm, searchTerm, searchTerm, pageRequest)
                .map(EmployeeResponseDto::fromEntity);
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(UUID employeeId, UpdateEmployeeDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        String normalizedEmail = dto.getEmail().trim().toLowerCase(Locale.ROOT);

        // If email changed, check uniqueness
        if (!employee.getEmail().equals(normalizedEmail)
                && employeeRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        // Update fields
        employee.setName(dto.getName().trim());
        employee.setPosition(dto.getPosition().trim());
        employee.setEmail(normalizedEmail);

        Employee updated = employeeRepository.save(employee);
        return EmployeeResponseDto.fromEntity(updated);
    }

    @Transactional
    public EmployeeResponseDto patchEmployee(UUID employeeId, PatchEmployeeDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        // Update only non-null fields
        if (dto.getName() != null) {
            employee.setName(dto.getName().trim());
        }
        if (dto.getPosition() != null) {
            employee.setPosition(dto.getPosition().trim());
        }
        if (dto.getEmail() != null) {
            String normalizedEmail = dto.getEmail().trim().toLowerCase(Locale.ROOT);
            if (!employee.getEmail().equals(normalizedEmail)
                    && employeeRepository.existsByEmail(normalizedEmail)) {
                throw new EmailAlreadyExistsException(normalizedEmail);
            }
            employee.setEmail(normalizedEmail);
        }

        Employee updated = employeeRepository.save(employee);
        return EmployeeResponseDto.fromEntity(updated);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        employeeRepository.delete(employee);
    }
}
