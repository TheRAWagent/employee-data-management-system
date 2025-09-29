package co.dj.employee_data_management.repo;

import co.dj.employee_data_management.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmail(String email);

    // Search by name, email, or position containing text (case-insensitive)
    Page<Employee> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPositionContainingIgnoreCase(String name, String email, String position, Pageable pageable);
}
