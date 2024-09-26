package com.sajib_4414.expense.tracker.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;


public class ExpenseDTO implements Serializable {

    @NotNull(message = "Cost is required")
    @Min(value = 1,message = "Please provide a valid cost")
    private int cost;

    @NotNull(message = "category is required")
    @Min(value = 1, message = "Please provide a valid category")
    private int category_id;


    public int getCost() {
        return cost;
    }


    public int getCategory_id() {
        return category_id;
    }
}
