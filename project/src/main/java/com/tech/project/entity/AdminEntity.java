package com.tech.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "admins")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String phone;
    private String email;
    private LocalDate registerDate;
    private LocalDate updateDate;

    private String username;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private List<CategoryEntity> category;
}
