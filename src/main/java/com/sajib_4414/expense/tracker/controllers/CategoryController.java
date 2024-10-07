package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.config.exceptions.ErrorDTO;
import com.sajib_4414.expense.tracker.config.exceptions.ErrorHttpResponse;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.payload.CategoryCreate;
import com.sajib_4414.expense.tracker.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("")
    protected ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryCreate payload){
        return ResponseEntity.ok().body(categoryService.create(payload));
    }

    @PutMapping("/{categoryId}")
    protected ResponseEntity<Category> updateCategory(@Valid @RequestBody CategoryCreate payload, @PathVariable int categoryId){
        return ResponseEntity.ok().body(categoryService.updateCategory(payload, categoryId));
    }

    @DeleteMapping("/{categoryId}")
    protected ResponseEntity<Category> deleteCategory( @PathVariable int categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    //returns all the categories created by all the users, only open to admin user.
    @GetMapping("/all-categories")
    protected ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok().body(categoryService.getFullCategories());
    }

    //returns system categories plus categories created by user.
    @GetMapping("")
    protected ResponseEntity<List<Category>> getCategories(){
        return ResponseEntity.ok().body(categoryService.getCategories());
    }

    //returns only user's created custom categories
    @GetMapping("/mycategories")
    protected ResponseEntity<List<Category>> getMyCategories(){
        return ResponseEntity.ok().body(categoryService.getMyCategories());
    }


}
