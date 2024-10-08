package com.sajib_4414.expense.tracker.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CategoryExpense {
    private final int category_id;
    private final String category_name;
    private final Long category_cost;

    @JsonCreator
    public CategoryExpense(
            @JsonProperty("category_id") int category_id,
            @JsonProperty("category_name") String category_name,
            @JsonProperty("category_cost") Long category_cost
    ) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_cost = category_cost;
    }

    @JsonProperty("category_id")
    public int getCategoryId() {
        return category_id;
    }

    @JsonProperty("category_name")
    public String getCategoryName() {
        return category_name;
    }

    @JsonProperty("category_cost")
    public Long getCategoryCost() {
        return category_cost;
    }
}
