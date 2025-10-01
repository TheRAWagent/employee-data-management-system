package co.dj.employee_data_management.dto;

import co.dj.employee_data_management.model.Employee;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeResponseDto {
    @NotNull
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String position;
    @NotNull
    private String email;

    public static EmployeeResponseDto fromEntity(Employee e) {
        EmployeeResponseDto d = new EmployeeResponseDto();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setPosition(e.getPosition());
        d.setEmail(e.getEmail());
        return d;
    }
}
