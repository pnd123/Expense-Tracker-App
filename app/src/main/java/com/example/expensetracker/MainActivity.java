package com.example.expensetracker;

import com.example.expensetracker.Adapter.ExpenseAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;

    private PieChart pieChart;
    private ExpenseDatabase expenseDb;
    private SharedPreferences prefs;
    private String currencySymbol;
    private TextView balanceTextView;

    private static final String PREF_BUDGET = "budget";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Theme setup first
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String theme = prefs.getString("theme", "light");
        if ("dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView); // Make sure your layout has this ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expenseDb = new ExpenseDatabase(this); 
        
        fetchExpensesFromBackend();

       
        pieChart = findViewById(R.id.expense_chart);
        balanceTextView = findViewById(R.id.balance_text);
        currencySymbol = prefs.getString("currency", "₹");

        balanceTextView.setOnClickListener(v -> showEditBudgetDialog());

        FloatingActionButton addExpenseButton = findViewById(R.id.add_expense_btn);
        addExpenseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });

        FloatingActionButton viewExpenseButton = findViewById(R.id.view_expense_btn);
        viewExpenseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewExpensesActivity.class);
            startActivity(intent);
        });
    }

    private void loadFromLocalDb() {
    List<Expense> localExpenses = expenseDb.getAllExpenses();
    if (!localExpenses.isEmpty()) {
        expenseAdapter = new ExpenseAdapter(localExpenses);
        recyclerView.setAdapter(expenseAdapter);
        updateChartAndBalance();  // Update PieChart and Balance
    } else {
        Toast.makeText(this, "No local data found", Toast.LENGTH_SHORT).show();
    }
}



   private void fetchExpensesFromBackend() {
    if (InternetUtil.isInternetAvailable(this)) {
        // ✅ Online: Fetch from API
        ExpenseApi expenseApi = RetrofitClient.getInstance().getExpenseApi();
        Call<List<Expense>> call = expenseApi.getAllExpenses();

        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Expense> expenses = response.body();

                    expenseDb.clearAllExpenses(); // avoid duplicates
                    for (Expense e : expenses) {
                        if (e.getCategory() != null && !e.getCategory().isEmpty()) {
                            expenseDb.insertExpense(e);
                        }
                    }

                    expenseAdapter = new ExpenseAdapter(expenseDb.getAllExpenses());
                    recyclerView.setAdapter(expenseAdapter);
                    updateChartAndBalance();

                } else {
                    Toast.makeText(MainActivity.this, "No valid data from server", Toast.LENGTH_SHORT).show();
                    loadFromLocalDb(); // ✅ fallback to local if response is empty
                }
            }


            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadFromLocalDb();  // 🔁 fallback to SQLite
            }
        });
    } else {
        // 🔌 Offline: Use local SQLite
        Toast.makeText(MainActivity.this, "Offline: loading from local DB", Toast.LENGTH_SHORT).show();
        loadFromLocalDb();
    }
}

    @Override
    protected void onResume() {
    super.onResume();
    fetchExpensesFromBackend(); // this will update chart after response
}


    private void updateChartAndBalance() {
        List<Expense> expenses = expenseDb.getAllExpenses();
        Map<String, Float> categoryTotals = new HashMap<>();

        float totalExpenses = 0f;

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            Float currentTotal = categoryTotals.get(category);
            if (currentTotal == null) currentTotal = 0f;
            categoryTotals.put(category, currentTotal + expense.getAmount());
            totalExpenses += expense.getAmount();
        }

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(24f);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.animateY(1000);
        pieChart.invalidate();

        float budget = prefs.getFloat(PREF_BUDGET, 0f);
        float availableBalance = budget - totalExpenses;

        String balanceText = String.format(Locale.getDefault(), "Balance: %s%.2f", currencySymbol, availableBalance);
        balanceTextView.setText(balanceText);

        if (availableBalance >= 0) {
            balanceTextView.setTextColor(Color.parseColor("#388E3C"));
        } else {
            balanceTextView.setTextColor(Color.parseColor("#D32F2F"));
        }
    }

    private void showEditBudgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Budget");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        float currentBudget = prefs.getFloat(PREF_BUDGET, 0f);
        input.setText(String.format(Locale.getDefault(), "%.2f", currentBudget));
        input.setSelection(input.getText().length());

        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            try {
                float newBudget = Float.parseFloat(input.getText().toString());
                prefs.edit().putFloat(PREF_BUDGET, newBudget).apply();
                Toast.makeText(this, "Budget updated!", Toast.LENGTH_SHORT).show();
                updateChartAndBalance();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        } else if (id == R.id.action_contact) {
            startActivity(new Intent(this, ContactActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
