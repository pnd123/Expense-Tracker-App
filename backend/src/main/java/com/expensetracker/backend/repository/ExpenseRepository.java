package com.expensetracker.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.expensetracker.backend.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // No need to write anything, Spring handles basic CRUD
}
