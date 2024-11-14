package com.sajib_4414.expense.tracker.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


public class ExpenseDTO implements Serializable {

    @NotNull(message = "Cost is required")
    @Min(value = 1,message = "Please provide a valid cost")
    private Integer cost;

//    @NotNull(message = "category is required")

    private Integer category_id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Date is required")
    private Date dateTime;

    private String notes;

    public String getNotes() {
        return notes;
    }

    public int getCost() {
        return cost;
    }


    public Integer getCategory_id() {
        return category_id;
    }

    public @NotNull(message = "Date is required") Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NotNull(message = "Date is required") Date dateTime) {
        this.dateTime = dateTime;
    }
}
