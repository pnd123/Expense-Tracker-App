package com.expensetracker.backend.model;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;

    private double amount;

    private Date date;

    private String category; // ✅ Add this line

    // Constructors
    public Expense() {}

    public Expense(String note, double amount, Date date, String category) {
        this.note = note;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getCategory() { return category; }  // ✅ Add this
    public void setCategory(String category) { this.category = category; }  // ✅ Add this
}
