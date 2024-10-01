package com.sajib_4414.expense.tracker.models.category;

import com.sajib_4414.expense.tracker.models.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findByCreatedByIsNullOrCreatedBy(User user);
}
