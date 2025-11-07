package com.example.expensemanager.web;

import com.example.expensemanager.dto.*;
import com.example.expensemanager.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService svc;
    public ExpenseController(ExpenseService svc){ this.svc = svc; }

    @GetMapping
    public PagedResponse<ExpenseDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date,desc") String sort,
            @RequestParam(required = false) String search){
        return svc.list(page, size, sort, search);
    }

    @GetMapping("/{id}") public ExpenseDto get(@PathVariable UUID id){ return svc.get(id); }

    @PostMapping public ResponseEntity<ExpenseDto> create(@Valid @RequestBody ExpenseDto dto){
        return ResponseEntity.ok(svc.create(dto));
    }

    @PutMapping("/{id}") public ResponseEntity<ExpenseDto> update(@PathVariable UUID id, @Valid @RequestBody ExpenseDto dto){
        return ResponseEntity.ok(svc.update(id, dto));
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable UUID id){ svc.delete(id); }

    // Summary endpoints for Dashboard
    @GetMapping("/summary")
    public SummaryDto summary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
        return svc.summary(from, to);
    }

    @GetMapping("/top-categories")
    public TopCategoriesDto topCategories(@RequestParam String month){ // YYYY-MM
        return svc.topCategories(month);
    }

    @GetMapping("/trend")
    public TrendDto trend(@RequestParam(defaultValue = "6") int months){
        return svc.trend(Math.max(1, months));
    }
}
