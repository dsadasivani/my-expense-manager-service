package com.example.expensemanager.dto;

import java.math.BigDecimal;
import java.util.List;

public record TrendDto(List<Point> points) {
    public record Point(String month, BigDecimal total) {}
}
