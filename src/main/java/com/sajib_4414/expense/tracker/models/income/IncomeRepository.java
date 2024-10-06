package com.sajib_4414.expense.tracker.models.income;

import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeRepository extends CrudRepository<Income, Integer> {
    List<Income> findByUser(User user);

    @Query(value = "SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM from Income i " +
            "JOIN i.user u where u.username= :username and i.id= :incomeId")
    Boolean isUserObjectOwner(@Param("username") String username, @Param("incomeId") int incomeId);

    //looks to find an income by id and also ensures the given user is owner of that
    @Query(value = "Select i from Income i " +
            "where i.user.id= :user_id and i.id= :incomeId")
    Income findIncomeWithUserAndId(@Param("user_id") int user_id, @Param("incomeId") int incomeId);
}
