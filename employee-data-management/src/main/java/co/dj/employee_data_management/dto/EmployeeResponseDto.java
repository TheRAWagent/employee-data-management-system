package co.dj.employee_data_management.dto;

import co.dj.employee_data_management.model.Employee;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeResponseDto {
    private UUID id;
    private String name;
    private String position;
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
