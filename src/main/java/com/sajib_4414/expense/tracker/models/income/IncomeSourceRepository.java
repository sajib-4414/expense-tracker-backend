package com.sajib_4414.expense.tracker.models.income;

import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.payload.IncomeSourceDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeSourceRepository extends CrudRepository<IncomeSource, Integer> {
    public List<IncomeSource> findByCreatedByIsNullOrCreatedBy(User user);

    @Query(value = "SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM from IncomeSource i " +
            "JOIN i.createdBy u where u.username= :username and i.id= :incomeSourceId")
    Boolean isUserObjectOwner(@Param("username") String username, @Param("incomeSourceId") int incomeSourceId);

    @Query(value = "Select i from IncomeSource i " +
            "where i.createdBy.id= :user_id and i.id= :incomeSourceId")
    IncomeSource findIncomeSourceWithUserAndId(@Param("user_id") int user_id, @Param("incomeSourceId") int incomeSourceId);

}
