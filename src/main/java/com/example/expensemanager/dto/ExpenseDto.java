package com.example.expensemanager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExpenseDto(
        UUID id,
        LocalDate date,
        String category,
        String description,
        BigDecimal amount,
        String paymentMethod,
        List<String> tags
) {}
