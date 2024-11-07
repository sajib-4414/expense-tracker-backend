package com.sajib_4414.expense.tracker.payload;

import lombok.Data;

@Data
public class CategoryWiseBudgetSummary {
    private int budget_item_id;
    private int max_spend;
    private int warning_spend;
    private int category_id;
    private String category_name;
    private Long total_expense;
    private boolean unbudgeted;

    public CategoryWiseBudgetSummary(int budget_item_id, int max_spend, int warning_spend, int category_id, String category_name,Long total_expense ) {
        this.budget_item_id = budget_item_id;
        this.max_spend = max_spend;
        this.warning_spend = warning_spend;
        this.category_id = category_id;
        this.category_name = category_name;
        this.total_expense = total_expense;
        this.unbudgeted = false;
    }
    public CategoryWiseBudgetSummary(int budget_item_id, int max_spend, int warning_spend, int category_id, String category_name,Long total_expense, boolean unbudgeted ) {
        this.budget_item_id = budget_item_id;
        this.max_spend = max_spend;
        this.warning_spend = warning_spend;
        this.category_id = category_id;
        this.category_name = category_name;
        this.total_expense = total_expense;
        this.unbudgeted = true;
    }
}
