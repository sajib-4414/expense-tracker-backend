package com.sajib_4414.expense.tracker.models.budget;

import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetItemRepository extends CrudRepository<BudgetItem, Integer> {


    List<BudgetItem> findByBudgetIdAndUser(Integer budgetId, User user);

    BudgetItem findByIdAndUser(Integer id, User user);

    BudgetItem findByCategoryAndBudgetAndUser(Category category, Budget budget, User user);


}
