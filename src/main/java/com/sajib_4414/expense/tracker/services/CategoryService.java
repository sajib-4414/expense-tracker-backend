package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.models.user.UserRepository;
import com.sajib_4414.expense.tracker.payload.CategoryCreate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Transactional
    public Category create(CategoryCreate payload){
        Category category = Category.builder().name(payload.getName()).createdBy(getCurrentUser()).build();
        categoryRepository.save(category);
        return category;
    }

    public List<Category> getFullCategories(){
        return StreamSupport.stream(categoryRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());

    }

    //get all categoris that user shuld see = usercategirues+system categories
    public List<Category> getCategories() {
        return StreamSupport.stream(categoryRepository.findByCreatedByIsNullOrCreatedBy(getCurrentUser()).spliterator(),false)
                .collect(Collectors.toList());
    }

    public List<Category> getMyCategories() {
        return new ArrayList<>();
    }
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //we have our user stored here
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()->new ItemNotFoundException("User not found"));
        return user;
    }
}
