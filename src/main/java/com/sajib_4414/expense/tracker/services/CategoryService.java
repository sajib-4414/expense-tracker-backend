package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.PermissionError;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.SystemException;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.models.user.UserRepository;
import com.sajib_4414.expense.tracker.payload.CategoryCreate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.sajib_4414.expense.tracker.config.Helper.getCurrentUser;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Transactional
    public Category create(CategoryCreate payload){
        Category category = Category.builder().name(payload.getName()).user(getCurrentUser()).build();
        categoryRepository.save(category);
        return category;
    }

    public List<Category> getFullCategories(){
        return StreamSupport.stream(categoryRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());

    }

    //get all categoris that user shuld see = usercategirues+system categories
    public List<Category> getCategories() {
        return StreamSupport.stream(categoryRepository.findByUserIsNullOrUser(getCurrentUser()).spliterator(),false)
                .collect(Collectors.toList());
    }

    //get only user created categories
    public List<Category> getMyCategories() {
        return StreamSupport.stream(categoryRepository.getAllCategoriesByUser(getCurrentUser().getId()).spliterator(),false)
                .collect(Collectors.toList());
    }

    @Transactional
    public Category updateCategory(CategoryCreate payload, int categoryId){
        //first check if user owns the category
        checkOwnershipAndThrow(categoryId);
        //find the category
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ItemNotFoundException("category not found"));
        category.setName(payload.getName());
        categoryRepository.save( category);
        return category;
    }

    @Transactional
    public void deleteCategory(int categoryId){
        //first check if user owns the category
        checkOwnershipAndThrow(categoryId);
        System.out.println("logged.........."+categoryId);
        try{
            if(categoryRepository.existsById(categoryId)){
                categoryRepository.deleteById(categoryId);
            }
            else{
                throw new ItemNotFoundException("category not found");
            }
        }catch (Exception e){
            System.out.println("exception happened"+e);
            throw new SystemException("something went wrong");
        }



    }


    public void checkOwnershipAndThrow(int categoryId){
        Boolean isOwner = categoryRepository.isUserCategoryOwner(getCurrentUser().getUsername(), categoryId);
        if (!isOwner)
            throw new PermissionError("You are not autorized to modify this category");
    }
}
