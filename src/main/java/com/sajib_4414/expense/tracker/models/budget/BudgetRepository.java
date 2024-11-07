package com.sajib_4414.expense.tracker.models.budget;

import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.payload.CategoryWiseBudgetSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends CrudRepository<Budget, Integer>{
    public Page<Budget> findByUser(Pageable page, User user);
    Optional<Budget> findByIdAndUser(Integer id, User user);

    @Query("select b from Budget b LEFT JOIN FETCH b.budgetItemList where b.id=:budgetId and b.user.id=:userId")
    Budget getBudgetWithItems(@Param("budgetId") int budgetId, @Param("userId") int userId);


    @Query("select EXISTS( " +
            "select 1 from Budget b " +
            "where EXTRACT(YEAR from b.budgetPeriod)=:yearNo " +
            "and EXTRACT(MONTH from b.budgetPeriod)=:monthNo " +
            "and b.user.id=:userId)")
    boolean ifBudgetExistForUserAndMonth(@Param("userId") int userId, @Param("monthNo") int monthNo, @Param("yearNo") int yearNo);
}
