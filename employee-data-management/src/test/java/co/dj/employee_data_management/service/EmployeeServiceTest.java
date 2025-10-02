package co.dj.employee_data_management.service;

import co.dj.employee_data_management.dto.CreateEmployeeDto;
import co.dj.employee_data_management.dto.EmployeeResponseDto;
import co.dj.employee_data_management.dto.PatchEmployeeDto;
import co.dj.employee_data_management.dto.UpdateEmployeeDto;
import co.dj.employee_data_management.exception.EmailAlreadyExistsException;
import co.dj.employee_data_management.exception.EmployeeNotFoundException;
import co.dj.employee_data_management.model.Employee;
import co.dj.employee_data_management.repo.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
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
    private Employee emp1;
    private Employee emp2;

    @BeforeEach
    void setUp() {
        dto = new CreateEmployeeDto();
        dto.setName(" Alice ");
        dto.setPosition(" Engineer ");
        dto.setEmail(" Alice@Example.com ");

        emp1 = new Employee();
        emp1.setId(UUID.randomUUID());
        emp1.setName("Alice");
        emp1.setPosition("Engineer");
        emp1.setEmail("alice@example.com");

        emp2 = new Employee();
        emp2.setId(UUID.randomUUID());
        emp2.setName("Bob");
        emp2.setPosition("Manager");
        emp2.setEmail("bob@example.com");
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

    @Test
    void getEmployees_noSearch_defaultsToPage0Size10() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(emp1, emp2), pageable, 2);

        when(employeeRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCase(
                "", "", "", pageable))
                .thenReturn(page);

        Page<EmployeeResponseDto> result = employeeService.getEmployees(null, null, null, null);

        assertEquals(2, result.getTotalElements());
        assertEquals("Alice", result.getContent().get(0).getName());
        assertEquals("Bob", result.getContent().get(1).getName());
        verify(employeeRepository).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCase(
                "", "", "", pageable);
    }

    @Test
    void getEmployees_withSearchAndPagination() {
        String search = "alice";
        int pageNum = 0;
        int pageSize = 5;
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Page<Employee> page = new PageImpl<>(List.of(emp1), pageable, 1);

        when(employeeRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCase(
                search, search, search, pageable))
                .thenReturn(page);

        Page<EmployeeResponseDto> result = employeeService.getEmployees(null, search, pageNum, pageSize);

        assertEquals(1, result.getContent().size());
        assertEquals("Alice", result.getContent().getFirst().getName());
        verify(employeeRepository).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCase(
                search, search, search, pageable);
    }

    @Test
    void getEmployees_byId_found() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("Alice");
        emp.setPosition("Engineer");
        emp.setEmail("alice@example.com");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));

        Page<EmployeeResponseDto> result = employeeService.getEmployees(id, null, null, null);

        assertEquals(1, result.getContent().size());
        assertEquals("Alice", result.getContent().getFirst().getName());
        verify(employeeRepository).findById(id);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void getEmployees_byId_notFound_returnsEmptyPage() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        Page<EmployeeResponseDto> result = employeeService.getEmployees(id, null, null, null);

        assertTrue(result.getContent().isEmpty());
        verify(employeeRepository).findById(id);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void updateEmployee_success() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("Old Name");
        emp.setPosition("Old Position");
        emp.setEmail("old@example.com");

        UpdateEmployeeDto dto = new UpdateEmployeeDto();
        dto.setName("New Name");
        dto.setPosition("New Position");
        dto.setEmail("new@example.com");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));
        when(employeeRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        var updated = employeeService.updateEmployee(id, dto);

        assertEquals("New Name", updated.getName());
        assertEquals("New Position", updated.getPosition());
        assertEquals("new@example.com", updated.getEmail());

        verify(employeeRepository).findById(id);
        verify(employeeRepository).existsByEmail("new@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void updateEmployee_emailAlreadyExists_throwsException() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("Old Name");
        emp.setPosition("Old Position");
        emp.setEmail("old@example.com");

        UpdateEmployeeDto dto = new UpdateEmployeeDto();
        dto.setName("New Name");
        dto.setPosition("New Position");
        dto.setEmail("existing@example.com");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));
        when(employeeRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> employeeService.updateEmployee(id, dto));

        verify(employeeRepository).findById(id);
        verify(employeeRepository).existsByEmail("existing@example.com");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        UpdateEmployeeDto dto = new UpdateEmployeeDto();
        dto.setName("New Name");
        dto.setPosition("New Position");
        dto.setEmail("new@example.com");

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(id, dto));

        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void patchEmployee_updateNameAndEmail_success() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("Old Name");
        emp.setPosition("Old Position");
        emp.setEmail("old@example.com");

        PatchEmployeeDto dto = new PatchEmployeeDto();
        dto.setName("New Name");
        dto.setEmail("new@example.com"); // only name & email updated

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));
        when(employeeRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        var updated = employeeService.patchEmployee(id, dto);

        assertEquals("New Name", updated.getName());
        assertEquals("Old Position", updated.getPosition());
        assertEquals("new@example.com", updated.getEmail());

        verify(employeeRepository).findById(id);
        verify(employeeRepository).existsByEmail("new@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void patchEmployee_emailAlreadyExists_throwsException() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("Old Name");
        emp.setPosition("Old Position");
        emp.setEmail("old@example.com");

        PatchEmployeeDto dto = new PatchEmployeeDto();
        dto.setEmail("existing@example.com");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));
        when(employeeRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> employeeService.patchEmployee(id, dto));

        verify(employeeRepository).findById(id);
        verify(employeeRepository).existsByEmail("existing@example.com");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void patchEmployee_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        PatchEmployeeDto dto = new PatchEmployeeDto();
        dto.setName("New Name");

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.patchEmployee(id, dto));

        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void patchEmployee_noFieldsProvided_returnsUnchanged() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("Old Name");
        emp.setPosition("Old Position");
        emp.setEmail("old@example.com");

        PatchEmployeeDto dto = new PatchEmployeeDto(); // all fields null

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        var updated = employeeService.patchEmployee(id, dto);

        assertEquals("Old Name", updated.getName());
        assertEquals("Old Position", updated.getPosition());
        assertEquals("old@example.com", updated.getEmail());

        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_success() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(emp));

        employeeService.deleteEmployee(id);

        verify(employeeRepository).findById(id);
        verify(employeeRepository).delete(emp);
    }

    @Test
    void deleteEmployee_notFound_throwsException() {
        UUID id = UUID.randomUUID();

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(id));

        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).delete(any());
    }
}