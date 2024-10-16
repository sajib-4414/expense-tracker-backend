package com.sajib_4414.expense.tracker.payload;

import com.sajib_4414.expense.tracker.models.budget.Budget;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BudgetSummaryBoardDTO {
    private Budget budget;
    private Double total_spent;
    private List<CategoryWiseBudgetSummary> category_wise_budget_summary;

}
