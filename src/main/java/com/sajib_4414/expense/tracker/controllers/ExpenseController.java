package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.models.expense.Expense;
import com.sajib_4414.expense.tracker.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    @GetMapping("")
    public ResponseEntity<List<Expense>> getAllMyExpenses(){

        return ResponseEntity.ok().body(expenseService.getCurrentUserExpense());
    }

    @PostMapping("")
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense payload){

        return ResponseEntity.ok().body(expenseService.createExpense(payload));
    }

}
