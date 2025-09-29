package co.dj.employee_data_management.service;

import co.dj.employee_data_management.dto.CreateEmployeeDto;
import co.dj.employee_data_management.exception.EmailAlreadyExistsException;
import co.dj.employee_data_management.model.Employee;
import co.dj.employee_data_management.repo.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private CreateEmployeeDto dto;

    @BeforeEach
    void setUp() {
        dto = new CreateEmployeeDto();
        dto.setName(" Alice ");
        dto.setPosition(" Engineer ");
        dto.setEmail(" Alice@Example.com ");
    }

    @Test
    void createEmployee_success() {
        // Arrange
        when(employeeRepository.existsByEmail("alice@example.com")).thenReturn(false);

        Employee saved = new Employee();
        saved.setId(UUID.randomUUID());
        saved.setName("Alice");
        saved.setPosition("Engineer");
        saved.setEmail("alice@example.com");

        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        // Act
        Employee result = employeeService.createEmployee(dto);

        // Assert
        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        assertEquals("Alice", result.getName());
        assertEquals("Engineer", result.getPosition());
        assertEquals("alice@example.com", result.getEmail());

        verify(employeeRepository).existsByEmail("alice@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_emailAlreadyExists_throwsException() {
        // Arrange
        when(employeeRepository.existsByEmail("alice@example.com")).thenReturn(true);

        // Act + Assert
        EmailAlreadyExistsException ex = assertThrows(EmailAlreadyExistsException.class, () -> employeeService.createEmployee(dto));

        assertEquals("Email already in use: alice@example.com", ex.getMessage());
        verify(employeeRepository).existsByEmail("alice@example.com");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void createEmployee_trimsAndNormalizesInput() {
        // Arrange
        when(employeeRepository.existsByEmail("alice@example.com")).thenReturn(false);

        UUID id = UUID.randomUUID();
        Employee saved = new Employee();
        saved.setId(id);
        saved.setName("Alice");
        saved.setPosition("Engineer");
        saved.setEmail("alice@example.com");

        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        // Act
        Employee result = employeeService.createEmployee(dto);

        // Capture argument
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());

        Employee captured = captor.getValue();

        // Assert normalization BEFORE save
        assertEquals("Alice", captured.getName());
        assertEquals("Engineer", captured.getPosition());
        assertEquals("alice@example.com", captured.getEmail());

        // Assert returned entity
        assertEquals(id, result.getId());
    }
}