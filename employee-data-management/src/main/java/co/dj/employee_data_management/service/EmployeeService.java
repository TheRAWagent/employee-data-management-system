package co.dj.employee_data_management.service;

import co.dj.employee_data_management.dto.CreateEmployeeDto;
import co.dj.employee_data_management.exception.EmailAlreadyExistsException;
import co.dj.employee_data_management.model.Employee;
import co.dj.employee_data_management.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

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
}
