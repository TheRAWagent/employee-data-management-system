package co.dj.employee_data_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull
    @Email(message = "Email should be valid")
    private String email;
    @NotNull
    @NotBlank(message = "Position cannot be empty")
    private String position;
}
