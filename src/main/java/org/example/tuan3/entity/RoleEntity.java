package org.example.tuan3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.RoleName;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private RoleName name;
}