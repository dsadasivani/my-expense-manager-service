package com.example.expensemanager.service;

import com.example.expensemanager.domain.Category;
import com.example.expensemanager.repo.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service @Transactional
public class CategoryService {
    private final CategoryRepository repo;
    public CategoryService(CategoryRepository repo){ this.repo = repo; }

    public List<Category> list(){ return repo.findAll(); }
    public Category create(Category c){
        if (repo.existsByNameIgnoreCase(c.getName()))
            throw new IllegalArgumentException("Category already exists");
        return repo.save(c);
    }
    public Category update(UUID id, Category payload){
        var c = repo.findById(id).orElseThrow();
        if (!c.getName().equalsIgnoreCase(payload.getName())
                && repo.existsByNameIgnoreCase(payload.getName())){
            throw new IllegalArgumentException("Category already exists");
        }
        c.setName(payload.getName());
        c.setColor(payload.getColor());
        return c;
    }
    public void delete(UUID id){ repo.deleteById(id); }
}
