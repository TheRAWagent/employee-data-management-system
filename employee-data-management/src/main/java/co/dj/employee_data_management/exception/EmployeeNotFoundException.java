package co.dj.employee_data_management.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(UUID id) {
        super("User not found with email: " + id);
    }
}
