package com.sajib_4414.expense.tracker.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExpenseSummaryDTO {
    private Double totalExpenseThisMonth;
    private Double totalExpenseLastMonth;
    private Integer budgetedExpenseThisMonth;
    private List<CategoryExpense> categoryWiseExpense;
}
