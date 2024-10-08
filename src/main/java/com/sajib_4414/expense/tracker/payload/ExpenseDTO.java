package com.sajib_4414.expense.tracker.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;


public class ExpenseDTO implements Serializable {

    @NotNull(message = "Cost is required")
    @Min(value = 1,message = "Please provide a valid cost")
    private Integer cost;

    @NotNull(message = "category is required")
    @Min(value = 1, message = "Please provide a valid category")
    private Integer category_id;

    private String notes;

    public String getNotes() {
        return notes;
    }

    public int getCost() {
        return cost;
    }


    public int getCategory_id() {
        return category_id;
    }
}
