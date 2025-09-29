package co.dj.employee_data_management.service;

import co.dj.employee_data_management.dto.CreateEmployeeDto;
import co.dj.employee_data_management.dto.EmployeeResponseDto;
import co.dj.employee_data_management.exception.EmailAlreadyExistsException;
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


        int pageNumber = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 ? size : 10;

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        String searchTerm = search != null ? search.trim() : "";

        return employeeRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCase(
                        searchTerm, searchTerm, searchTerm, pageRequest)
                .map(EmployeeResponseDto::fromEntity);
    }
}
