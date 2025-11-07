package com.example.expensemanager.dto;

import java.util.List;

public record PagedResponse<T>(List<T> data, long total) {}
