package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.config.exceptions.ErrorDTO;
import com.sajib_4414.expense.tracker.config.exceptions.ErrorHttpResponse;
import com.sajib_4414.expense.tracker.config.exceptions.JWTExpiredException;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.payload.CategoryCreate;
import com.sajib_4414.expense.tracker.payload.LoginResponse;
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

    @GetMapping("")
    protected ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok().body(categoryService.getAll());
//        return ResponseEntity.ok().body(categoryService.create(payload));
    }

    @ExceptionHandler(JWTExpiredException.class)
    public ResponseEntity<ErrorHttpResponse> handleJwtTokenExpiredException(JWTExpiredException ex) {

        ErrorDTO error = ErrorDTO.builder().code("token_expired").message("Token Expired, "+ex.getMessage()).build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

//        ErrorResponse errorResponse = new ErrorResponse("Token Expired", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
