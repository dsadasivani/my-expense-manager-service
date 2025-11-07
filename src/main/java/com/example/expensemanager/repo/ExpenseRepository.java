package com.example.expensemanager.repo;

import com.example.expensemanager.domain.Expense;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID>, JpaSpecificationExecutor<Expense> {

    @Query("""
    select coalesce(sum(e.amount),0) from Expense e
    where e.date between :from and :to
  """)
    BigDecimal sumBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
    select e.category as category, coalesce(sum(e.amount),0) as total
    from Expense e
    where to_char(e.date,'YYYY-MM') = :month
    group by e.category
    order by total desc
  """)
    List<Object[]> topByMonth(@Param("month") String month);

    @Query("""
    select to_char(e.date,'YYYY-MM') as ym, coalesce(sum(e.amount),0) as total
    from Expense e
    where e.date >= :start
    group by to_char(e.date,'YYYY-MM')
    order by ym asc
  """)
    List<Object[]> trendFrom(@Param("start") LocalDate start);

    default Page<Expense> search(String search, Pageable pageable) {
        if (search == null || search.isBlank()) return findAll(pageable);
        return findAll((root, q, cb) -> {
            var like = "%" + search.toLowerCase() + "%";
            var cat  = cb.like(cb.lower(root.get("category")), like);
            var desc = cb.like(cb.lower(root.get("description")), like);
            // tags join (optional)
            ListJoin<Expense, String> tagsJoin = root.joinList("tags", JoinType.LEFT);
            Expression<String> tagExpr = tagsJoin.as(String.class);
            var tag  = cb.like(cb.lower(tagExpr), like);
            q.distinct(true);
            return cb.or(cat, desc, tag);
        }, pageable);
    }
}
