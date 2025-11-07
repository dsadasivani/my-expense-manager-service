package com.example.expensemanager.web;

import com.example.expensemanager.domain.Category;
import com.example.expensemanager.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService svc;
    public CategoryController(CategoryService svc){ this.svc = svc; }

    @GetMapping public List<Category> list(){ return svc.list(); }

    @PostMapping public ResponseEntity<Category> create(@Valid @RequestBody Category c){
        return ResponseEntity.ok(svc.create(c));
    }

    @PutMapping("/{id}") public ResponseEntity<Category> update(@PathVariable UUID id, @Valid @RequestBody Category c){
        return ResponseEntity.ok(svc.update(id, c));
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable UUID id){ svc.delete(id); }
}
