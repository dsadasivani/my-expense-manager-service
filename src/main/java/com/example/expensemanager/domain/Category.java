package com.example.expensemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {

    @Id @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 16)
    private String color; // hex
}
