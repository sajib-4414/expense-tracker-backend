package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.payload.CategoryCreate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category create(CategoryCreate payload){
        Category category = Category.builder().name(payload.getName()).build();
        categoryRepository.save(category);
        return category;
    }

    public List<Category> getAll(){
        return StreamSupport.stream(categoryRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());

    }
}
