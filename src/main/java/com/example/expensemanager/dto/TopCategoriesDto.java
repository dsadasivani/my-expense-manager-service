package com.example.expensemanager.dto;

import java.math.BigDecimal;
import java.util.List;

public record TopCategoriesDto(List<Item> items) {
    public record Item(String category, BigDecimal amount) {}
}
