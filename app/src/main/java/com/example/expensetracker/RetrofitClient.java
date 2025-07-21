package com.example.expensetracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "http://192.168.193.205:8080/api/";
    private static RetrofitClient instance;
    private final ExpenseApi expenseApi;

    private RetrofitClient() {
        // Define correct date format
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd") // Match backend expected format
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        expenseApi = retrofit.create(ExpenseApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ExpenseApi getExpenseApi() {
        return expenseApi;
    }
}
