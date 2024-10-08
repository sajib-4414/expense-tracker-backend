package com.sajib_4414.expense.tracker.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryExpense {
    private final int category_id;
    private final String category_name;
    private final Long category_cost;

}
