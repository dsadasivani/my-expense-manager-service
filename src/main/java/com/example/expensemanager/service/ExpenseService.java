package com.example.expensemanager.service;

import com.example.expensemanager.domain.Expense;
import com.example.expensemanager.dto.*;
import com.example.expensemanager.repo.ExpenseRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service @Transactional
public class ExpenseService {
    private final ExpenseRepository repo;
    public ExpenseService(ExpenseRepository repo){ this.repo = repo; }

    @Transactional(readOnly = true)
    public PagedResponse<ExpenseDto> list(int page, int size, String sort, String search){
        Sort sortObj = Sort.unsorted();
        if (sort != null && !sort.isBlank()){
            var parts = sort.split(",");
            var by = parts[0];
            var dir = parts.length>1 && "desc".equalsIgnoreCase(parts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortObj = Sort.by(dir, by);
        }
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<Expense> p = repo.search(search, pageable);
        // Map to DTOs INSIDE the TX and COPY tags to plain list
        var dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getTotalElements());
    }
    @Transactional(readOnly = true)
    public ExpenseDto get(UUID id){ return toDto(repo.findById(id).orElseThrow()); }

    public ExpenseDto create(ExpenseDto dto){
        Expense e = fromDto(dto);
        e.setId(null);
        return toDto(repo.save(e));
    }

    public ExpenseDto update(UUID id, ExpenseDto dto){
        Expense e = repo.findById(id).orElseThrow();
        e.setDate(dto.date());
        e.setCategory(dto.category());
        e.setDescription(dto.description());
        e.setAmount(dto.amount());
        e.setPaymentMethod(dto.paymentMethod());
        e.setTags(new ArrayList<>(Optional.ofNullable(dto.tags()).orElse(List.of())));
        return toDto(e);
    }

    public void delete(UUID id){ repo.deleteById(id); }

    public SummaryDto summary(LocalDate from, LocalDate to){
        BigDecimal total = repo.sumBetween(from, to);
        return new SummaryDto(total);
    }

    public TopCategoriesDto topCategories(String month){
        var items = repo.topByMonth(month).stream()
                .map(a -> new TopCategoriesDto.Item((String)a[0], (BigDecimal)a[1]))
                .toList();
        return new TopCategoriesDto(items);
    }

    public TrendDto trend(int months){
        LocalDate start = LocalDate.now().withDayOfMonth(1).minusMonths(months-1L);
        var raw = repo.trendFrom(start);
        Map<String, BigDecimal> map = raw.stream().collect(Collectors.toMap(
                a -> (String)a[0],
                a -> (BigDecimal)a[1]
        ));
        // fill gaps with 0
        List<TrendDto.Point> points = new ArrayList<>();
        for (int i=months-1; i>=0; i--){
            LocalDate d = LocalDate.now().withDayOfMonth(1).minusMonths(i);
            String key = String.format("%d-%02d", d.getYear(), d.getMonthValue());
            points.add(new TrendDto.Point(key, map.getOrDefault(key, BigDecimal.ZERO)));
        }
        return new TrendDto(points);
    }

    private ExpenseDto toDto(Expense e){
        var tagsCopy = new ArrayList<>(Optional.ofNullable(e.getTags()).orElse(List.of()));
        return new ExpenseDto(
                e.getId(), e.getDate(), e.getCategory(), e.getDescription(),
                e.getAmount(), e.getPaymentMethod(), tagsCopy
        );
    }

    private Expense fromDto(ExpenseDto d){
        return Expense.builder()
                .id(d.id()).date(d.date()).category(d.category())
                .description(d.description()).amount(d.amount())
                .paymentMethod(d.paymentMethod())
                .tags(new ArrayList<>(Optional.ofNullable(d.tags()).orElse(List.of())))
                .build();
    }
}
