package com.sajib_4414.expense.tracker.models.income;

import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.payload.IncomeByIncomeSourceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends CrudRepository<Income, Integer> {
    Page<Income> findByUser(final Pageable pageable, User user);

    @Query(value = "SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM from Income i " +
            "JOIN i.user u where u.username= :username and i.id= :incomeId")
    Boolean isUserObjectOwner(@Param("username") String username, @Param("incomeId") int incomeId);

    //looks to find an income by id and also ensures the given user is owner of that
    @Query(value = "Select i from Income i " +
            "where i.user.id= :user_id and i.id= :incomeId")
    Income findIncomeWithUserAndId(@Param("user_id") int user_id, @Param("incomeId") int incomeId);

    @Query(value = "SELECT sum(amount) FROM incomes " +
            "where user_id=:user_id and " +
            "EXTRACT(MONTH FROM date_time) = :month", nativeQuery = true)
    Optional<Double> getTotalIncomeOfMonth(@Param("user_id") int user_id, @Param("month") int month);

    @Query(value = "SELECT i FROM Income i " +
            "where i.user.id=:user_id and " +
            "(EXTRACT(MONTH FROM i.dateTime) = :month or :month is NULL)" +
            " and (EXTRACT(YEAR FROM i.dateTime) = :year OR :year is NULL)")
    Optional<Page<Income>> getAllIncomeOfMonthAndYear(final Pageable pageable, @Param("user_id") Integer user_id, @Param("month") Integer month, @Param("year") Integer year);

    @Query(value = "SELECT sum(amount) FROM incomes " +
            "where user_id=:user_id and " +
            "EXTRACT(MONTH FROM date_time) = :month and EXTRACT(YEAR FROM date_time)= :year", nativeQuery = true)
    Optional<Double> getTotalIncomeOfMonthAndYear(@Param("user_id") int user_id, @Param("month") int month, @Param("year") int year);
    //

    @Query("select new com.sajib_4414.expense.tracker.payload.IncomeByIncomeSourceDTO(sum(i.amount)  as income_total, " +
            "case " +
            "when is.id IS NULL then 'undefined_source' " +
            "else is.name " +
            "end " +
            "as income_source_name, " +
            "case " +
            "when is.id IS NULL then 0 " +
            "else is.id " +
            "end " +
            "as income_source_id, " +
            "EXTRACT(MONTH from i.dateTime) as income_month )" +//closing bracket of new
            "from Income i " +
            "LEFT join i.incomeSource is " +
            "where i.user.id=50 " +
            "and EXTRACT(MONTH from i.dateTime)=10\n" +
            "group by is.id, is.name,income_month")
    List<IncomeByIncomeSourceDTO> getIncomeByIncomeSource();
}
