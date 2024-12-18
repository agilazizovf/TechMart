package com.tech.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity admin;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<ProductEntity> products;
}
