package com.sajib_4414.expense.tracker.models.category;

import com.sajib_4414.expense.tracker.models.budget.Budget;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.payload.CategoryCreate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findByUserIsNullOrUser(User user);


    @Query(value = "select c from Category c JOIN c.user u where u.id= :userid")
    List<Category> getAllCategoriesByUser(@Param("userid") int userid);

    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM from Category c " +
            "JOIN c.user u where u.username= :username and c.id= :categoryId")
    Boolean isUserCategoryOwner(@Param("username") String username, @Param("categoryId") int categoryId);

    Optional<Category> findByIdAndUser(Integer id, User user);

}
