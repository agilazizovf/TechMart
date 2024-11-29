package com.tech.project.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRequest {

    @NotEmpty(message = "Name must be not empty")
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    @Column(columnDefinition = "VARCHAR(15)")
    private String name;

    @NotEmpty(message = "Surname must be not empty")
    @Size(min = 3, max = 15, message = "Surname must be between 3 and 15 characters")
    @Column(columnDefinition = "VARCHAR(15)")
    private String surname;

    @NotEmpty(message = "Phone must be not empty")
    @Pattern(regexp = "^(\\+994|0)(50|51|55|70|77|99)\\d{7}$")
    @Column(columnDefinition = "VARCHAR(20)")
    @Size(min = 10, max = 20, message = "Name must be between 10 and 20 characters")
    private String phone;

    @NotEmpty(message = "Email must be not empty")
    @Size(min = 10, max = 15, message = "Email must be between 10 and 15 characters")
    @Column(columnDefinition = "VARCHAR(15)")
    private String email;

    @NotEmpty(message = "Password must be not empty")
    @Size(min = 4, max = 15, message = "Password must be between 3 and 15 characters")
    @Column(columnDefinition = "VARCHAR(15)")
    private String password;

    @NotEmpty(message = "Username must be not empty")
    @Size(min = 1, max = 15, message = "Username must be between 1 and 15 characters")
    @Column(columnDefinition = "VARCHAR(15)")
    private String username;
}
