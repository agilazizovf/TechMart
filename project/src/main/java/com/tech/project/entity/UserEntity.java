package com.tech.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private AdminEntity admin;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ProductEntity> products;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_authorities",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<AuthorityEntity> authorities = new HashSet<>();

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
