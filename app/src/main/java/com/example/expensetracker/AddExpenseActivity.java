package com.example.expensetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText amountEditText;
    private Spinner categorySpinner;
    private EditText noteEditText;
    private ExpenseDatabase expenseDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Expense");

        // DB init
        expenseDb = new ExpenseDatabase(this);

        // Views
        amountEditText = findViewById(R.id.expense_amount);
        categorySpinner = findViewById(R.id.expense_category);
        noteEditText = findViewById(R.id.expense_note);
        Button saveButton = findViewById(R.id.save_expense_btn);

        // Category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        String amountText = amountEditText.getText().toString();

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float amount = Float.parseFloat(amountText);
            String category = categorySpinner.getSelectedItem().toString();
            String note = noteEditText.getText().toString();

            Expense expense = new Expense();
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setNote(note);
            expense.setDate(new Date());

            // ✅ 1. Save to local SQLite
            expenseDb.insertExpense(expense);

            // ✅ 2. Send to backend via Retrofit
            ExpenseApi expenseApi = RetrofitClient.getInstance().getExpenseApi();
            Call<Expense> call = expenseApi.addExpense(expense);

            call.enqueue(new Callback<Expense>() {
                @Override
                public void onResponse(Call<Expense> call, Response<Expense> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddExpenseActivity.this, "Saved locally & to backend!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddExpenseActivity.this, "Saved locally, but backend failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                    finish(); // Close activity
                }

                @Override
                public void onFailure(Call<Expense> call, Throwable t) {
                    Toast.makeText(AddExpenseActivity.this, "Saved locally, but backend error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    finish(); // Close anyway
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }
}
