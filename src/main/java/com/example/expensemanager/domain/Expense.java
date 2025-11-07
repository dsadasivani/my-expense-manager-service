package com.example.expensemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "expenses", indexes = {
        @Index(name="idx_expense_date", columnList = "date"),
        @Index(name="idx_expense_category", columnList = "category")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Expense {

    @Id @GeneratedValue
    private UUID id;

    @NotNull
    private LocalDate date;

    @NotBlank
    @Column(length = 64)
    private String category; // denormalized name to simplify (also use Category link if you want)

    @Column(length = 120)
    private String description;

    @NotNull @DecimalMin("0.01")
    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(length = 32)
    private String paymentMethod; // UPI, Card, Cash, etc.

    @ElementCollection
    @CollectionTable(name = "expense_tags", joinColumns = @JoinColumn(name = "expense_id"))
    @Column(name="tag", length = 32)
    private List<String> tags = new ArrayList<>();
}
