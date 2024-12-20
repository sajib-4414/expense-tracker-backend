package com.sajib_4414.expense.tracker.models.income;

import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.payload.IncomeSourceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeSourceRepository extends CrudRepository<IncomeSource, Integer> {
    public Page<IncomeSource> findByUserIsNullOrUser(final Pageable pageable, User user);

    @Query(value = "SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM from IncomeSource i " +
            "JOIN i.user u where u.username= :username and i.id= :incomeSourceId")
    Boolean isUserObjectOwner(@Param("username") String username, @Param("incomeSourceId") int incomeSourceId);

    @Query(value = "Select i from IncomeSource i " +
            "where i.user.id= :user_id and i.id= :incomeSourceId")
    IncomeSource findIncomeSourceWithUserAndId(@Param("user_id") int user_id, @Param("incomeSourceId") int incomeSourceId);

}
