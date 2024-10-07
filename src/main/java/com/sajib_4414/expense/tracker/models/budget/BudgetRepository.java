package com.sajib_4414.expense.tracker.models.budget;

import com.sajib_4414.expense.tracker.models.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends CrudRepository<Budget, Integer> {
    public List<Budget> findByUser(User user);
    Optional<Budget> findByIdAndUser(Integer id, User user);

    @Query("select b from Budget b LEFT JOIN FETCH b.budgetItemList where b.id=:budgetId and b.user.id=:userId")
    Budget getBudgetWithItems(@Param("budgetId") int budgetId, @Param("userId") int userId);
}
