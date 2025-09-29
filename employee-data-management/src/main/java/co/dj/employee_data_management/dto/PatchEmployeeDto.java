package co.dj.employee_data_management.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class PatchEmployeeDto {
    private String name;

    private String position;

    @Email(message = "Email should be valid")
    private String email;
}
