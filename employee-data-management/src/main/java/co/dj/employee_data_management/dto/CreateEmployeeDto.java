package co.dj.employee_data_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEmployeeDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "position is required")
    private String position;
    @NotBlank(message = "email is required")
    @Email(message = "Email should be valid")
    private String email;
}
