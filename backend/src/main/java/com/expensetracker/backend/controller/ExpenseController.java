package com.expensetracker.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.repository.ExpenseRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;



@RestController
@CrossOrigin(origins = "*") // Allow all origins for now
@RequestMapping("/api")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // GET all expenses
    @GetMapping("/expenses")
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // ✅ NEW: GET a single expense by ID
    @GetMapping("/expenses/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expenseOpt = expenseRepository.findById(id);
        return expenseOpt.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST new expense
    @PostMapping("/expenses")
    public Expense createExpense(@RequestBody Expense expense) {
    	expense.setId(null);
        return expenseRepository.save(expense);
    }

    // PUT update expense
    @PutMapping("/expenses/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense updatedExpense) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findById(id);
        if (existingExpenseOpt.isPresent()) {
            Expense existingExpense = existingExpenseOpt.get();
            existingExpense.setNote(updatedExpense.getNote());
            existingExpense.setAmount(updatedExpense.getAmount());
            existingExpense.setDate(updatedExpense.getDate());
            existingExpense.setCategory(updatedExpense.getCategory()); // ✅ this line
            Expense savedExpense = expenseRepository.save(existingExpense);
            
            return ResponseEntity.ok(savedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE expense
    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/expenses/export")
    public ResponseEntity<byte[]> exportExpensesToCSV() {
        List<Expense> expenses = expenseRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // CSV header
        writer.println("ID,Note,Amount,Date");

        // CSV rows
        for (Expense e : expenses) {
            writer.println(String.format("%d,%s,%.2f,%s", e.getId(), e.getNote(), e.getAmount(), e.getDate()));
        }

        writer.flush();

        byte[] csvData = out.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
}
