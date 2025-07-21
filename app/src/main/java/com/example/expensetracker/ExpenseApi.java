package com.example.expensetracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExpenseApi {

    @GET("expenses")
    Call<List<Expense>> getAllExpenses();

    @POST("expenses")
    Call<Expense> addExpense(@Body Expense expense);

    @PUT("expenses/{id}")
    Call<Expense> updateExpense(@Path("id") Long id, @Body Expense expense);

    @DELETE("expenses/{id}")
    Call<Void> deleteExpense(@Path("id") Long id);
}
