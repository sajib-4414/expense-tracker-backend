package com.sajib_4414.expense.tracker.models.budget;

import org.springframework.data.repository.query.Param;

public interface BudgetQRepository {
    Budget getBudgetOfMonth( Integer user_id, int month);
}
