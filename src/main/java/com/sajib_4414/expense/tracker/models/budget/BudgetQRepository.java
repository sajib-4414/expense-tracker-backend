package com.sajib_4414.expense.tracker.models.budget;

import com.sajib_4414.expense.tracker.payload.CategoryWiseBudgetSummary;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetQRepository {
    Budget getBudgetOfMonth( Integer user_id, int month);
    public List<CategoryWiseBudgetSummary> getBudgetSummaryList(Integer userId, int budgetId, int month) ;
}
