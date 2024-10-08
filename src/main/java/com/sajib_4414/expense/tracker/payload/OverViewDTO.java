package com.sajib_4414.expense.tracker.payload;

import com.sajib_4414.expense.tracker.models.expense.Expense;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OverViewDTO {
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
    private List<CategoryExpense> topCategoryExpense;

}
